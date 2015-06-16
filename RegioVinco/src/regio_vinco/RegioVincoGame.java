package regio_vinco;

import audio_manager.AudioManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pacg.PointAndClickGame;
import static regio_vinco.RegioVinco.*;

/**
 * This class is a concrete PointAndClickGame, as specified in The PACG
 * Framework. Note that this one plays Regio Vinco.
 *
 * @author McKillaGorilla
 */
public class RegioVincoGame extends PointAndClickGame {

    // THIS PROVIDES GAME AND GUI EVENT RESPONSES
    RegioVincoController controller;

    // THIS PROVIDES MUSIC AND SOUND EFFECTS
    AudioManager audio;
    
    // THESE ARE THE GUI LAYERS
    Pane backgroundLayer;
    Pane gameLayer;
    Pane guiLayer;
    Label timer ;
    long start;
 //   RegioVincoDataModel dataModel = new RegioVincoDataModel();
    /**
     * Get the game setup.
     */
    public RegioVincoGame(Stage initWindow) {
	super(initWindow, APP_TITLE, TARGET_FRAME_RATE);
	initAudio();
    }
    
    public AudioManager getAudio() {
	return audio;
    }
    
    public Pane getGameLayer() {
	return gameLayer;
    }
    public Pane getGuiLayer(){
                    return guiLayer;
    }

    public Pane getBackgroundLayer(){
                     return backgroundLayer;
    }
    /**
     * Initializes audio for the game.
     */
    private void initAudio() {
	audio = new AudioManager();
	try {
	    audio.loadAudio(TRACKED_SONG, TRACKED_FILE_NAME);
	    audio.play(TRACKED_SONG, true);

	    audio.loadAudio(AFGHAN_ANTHEM, AFGHAN_ANTHEM_FILE_NAME);
	    audio.loadAudio(SUCCESS, SUCCESS_FILE_NAME);
	    audio.loadAudio(FAILURE, FAILURE_FILE_NAME);
	} catch (Exception e) {
	    
	}
    }

    // OVERRIDDEN METHODS - REGIO VINCO IMPLEMENTATIONS
    // initData
    // initGUIControls
    // initGUIHandlers
    // reset
    // updateGUI
    /**
     * Initializes the complete data model for this application, forcing the
     * setting of all game data, including all needed SpriteType objects.
     */
    @Override
    public void initData() {
	// INIT OUR DATA MANAGER
	data = new RegioVincoDataModel();
	data.setGameDimensions(GAME_WIDTH, GAME_HEIGHT);

	boundaryLeft = 0;
	boundaryRight = GAME_WIDTH;
	boundaryTop = 0;
	boundaryBottom = GAME_HEIGHT;
    }

    /**
     * For initializing all GUI controls, specifically all the buttons and
     * decor. Note that this method must construct the canvas with its custom
     * renderer.
     */
    @Override
    public void initGUIControls() {
	// LOAD THE GUI IMAGES, WHICH INCLUDES THE BUTTONS
	// THESE WILL BE ON SCREEN AT ALL TIMES
                   // data.setGameDimensions(GAME_WIDTH, GAME_HEIGHT);
                    start = System.currentTimeMillis();
	backgroundLayer = new Pane();
	addStackPaneLayer(backgroundLayer);
	addGUIImage(backgroundLayer, BACKGROUND_TYPE, loadImage(BACKGROUND_FILE_PATH), BACKGROUND_X, BACKGROUND_Y);
	
//                    timer = new Label();
//                    Label regionFound = new Label();
//                    Label regionNotFound = new Label();
//                    
//                    timer.setLayoutX(20);
//                    timer.setLayoutY(650);
//                    Text node1 = new Text(((RegioVincoDataModel)data).getSecondsAsTimeText(System.currentTimeMillis()-start));
//	node1.setFill(Color.ORANGERED);
//                    node1.setFont(Font.font("Book Antiqua",FontWeight.BOLD, 26));
//                    timer.setGraphic(node1);
//                                        
//                    regionFound.setLayoutX(200);
//                    regionFound.setLayoutY(650);
//                    Text node2 = new Text(String.valueOf(((RegioVincoDataModel)data).getRegionsFound()));
//                    node2.setFill(Color.ORANGERED);
//                    node2.setFont(Font.font("Book Antiqua",FontWeight.BOLD, 26));
//                    regionFound.setGraphic(node2);
//                    
//                    regionNotFound.setLayoutX(300);
//                    regionNotFound.setLayoutY(650);
//                    Text node3 = new Text(String.valueOf(((RegioVincoDataModel)data).getRegionsNotFound()));
//                    node3.setFill(Color.ORANGERED);
//                    node3.setFont(Font.font("Book Antiqua",FontWeight.BOLD, 26));
//                    regionNotFound.setGraphic(node3);
                    
                                     
	// THEN THE GAME LAYER
	gameLayer = new Pane();
	addStackPaneLayer(gameLayer);
	
	// THEN THE GUI LAYER
	guiLayer = new Pane();

	addStackPaneLayer(guiLayer);
	addGUIImage(guiLayer, TITLE_TYPE, loadImage(TITLE_FILE_PATH), TITLE_X, TITLE_Y);
	addGUIButton(guiLayer, START_TYPE, loadImage(START_BUTTON_FILE_PATH), START_X, START_Y);
	addGUIButton(guiLayer, EXIT_TYPE, loadImage(EXIT_BUTTON_FILE_PATH), EXIT_X, EXIT_Y);
	//Label la = new Label();
                    
	// NOTE THAT THE MAP IS ALSO AN IMAGE, BUT
	// WE'LL LOAD THAT WHEN A GAME STARTS, SINCE
	// WE'LL BE CHANGING THE PIXELS EACH TIME
	// FOR NOW WE'LL JUST LOAD THE ImageView
	// THAT WILL STORE THAT IMAGE
	ImageView mapView = new ImageView();
	mapView.setX(MAP_X);
	mapView.setY(MAP_Y);
	guiImages.put(MAP_TYPE, mapView);
	guiLayer.getChildren().add(mapView);
                    // add timer and the rest of the things down the screen
                  //  String timer = data.getSecondsAsTimeText(second);
                    
	// NOW LOAD THE WIN DISPLAY, WHICH WE'LL ONLY
	// MAKE VISIBLE AND ENABLED AS NEEDED
	ImageView winView = addGUIImage(guiLayer, WIN_DISPLAY_TYPE, loadImage(WIN_DISPLAY_FILE_PATH), WIN_X, WIN_Y);
	winView.setVisible(false);
    }
    
