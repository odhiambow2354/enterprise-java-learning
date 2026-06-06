package mini_projects;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * AtmOperationImplementation.java — THE BRAIN / LOGIC LAYER
 *
 * WHAT THIS CLASS DOES:
 * It implements every method promised by the interface.
 * All business rules live here — validation, transaction recording, PIN logic.
 *
 * KEY DESIGN DECISIONS EXPLAINED:
 *
 * 1. ArrayList<String> instead of HashMap<Double, String>
 *    Your original used HashMap — good instinct, but HashMap has a fatal flaw
 *    for transaction history: if you deposit 5000 TWICE, the second entry
 *    overwrites the first (same key!). ArrayList preserves every transaction
 *    in insertion order. That's why banks use lists, not maps, for statements.
 *
 * 2. Scanner is injected (passed in), not created here.
 *    WHY? If every class creates its own Scanner on System.in, you get
 *    unpredictable behaviour. One Scanner per input stream is the rule.
 *    MainClass owns the Scanner and passes it down — that's dependency injection.
 *
 * 3. Counters for admin dashboard (totalDeposited, totalWithdrawn, etc.)
 *    These are running totals updated on every transaction.
 *    The admin dashboard simply reads them — O(1) lookup, no looping needed.
 */
public class AtmOperationImplementation implements AtmOperationInterface {

    // ─── DEPENDENCIES ────────────────────────────────────────────────────────

    private final ATMSimulator atm;     // The data model — holds balance & PIN
    private final Scanner scanner;      // Shared input scanner from MainClass

    // ─── TRANSACTION HISTORY ─────────────────────────────────────────────────

    /**
     * ArrayList chosen over array because:
     * - We don't know in advance how many transactions will occur.
     * - ArrayList grows dynamically — no fixed size to manage.
     * - Each entry is a human-readable String, easy to print.
     * Format example: "DEPOSIT    | +KES 5,000.00 | Balance: KES 55,000.00"
     */
    private final ArrayList<String> transactions = new ArrayList<>();

    // ─── ADMIN STATISTICS ────────────────────────────────────────────────────
    // These accumulate across the session for the admin dashboard.

    private double totalDeposited   = 0;
    private double totalWithdrawn   = 0;
    private double totalTransferred = 0;
    private int    failedLoginAttempts = 0;  // Tracks across the whole session

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────

    /**
     * Constructor receives the shared Scanner.
     * We also instantiate ATMSimulator here — the implementation OWNS the model.
     * MainClass only knows about the interface, never the model directly.
     */
    public AtmOperationImplementation(Scanner scanner) {
        this.atm     = new ATMSimulator();
        this.scanner  = scanner;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // LOGIN
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * login() — handles user authentication with lockout after 3 failures.
     *
     * LOOP STRATEGY: a counted for-loop (not while-true) because we have a
     * fixed maximum number of attempts. Using 'for' makes the limit obvious
     * to any reader — no need to hunt for a break condition.
     *
     * @return true if PIN matched; false if all attempts exhausted.
     */
    @Override
    public boolean login() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║   SecureBank Kenya — Login   ║");
        System.out.println("╚══════════════════════════════╝");

        for (int attempt = 1; attempt <= 3; attempt++) {
            System.out.printf("Enter PIN (Attempt %d of 3): ", attempt);
            int enteredPin = readInt();  // Safe integer reading (see helper below)

            if (enteredPin == atm.getPin()) {
                System.out.println("✔  Login successful. Welcome!\n");
                return true;  // Early return — no need to finish the loop
            } else {
                failedLoginAttempts++;  // Increment admin stat
                int remaining = 3 - attempt;
                if (remaining > 0) {
                    System.out.printf("✘  Incorrect PIN. %d attempt(s) remaining.%n", remaining);
                } else {
                    // Last attempt failed
                    System.out.println("\n🔒 ACCOUNT LOCKED — Too many failed attempts.");
                    System.out.println("   Please visit a SecureBank branch to unlock.\n");
                }
            }
        }
        return false;  // Fell through all attempts without success
    }

