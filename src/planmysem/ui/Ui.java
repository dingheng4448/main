package planmysem.ui;

import java.io.IOException;

import javafx.stage.Stage;

/**
 * API of UI component
 */
public interface Ui {

    /** Starts the UI (and the App).  */
    void start(Stage stage, Stoppable mainApp) throws IOException;

}
