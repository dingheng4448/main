package planmysem.data.slot;

import planmysem.data.exception.IllegalValueException;

/**
 * Represents a Slot's location in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValid(String)}
 */
public class Location {
    private static final String MESSAGE_CONSTRAINTS =
            "Slot's location should be spaces or alphanumeric characters";
    private static final String VALIDATION_REGEX = ".+";

    private final String value;

    /**
     * Validates given value.
     *
     * @throws IllegalValueException if given value string is invalid.
     */
    public Location(String value) throws IllegalValueException {
        if (value != null && !isValid(value)) {
            throw new IllegalValueException(MESSAGE_CONSTRAINTS);
        }
        this.value = value;
    }

    /**
     * Returns true if a given string is a valid.
     */
    public static boolean isValid(String value) {
        return "".equals(value) || value.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Location // instanceof handles nulls
                && this.value.equals(((Location) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
