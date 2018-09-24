package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Description;
import seedu.address.model.person.DueDate;
import seedu.address.model.person.PriorityValue;
import seedu.address.model.person.Name;
import seedu.address.model.person.Task;
import seedu.address.model.tag.Label;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Task objects.
 */
public class PersonBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "alice@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private DueDate phone;
    private PriorityValue email;
    private Description address;
    private Set<Label> tags;

    public PersonBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new DueDate(DEFAULT_PHONE);
        email = new PriorityValue(DEFAULT_EMAIL);
        address = new Description(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Task personToCopy) {
        name = personToCopy.getName();
        phone = personToCopy.getDueDate();
        email = personToCopy.getPriorityValue();
        address = personToCopy.getDescription();
        tags = new HashSet<>(personToCopy.getLabels());
    }

    /**
     * Sets the {@code Name} of the {@code Task} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Label>} and set it to the {@code Task} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getLabelSet(tags);
        return this;
    }

    /**
     * Sets the {@code Description} of the {@code Task} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Description(address);
        return this;
    }

    /**
     * Sets the {@code DueDate} of the {@code Task} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new DueDate(phone);
        return this;
    }

    /**
     * Sets the {@code PriorityValue} of the {@code Task} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new PriorityValue(email);
        return this;
    }

    public Task build() {
        return new Task(name, phone, email, address, tags);
    }

}
