package mini_projects;

/**
 * AtmOperationInterface.java — THE CONTRACT
 *
 * WHAT IS AN INTERFACE?
 * An interface is a PROMISE. It says:
 * "Any class that implements me MUST provide these methods."
 * It defines WHAT to do, never HOW to do it.
 *
 * WHY USE AN INTERFACE HERE?
 * 1. Swappability — you could swap AtmOperationImplementation for a
 *    DatabaseAtmImplementation without changing MainClass at all.
 * 2. Testability — in unit tests you can create a MockAtmImplementation.
 * 3. Clean architecture — the interface is the stable API; implementations
 *    can change freely behind it.
 *
 * In your original code this was good thinking — we keep it and expand it
 * to cover all the new features (transfer, changePin, admin mode).
 */
public interface AtmOperationInterface {

    /**
     * Handles the full login flow:
     * - Prompts for PIN up to 3 times
     * - Locks account on 3 failures
     * - Returns true if login succeeded, false if account locked
     *
     * WHY return boolean?
     * The caller (MainClass) decides what to do next based on the result.
     * The login method should NOT call System.exit() — that's the caller's job.
     * This keeps methods focused on ONE thing.
     */
    boolean login();

    /**
     * Admin login — separate flow, separate PIN.
     * Returns true if admin PIN matches.
     */
    boolean adminLogin();

    /** Prints current balance to console. */
    void viewBalance();

    /**
     * Deposits the given amount.
     * @param amount — must be positive; method validates this.
     */
    void deposit(double amount);

    /**
     * Withdraws the given amount.
     * Rules enforced inside: no negatives, can't exceed balance, min KES 500 left.
     * @param amount — the requested withdrawal amount.
     */
    void withdraw(double amount);

    /**
     * Transfers amount to a recipient account.
     * @param recipientAccount — must be exactly 10 digits.
     * @param amount           — validated same as withdrawal.
     */
    void transferFunds(String recipientAccount, double amount);

    /** Prints the last N transactions from the ArrayList. */
    void printMiniStatement();

    /**
     * Guides user through PIN change flow:
     * verify current PIN → enter new PIN → confirm new PIN → enforce 4-digit rule.
     */
    void changePin();

    /** Shows the admin dashboard (totals, failed attempts, etc.). */
    void showAdminDashboard();
}