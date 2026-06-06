package mini_projects;

/**
 * ATMSimulator.java — The DATA MODEL (Plain Old Java Object / POJO)
 *
 * WHAT THIS CLASS IS:
 * This is the "blueprint" that represents one ATM account.
 * It holds only DATA — no logic, no printing, no decisions.
 * This is called the "Model" in real enterprise architecture (MVC pattern).
 *
 * WHY SEPARATE IT FROM LOGIC?
 * Because if tomorrow you want to add a new field (e.g., accountHolderName),
 * you only touch this class — not the logic classes.
 * This is the Single Responsibility Principle (SRP) in action.
 */
public class ATMSimulator {

    // ─── FIELDS ──────────────────────────────────────────────────────────────

    // 'private' means ONLY this class can directly read/write these values.
    // Other classes MUST use getters/setters — that's ENCAPSULATION.

    private double balance;       // Current account balance in KES
    private int pin;              // Current PIN (can change via changePin())
    private int adminPin;         // Admin PIN — separate from user PIN

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────

    /**
     * Constructor: called once when we do 'new ATMSimulator()'
     * We set the STARTING STATE of the ATM here.
     * Requirements say: starting balance = KES 50,000, PIN = 2026
     */
    public ATMSimulator() {
        this.balance  = 50_000.0;  // Underscores are legal in Java numerics — improves readability
        this.pin      = 2026;      // Correct PIN as per spec
        this.adminPin = 9999;      // Admin PIN for the bonus admin mode
    }

    // ─── GETTERS & SETTERS ───────────────────────────────────────────────────
    // These are the "controlled doors" to our private fields.
    // A getter READS a field. A setter WRITES a field.
    // We can add validation inside setters (e.g., reject negative balance).

    public double getBalance() {
        return balance;
    }

    /**
     * WHY validate here instead of in the implementation class?
     * Defence in depth — even if implementation has a bug, the model
     * will never allow an impossible negative balance.
     */
    public void setBalance(double balance) {
        if (balance < 0) {
            // Guard clause — refuse the update silently; the caller handles messaging
            throw new IllegalArgumentException("Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getAdminPin() {
        return adminPin;
    }
}