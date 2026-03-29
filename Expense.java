import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a single expense entry.
 * Implements Serializable so it can be saved to a file.
 */
public class Expense implements Serializable {

    // Category constants — students pick from these
    public static final String[] CATEGORIES = {
        "Food", "Travel", "Stationery", "Rent", "Entertainment", "Health", "Other"
    };

    private String description;
    private double amount;
    private String category;
    private LocalDate date;

    // Constructor
    public Expense(String description, double amount, String category, LocalDate date) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Getters
    public String getDescription() { return description; }
    public double getAmount()      { return amount; }
    public String getCategory()    { return category; }
    public LocalDate getDate()     { return date; }

    // Convert expense to a line of text for saving to file
    // Format: description|amount|category|date
    public String toFileString() {
        return description + "|" + amount + "|" + category + "|" + date.toString();
    }

    // Rebuild an Expense object from a saved file line
    public static Expense fromFileString(String line) {
        String[] parts = line.split("\\|");
        String desc     = parts[0];
        double amount   = Double.parseDouble(parts[1]);
        String category = parts[2];
        LocalDate date  = LocalDate.parse(parts[3]);
        return new Expense(desc, amount, category, date);
    }

    @Override
    public String toString() {
        return String.format("%-20s | %-12s | Rs.%-8.2f | %s",
                description, category, amount, date);
    }
}
