package seedu.address.model.module;

import java.util.Objects;

import javafx.collections.ObservableList;
import seedu.address.model.group.Group;
import seedu.address.model.group.UniqueGroupList;

/**
 * Represents a module in the TAT.
 */
public class Module {
    private final String identifier;
    private final String name;
    private final UniqueGroupList groups;

    /**
     * Constructs a group object.
     *
     * @param identifier identifies the module. Usually equal
     *                   to the module code.
     */
    public Module(String identifier, String name) {
        this.identifier = identifier;
        this.name = name;
        groups = new UniqueGroupList();
    }

    /**
     * Adds a group to groups.
     */
    public void addGroup(Group group) {
        groups.add(group);
    }

    public boolean hasGroup(Group group) {
        return groups.contains(group);
    }

    /**
     * Gets group with given identifier.
     */
    public Group getGroup(String identifier) {
        Group group = null;
        for (int i = 0; i < groups.size(); ++i) {
            group = groups.get(i);
            if (group.getIdentifier().equals(identifier)) {
                break;
            }
        }
        return group;
    }

    /**
     * Returns the group list.
     */
    public ObservableList<Group> getGroupList() {
        return groups.asUnmodifiableObservableList();
    }

    /**
     * Returns the module identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns module name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns true if both modules have the same identifiers.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Module)) {
            return false;
        }

        Module otherModule = (Module) other;
        return otherModule.getIdentifier().equals(this.getIdentifier());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(identifier);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" with Module Code ")
                .append(getIdentifier());
        return builder.toString();
    }
}