    /**
     * adminLogin() — separate from user login.
     * Admin PIN is fixed at 9999 and stored in the model.
     * No lockout for admin (simplification — in production you'd add one).
     */
    @Override
    public boolean adminLogin() {
        System.out.print("Enter Admin PIN: ");
        int enteredPin = readInt();
        if (enteredPin == atm.getAdminPin()) {
            System.out.println("✔  Admin access granted.\n");
            return true;
        }
        System.out.println("✘  Invalid Admin PIN.\n");
        return false;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // BALANCE
    // ═════════════════════════════════════════════════════════════════════════

    @Override
    public void viewBalance() {
        // formatKES() is a helper that formats numbers as "KES 50,000.00"
        System.out.println("\n  💳 Current Balance: " + formatKES(atm.getBalance()) + "\n");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // DEPOSIT
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * deposit() — adds money to the account.
     *
     * VALIDATION FIRST pattern (Guard Clauses):
     * Check all invalid conditions at the top and return early.
     * This avoids deeply nested if-else blocks and keeps the happy path flat.
     *
     * @param amount — the deposit amount (already read by the caller)
     */
    @Override
    public void deposit(double amount) {
        // Guard clause 1: reject non-positive amounts
        if (amount <= 0) {
            System.out.println("  ✘ Deposit amount must be greater than KES 0.\n");
            return;  // Exit method early — nothing more to do
        }

        // Happy path: update balance, record transaction, show result
        double newBalance = atm.getBalance() + amount;
        atm.setBalance(newBalance);
        totalDeposited += amount;  // Update admin stat

        // Record transaction as a formatted string
        String record = String.format("DEPOSIT    | +%-12s | Balance: %s",
                formatKES(amount), formatKES(newBalance));
        transactions.add(record);  // ArrayList.add() appends to the end

        System.out.println("  ✔  Deposit successful.");
        System.out.println("  " + record + "\n");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // WITHDRAW
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * withdraw() — removes money from the account with multiple safety checks.
     *
     * MINIMUM BALANCE RULE: KES 500 must always remain.
     * So the maximum you can withdraw = balance - 500.
     * We compute this once and check against it — cleaner than two conditions.
     *
     * @param amount — the withdrawal amount (already read by the caller)
     */
    @Override
    public void withdraw(double amount) {
        // Guard clause 1: no negatives or zero
        if (amount <= 0) {
            System.out.println("  ✘ Withdrawal amount must be greater than KES 0.\n");
            return;
        }

        // Guard clause 2: minimum balance rule (KES 500 must remain)
        // maxWithdrawable is the most we can ever take out
        double maxWithdrawable = atm.getBalance() - 500.0;

        if (amount > maxWithdrawable) {
            System.out.println("  ✘ Insufficient funds or minimum balance rule violated.");
            System.out.printf("     Max you can withdraw: %s (KES 500 must remain)%n%n",
                    formatKES(maxWithdrawable));
            return;
        }

        // Happy path
        double newBalance = atm.getBalance() - amount;
        atm.setBalance(newBalance);
        totalWithdrawn += amount;  // Update admin stat

        String record = String.format("WITHDRAWAL | -%-12s | Balance: %s",
                formatKES(amount), formatKES(newBalance));
        transactions.add(record);

        System.out.println("  ✔  Withdrawal successful. Please collect your cash.");
        System.out.println("  " + record + "\n");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // TRANSFER
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * transferFunds() — sends money to another account.
     *
     * ACCOUNT NUMBER VALIDATION:
     * We receive a String (not int/long) because:
     * - Account numbers may have leading zeros (e.g., 0712345678)
     * - int/long would silently drop leading zeros
     * - String lets us check .length() == 10 and .matches("\\d+") (all digits)
     *
     * @param recipientAccount — 10-digit string
     * @param amount           — validated same as withdrawal
     */
    @Override
    public void transferFunds(String recipientAccount, double amount) {
        // Guard clause 1: account number must be exactly 10 digits
        // \\d+ is a regex meaning "one or more digit characters"
        if (recipientAccount == null
                || recipientAccount.length() != 10
                || !recipientAccount.matches("\\d+")) {
            System.out.println("  ✘ Invalid account number. Must be exactly 10 digits.\n");
            return;
        }

        // Guard clause 2: same amount rules as withdrawal
        if (amount <= 0) {
            System.out.println("  ✘ Transfer amount must be greater than KES 0.\n");
            return;
        }

        double maxTransferable = atm.getBalance() - 500.0;
        if (amount > maxTransferable) {
            System.out.println("  ✘ Insufficient funds or minimum balance rule violated.");
            System.out.printf("     Max you can transfer: %s%n%n", formatKES(maxTransferable));
            return;
        }

        // Happy path
        double newBalance = atm.getBalance() - amount;
        atm.setBalance(newBalance);
        totalTransferred += amount;

        String record = String.format("TRANSFER   | -%-12s | To: %s | Balance: %s",
                formatKES(amount), recipientAccount, formatKES(newBalance));
        transactions.add(record);

        System.out.println("  ✔  Transfer successful.");
        System.out.println("  " + record + "\n");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // MINI STATEMENT
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * printMiniStatement() — shows the last 10 transactions.
     *
     * WHY LAST 10 ONLY?
     * Real ATM mini-statements show a limited window (typically 5–10 entries).
     * We calculate startIndex to handle cases where there are fewer than 10.
     *
     * Math.max(0, size - 10) means:
     *   - If 15 transactions exist: start at index 5 (show 5–14)
     *   - If 3 transactions exist:  start at index 0 (show all 3)
     */
    @Override
    public void printMiniStatement() {
        System.out.println("\n─────────────── MINI STATEMENT ───────────────");

        if (transactions.isEmpty()) {
            System.out.println("  No transactions yet in this session.\n");
            return;
        }

        int size       = transactions.size();
        int startIndex = Math.max(0, size - 10);  // Show last 10

        // Enhanced for loop: 'i' is the display number (1-based for readability)
        for (int i = startIndex; i < size; i++) {
            System.out.printf("  %2d. %s%n", (i - startIndex + 1), transactions.get(i));
        }

        System.out.println("──────────────────────────────────────────────\n");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // CHANGE PIN
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * changePin() — secure PIN change flow.
     *
     * FLOW:
     * 1. Verify current PIN (prevents unauthorized changes)
     * 2. Ask for new PIN
     * 3. Confirm new PIN
     * 4. Validate: must be exactly 4 digits (1000–9999)
     * 5. Update model only if ALL checks pass
     *
     * WHY 4-DIGIT CHECK?
     * A 4-digit PIN is between 1000 and 9999 inclusive.
     * We check this arithmetically (no string conversion needed).
     */
    @Override
    public void changePin() {
        System.out.println("\n─────── CHANGE PIN ───────");

        // Step 1: verify current PIN
        System.out.print("  Enter current PIN: ");
        int currentPin = readInt();
        if (currentPin != atm.getPin()) {
            System.out.println("  ✘ Current PIN incorrect. PIN not changed.\n");
            return;
        }

        // Step 2: new PIN
        System.out.print("  Enter new PIN (4 digits): ");
        int newPin = readInt();

        // Step 3: validate 4-digit constraint
        if (newPin < 1000 || newPin > 9999) {
            System.out.println("  ✘ PIN must be exactly 4 digits (1000–9999). PIN not changed.\n");
            return;
        }

        // Step 4: confirmation
        System.out.print("  Confirm new PIN: ");
        int confirmPin = readInt();

        if (newPin != confirmPin) {
            System.out.println("  ✘ PINs do not match. PIN not changed.\n");
            return;
        }

        // All checks passed — update the model
        atm.setPin(newPin);
        System.out.println("  ✔  PIN changed successfully.\n");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // ADMIN DASHBOARD
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * showAdminDashboard() — system monitoring view.
     *
     * WHY HAVE AN ADMIN MODE?
     * In real systems, operations teams need visibility into system activity
     * without being able to transact. This is the principle of least privilege —
     * admin sees reports but can't change balances.
     */
    @Override
    public void showAdminDashboard() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║         ADMIN DASHBOARD — SecureBank     ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.printf( "║  Total Cash Deposited  : %-16s║%n", formatKES(totalDeposited));
        System.out.printf( "║  Total Cash Withdrawn  : %-16s║%n", formatKES(totalWithdrawn));
        System.out.printf( "║  Total Transfers Sent  : %-16s║%n", formatKES(totalTransferred));
        System.out.printf( "║  Total Transactions    : %-16d║%n", transactions.size());
        System.out.printf( "║  Failed Login Attempts : %-16d║%n", failedLoginAttempts);
        System.out.printf( "║  Current Balance       : %-16s║%n", formatKES(atm.getBalance()));
        System.out.println("╚══════════════════════════════════════════╝\n");
    }

    // ═════════════════════════════════════════════════════════════════════════
    // PRIVATE HELPER METHODS
    // ═════════════════════════════════════════════════════════════════════════

    /**
     * readInt() — safe integer reader that never crashes on bad input.
     *
     * PROBLEM WITH scanner.nextInt():
     * If the user types "abc", nextInt() throws InputMismatchException
     * and crashes. This is the bug in your original code.
     *
     * SOLUTION:
     * Read the WHOLE line as a String first, then try to parse it.
     * If parsing fails, we catch the exception and return -1 as a sentinel value.
     * The caller's guard clauses will then reject -1 naturally.
     *
     * @return the parsed integer, or -1 if input was invalid.
     */
    private int readInt() {
        try {
            String line = scanner.nextLine().trim();
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            // User typed letters or left it blank
            System.out.println("  [!] Invalid input — please enter a number.");
            return -1;  // Sentinel: callers treat this as wrong/invalid
        }
    }

    /**
     * readDouble() — same crash-safe pattern but for decimal amounts.
     *
     * @return the parsed double, or -1.0 if input was invalid.
     */
    public double readDouble() {
        try {
            String line = scanner.nextLine().trim();
            return Double.parseDouble(line);
        } catch (NumberFormatException e) {
            System.out.println("  [!] Invalid input — please enter a number (e.g. 5000 or 1500.50).");
            return -1.0;
        }
    }

    /**
     * readAccountNumber() — reads a line and returns it as-is (String, not int).
     * Used for transfer recipient — preserves leading zeros.
     *
     * @return trimmed string from input.
     */
    public String readAccountNumber() {
        return scanner.nextLine().trim();
    }

    /**
     * formatKES() — formats a double as a clean currency string.
     *
     * WHY NOT USE printf DIRECTLY?
     * Because we need the formatted string in multiple places
     * (inside other strings, in records, in the admin dashboard).
     * Extracting it to a method avoids repeating the format pattern.
     *
     * String.format("%,.2f") means:
     *   ,  → add thousands separators (50000 → 50,000)
     *   .2 → 2 decimal places
     *   f  → floating-point number
     *
     * @param amount — the number to format.
     * @return e.g. "KES 50,000.00"
     */
    private String formatKES(double amount) {
        return String.format("KES %,.2f", amount);
    }
}