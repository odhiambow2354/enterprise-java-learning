package model;

import java.time.LocalDate;

/**
 * Represents an insured customer of NIC.
 * Extends Person to inherit shared identity fields.
 */
public class Customer extends Person {

    private static int idCounter = 1000;  // Auto-incrementing base for customer IDs

    private final String customerId;
    private String nationalId;
    private final LocalDate registrationDate;

    public Customer(String nationalId, String firstName, String lastName,
                    String phoneNumber, String email, String dateOfBirth, String address) {
        super(firstName, lastName, phoneNumber, email, dateOfBirth, address);
        this.customerId       = "C" + (++idCounter);
        this.nationalId       = nationalId;
        this.registrationDate = LocalDate.now();
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public String toString() {
        return String.format(
                "| %-8s | %-15s | %-20s | %-12s | %-25s | %-12s |",
                customerId, nationalId, getFullName(), phoneNumber, email, registrationDate
        );
    }

    // --- Getters ---
    public String getCustomerId()       { return customerId; }
    public String getNationalId()       { return nationalId; }
    public LocalDate getRegistrationDate() { return registrationDate; }

    // --- Setters (ID and registration date are immutable) ---
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }
}