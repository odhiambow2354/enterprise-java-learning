package model;

import utility.Role;

/**
 * Represents a staff member of NIC.
 * Bridges the Person hierarchy with the authentication system via Role enum.
 */
public abstract class Employee extends Person {

    private static int empCounter = 100;

    private final String employeeId;
    private String username;
    private String password;
    private final Role role;
    private boolean active;

    public Employee(String firstName, String lastName, String phoneNumber,
                    String email, String dateOfBirth, String address,
                    String username, String password, Role role) {
        super(firstName, lastName, phoneNumber, email, dateOfBirth, address);
        this.employeeId = role.getPrefix() + (++empCounter);
        this.username   = username;
        this.password   = password;
        this.role       = role;
        this.active     = true;
    }

    public boolean authenticate(String inputPassword) {
        // Compare plaintext for Phase I; swap in hashing for production
        return this.password.equals(inputPassword);
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    // --- Getters ---
    public String getEmployeeId() { return employeeId; }
    public String getUsername()   { return username; }
    public Role   getRoleEnum()   { return role; }
    public boolean isActive()     { return active; }

    /**
     * Returns the raw password for file serialisation only.
     * Named explicitly to discourage accidental use outside the persistence layer.
     */
    public String getPasswordForStorage() { return password; }

    // --- Setters ---
    public void setUsername(String username) { this.username = username; }
    public void setActive(boolean active)    { this.active   = active; }

    @Override
    public String getRole() { return role.getDisplayName(); }
}