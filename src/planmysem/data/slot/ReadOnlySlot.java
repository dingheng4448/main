package planmysem.data.slot;

import java.time.LocalTime;
import java.util.Set;

import planmysem.common.Utils;
import planmysem.data.tag.Tag;

/**
 * A read-only immutable interface for a Slot in the Planner.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlySlot {
    Name getName();
    Location getLocation();
    Description getDescription();
    int getDuration();
    LocalTime getStartTime();

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
                && other.getStartTime().equals(this.getStartTime())
                && other.getDuration() == this.getDuration()
                && other.getTags().equals(this.getTags()));
    }

    /**
     * Formats the slot as text, showing all contact details.
     */
    default String getAsTextShowAll() {
        final StringBuilder sb = new StringBuilder();

        sb.append("Slot Details:");

        sb.append("\n\tName: ");
        sb.append(getName());

        sb.append("\n\tLocation: ");
        sb.append(getLocation());

        sb.append("\n\tDescription: ");
        sb.append(getDescription());

        sb.append("\n\tTime: ");
        sb.append(getStartTime());

        sb.append(" to ");
        sb.append(Utils.getEndTime(getStartTime(), getDuration()));

        sb.append("\n\tDuration: ");
        sb.append(getDuration());

        sb.append("\n\tTags: ");
        for (Tag tag : getTags()) {
            sb.append("\n\t\t");
            sb.append(tag.toString());
        }
        return sb.toString();
    }
}
