package regio_vinco_dialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import static regio_vinco_dialog.RegionEditorConstants.CLASS_PROMPT_LABEL;
import static regio_vinco_dialog.RegionEditorConstants.STYLESHEET_PRIMARY;

/**
 * This class can be used for retrieving a single line of text
 * input from the user via a small dialog with a custom
 * prompt.
 */
public class InputDialog  extends Stage {    
    // GUI CONTROLS FOR OUR DIALOG
    GridPane gridPane;
    Scene dialogScene;
    Label promptLabel;
    TextField promptTextField;
    Button completeButton;
    Button cancelButton;
    
    // THIS IS FOR KEEPING TRACK OF WHICH BUTTON THE USER PRESSED
    String selection;
    
    // CONSTANTS FOR OUR UI
    public static final String COMPLETE = "Complete";
    public static final String CANCEL = "Cancel";
    
    /**
     * Initializes this dialog so that it can be used for either adding
     * new schedule items or editing existing ones.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    public InputDialog(Stage primaryStage, MessageDialog messageDialog) {       
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        initOwner(primaryStage);
        
        // FIRST OUR CONTAINER
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // NOW THE DESCRIPTION 
        promptLabel = new Label();
        promptLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        promptTextField = new TextField();

        // AND FINALLY, THE BUTTONS
        completeButton = new Button(COMPLETE);
        cancelButton = new Button(CANCEL);

        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        EventHandler completeCancelHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            Button sourceButton = (Button)ae.getSource();
            InputDialog.this.selection = sourceButton.getText();
            InputDialog.this.hide();
        };
        completeButton.setOnAction(completeCancelHandler);
        cancelButton.setOnAction(completeCancelHandler);

        // NOW LET'S ARRANGE THEM ALL AT ONCE
        gridPane.add(promptLabel,	0, 0, 4, 1);
        gridPane.add(promptTextField,	0, 1, 4, 1);
        gridPane.add(completeButton,	1, 2, 1, 1);
        gridPane.add(cancelButton,	2, 2, 1, 1);

        // AND PUT THE GRID PANE IN THE WINDOW
        dialogScene = new Scene(gridPane);
        dialogScene.getStylesheets().add(STYLESHEET_PRIMARY);
        this.setScene(dialogScene);
    }
    
    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either YES, NO, or CANCEL, depending on which
     * button the user selected when this dialog was presented.
     */
    public String getSelection() {
        return selection;
    }
    
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * @param message Message to appear inside the dialog.
     */
    public String showInputDialog(String title, String prompt) {
        // SET THE DIALOG TITLE
        setTitle(title);
        
        // SET THE PROMPT
	promptLabel.setText(prompt);
        
        // LOAD THE UI STUFF
        promptTextField.setText("");
        
        // AND OPEN IT UP
        this.showAndWait();
        
        return promptTextField.getText();
    }
    
    /**
     * Tests to see if the complete button was pressed, returns
     * true if it was, false otherwise.
     */
    public boolean wasCompleteSelected() {
        return selection.equals(COMPLETE);
    }
}