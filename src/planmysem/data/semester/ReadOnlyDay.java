package planmysem.data.semester;

import java.time.DayOfWeek;
import java.util.ArrayList;

import planmysem.data.slot.Slot;

/**
 * Represents a Day in the planner.
 * Guarantees: details are present and not null, field values are validated.
 */
public interface ReadOnlyDay {
    DayOfWeek getDayOfWeek();

    String getDay();

    String getType();

    ArrayList<Slot> getSlots();

    /**
     * Returns true if the values inside this object is same as those of the other
     * (Note: interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyDay other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getDayOfWeek().equals(this.getDayOfWeek()) // state checks here onwards
                && other.getDay().equals(this.getDay())
                && other.getType().equals(this.getType())
                && other.getSlots().equals(this.getSlots()));
    }
}
