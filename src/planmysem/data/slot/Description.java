package planmysem.data.slot;

import planmysem.data.exception.IllegalValueException;

/**
 * Represents a Slot's description in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValid(String)}
 */
public class Description {
    public static final String EXAMPLE = "Meeting with John Doe regarding CS2113T project.";
    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Slot's description should be spaces or alphanumeric characters";
    public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum} ]+";

    public final String value;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Description(String value) throws IllegalValueException {
        String description = value.trim();
        if (!isValid(description)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.value = description;
    }

    /**
     * Returns true if a given string is a valid Slot description.
     */
    public static boolean isValid(String value) {
        return value.matches(NAME_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && this.value.equals(((Description) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
