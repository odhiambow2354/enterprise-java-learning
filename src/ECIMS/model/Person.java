package ECIMS.model;

/**
 * Abstract base class for all people in the system.
 * Centralises shared identity fields to avoid duplication across Customer and Employee hierarchies.
 */
public abstract class Person {

    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected String email;
    protected String dateOfBirth;
    protected String address;

    public Person(String firstName, String lastName, String phoneNumber,
                  String email, String dateOfBirth, String address) {
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.phoneNumber = phoneNumber;
        this.email       = email;
        this.dateOfBirth = dateOfBirth;
        this.address     = address;
    }

    // Each subclass must be able to describe itself
    public abstract String getRole();

    public String getFullName() {
        return firstName + " " + lastName;
    }

    // --- Getters ---
    public String getFirstName()   { return firstName; }
    public String getLastName()    { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail()       { return email; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getAddress()     { return address; }

    // --- Setters ---
    public void setFirstName(String firstName)     { this.firstName   = firstName; }
    public void setLastName(String lastName)       { this.lastName    = lastName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email)             { this.email       = email; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setAddress(String address)         { this.address     = address; }
}