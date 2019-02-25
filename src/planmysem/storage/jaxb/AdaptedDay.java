package planmysem.storage.jaxb;

import java.time.DayOfWeek;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

import planmysem.common.Utils;
import planmysem.data.exception.IllegalValueException;
import planmysem.data.semester.Day;
import planmysem.data.semester.ReadOnlyDay;
import planmysem.data.slot.Slot;

/**
 * JAXB-friendly adapted Day data holder class.
 */
public class AdaptedDay {

    @XmlValue
    private DayOfWeek dayOfWeek;
    @XmlElement(required = true)
    private ArrayList<AdaptedSlot> slots = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public AdaptedDay() {
    }

    /**
     * Converts a given Day into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created AdaptedDay
     */
    public AdaptedDay(ReadOnlyDay source) {
        dayOfWeek = source.getDayOfWeek();

        slots = new ArrayList<>();
        for (Slot slot : source.getSlots()) {
            slots.add(new AdaptedSlot(slot));
        }
    }

    /**
     * Returns true if any required field is missing.
     * <p>
     * JAXB does not enforce (required = true) without a given XML schema.
     * Since we do most of our validation using the data class constructors, the only extra logic we need
     * is to ensure that every xml element in the document is present. JAXB sets missing elements as null,
     * so we check for that.
     */
    public boolean isAnyRequiredFieldMissing() {
        for (AdaptedSlot slot : slots) {
            if (slot.isAnyRequiredFieldMissing()) {
                return true;
            }
        }
        // second call only happens if phone/email/address are all not null
        return Utils.isAnyNull(dayOfWeek, slots);
    }

    /**
     * Converts this jaxb-friendly adapted Day object into the Day object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted Day
     */
    public Day toModelType() throws IllegalValueException {
        final DayOfWeek dayOfWeek = this.dayOfWeek;

        final ArrayList<Slot> slots = new ArrayList<>();
        for (AdaptedSlot slot : this.slots) {
            slots.add(slot.toModelType());
        }

        return new Day(dayOfWeek, slots);
    }
}
