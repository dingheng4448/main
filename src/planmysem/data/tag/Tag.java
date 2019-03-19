package planmysem.data.tag;

import planmysem.data.exception.IllegalValueException;

/**
 * Represents a Tag in the Planner.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    private static final String MESSAGE_CONSTRAINTS = "Tags names should be alphanumeric";
    private static final String VALIDATION_REGEX = ".+";

    public final String value;

    /**
     * Validates given value.
     *
     * @throws IllegalValueException if the given tag name string is invalid.
     */
    public Tag(String name) throws IllegalValueException {
        String value = name.trim();
        if (!isValidTagName(value)) {
            throw new IllegalValueException(MESSAGE_CONSTRAINTS);
        }
        this.value = value;
    }

    /**
     * Returns true if a given value is a valid.
     */
    private static boolean isValidTagName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Tag // instanceof handles nulls
                && this.value.equals(((Tag) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

}
