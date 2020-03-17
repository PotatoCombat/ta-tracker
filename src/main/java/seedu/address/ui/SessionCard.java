package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.session.Session;

/**
 * An UI component that displays information of a {@code Session}.
 */
public class SessionCard extends UiPart<Region> {

    private static final String FXML = "SessionListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Session session;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label sessionType;
    @FXML
    private Label duration;
    @FXML
    private Label date;
    @FXML
    private Label description;

    public SessionCard(Session session, int displayedIndex) {
        super(FXML);
        this.session = session;
        id.setText(displayedIndex + ". ");
        // dont really know how to convert enum to string?? ****
        sessionType.setText(session.getSessionType().SESSION_TYPE_CONSULTATION.toString());
        duration.setText(session.getStartTime().toString()
                + " - " + session.getEndTime().toString());
        date.setText(session.getDate().toString());
        description.setText(session.getDescription());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SessionCard)) {
            return false;
        }

        // state check
        SessionCard card = (SessionCard) other;
        return id.getText().equals(card.id.getText())
                && session.equals(card.session);
    }
}
