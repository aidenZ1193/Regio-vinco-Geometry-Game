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
    Pane labelLayer;
    Label timer ;
    Long endTime;
    long start;
    
                    Text time;// = new Text();//dataModel.getTimer();
                Text region;// = new Text();
                 Text subRegions;// = new Text();
                Text guesses;// = new Text();
                Text score;// = new Text();
    boolean add = false;
    boolean end = false;
    boolean isPlayed = false;
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
        
                    labelLayer = new Pane();
                    labelLayer.setMinSize(500, 400);
                    labelLayer.setMaxSize(500, 400);
                    addStackPaneLayer(labelLayer);
                    labelLayer.setLayoutX(350);
                    labelLayer.setLayoutY(200);
                    labelLayer.setVisible(false);
                    time = new Text();//dataModel.getTimer();
                   region = new Text();
                   subRegions = new Text();
                   guesses = new Text();
                   score = new Text();
                   
                             labelLayer.getChildren().add(time);
                             labelLayer.getChildren().add(region);
                             labelLayer.getChildren().add(subRegions);
                             labelLayer.getChildren().add(score);
                             labelLayer.getChildren().add(guesses);
                             add = true;
                        
                //    addLabels();
                    
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
                    ImageView mapView = guiImages.get(MAP_TYPE);
                    mapView.setVisible(true);
                    labelLayer.setVisible(false);
                    block(false);
	// AND RESET ALL GAME DATA
                   // RegioVincoDataModel dataModel = (RegioVincoDataModel)data;
                    //dataModel.setAdded(false);
	data.reset(this);
                    isPlayed = false;
                    end = false;
                    add = false;
                    audio.stop(AFGHAN_ANTHEM);
                    audio.play(TRACKED_SONG, true);
                    
                    //audio.play(AFGHAN_ANTHEM, false);
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
                   // System.out.println(data.getGameState());
                   RegioVincoDataModel dataModel = (RegioVincoDataModel)data;
                   
                   if(dataModel.getRegionsFound() == 34){
                       if(!end){
                            data.endGameAsWin();
                            endTime = System.currentTimeMillis();
                            addLabels();
                            end = true;
                        }
                   }
	if (data.won()) {
	    ImageView winImage = guiImages.get(WIN_DISPLAY_TYPE);
	    winImage.setVisible(true);
                        labelLayer.setVisible(true);
                        block(true);
                      // gameLayer.setVisible(false);
                   ImageView mapView = guiImages.get(MAP_TYPE);
                        mapView.setVisible(false);
                        dataModel.getRegionFound().setVisible(false);
                        dataModel.getTimer().setVisible(false);
                        dataModel.getRegionNotFound().setVisible(false);
                        dataModel.getWrongGuess().setVisible(false);
                        if(!audio.isPlaying(AFGHAN_ANTHEM)){
                        //audio.play(TRACKED_SONG, false);
                        audio.play(AFGHAN_ANTHEM, true);
                       // isPlayed = true;
                        }
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
    
    public void addLabels(){
        RegioVincoDataModel dataModel = (RegioVincoDataModel)data;
                       
                        
                        
                        time.setLayoutX(70);//380);
                        time.setLayoutY(220);//200);
                        time.setText("Game Duration: "+dataModel.getSecondsAsTimeText(endTime/1000-dataModel.getStart()/1000));
                        time.setFill(Color.NAVY);
                        time.setFont(Font.font("BookAntiqua",FontWeight.BOLD,22));
                        
                        region.setLayoutX(70);//380);
                        region.setLayoutY(180);//200);
                        region.setText("Region: "+dataModel.getRegionName());
                        region.setFill(Color.NAVY);
                        region.setFont(Font.font("BookAntiqua",FontWeight.BOLD,22));
                        
                        subRegions.setLayoutX(70);//380);
                        subRegions.setLayoutY(300);//200);
                        subRegions.setText("Sub Regions: "+dataModel.getNumberOfSubRegions());
                        subRegions.setFill(Color.NAVY);
                        subRegions.setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                        
                        score.setLayoutX(70);//380);
                        score.setLayoutY(260);//200);
                        score.setText("Score: "+score());
                        score.setFill(Color.NAVY);
                        score.setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                        
                        guesses.setLayoutX(70);//380);
                        guesses.setLayoutY(340);//200);
                        guesses.setText("Incorrect Guesses: "+dataModel.getWrong());
                        guesses.setFill(Color.NAVY);
                        guesses.setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                     
    }
   
   public String score(){
       RegioVincoDataModel dataModel = (RegioVincoDataModel)data;
        int score = 0;
        score = 10000-(((int)(long)endTime/1000-(int)(long)dataModel.getStart()/1000));
        score = score-(100*dataModel.getWrong());
        if(score<0)
            score = 0;
        return String.valueOf(score);
    }
   
   public void block(boolean b){
       Label la = new Label();
       guiLayer.getChildren().add(la);
                    la.setPrefSize(290, 50);
                   
                    la.setLayoutX(STACK_X);
                    la.setLayoutY(150);
                    la.setVisible(b);
                    la.setStyle("-fx-background-color: black");
                    
   }
}
