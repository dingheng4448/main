package planmysem.storage.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import planmysem.data.Planner;
import planmysem.data.exception.IllegalValueException;
import planmysem.data.semester.Semester;

/**
 * JAXB-friendly adapted Planner data holder class.
 */
@XmlRootElement(name = "Planner")
public class AdaptedPlanner {
    @XmlElement
    private AdaptedSemester semester = new AdaptedSemester();

    /**
     * No-arg constructor for JAXB use.
     */
    public AdaptedPlanner() {
    }

    /**
     * Converts a given Planner into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created AdaptedPlanner
     */
    public AdaptedPlanner(Planner source) {
        semester = new AdaptedSemester();
        semester = new AdaptedSemester(source.getSemester());
    }
    //    public AdaptedAddressBook(AddressBook source) {
    //        persons = new ArrayList<>();
    //        source.getAllPersons().forEach(person -> persons.add(new AdaptedPerson(person)));
    //    }

    /**
     * Returns true if any required field is missing.
     * <p>
     * JAXB does not enforce (required = true) without a given XML schema.
     * Since we do most of our validation using the data class constructors, the only extra logic we need
     * is to ensure that every xml element in the document is present. JAXB sets missing elements as null,
     * so we check for that.
     */
    public boolean isAnyRequiredFieldMissing() {
        return semester.isAnyRequiredFieldMissing();
    }


    /**
     * Converts this jaxb-friendly {@code AdaptedPlanner} object into the corresponding(@code Planner} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Planner toModelType() throws IllegalValueException {
        return new Planner(new Semester(semester.toModelType()));
    }
}
