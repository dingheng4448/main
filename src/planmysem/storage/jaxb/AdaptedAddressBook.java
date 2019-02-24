package planmysem.storage.jaxb;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import planmysem.data.Planner;
import planmysem.data.exception.IllegalValueException;
import planmysem.data.person.Person;
import planmysem.data.person.UniquePersonList;

/**
 * JAXB-friendly adapted address book data holder class.
 */
@XmlRootElement(name = "Planner")
public class AdaptedAddressBook {

    @XmlElement
    private List<AdaptedPerson> persons = new ArrayList<>();

    /**
     * No-arg constructor for JAXB use.
     */
    public AdaptedAddressBook() {
    }

    /**
     * Converts a given Planner into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created AdaptedAddressBook
     */
    public AdaptedAddressBook(Planner source) {
        persons = new ArrayList<>();
        source.getAllPersons().forEach(person -> persons.add(new AdaptedPerson(person)));
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
        return persons.stream().anyMatch(AdaptedPerson::isAnyRequiredFieldMissing);
    }


    /**
     * Converts this jaxb-friendly {@code AdaptedAddressBook} object into the corresponding(@code Planner} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Planner toModelType() throws IllegalValueException {
        final List<Person> personList = new ArrayList<>();
        for (AdaptedPerson person : persons) {
            personList.add(person.toModelType());
        }
        return new Planner(new UniquePersonList(personList));
    }
}
