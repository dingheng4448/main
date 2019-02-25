package planmysem.storage.jaxb;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlValue;

import planmysem.common.Utils;
import planmysem.data.exception.IllegalValueException;
import planmysem.data.semester.Day;
import planmysem.data.semester.ReadOnlySemester;
import planmysem.data.semester.Semester;

/**
 * JAXB-friendly adapted person data holder class.
 */
public class AdaptedSemester {

    @XmlValue
    private String name;
    @XmlValue
    private String academicYear;
    @XmlValue
    private String startDate;
    @XmlValue
    private String endDate;
    @XmlValue
    private int noOfWeeks;
    @XmlElement
    private HashMap<LocalDate, AdaptedDay> days = new HashMap<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public AdaptedSemester() {
    }

    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created AdaptedPerson
     */
    public AdaptedSemester(ReadOnlySemester source) {
        name = source.getName();
        academicYear = source.getAcademicYear();
        startDate = source.getStartDate();
        endDate = source.getEndDate();
        noOfWeeks = source.getNoOfWeeks();

        days = new HashMap<>();
        for (Map.Entry<LocalDate, Day> day : source.getDays().entrySet()) {
            days.put(day.getKey(), new AdaptedDay(day.getValue()));
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
        for (Map.Entry<LocalDate, AdaptedDay> day : days.entrySet()) {
            if (day.getValue().isAnyRequiredFieldMissing()) {
                return true;
            }
        }

        // second call only happens if phone/email/address are all not null
        return Utils.isAnyNull(name, academicYear, days, startDate, endDate);
    }

    /**
     * Converts this jaxb-friendly adapted person object into the Person object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Semester toModelType() throws IllegalValueException {
        final String name = this.name;
        final String academicYear = this.academicYear;
        final String startDate = this.startDate;
        final String endDate = this.endDate;
        final int noOfWeeks = this.noOfWeeks;

        final HashMap<LocalDate, Day> days = new HashMap<>();
        for (Map.Entry<LocalDate, AdaptedDay> day : this.days.entrySet()) {
            days.put(day.getKey(), day.getValue().toModelType());
        }

        return new Semester(name, academicYear, days, startDate, endDate, noOfWeeks);
    }
}
