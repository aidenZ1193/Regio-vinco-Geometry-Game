package regio_vinco;

import java.io.File;
import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import world_data.WorldDataManager;
import world_io.WorldIO;

/**
 * This is the Regio Vinco game application. Note that it extends the
 * PointAndClickGame class and overrides all the proper methods for setting up
 * the Data, the GUI, the Event Handlers, and update and timer task, the thing
 * that actually does the update scheduled rendering.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class RegioVinco extends Application {

    // THESE CONSTANTS SETUP THE GAME DIMENSIONS. THE GAME WIDTH
    // AND HEIGHT SHOULD MIRROR THE BACKGROUND IMAGE DIMENSIONS. WE
    // WILL NOT RENDER ANYTHING OUTSIDE THOSE BOUNDS.

    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;

    // FOR THIS APP WE'RE ONLY PLAYING WITH ONE MAP, BUT
    // IN THE FUTURE OUR GAMES WILL USE LOTS OF THEM
    //public static final String REGION_NAME = "Afghanistan";
    
   // public static final String AFG_MAP_FILE_PATH = MAPS_PATH + "GreyscaleAFG.png";
    

    // HERE ARE THE PATHS TO THE REST OF THE IMAGES WE'LL USE
    public static final String GUI_PATH = "./data/gui/";
    public static final String SPLASH_FILE_PATH = GUI_PATH+ "RegioVincoSplash.jpg";
    public static final String LOGO_FILE_PATH = GUI_PATH+"RegioVincoLogo.png";
    public static final String HELP_BUTTON_PATH = GUI_PATH+"RegioVincoHelpButton.png";
    public static final String SETTING_BUTTON_PATH = GUI_PATH+"RegioVincoSettingButton.png";
    
    public static final String MUSIC_MUTE_BUTTON_PATH = GUI_PATH+"RegioVincoMusicButton.png";
    public static final String SOUND_MUTE_BUTTON_PATH = GUI_PATH+"RegioVincoSoundButton.png";
    
    public static final String REGION_BUTTON_PATH = GUI_PATH+"RegioVincoRegionButton.png";
    public static final String CAPITAL_BUTTON_PATH = GUI_PATH+"RegioVincoCapitalButton.png";
    public static final String FLAG_BUTTON_PATH = GUI_PATH+"RegioVincoFlagButton.png";
    public static final String LEADER_BUTTON_PATH = GUI_PATH+"RegioVincoLeaderButton.png";
    public static final String STOP_BUTTON_PATH = GUI_PATH+"RegioVincoStopButton.png";
    
    public static final String RETURN_BUTTON_PATH = GUI_PATH+"RegioVincoReturnButton.jpg";
    public static final String BACKGROUND_FILE_PATH = GUI_PATH + "RegioVincoBackground.jpg";
    public static final String TITLE_FILE_PATH = GUI_PATH + "RegioVincoTitle.png";
    public static final String ENTER_BUTTON_FILE_PATH = GUI_PATH+"RegioVincoEnterButton.png";
    public static final String START_BUTTON_FILE_PATH = GUI_PATH + "RegioVincoStartButton.png";
    public static final String START_BUTTON_MO_FILE_PATH = GUI_PATH + "RegioVincoStartButtonMouseOver.png";
    public static final String EXIT_BUTTON_FILE_PATH = GUI_PATH + "RegioVincoExitButton.png";
    public static final String EXIT_BUTTON_MO_FILE_PATH = GUI_PATH + "RegioVincoExitButtonMouseOver.png";
    public static final String SUB_REGION_FILE_PATH = GUI_PATH + "RegioVincoSubRegion.png";
    public static final String WIN_DISPLAY_FILE_PATH = GUI_PATH + "RegioVincoWinDisplay.png";
    // .data/G..
    public static final String GAME_RECORD_PATH = "./data/Game Record.txt";
    
   // public static final String MAPS_PATH = "./data/The World/";
    public static final String FILES_PATH = "./data/";
    public static final String THE_WORLD_MAP = FILES_PATH+"The World Map.png";
    public static final String THE_WORLD_REGION = FILES_PATH+"The World Data.xml";
    public static final String REGION_SCHEMA = FILES_PATH+"RegionData.xsd";

    // HERE ARE SOME APP-LEVEL SETTINGS, LIKE THE FRAME RATE. ALSO,
    // WE WILL BE LOADING SpriteType DATA FROM A FILE, SO THAT FILE
    // LOCATION IS PROVIDED HERE AS WELL. NOTE THAT IT MIGHT BE A 
    // GOOD IDEA TO LOAD ALL OF THESE SETTINGS FROM A FILE, BUT ALAS,
    // THERE ARE ONLY SO MANY HOURS IN A DAY
    public static final int TARGET_FRAME_RATE = 30;
    public static final String APP_TITLE = "Regio Vinco";
    
    // LOGO 
    
    public static final int LOGO_X = 500;
    public static final int LOGO_Y = 0;
    
    // BACKGROUND IMAGE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    public static final int BACKGROUND_X = 0;
    public static final int BACKGROUND_Y = 0;
    
    public static final String HELP_TYPE = "HELP_TYPE";
    public static final String SETTING_TYPE = "SETTING_TYPE";
    public static final String RETURN_TO_MAP_TYPE = "RETURN_TO_MAP-TYPE";
    public static final int HELP_X=1130;
    public static final int HELP_Y= 50;
    public static final int SETTING_X=1080;
    public static final int SETTING_Y= 50;
    
    // mute buttons in setting
    public static final String MUSIC_TYPE = "MUSIC_TYPE";
    public static final String SOUND_TYPE = "SOUND_TYPE";
    public static final int MUSIC_X = 400;
    public static final int MUSIC_Y = 400;
    public static final int SOUND_X = 800;
    public static final int SOUND_Y = 400;
    
    // TITLE IMAGE
    public static final String TITLE_TYPE = "TITLE_TYPE";
    public static final int TITLE_X = 900;
    public static final int TITLE_Y = 0;
    
    // ENTER GAME BUTTON
    public static final String ENTER_TYPE = "ENTER_TYPE";
    public static final int ENTER_X = 520;
    public static final int ENTER_Y = 630;
    
    // all mode buttons
    public static final String REGION_TYPE = "REGION_TYPE";
    public static final int REGION_X = 900;
    public static final int REGION_Y = 100;
    public static final String CAPITAL_TYPE = "CAPITAL_TYPE";
    public static final int CAPITAL_X = 961;
    public static final int CAPITAL_Y = 100;
    public static final String FLAG_TYPE = "FLAG_TYPE";
    public static final int FLAG_X = 1007;
    public static final int FLAG_Y = 100;
    public static final String LEADER_TYPE = "LEADER_TYPE";
    public static final int LEADER_X = 1058;
    public static final int LEADER_Y = 100;
    public static final String STOP_TYPE = "STOP_TYPE";
    public static final int STOP_X = 1131;
    public static final int STOP_Y = 100;
    
    // START GAME BUTTON
    public static final String START_TYPE = "START_TYPE";
    public static final int START_X = 900;
    public static final int START_Y = 100;

    // EXIT GAME BUTTON
    public static final String EXIT_TYPE = "EXIT_TYPE";
    public static final int EXIT_X = 1050;
    public static final int EXIT_Y = 100;
    
    // THE GAME MAP LOCATION
    public static final String MAP_TYPE = "MAP_TYPE";
    public static final String SUB_REGION_TYPE = "SUB_REGION_TYPE";
    public static final int MAP_X = 0;
    public static final int MAP_Y = 0;
    
    public static final String FLAG_DISPLAY_TYPE = "FLAG_DISPLAY_TYPE";
    
    public static final String WORLD_MAP_TYPE = "WORLD_MAP_TYPE";

    // THE WIN DIALOG
    public static final String WIN_DISPLAY_TYPE = "WIN_DISPLAY";
    public static final int WIN_X = 350;
    public static final int WIN_Y = 150;
    
    // THIS IS THE X WHERE WE'LL DRAW ALL THE STACK NODES
    public static final int STACK_X = 900;
    public static final int STACK_INIT_Y = 600;
    public static final int STACK_INIT_Y_INC = 50;

    public static final Color REGION_NAME_COLOR = RegioVincoDataModel.makeColor(240, 240, 240);

    public static final int SUB_STACK_VELOCITY = 2;
    public static final int FIRST_REGION_Y_IN_STACK = GAME_HEIGHT - 50;

    public static final String AUDIO_DIR = "./data/audio/";
    public static final String AFGHAN_ANTHEM_FILE_NAME = AUDIO_DIR + "AfghanistanNationalAnthem.mid";
    public static final String SUCCESS_FILE_NAME = AUDIO_DIR + "Success.wav";
    public static final String FAILURE_FILE_NAME = AUDIO_DIR + "Failure.wav";
    public static final String TRACKED_FILE_NAME = AUDIO_DIR + "Tracked.wav";
    public static final String AFGHAN_ANTHEM = "AFGHAN_ANTHEM";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String TRACKED_SONG = "TRACKED_SONG";
    public static final String BACKGROUND_SONG = "BACKGROUND_SONG";
    public static final String BACKGROUND_FILE_NAME = AUDIO_DIR+ "viva la vida.wav";

    /**
     * This is where the RegioVinco application starts. It proceeds to make a
     * game and pass it the window, and then starts it.
     *
     * @param primaryStage The window for this JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
	
        WorldDataManager worldDataManager = new WorldDataManager();
	
	// INIT THE FILE I/O
        // AND OUR IMPORTER/EXPORTER
        File schemaFile = new File(REGION_SCHEMA);
        WorldIO worldIO = new WorldIO(schemaFile);
        worldDataManager.setWorldImporterExporter(worldIO);
        
        RegioVincoGame game = new RegioVincoGame(primaryStage,worldDataManager);
	game.startGame();
	
    }

    /**
     * The RegioVinco game application starts here. All game data and GUI
     * initialization is done through the constructor, so we will just construct
     * our game and set it visible to start it up.
     *
     * @param args command line arguments, which will not be used
     */
    public static void main(String[] args) {
	launch(args);
    }
}
