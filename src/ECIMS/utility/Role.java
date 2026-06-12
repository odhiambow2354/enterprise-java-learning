package ECIMS.utility;

/**
 * Defines the four staff roles and their system prefixes.
 * Using an enum prevents invalid role strings from ever entering the system.
 */
public enum Role {
    ADMINISTRATOR("Administrator", "ADM"),
    AGENT        ("Agent",         "AGT"),
    CLAIMS_OFFICER("Claims Officer","CLM"),
    FINANCE_OFFICER("Finance Officer","FIN");

    private final String displayName;
    private final String prefix;

    Role(String displayName, String prefix) {
        this.displayName = displayName;
        this.prefix      = prefix;
    }

    public String getDisplayName() { return displayName; }
    public String getPrefix()      { return prefix; }
}