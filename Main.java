import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Main entry point for the Student Expense Tracker.
 * Handles all user interaction through a console menu.
 */
public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static ExpenseManager manager = new ExpenseManager();

    public static void main(String[] args) {
        printWelcome();

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("  Enter your choice: ");

            switch (choice) {
                case 1 -> addExpense();
                case 2 -> viewAllExpenses();
                case 3 -> viewByCategory();
                case 4 -> setMonthlyBudget();
                case 5 -> showBudgetStatus();
                case 6 -> showMonthlySummary();
                case 7 -> {
                    System.out.println("\n  👋 Goodbye! Stay within budget!\n");
                    running = false;
                }
                default -> System.out.println("\n  ⚠  Invalid choice. Please enter 1–7.");
            }
        }

        scanner.close();
    }

    // ─────────────────────────────────────────────
    //  MENU
    // ─────────────────────────────────────────────
    private static void printWelcome() {
        System.out.println("\n  ╔══════════════════════════════════════════╗");
        System.out.println("  ║     💰 STUDENT EXPENSE TRACKER 💰        ║");
        System.out.println("  ║       Track Smart. Save More.            ║");
        System.out.println("  ╚══════════════════════════════════════════╝");

        if (manager.getMonthlyBudget() > 0) {
            System.out.printf("%n  Welcome back! Your monthly budget is Rs.%.2f%n",
                    manager.getMonthlyBudget());
        }
    }

    private static void printMenu() {
        System.out.println("\n  ┌──────────────────────────────────────┐");
        System.out.println("  │             MAIN MENU                │");
        System.out.println("  ├──────────────────────────────────────┤");
        System.out.println("  │  1. Add Expense                      │");
        System.out.println("  │  2. View All Expenses                │");
        System.out.println("  │  3. View by Category                 │");
        System.out.println("  │  4. Set Monthly Budget               │");
        System.out.println("  │  5. Show Budget Status (Energy Bar)  │");
        System.out.println("  │  6. Monthly Summary Report           │");
        System.out.println("  │  7. Exit                             │");
        System.out.println("  └──────────────────────────────────────┘");
    }

    // ─────────────────────────────────────────────
    //  1. ADD EXPENSE
    // ─────────────────────────────────────────────
    private static void addExpense() {
        System.out.println("\n  ── ADD NEW EXPENSE ──");

        System.out.print("  Description: ");
        String description = scanner.nextLine().trim();
        if (description.isEmpty()) {
            System.out.println("  ⚠  Description cannot be empty.");
            return;
        }

        double amount = readPositiveDouble("  Amount (Rs.): ");

        // Show category options
        System.out.println("\n  Select Category:");
        for (int i = 0; i < Expense.CATEGORIES.length; i++) {
            System.out.printf("    %d. %s%n", i + 1, Expense.CATEGORIES[i]);
        }
        int catChoice = readInt("  Your choice (1-" + Expense.CATEGORIES.length + "): ");
        if (catChoice < 1 || catChoice > Expense.CATEGORIES.length) {
            System.out.println("  ⚠  Invalid category.");
            return;
        }
        String category = Expense.CATEGORIES[catChoice - 1];

        // Date input — default to today if user presses Enter
        System.out.print("  Date (YYYY-MM-DD) [press Enter for today]: ");
        String dateInput = scanner.nextLine().trim();
        LocalDate date;
        if (dateInput.isEmpty()) {
            date = LocalDate.now();
        } else {
            try {
                date = LocalDate.parse(dateInput);
            } catch (DateTimeParseException e) {
                System.out.println("  ⚠  Invalid date format. Use YYYY-MM-DD.");
                return;
            }
        }

        manager.addExpense(description, amount, category, date);
    }

    // ─────────────────────────────────────────────
    //  2. VIEW ALL EXPENSES
    // ─────────────────────────────────────────────
    private static void viewAllExpenses() {
        System.out.println("\n  ── ALL EXPENSES ──");
        manager.viewAllExpenses();
    }

    // ─────────────────────────────────────────────
    //  3. VIEW BY CATEGORY
    // ─────────────────────────────────────────────
    private static void viewByCategory() {
        System.out.println("\n  ── VIEW BY CATEGORY ──");
        System.out.println("  Categories:");
        for (int i = 0; i < Expense.CATEGORIES.length; i++) {
            System.out.printf("    %d. %s%n", i + 1, Expense.CATEGORIES[i]);
        }
        int choice = readInt("  Your choice: ");
        if (choice < 1 || choice > Expense.CATEGORIES.length) {
            System.out.println("  ⚠  Invalid choice.");
            return;
        }
        manager.viewByCategory(Expense.CATEGORIES[choice - 1]);
    }

    // ─────────────────────────────────────────────
    //  4. SET MONTHLY BUDGET
    // ─────────────────────────────────────────────
    private static void setMonthlyBudget() {
        System.out.println("\n  ── SET MONTHLY BUDGET ──");
        double budget = readPositiveDouble("  Enter your monthly budget (Rs.): ");
        manager.setMonthlyBudget(budget);
    }

    // ─────────────────────────────────────────────
    //  5. SHOW BUDGET STATUS
    // ─────────────────────────────────────────────
    private static void showBudgetStatus() {
        System.out.println("\n  ── BUDGET STATUS ──");
        // Show status for current month
        LocalDate now = LocalDate.now();
        double thisMonthSpent = 0;

        // We compute it here as a quick inline loop
        // (ExpenseManager keeps all expenses — we filter for this month)
        manager.showMonthlySummary(now.getMonthValue(), now.getYear());
    }

    // ─────────────────────────────────────────────
    //  6. MONTHLY SUMMARY REPORT
    // ─────────────────────────────────────────────
    private static void showMonthlySummary() {
        System.out.println("\n  ── MONTHLY SUMMARY ──");
        int month = readInt("  Enter month (1-12): ");
        int year  = readInt("  Enter year (e.g. 2025): ");

        if (month < 1 || month > 12) {
            System.out.println("  ⚠  Invalid month.");
            return;
        }
        manager.showMonthlySummary(month, year);
    }

    // ─────────────────────────────────────────────
    //  INPUT HELPERS — with exception handling
    // ─────────────────────────────────────────────

    // Read an integer safely
    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Please enter a valid whole number.");
            }
        }
    }

    // Read a positive double safely
    private static double readPositiveDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value <= 0) {
                    System.out.println("  ⚠  Amount must be greater than zero.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("  ⚠  Please enter a valid number (e.g. 150.50).");
            }
        }
    }
}
