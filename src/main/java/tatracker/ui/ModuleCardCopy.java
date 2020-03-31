package tatracker.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import tatracker.model.module.Module;

/**
 * An UI component that displays information of a {@code Module}.
 */
public class ModuleCardCopy extends UiPart<Region> {

    private static final String FXML = "ModuleListCardCopy.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Module module;

    @javafx.fxml.FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label identifier;

    public ModuleCardCopy(Module module, int displayedIndex) {
        super(FXML);
        this.module = module;
        id.setText(displayedIndex + ". ");
        name.setText(module.getName());
        identifier.setText(module.getIdentifier());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof tatracker.ui.ModuleCardCopy)) {
            return false;
        }

        // state check
        tatracker.ui.ModuleCardCopy card = (tatracker.ui.ModuleCardCopy) other;
        return id.getText().equals(card.id.getText())
                && module.equals(card.module);
    }
}
