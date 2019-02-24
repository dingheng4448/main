package planmysem.data.slot;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import planmysem.data.person.Name;
import planmysem.data.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Slot implements ReadOnlySlot {
    private final Set<Tag> tags = new HashSet<>();
    private Name name;
    private Location location;
    private Description description;
    private DateTime dateTime;

    //    private Phone phone;
    //    private Email email;
    //    private Address address;

    /**
     * Assumption: Every field must be present and not null.
     */
    public Slot(Name name, Location location, Description description, DateTime dateTime, Set<Tag> tags) {
        this.name = name;
        this.location = location;
        this.description = description;
        this.dateTime = dateTime;
        //        this.phone = phone;
        //        this.email = email;
        //        this.address = address;
        this.tags.addAll(tags);
    }

    /**
     * Copy constructor.
     */
    public Slot(ReadOnlySlot source) {
        this(source.getName(), source.getLocation(), source.getDescription(), source.getDateTime(), source.getTags());
    }

    @Override
    public Name getName() {
        return name;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Description getDescription() {
        return description;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public Set<Tag> getTags() {
        return new HashSet<>(tags);
    }

    /**
     * Replaces this person's tags with the tags in {@code replacement}.
     */
    public void setTags(Set<Tag> replacement) {
        tags.clear();
        tags.addAll(replacement);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlySlot // instanceof handles nulls
                && this.isSameStateAs((ReadOnlySlot) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, location, description, dateTime, tags);
    }

    @Override
    public String toString() {
        return getAsTextShowAll();
    }

}
