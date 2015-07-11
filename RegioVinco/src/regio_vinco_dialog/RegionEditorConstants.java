package regio_vinco_dialog;

import java.awt.Font;

/**
 * This class lists all of the constants used for running the
 * application. The point of doing this is that String Literals,
 * i.e. text in quotes in a program, are not buried somewhere deep
 * in the middle of a program, instead, they're all here, easy
 * to find and change if need be. The same goes for other constants
 * that are used for setting up the application colors, fonts, etc.
 * 
 * Note that this is not an ideal approach. A better approach still
 * would be loading all of this data from an external file, like an
 * XML file, but for now this will do.
 * 
 * @author  Richard McKenna
 *          Debugging Enterprises
 * @version 1.0
 */
public class RegionEditorConstants 
{
    // PATH SETTINGS
    public static final String	PATH_CSS    = "region_editor/css/";
    public static final String	PATH_DATA   = "regions_data/";
    public static final String	PATH_IMAGES = "images/";
    
    // USED FOR FINDING THE CSS AND IMAGES
    public static final String PROTOCOL_FILE	= "file:";
    
    // THE REGION EDITOR ICONS
    public static final String ICON_APP = "RegionEditorLogo.png";
    public static final String ICON_NEW = "New.png";
    public static final String ICON_OPEN = "Open.png";
    public static final String ICON_SAVE = "Save.png";
    public static final String ICON_SAVE_AS = "SaveAs.png";
    public static final String ICON_EXIT = "Exit.png";
    public static final String ICON_ADD_REGION = "AddRegion.png";
    public static final String ICON_REMOVE_REGION = "RemoveRegion.png";
    public static final String ICON_EDIT_REGION = "EditRegion.png";
    public static final String ICON_UPDATE = "Update.png";
    public static final String ICON_CANCEL = "Cancel.png";

    // BUTTON TOOLTIPS
    public static final String TOOLTIP_NEW      = "New World";
    public static final String TOOLTIP_OPEN     = "Open World";
    public static final String TOOLTIP_SAVE     = "Save World";
    public static final String TOOLTIP_SAVE_AS  = "Save As World";
    public static final String TOOLTIP_EXIT     = "Exit";
    public static final String TOOLTIP_ADD_REGION       = "Add Region";
    public static final String TOOLTIP_REMOVE_REGION    = "Remove Region";
    public static final String TOOLTIP_EDIT_REGION      = "Edit Region";
    public static final String TOOLTIP_UPDATE_REGION    = "Update Region";
    public static final String TOOLTIP_CANCEL_UPDATE    = "Cancel Update";    
        
    // FILE THAT MANAGES APP STYLE
    public static final String STYLESHEET_PRIMARY = PATH_CSS + "re_style.css";
    public static final String CLASS_BORDERED_PANE	= "bordered_pane";
    public static final String CLASS_HEADING_LABEL	= "heading_label";
    public static final String CLASS_SUBHEADING_LABEL	= "subheading_label";
    public static final String CLASS_PROMPT_LABEL	= "prompt_label";
    public static final String CLASS_TEXT_FIELD		= "text_field";
    public static final String CLASS_REGION_COMBO	= "region_combo";

    // WINDOW SETTINGS
    public static final int     WINDOW_WIDTH        = 900;
    public static final int     WINDOW_HEIGHT       = 600;
    public static final boolean WINDOW_IS_RESIZABLE = false;
    
    // THIS IS THE SCHEMA WE'LL USE
    public static final String  FILE_WORLD_SCHEMA = PATH_DATA + "WorldRegions.xsd";
    
    // TREE SETTINGS
    public static final String  INIT_WORLD_ROOT = "World";
      
    // REGION EDITOR HEADER AND LABEL TEXT
    public static final String  HEADER_REGION_EDITOR    = "REGION EDITOR";
    public static final String  PROMPT_ID           = "Id: ";
    public static final String  PROMPT_NAME         = "Name: ";
    public static final String  PROMPT_TYPE         = "Type: ";
    public static final String  PROMPT_CAPITAL      = "Capital: ";

    // APP FONTS
    public static final Font    REGION_EDITOR_HEADER_FONT   = new Font("Serif", Font.BOLD, 32);
    public static final Font    REGION_EDITOR_LABEL_FONT    = new Font("Serif", Font.BOLD, 20);
    public static final Font    REGION_EDITOR_INPUT_FONT    = new Font("Console", Font.PLAIN, 20);
  
    // SIZE AND POSITIONING SETTINGS FOR CONTROLS
    public static final int     REGION_EDITOR_CONTROLS_IPADX    = 5;
    public static final int     REGION_EDITOR_CONTROLS_IPADY    = 10;
    public static final double	SPLIT_PANE_LEFT_LOCATION        = .3;
    public static final int     REGION_TEXT_FIELD_COLUMNS       = 15;
 
    // WE'LL NEED THESE TO DYNAMICALLY BUILD TEXT
    public static final String EMPTY_TEXT                  = "";
    public static final String WORLD_FILE_EXTENSION         = ".xml";
    public static final String APP_NAME                     = "Region Editor";
    public static final String APP_NAME_FILE_NAME_SEPARATOR = " - ";
    public static final String PNG_FORMAT_NAME              = "png";
    public static final String PNG_FILE_EXTENSION           = "." + PNG_FORMAT_NAME;
       
   /***** DIALOG MESSAGES, PROMPTS, AND TITLES *****/
    public static final String MESSAGE_CLOSE_DIALOG		    = "Close";
    public static final String MESSAGE_NO_FILE_SELECTED                = "No File was Selected to Open";
    public static final String MESSAGE_WORLD_LOADED                    = "World File has been Loaded";
    public static final String MESSAGE_WORLD_LOADING_ERROR             = "An Error Occured While Loading the World";
    public static final String MESSAGE_WORLD_SAVED                     = "World File has been Saved";
    public static final String MESSAGE_WORLD_SAVING_ERROR              = "An Error Occured While Saving the World";
    public static final String MESSAGE_NO_REGION_NAME_PROVIDED       = "Error - No Region Name Provided";

    public static final String PROMPT_WORLD_NAME_REQUEST              = "What do you want to name your world?";
    public static final String TITLE_WORLD_NAME_REQUEST		= "Enter World File Name";
    public static final String PROMPT_SAVE                  = "Would you like to save your World?";
    public static final String TITLE_SAVE            = "Save your world?";
    public static final String TITLE_NO_REGION_NAME_PROVIDED	    = "No Region Name Provided";
    public static final String PROMPT_DUPLICATE_ID                  = "Illegal Change - Region Ids Must Be Unique";
    public static final String TITLE_DUPLICATE_ID		    = "Duplicate Region Id Error";
    public static final String PROMPT_ENTER_NEW_REGION_ID           = "What is the Id for your new Region?";
    public static final String TITLE_ENTER_NEW_REGION_ID	    = "Enter Region Id";
    public static final String PROMPT_INVALID_ID                    = "Invalid Region Id Provided";
    public static final String TITLE_INVALID_ID			    = "Invalid Id";
    
    public static final String PROMPT_A_OVERWRITE_FILE_REQUEST        = "There is already a file called \n";
    public static final String PROMPT_B_OVERWRITE_FILE_REQUEST        = "\nWould you like to overwrite it?";
    public static final String TITLE_OVERWRITE_FILE_REQUEST    = "Overwrite File?";  
}