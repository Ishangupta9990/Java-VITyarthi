# Programming in Java Evaluated Course Project
Name - Ishan Gupta
Registration No: 24BAI10654

# 💰 Student Expense Tracker

A console-based Java application that helps students track daily expenses, set a monthly budget, and visualize their spending with a live budget energy bar.

---

## 📌 Problem Statement

Students often overspend without realizing it — especially those living away from home in hostels or PGs. There's no simple, offline tool to track daily expenses by category, set a budget, and get warnings before money runs out. This project solves that with a lightweight Java console application that requires no internet or installation beyond Java itself.

---

## ✨ Features

- ➕ **Add Expenses** — Log spending with description, amount, category, and date
- 📋 **View All Expenses** — See a formatted table of every recorded expense
- 🗂️ **Filter by Category** — View expenses under Food, Travel, Stationery, etc.
- 💵 **Set Monthly Budget** — Define how much you plan to spend this month
- 🔋 **Budget Energy Bar** — Visual ASCII bar showing budget usage (like a game energy meter)
- ⚠️ **Smart Warnings** — Alerts at 80% and 100% of budget usage
- 📊 **Monthly Summary Report** — Category-wise breakdown for any month/year
- 💾 **Persistent Storage** — All data saved to local text files; nothing is lost on exit

---

## 🗂️ Project Structure

```
ExpenseTracker/
├── Expense.java          # Data model — represents a single expense
├── ExpenseManager.java   # Core logic — add, view, filter, save, load
├── Main.java             # Entry point — menu-driven console interface
├── expenses.txt          # Auto-generated: saved expenses (created on first use)
└── budget.txt            # Auto-generated: saved budget (created when budget is set)
```

---

## 🛠️ How to Set Up and Run

### Prerequisites
- Java JDK 11 or higher installed
- A terminal / command prompt

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/YOUR_USERNAME/student-expense-tracker.git
cd student-expense-tracker
```

**2. Compile the Java files**
```bash
javac Expense.java ExpenseManager.java Main.java
```

**3. Run the program**
```bash
java Main
```

---

## 🖥️ Sample Output

```
  ╔══════════════════════════════════════════╗
  ║     💰 STUDENT EXPENSE TRACKER 💰        ║
  ║       Track Smart. Save More.            ║
  ╚══════════════════════════════════════════╝

  ┌──────────────────────────────────────┐
  │             MAIN MENU                │
  ├──────────────────────────────────────┤
  │  1. Add Expense                      │
  │  2. View All Expenses                │
  │  3. View by Category                 │
  │  4. Set Monthly Budget               │
  │  5. Show Budget Status (Energy Bar)  │
  │  6. Monthly Summary Report           │
  │  7. Exit                             │
  └──────────────────────────────────────┘
```

**Budget Energy Bar Example:**
```
  ┌─────────────────────────────────────────┐
  │  BUDGET USAGE: [████████████░░░░░░░░] 62.5%
  │  Rs.3125.00 spent of Rs.5000.00 budget
  │  Status : 🟡 MODERATE
  └─────────────────────────────────────────┘
```

---

## 💡 Java Concepts Used

| Concept | Where Used |
|---|---|
| OOP (Classes & Objects) | `Expense` class models each expense |
| Collections (ArrayList, HashMap) | Storing expenses, category-wise summary |
| File Handling (BufferedReader/Writer) | Saving and loading data from `.txt` files |
| Exception Handling (try-catch) | Invalid inputs, file errors |
| Interfaces (Serializable) | `Expense` implements `Serializable` |
| String manipulation | Parsing file lines, formatting output |

---

## 📁 Categories Supported

- Food
- Travel
- Stationery
- Rent
- Entertainment
- Health
- Other

---

## 👨‍💻 Author

**Your Name**  
B.Tech CSE | VIT  
GitHub: [@your_username](https://github.com/your_username)

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).
