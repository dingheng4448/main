package planmysem.data.slot;

import planmysem.data.exception.IllegalValueException;

/**
 * Represents a Slot's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValid(String)}
 */
public class Name {

    public static final String EXAMPLE = "CS2113T Meeting";
    public static final String MESSAGE_NAME_CONSTRAINTS = "Slot names should be spaces or alphanumeric characters";
    public static final String NAME_VALIDATION_REGEX = "[\\p{Alnum} ]+";

    public final String value;

    /**
     * Validates given name.
     *
     * @throws IllegalValueException if given name string is invalid.
     */
    public Name(String value) throws IllegalValueException {
        String name = value.trim();
        if (!isValid(name)) {
            throw new IllegalValueException(MESSAGE_NAME_CONSTRAINTS);
        }
        this.value = name;
    }

    /**
     * Returns true if a given string is a valid slot name.
     */
    public static boolean isValid(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    /**
     * Retrieves a listing of every word in the name, in order.
     */
    //    public List<String> getWordsInValue() {
    //        return Arrays.asList(value.split("\\s+"));
    //    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                && this.value.equals(((Name) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
