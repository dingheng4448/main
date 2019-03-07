package planmysem.data.semester;

import java.time.DayOfWeek;
import java.util.ArrayList;

import planmysem.data.slot.Slot;

/**
 * Represents a Day in the planner.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Day implements ReadOnlyDay {
    private final DayOfWeek dayOfWeek;
    private final String type;
    private final ArrayList<Slot> slots = new ArrayList<>();

    /**
     * Assumption: Every field must be present and not null.
     * TODO: Ding Heng, please init type
     */
    public Day(DayOfWeek dayOfWeek, String weekType) {
        this.dayOfWeek = dayOfWeek;
        this.type = weekType;
    }

    public Day(DayOfWeek dayOfWeek, String weekType, ArrayList<Slot> slots) {
        this.dayOfWeek = dayOfWeek;
        this.type = weekType;

        for (Slot slot : slots) {
            this.slots.add(slot);
        }
    }

    public void addSlot(Slot slot) {
        slots.add(slot);
    }

    public void clear() {
        slots.clear();
    }

    @Override
    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    @Override
    public String getDay() {
        return dayOfWeek.toString();
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public ArrayList<Slot> getSlots() {
        return slots;
    }
}
