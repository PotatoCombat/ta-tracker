package tatracker.ui;

import static tatracker.model.TaTracker.getCurrentlyShownModule;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import tatracker.commons.core.LogsCenter;
import tatracker.model.module.Module;

/**
 * Panel containing the list of modules.
 */
public class ModuleListPanel extends UiPart<Region> {
    private static final String FXML = "ModuleListPanel.fxml";
    private static final String BACKGROUND_COLOUR = "#424d5f";
    private static final String BORDER_COLOUR = "#3e7b91";
    private static final String BORDER_WIDTH = "1";

    private final Logger logger = LogsCenter.getLogger(ModuleListPanel.class);

    @FXML
    private ListView<Module> moduleListView;

    public ModuleListPanel(ObservableList<Module> moduleList) {
        super(FXML);
        moduleListView.setItems(moduleList);
        moduleListView.setCellFactory(listView -> new ModuleListViewCell());
    }

    /**
     * Update ListCells in order to facilitate highlighting when a filter command is entered
     * @param moduleList the updated moduleList
     */
    public void updateCells(ObservableList<Module> moduleList) {
        System.out.print("reached updateCells");
        moduleListView.setItems(moduleList);
        moduleListView.setCellFactory(listView -> new ModuleListViewCell());
    }

    /**
    * Custom {@code ListCell} that displays the graphics of a {@code Module} using a {@code ModuleCard}.
    */
    class ModuleListViewCell extends ListCell<Module> {
        @Override
        protected void updateItem(Module module, boolean empty) {
            super.updateItem(module, empty);

            if (empty || module == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new ModuleCard(module, getIndex() + 1).getRoot());
                if (module.equals(getCurrentlyShownModule())) {
                    setStyle("-fx-background-color: " + BACKGROUND_COLOUR + "; "
                            + "-fx-border-color: " + BORDER_COLOUR + "; "
                            + "-fx-border-width: " + BORDER_WIDTH + ";");
                }
            }
        }
    }
}
