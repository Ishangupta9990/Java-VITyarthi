import java.io.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * Manages all expenses — adding, viewing, filtering, saving, loading.
 * Uses an ArrayList (Collection) to store Expense objects.
 */
public class ExpenseManager {

    private ArrayList<Expense> expenses;   // All expenses in memory
    private double monthlyBudget;          // Budget set by the user
    private static final String FILE_NAME = "expenses.txt";
    private static final String BUDGET_FILE = "budget.txt";

    // Constructor — loads existing data from files when program starts
    public ExpenseManager() {
        expenses = new ArrayList<>();
        monthlyBudget = 0.0;
        loadExpenses();
        loadBudget();
    }

    // ─────────────────────────────────────────────
    //  ADD EXPENSE
    // ─────────────────────────────────────────────
    public void addExpense(String description, double amount, String category, LocalDate date) {
        Expense expense = new Expense(description, amount, category, date);
        expenses.add(expense);
        saveExpenses();  // Auto-save after every addition
        System.out.println("\n✔ Expense added successfully!");
        checkBudgetWarning();  // Immediately warn if budget is being crossed
    }

    // ─────────────────────────────────────────────
    //  VIEW ALL EXPENSES
    // ─────────────────────────────────────────────
    public void viewAllExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("\n No expenses recorded yet.");
            return;
        }
        printHeader();
        for (Expense e : expenses) {
            System.out.println(e);
        }
        System.out.println("─".repeat(65));
        System.out.printf("  TOTAL: Rs.%.2f%n", getTotalSpent());
    }

    // ─────────────────────────────────────────────
    //  VIEW BY CATEGORY
    // ─────────────────────────────────────────────
    public void viewByCategory(String category) {
        boolean found = false;
        double total = 0;

        printHeader();
        for (Expense e : expenses) {
            if (e.getCategory().equalsIgnoreCase(category)) {
                System.out.println(e);
                total += e.getAmount();
                found = true;
            }
        }

        if (!found) {
            System.out.println("  No expenses found under category: " + category);
        } else {
            System.out.println("─".repeat(65));
            System.out.printf("  TOTAL for %s: Rs.%.2f%n", category, total);
        }
    }

    // ─────────────────────────────────────────────
    //  MONTHLY SUMMARY REPORT
    // ─────────────────────────────────────────────
    public void showMonthlySummary(int month, int year) {
        // Use a HashMap to accumulate spending per category
        Map<String, Double> categoryTotals = new HashMap<>();
        double grandTotal = 0;

        for (Expense e : expenses) {
            if (e.getDate().getMonthValue() == month && e.getDate().getYear() == year) {
                categoryTotals.merge(e.getCategory(), e.getAmount(), Double::sum);
                grandTotal += e.getAmount();
            }
        }

        if (categoryTotals.isEmpty()) {
            System.out.println("\n  No expenses found for " + Month.of(month) + " " + year);
            return;
        }

        System.out.println("\n  ══════════════════════════════════════");
        System.out.printf ("  📊 MONTHLY REPORT — %s %d%n", Month.of(month), year);
        System.out.println("  ══════════════════════════════════════");

        // Sort categories alphabetically for a clean report
        List<String> sortedCategories = new ArrayList<>(categoryTotals.keySet());
        Collections.sort(sortedCategories);

        for (String cat : sortedCategories) {
            System.out.printf("  %-15s : Rs.%.2f%n", cat, categoryTotals.get(cat));
        }

        System.out.println("  ──────────────────────────────────────");
        System.out.printf ("  %-15s : Rs.%.2f%n", "GRAND TOTAL", grandTotal);
        System.out.println("  ══════════════════════════════════════");

        // Show budget bar if budget is set
        if (monthlyBudget > 0) {
            showBudgetBar(grandTotal);
        }
    }

    // ─────────────────────────────────────────────
    //  BUDGET BAR — the fun energy-level display
    // ─────────────────────────────────────────────
    public void showBudgetBar(double spent) {
        if (monthlyBudget <= 0) {
            System.out.println("\n  ⚠  No budget set. Use option 4 to set a monthly budget.");
            return;
        }

        double percentage = (spent / monthlyBudget) * 100;
        int filled = (int) Math.min(percentage / 5, 20); // 20 blocks = 100%
        int empty  = 20 - filled;

        String bar = "█".repeat(filled) + "░".repeat(empty);

        // Choose color label based on usage
        String status;
        if (percentage < 50)       status = "✅ GOOD";
        else if (percentage < 80)  status = "🟡 MODERATE";
        else if (percentage < 100) status = "🔴 WARNING";
        else                       status = "💀 OVER BUDGET";

        System.out.println("\n  ┌─────────────────────────────────────────┐");
        System.out.printf ("  │  BUDGET USAGE: [%s] %.1f%%%n", bar, percentage);
        System.out.printf ("  │  Rs.%.2f spent of Rs.%.2f budget%n", spent, monthlyBudget);
        System.out.printf ("  │  Status : %s%n", status);
        System.out.println("  └─────────────────────────────────────────┘");
    }

    // ─────────────────────────────────────────────
    //  SET BUDGET
    // ─────────────────────────────────────────────
    public void setMonthlyBudget(double budget) {
        this.monthlyBudget = budget;
        saveBudget();
        System.out.printf("\n✔ Monthly budget set to Rs.%.2f%n", budget);
    }

    public double getMonthlyBudget() { return monthlyBudget; }

    // ─────────────────────────────────────────────
    //  BUDGET WARNING (called after each new expense)
    // ─────────────────────────────────────────────
    private void checkBudgetWarning() {
        if (monthlyBudget <= 0) return;

        // Only check current month's spending
        LocalDate now = LocalDate.now();
        double thisMonthSpent = 0;
        for (Expense e : expenses) {
            if (e.getDate().getMonthValue() == now.getMonthValue()
                    && e.getDate().getYear() == now.getYear()) {
                thisMonthSpent += e.getAmount();
            }
        }

        double pct = (thisMonthSpent / monthlyBudget) * 100;
        if (pct >= 100) {
            System.out.println("\n  💀 ALERT! You have EXCEEDED your monthly budget!");
        } else if (pct >= 80) {
            System.out.printf("\n  🔴 WARNING: You've used %.1f%% of your monthly budget!%n", pct);
        }
    }

    // ─────────────────────────────────────────────
    //  HELPER: total spent across all expenses
    // ─────────────────────────────────────────────
    public double getTotalSpent() {
        double total = 0;
        for (Expense e : expenses) total += e.getAmount();
        return total;
    }

    // ─────────────────────────────────────────────
    //  FILE I/O — Save expenses to text file
    // ─────────────────────────────────────────────
    private void saveExpenses() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.write(e.toFileString());
                writer.newLine();
            }
        } catch (IOException ex) {
            System.out.println("  ⚠  Error saving expenses: " + ex.getMessage());
        }
    }

    // FILE I/O — Load expenses from text file
    private void loadExpenses() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;  // First run — no file yet, that's fine

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    expenses.add(Expense.fromFileString(line));
                }
            }
        } catch (IOException ex) {
            System.out.println("  ⚠  Error loading expenses: " + ex.getMessage());
        }
    }

    // FILE I/O — Save budget
    private void saveBudget() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BUDGET_FILE))) {
            writer.write(String.valueOf(monthlyBudget));
        } catch (IOException ex) {
            System.out.println("  ⚠  Error saving budget: " + ex.getMessage());
        }
    }

    // FILE I/O — Load budget
    private void loadBudget() {
        File file = new File(BUDGET_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(BUDGET_FILE))) {
            String line = reader.readLine();
            if (line != null) monthlyBudget = Double.parseDouble(line.trim());
        } catch (IOException | NumberFormatException ex) {
            System.out.println("  ⚠  Error loading budget: " + ex.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    //  HELPER: table header
    // ─────────────────────────────────────────────
    private void printHeader() {
        System.out.println("\n" + "─".repeat(65));
        System.out.printf("  %-20s | %-12s | %-11s | %s%n",
                "Description", "Category", "Amount", "Date");
        System.out.println("─".repeat(65));
    }
}
