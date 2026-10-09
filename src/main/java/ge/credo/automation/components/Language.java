package ge.credo.automation.components;

/**
 * Language categories for the manually supplied username test data.
 */
public enum Language {
    GEORGIAN("Georgian", "ნინი"),
    MEGRELIAN("Megrelian", "მადიშა"),
    SVAN("Svan", "თარაშ");

    private final String displayName;
    private final String username;

    Language(String displayName, String username) {
        this.displayName = displayName;
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getUsername() {
        return username;
    }
}
