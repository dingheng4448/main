package planmysem.storage.jaxb;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import planmysem.common.exceptions.IllegalValueException;
import planmysem.model.Planner;

/**
 * JAXB-friendly adapted Planner model holder class.
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
        semester = new AdaptedSemester(source.getSemester());
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
        return semester.isAnyRequiredFieldMissing();
    }


    /**
     * Converts this jaxb-friendly {@code AdaptedPlanner} object into the corresponding(@code Planner} object.
     *
     * @throws IllegalValueException if there were any model constraints violated in the IcsSemester
     */
    public Planner toModelType() throws IllegalValueException {
        return new Planner(semester.toModelType());
    }
}
