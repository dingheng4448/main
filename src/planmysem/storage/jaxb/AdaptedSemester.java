package planmysem.storage.jaxb;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;

import planmysem.common.exceptions.IllegalValueException;
import planmysem.model.semester.Day;
import planmysem.model.semester.ReadOnlySemester;
import planmysem.model.semester.Semester;

/**
 * JAXB-friendly adapted person model holder class.
 */
public class AdaptedSemester {
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String academicYear;
    @XmlElement(required = true)
    private String startDate;
    @XmlElement(required = true)
    private String endDate;
    @XmlElement(required = true)
    private int noOfWeeks;
    @XmlElement(required = true)
    private HashMap<String, AdaptedDay> days = new HashMap<>();
    @XmlElement(required = true)
    private Set<String> recessDays = new HashSet<>();
    @XmlElement(required = true)
    private Set<String> readingDays = new HashSet<>();
    @XmlElement(required = true)
    private Set<String> normalDays = new HashSet<>();
    @XmlElement(required = true)
    private Set<String> examDays = new HashSet<>();

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
        startDate = source.getStartDate().toString();
        endDate = source.getEndDate().toString();
        noOfWeeks = source.getNoOfWeeks();

        for (Map.Entry<LocalDate, Day> day : source.getDays().entrySet()) {
            days.put(day.getKey().toString(), new AdaptedDay(day.getValue()));
        }

        for (LocalDate date : source.getRecessDays()) {
            recessDays.add(date.toString());
        }

        for (LocalDate date : source.getReadingDays()) {
            readingDays.add(date.toString());
        }

        for (LocalDate date : source.getNormalDays()) {
            normalDays.add(date.toString());
        }

        for (LocalDate date : source.getExamDays()) {
            examDays.add(date.toString());
        }
    }

    /**
     * Returns true if any required field is missing.
     * <p>
     * JAXB does not enforce (required = true) without a given XML schema.
     * Since we do most of our validation using the model class constructors, the only extra logic we need
     * is to ensure that every xml element in the document is present. JAXB sets missing elements as null,
     * so we check for that.
     */
    public boolean isAnyRequiredFieldMissing() {
        for (Map.Entry<String, AdaptedDay> day : days.entrySet()) {
            if (day.getValue().isAnyRequiredFieldMissing()) {
                return true;
            }
        }

        // TODO: removed for testing
        // second call only happens if phone/email/address are all not null
        // return Utils.isAnyNull(name, academicYear, days, startDate, endDate);
        return false;
    }

    /**
     * Converts this jaxb-friendly adapted person object into the Person object.
     *
     * @throws IllegalValueException if there were any model constraints violated in the IcsSemester
     */
    public Semester toModelType() throws IllegalValueException {
        final String name = this.name;
        final String academicYear = this.academicYear;
        final String startDate = this.startDate;
        final String endDate = this.endDate;

        if (startDate == null || endDate == null) {
            throw new IllegalValueException("");
        }

        final int noOfWeeks = this.noOfWeeks;

        final HashMap<LocalDate, Day> days = new HashMap<>();
        for (Map.Entry<String, AdaptedDay> day : this.days.entrySet()) {
            days.put(LocalDate.parse(day.getKey()), day.getValue().toModelType());
        }

        final Set<LocalDate> recessDays = new HashSet<>();
        for (String date : this.recessDays) {
            recessDays.add(LocalDate.parse(date));
        }

        final Set<LocalDate> readingDays = new HashSet<>();
        for (String date : this.readingDays) {
            readingDays.add(LocalDate.parse(date));
        }

        final Set<LocalDate> normalDays = new HashSet<>();
        for (String date : this.normalDays) {
            normalDays.add(LocalDate.parse(date));
        }

        final Set<LocalDate> examDays = new HashSet<>();
        for (String date : this.examDays) {
            examDays.add(LocalDate.parse(date));
        }

        return new Semester(name, academicYear, days,
                LocalDate.parse(startDate), LocalDate.parse(endDate), noOfWeeks,
                recessDays, readingDays, normalDays, examDays);
    }
}
