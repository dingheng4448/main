package planmysem.data.slot;

import java.time.LocalTime;
import java.util.Set;

import planmysem.data.tag.Tag;

/**
 * A read-only immutable interface for a Person in the planmysem.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlySlot {
    Name getName();
    Location getLocation();
    Description getDescription();
    int getDuration();
    LocalTime getTime();

    /**
     * The returned {@code Set} is a deep copy of the internal {@code Set},
     * changes on the returned list will not affect the person's internal tags.
     */
    Set<Tag> getTags();

    /**
     * Returns true if the values inside this object is same as
     * those of the other (Note: interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlySlot other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getLocation().equals(this.getLocation())
                && other.getDescription().equals(this.getDescription())
                && other.getTime().equals(this.getTime()));
    }

    /**
     * Formats the slot as text, showing all contact details.
     */
    default String getAsTextShowAll() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName());

        builder.append(" Location: ");
        builder.append(getLocation());

        builder.append(" Description: ");
        builder.append(getDescription());

        builder.append(" Start Time: ");
        builder.append(getTime());

        builder.append(" Duration: ");
        builder.append(getDuration());

        builder.append(" Tags: ");
        for (Tag tag : getTags()) {
            builder.append(tag);
        }
        return builder.toString();
    }

    //    /**
    //     * Formats a person as text, showing only non-private contact details.
    //     */
    //    default String getAsTextHidePrivate() {
    //        final StringBuilder builder = new StringBuilder();
    //        builder.append(getName());
    //        if (!getLocation().isPrivate()) {
    //            builder.append(" Phone: ").append(getPhone());
    //        }
    //        if (!getDescription().isPrivate()) {
    //            builder.append(" Email: ").append(getEmail());
    //        }
    //        if (!getTime().isPrivate()) {
    //            builder.append(" Address: ").append(getAddress());
    //        }
    //        builder.append(" Tags: ");
    //        for (Tag tag : getTags()) {
    //            builder.append(tag);
    //        }
    //        return builder.toString();
    //    }
}
