package planmysem.data.slot;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValid(String)}
 */
public class Location {
    private boolean isPrivate;

    public boolean isPrivate() {
        return isPrivate;
    }

}