    // HELPER METHOD FOR LOADING IMAGES
    private Image loadImage(String imagePath) {	
	Image img = new Image("file:" + imagePath);
	return img;
    }

    /**
     * For initializing all the button handlers for the GUI.
     */
    @Override
    public void initGUIHandlers() {
	controller = new RegioVincoController(this);

	Button startButton = guiButtons.get(START_TYPE);
	startButton.setOnAction(e -> {
	    controller.processStartGameRequest();
	});

	Button exitButton = guiButtons.get(EXIT_TYPE);
	exitButton.setOnAction(e -> {
	    controller.processExitGameRequest();
	});

	// MAKE THE CONTROLLER THE HOOK FOR KEY PRESSES
	keyController.setHook(controller);

	// SETUP MOUSE PRESSES ON THE MAP
	ImageView mapView = guiImages.get(MAP_TYPE);
	mapView.setOnMousePressed(e -> {
	    controller.processMapClickRequest((int) e.getX(), (int) e.getY());
	});
	
	// KILL THE APP IF THE USER CLOSES THE WINDOW
	window.setOnCloseRequest(e->{
	    controller.processExitGameRequest();
	});
    }

    /**
     * Called when a game is restarted from the beginning, it resets all game
     * data and GUI controls so that the game may start anew.
     */
    @Override
    public void reset() {
	// IF THE WIN DIALOG IS VISIBLE, MAKE IT INVISIBLE
	ImageView winView = guiImages.get(WIN_DISPLAY_TYPE);
	winView.setVisible(false);

	// AND RESET ALL GAME DATA
	data.reset(this);
    }

    /**
     * This mutator method changes the color of the debug text.
     *
     * @param initColor Color to use for rendering debug text.
     */
    public static void setDebugTextColor(Color initColor) {
//        debugTextColor = initColor;
    }

    /**
     * Called each frame, this method updates the rendering state of all
     * relevant GUI controls, like displaying win and loss states and whether
     * certain buttons should be enabled or disabled.
     */
    int backgroundChangeCounter = 0;

    @Override
    public void updateGUI() {
	// IF THE GAME IS OVER, DISPLAY THE APPROPRIATE RESPONSE
	if (data.won()) {
	    ImageView winImage = guiImages.get(WIN_DISPLAY_TYPE);
	    winImage.setVisible(true);
	}
    }

    public void reloadMap() {
	Image tempMapImage = loadImage(AFG_MAP_FILE_PATH);
	PixelReader pixelReader = tempMapImage.getPixelReader();
	WritableImage mapImage = new WritableImage(pixelReader, (int) tempMapImage.getWidth(), (int) tempMapImage.getHeight());
	
                    for(int i = 0; i<mapImage.getWidth();i++){
                        for(int j = 0; j<mapImage.getHeight();j++){
                            Color c = pixelReader.getColor(i, j);
                            Color c2 = Color.rgb(220, 110, 0);
                            if(c.equals(c2))
                                mapImage.getPixelWriter().setColor(i, j,Color.BLACK);
                        }
                    }
                    
                    ImageView mapView = guiImages.get(MAP_TYPE);
	mapView.setImage(mapImage);
	int numSubRegions = ((RegioVincoDataModel) data).getRegionsFound() + ((RegioVincoDataModel) data).getRegionsNotFound();
	this.boundaryTop = -(numSubRegions * 50);

	// AND GIVE THE WRITABLE MAP TO THE DATA MODEL
	((RegioVincoDataModel) data).setMapImage(mapImage);
    }
}
