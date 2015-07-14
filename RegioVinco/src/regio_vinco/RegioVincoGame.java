package regio_vinco;

import audio_manager.AudioManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
import static regio_vinco.GameMode.DISPLAY_MODE;
import static regio_vinco.RegioVinco.*;
import regio_vinco_dialog.ConfirmDialog;
import world_data.Region;
import world_data.WorldDataManager;
import world_io.WorldIO;

/**
 * This class is a concrete PointAndClickGame, as specified in The PACG
 * Framework. Note that this one plays Regio Vinco.
 *
 * @author McKillaGorilla
 */
public class RegioVincoGame extends PointAndClickGame {

    // THIS PROVIDES GAME AND GUI EVENT RESPONSES
    RegioVincoController controller;
    WorldDataManager worldDataManager;
    WorldIO worldIO;
    // THIS PROVIDES MUSIC AND SOUND EFFECTS
    AudioManager audio;
    
    // THESE ARE THE GUI LAYERS
    Pane backgroundLayer;
    Pane gameLayer;
    Text mapName;
    //Text ancestorText;
    private ArrayList<Text> ancestorList;
    Pane guiLayer;
    Pane labelLayer;
    
    Pane splashLayer;
    Pane helpLayer;
    Pane settingLayer;
    Label timer ;
    Long endTime;
    long start;
    
                    Text time;// = new Text();//dataModel.getTimer();
                Text region;// = new Text();
                 Text subRegions;// = new Text();
                Text guesses;// = new Text();
                Text score;// = new Text();
                Label flag;
    boolean add = false;
    boolean end = false;
    boolean isPlaying = false;
    // key is map name.
    private HashMap<String, Image> mapTable;
    private HashMap<String, Image> flagTable;
    // To store all loaded xml file with data manager. key is file name
    private HashMap<String, WorldDataManager> regionTable;
    
    private ArrayList<String> path;
    
    private ConfirmDialog confirmDialog;
    
    private GameMode gamingMode;
 //   RegioVincoDataModel dataModel = new RegioVincoDataModel();
    /**
     * Get the game setup.
     */
    public RegioVincoGame(Stage initWindow, WorldDataManager mana) {
	super(initWindow, APP_TITLE, TARGET_FRAME_RATE);
	initAudio();
                    worldDataManager = mana;
                    
                    mapTable=new HashMap<>();
                    flagTable = new HashMap<>();
                    regionTable=new HashMap<>();
                    path = new ArrayList();
                    confirmDialog = new ConfirmDialog(initWindow);
                    gamingMode = GameMode.DISPLAY_MODE;
                    buttonControl(gamingMode);
    }
    
    public AudioManager getAudio() {
	return audio;
    }
    
    public Pane getSplashLayer(){
        return splashLayer;
    }
    
    public Pane getGameLayer() {
	return gameLayer;
    }
    public Pane getGuiLayer(){
                    return guiLayer;
    }

    public Pane getLabelLayer() {
        return labelLayer;
    }

    public Pane getHelpLayer() {
        return helpLayer;
    }

    public Pane getSettingLayer() {
        return settingLayer;
    }

    public Pane getBackgroundLayer(){
                     return backgroundLayer;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public boolean isIsPlaying() {
        return isPlaying;
    }

    public GameMode getGamingMode() {
        return gamingMode;
    }

    public void setGamingMode(GameMode gamingMode) {
        this.gamingMode = gamingMode;
    }

    public void setIsPlaying(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }
    public void changeGameMode(GameMode mode){
        gamingMode = mode;
    }
    /**
     * Initializes audio for the game.
     */
    private void initAudio() {
	audio = new AudioManager();
	try {
	    audio.loadAudio(BACKGROUND_SONG, BACKGROUND_FILE_NAME);
	    audio.play(BACKGROUND_SONG, true);
                    System.out.println("inside initAudio of game.");
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
                    
                    splashLayer = new Pane();
                    addStackPaneLayer(splashLayer);
                    addGUIImage(splashLayer,BACKGROUND_TYPE, loadImage(SPLASH_FILE_PATH), BACKGROUND_X, BACKGROUND_Y);
                    addGUIImage(splashLayer, TITLE_TYPE, loadImage(LOGO_FILE_PATH), LOGO_X, LOGO_Y);
                    addGUIButton(splashLayer, ENTER_TYPE, loadImage(ENTER_BUTTON_FILE_PATH), ENTER_X, ENTER_Y);
                    
	// LOAD THE GUI IMAGES, WHICH INCLUDES THE BUTTONS
	// THESE WILL BE ON SCREEN AT ALL TIMES
                   // data.setGameDimensions(GAME_WIDTH, GAME_HEIGHT);
                    start = System.currentTimeMillis();
	backgroundLayer = new Pane();
	addStackPaneLayer(backgroundLayer);
	addGUIImage(backgroundLayer, BACKGROUND_TYPE, loadImage(BACKGROUND_FILE_PATH), BACKGROUND_X, BACKGROUND_Y);
                    backgroundLayer.setVisible(false);
                                     
                    helpLayer = new Pane();
                    helpLayer.setVisible(false);
                    stackPane.getChildren().add(helpLayer);  
//                    addStackPaneLayer(helpLayer);
                    addGUIButton(helpLayer, SETTING_TYPE+"help", loadImage(SETTING_BUTTON_PATH), SETTING_X, SETTING_Y);
                    addGUIButton(helpLayer, RETURN_TO_MAP_TYPE+"help", loadImage(RETURN_BUTTON_PATH), HELP_X, HELP_Y);
                    helpLayer.setStyle("-fx-background-color: red");
                    
                    settingLayer = new Pane();
                    settingLayer.setVisible(false);
                    addStackPaneLayer(settingLayer);
                    addGUIButton(settingLayer, RETURN_TO_MAP_TYPE+"setting", loadImage(RETURN_BUTTON_PATH),SETTING_X, SETTING_Y);
                    addGUIButton(settingLayer, HELP_TYPE+"setting",loadImage(HELP_BUTTON_PATH), HELP_X, HELP_Y);
                    settingLayer.setStyle("-fx-background-color: white");
                    
	// THEN THE GAME LAYER
	gameLayer = new Pane();
	addStackPaneLayer(gameLayer);
	gameLayer.setVisible(false);
	// THEN THE GUI LAYER
                    initGUILayer();
//	guiLayer = new Pane();
//
//	addStackPaneLayer(guiLayer);
//	addGUIImage(guiLayer, TITLE_TYPE, loadImage(TITLE_FILE_PATH), TITLE_X, TITLE_Y);
//	//addGUIButton(guiLayer, START_TYPE, loadImage(START_BUTTON_FILE_PATH), START_X, START_Y);
//	//addGUIButton(guiLayer, EXIT_TYPE, loadImage(EXIT_BUTTON_FILE_PATH), EXIT_X, EXIT_Y);
//                    addGUIButton(guiLayer, SETTING_TYPE, loadImage(SETTING_BUTTON_PATH), SETTING_X, SETTING_Y);
//                    addGUIButton(guiLayer, HELP_TYPE, loadImage(HELP_BUTTON_PATH), HELP_X, HELP_Y);
//                    addGUIButton(guiLayer, REGION_TYPE, loadImage(REGION_BUTTON_PATH), REGION_X, REGION_Y);
//                    addGUIButton(guiLayer, CAPITAL_TYPE, loadImage(CAPITAL_BUTTON_PATH), CAPITAL_X, CAPITAL_Y);
//                    addGUIButton(guiLayer, FLAG_TYPE, loadImage(FLAG_BUTTON_PATH), FLAG_X, FLAG_Y);
//                    addGUIButton(guiLayer, LEADER_TYPE, loadImage(LEADER_BUTTON_PATH), LEADER_X, LEADER_Y);
//                    addGUIButton(guiLayer, STOP_TYPE, loadImage(STOP_BUTTON_PATH), STOP_X, STOP_Y);
//                    
//                    Button capitalButton = guiButtons.get(CAPITAL_TYPE);
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
                    guiLayer.setVisible(false); 
                    
                    mapName = new Text();
	 guiLayer.getChildren().add(mapName);
                    ancestorList = new ArrayList();
                    guiLayer.getChildren().addAll(ancestorList);
                    // add timer and the rest of the things down the screen
                  //  String timer = data.getSecondsAsTimeText(second);
                    
	// NOW LOAD THE WIN DISPLAY, WHICH WE'LL ONLY
	// MAKE VISIBLE AND ENABLED AS NEEDED
	ImageView winView = addGUIImage(guiLayer, WIN_DISPLAY_TYPE, loadImage(WIN_DISPLAY_FILE_PATH), WIN_X, WIN_Y);
	winView.setVisible(false);
        
                   // Image flag = new Image(TITLE_FILE_PATH);
                    flag = new Label();
                    
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
                   addGUIImage(labelLayer, TITLE_TYPE, loadImage(TITLE_FILE_PATH), 20, 20);
                             labelLayer.getChildren().add(flag);
                             labelLayer.getChildren().add(time);
                             labelLayer.getChildren().add(region);
                             labelLayer.getChildren().add(subRegions);
                             labelLayer.getChildren().add(score);
                             labelLayer.getChildren().add(guesses);
                             add = true;                
    }
    
    public void initGUILayer(){
        	guiLayer = new Pane();

	addStackPaneLayer(guiLayer);
	addGUIImage(guiLayer, TITLE_TYPE, loadImage(TITLE_FILE_PATH), TITLE_X, TITLE_Y);
	//addGUIButton(guiLayer, START_TYPE, loadImage(START_BUTTON_FILE_PATH), START_X, START_Y);
	//addGUIButton(guiLayer, EXIT_TYPE, loadImage(EXIT_BUTTON_FILE_PATH), EXIT_X, EXIT_Y);
                    addGUIButton(guiLayer, SETTING_TYPE, loadImage(SETTING_BUTTON_PATH), SETTING_X, SETTING_Y);
                    addGUIButton(guiLayer, HELP_TYPE, loadImage(HELP_BUTTON_PATH), HELP_X, HELP_Y);
                    addGUIButton(guiLayer, REGION_TYPE, loadImage(REGION_BUTTON_PATH), REGION_X, REGION_Y);
                    addGUIButton(guiLayer, CAPITAL_TYPE, loadImage(CAPITAL_BUTTON_PATH), CAPITAL_X, CAPITAL_Y);
                    addGUIButton(guiLayer, FLAG_TYPE, loadImage(FLAG_BUTTON_PATH), FLAG_X, FLAG_Y);
                    addGUIButton(guiLayer, LEADER_TYPE, loadImage(LEADER_BUTTON_PATH), LEADER_X, LEADER_Y);
                    addGUIButton(guiLayer, STOP_TYPE, loadImage(STOP_BUTTON_PATH), STOP_X, STOP_Y);
                    
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
        
                    Button enterButton = guiButtons.get(ENTER_TYPE);
                    enterButton.setOnAction(e->{
                        controller.processEnterGameRequest();
                       // System.out.println("enter button's id: "+enterButton.getId());
                    });
                    
                    Button helpButton = guiButtons.get(HELP_TYPE);
                    helpButton.setOnAction(e->{
                        controller.processHelpRequest();
                    });
                    
                    Button helpInSettingButton = guiButtons.get(HELP_TYPE+"setting");
                    helpInSettingButton.setOnAction(e->{
                        controller.processHelpRequest();
                    });
                    
                    Button settingInHelpButton = guiButtons.get(SETTING_TYPE+"help");
                    settingInHelpButton.setOnAction(e->{
                        controller.processSettingRequest();
                    });
                    
                    Button settingButton = guiButtons.get(SETTING_TYPE);
                    settingButton.setOnAction(e->{
                        controller.processSettingRequest();
                    });

                    Button returnButton = guiButtons.get(RETURN_TO_MAP_TYPE+"help");
                    returnButton.setOnAction(e->{
                        controller.processReturnRequest();
                    });
                    
                    Button returnButton2 = guiButtons.get(RETURN_TO_MAP_TYPE+"setting");
                    returnButton2.setOnAction(e->{
                        controller.processReturnRequest();
                    });
                   
	// MAKE THE CONTROLLER THE HOOK FOR KEY PRESSES
	keyController.setHook(controller);
        
                  Button regionButton = guiButtons.get(REGION_TYPE);
                  regionButton.setOnAction(e->{
                      setGamingMode(GameMode.REGION_MODE);
                      ((RegioVincoDataModel)data).reset(this);
                      buttonControl(getGamingMode());
                      isPlaying = true;
                  });
                  
                  Button capitalButton = guiButtons.get(CAPITAL_TYPE);
                  capitalButton.setOnAction(e->{
                      setGamingMode(GameMode.CAPITAL_MODE);
                      ((RegioVincoDataModel)data).reset(this);
                      buttonControl(getGamingMode());
                      isPlaying = true;
                  });
                  
                  Button leaderButton = guiButtons.get(LEADER_TYPE);
                  leaderButton.setOnAction(e->{
                      setGamingMode(GameMode.LEADER_MODE);
                      ((RegioVincoDataModel)data).reset(this);
                      buttonControl(getGamingMode());
                      isPlaying = true;
                  });

	// SETUP MOUSE PRESSES ON THE MAP
	ImageView mapView = guiImages.get(MAP_TYPE);
                   //System.out.println("get map view?"+mapView2.toString());
	mapView.setOnMousePressed(e -> {
                        if(isPlaying)
                            controller.processMapClickRequest((int) e.getX(), (int) e.getY());
                        else
                            controller.processRegionClickRequest((int) e.getX(), (int) e.getY(),path);
                        buttonControl(getGamingMode());
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
                    isPlaying = false;
                    end = false;
                    add = false;
                    audio.stop(AFGHAN_ANTHEM);
                    audio.play(BACKGROUND_SONG, true);
                    
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
   // this method is for load maps. If map already exist in hash map,then just swith map
   // else load and put new map in the hash map
    public void reloadMap(String mapFile) {
        ImageView tempMapImage = new ImageView();
        WritableImage mapImage;
        Image tempImage;
        PixelReader pixelReader;
        if(mapTable.containsKey(mapFile)){
            tempImage= mapTable.get(mapFile);
          //Image image = tempMapImage.getImage();
            pixelReader = tempImage.getPixelReader();
            mapImage = new WritableImage(pixelReader, (int) tempImage.getWidth(), (int) tempImage.getHeight());
            tempMapImage.setImage(tempImage);
           // path.add(mapFile);
         }
        else {
                    String mapFilePath = FILES_PATH;
                       for(int i = 0; i<path.size(); i++){
                            mapFilePath+= path.get(i)+"/";
                        }
                    mapFilePath += (mapFile+" Map.png");
                    System.out.println("map file path in reload map: "+mapFilePath);
                    File file = new File(mapFilePath);
	tempImage = loadImage(mapFilePath);
	pixelReader = tempImage.getPixelReader();
	mapImage = new WritableImage(pixelReader, (int) tempImage.getWidth(), (int) tempImage.getHeight());
        }
        System.out.println("path's size and last element: "+path.size()+" "+path.get(path.size()-1));
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
                    mapTable.put(mapFile, tempImage);        
	int numSubRegions = ((RegioVincoDataModel) data).getRegionsFound() + ((RegioVincoDataModel) data).getRegionsNotFound();
	this.boundaryTop = -(numSubRegions * 50);
                    
	// AND GIVE THE WRITABLE MAP TO THE DATA MODEL
	((RegioVincoDataModel) data).setMapImage(mapImage);
                    //guiLayer.getChildren().add(mapName);
                        mapName.setLayoutX(400);//380);
                        mapName.setLayoutY(50);//200);
                        mapName.setText(path.get(path.size()-1));
                        mapName.setFill(Color.WHITE);
                        mapName.setFont(Font.font("BookAntiqua",FontWeight.BOLD,30));
                        mapName.setVisible(true);
                        
                        String ancestor;
                        //guiLayer.getChildren().remove(guiLayer.getChildren().size()-ancestorList.size(), guiLayer.getChildren().size());
                        for(int i = 0; i<path.size(); i++){
                            ancestorList.add(new Text());
                                if(i == 0){
                                    ancestor = path.get(i);
                                    ancestorList.get(i).setLayoutX(50);//380);
                                    ancestorList.get(i).setLayoutY(670);//200);
                                    ancestorList.get(i).setText(ancestor);
                                    ancestorList.get(i).setFill(Color.WHITE);
                                    ancestorList.get(i).setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                                    ancestorList.get(i).setVisible(true);  
                                    if (!guiLayer.getChildren().contains(ancestorList.get(i)))
                                        guiLayer.getChildren().add(ancestorList.get(i));
                                    System.out.println("ancestorList in reloapMap: "+ancestorList.get(i).getText());
                                }
                                else{
                                    ancestor = (" - "+path.get(i));
                                    ancestorList.get(i).setLayoutX(50+i*100);//380);
                                    ancestorList.get(i).setLayoutY(670);//200);
                                    ancestorList.get(i).setText(ancestor);
                                    ancestorList.get(i).setFill(Color.WHITE);
                                    ancestorList.get(i).setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                                    ancestorList.get(i).setVisible(true); 
                                    if (!guiLayer.getChildren().contains(ancestorList.get(i)))
                                        guiLayer.getChildren().add(ancestorList.get(i));
                                 }
                               // System.out.println("gui's children number before: "+guiLayer.getChildren().size());
                                ancestorList.get(i).setOnMouseClicked(e->{
                                        processClickOnAncestorNodeRequest((Text)e.getSource());
                                });
                        }
    }
    public void reloadFlag(String flagFile){
        
    }
    public void loadAndPut(String regionName, String filePath){
        WorldDataManager maner = new WorldDataManager();
        if(regionTable.containsKey(regionName)){
            return;
        } else{
            maner.setWorldImporterExporter(worldIO);
            maner.load(filePath);
            regionTable.put(regionName, maner);
            maner.setRoot(new Region(regionName,null,null,null,null,null));
        }
    }
   // this method if for load region xml files. If file already exist in hash map, then use file name
   // as  key to change xml. Else load and put new file in hash map.
    public boolean reloadFile(String regionName){
        boolean yeah = false;
       WorldDataManager maner = new WorldDataManager();
        if(regionTable.containsKey(regionName)){
            maner.setWorldImporterExporter(worldIO);
            maner = regionTable.get(regionName);
            ((RegioVincoDataModel)data).setWorldDataManager(maner);
                maner.setRoot(new Region(regionName,null,null,null,null,null));
                if(path.contains(regionName))
                    return true;
                else{
                    path.add(regionName);
                    return true;
                }
        }
        else {
            String filePath = FILES_PATH;
                    if(path.isEmpty()){
                        filePath += regionName+"/";
                        //path.add(regionName);
                    } else {
//                        path.add(regionName);
                        //ancestorList.add(new Text());
                        for(int i = 0; i<path.size(); i++){
                            filePath+= path.get(i)+"/";
                            //System.out.println("filePath loop in reloa file: "+filePath);
                        }
                        filePath += (regionName+"/");
                    }     
                    filePath += (regionName+" Data.xml");
            File toLoad = new File(filePath);
            maner.setWorldImporterExporter(worldIO);
            System.out.println("file path in reload file: "+filePath);
            yeah = maner.load(filePath);
            System.out.println("file loaded in reloadFile? "+yeah);
            if(yeah == true){
                path.add(regionName);
                regionTable.put(regionName, maner);
                ((RegioVincoDataModel)data).setWorldDataManager(maner);
                maner.setRoot(new Region(regionName,null,null,null,null,null));
        }
         }
        return yeah;
    }
            
    public void processClickOnAncestorNodeRequest(Text ancestor){
        String ancestorName = ancestor.getText();
        int size = ancestorList.size();
        for(int i = 0; i<size; i++){
            if(ancestorName.equals(ancestorList.get(i).getText())){
                //path.clear();
                guiLayer.getChildren().remove(guiLayer.getChildren().indexOf(ancestor)+1, guiLayer.getChildren().size());
                if(ancestorName.contains(" - ")){ // when clicked on europe or africa..
                    String a = path.get(0);
                    path.clear();
                    path.add(a);
                    reloadFile(ancestorName.substring(3, ancestorName.length()));
                    reloadMap(ancestorName.substring(3, ancestorName.length()));
                     ((RegioVincoDataModel)data).resetRegion(this);
                    ((RegioVincoDataModel)data).resetPinkRegions(this, path);
                     break;
                } else{ // when clicked on the world
                     path.clear();
                     ancestorList.clear();
                     reloadFile(ancestorName);
                     reloadMap(ancestorName);
                     ((RegioVincoDataModel)data).resetRegion(this);
                     ((RegioVincoDataModel)data).resetPinkRegions(this, path);
                     break;
                }
            }else{
                System.out.println("ancestorName get from click: " + ancestorName);
                System.out.println("ancestorList.get(i): "+ancestorList.get(i).getText());
               // break;
            }
        }
        setGamingMode(DISPLAY_MODE);
        buttonControl(getGamingMode());       
    }
    
    public void addLabels(){
        RegioVincoDataModel dataModel = (RegioVincoDataModel)data;

                        time.setLayoutX(70);//380);
                        time.setLayoutY(220);//200);
                        time.setText("Game Duration: "+dataModel.getSecondsAsTimeText(endTime/1000-dataModel.getStart()/1000));
                        time.setFill(Color.NAVY);
                        time.setFont(Font.font("BookAntiqua",FontWeight.BOLD,24));
                        
                        region.setLayoutX(70);//380);
                        region.setLayoutY(180);//200);
                        region.setText("Region: "+dataModel.getRegionName());
                        region.setFill(Color.NAVY);
                        region.setFont(Font.font("BookAntiqua",FontWeight.BOLD,24));
                        
                        subRegions.setLayoutX(70);//380);
                        subRegions.setLayoutY(300);//200);
                        subRegions.setText("Sub Regions: "+dataModel.getNumberOfSubRegions());
                        subRegions.setFill(Color.NAVY);
                        subRegions.setFont(Font.font("BookAntiqua",FontWeight.BOLD,24));
                        
                        score.setLayoutX(70);//380);
                        score.setLayoutY(260);//200);
                        score.setText("Score: "+score());
                        score.setFill(Color.NAVY);
                        score.setFont(Font.font("BookAntiqua",FontWeight.BOLD,24));
                        
                        guesses.setLayoutX(70);//380);
                        guesses.setLayoutY(340);//200);
                        guesses.setText("Incorrect Guesses: "+dataModel.getWrong());
                        guesses.setFill(Color.NAVY);
                        guesses.setFont(Font.font("BookAntiqua",FontWeight.BOLD,24));
                     
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
   
   public void setLayerToVisible(String layerToSee){
       if(layerToSee.equalsIgnoreCase("help")){
           gameLayer.setVisible(false);
           splashLayer.setVisible(false);
           settingLayer.setVisible(false);
           helpLayer.setVisible(true);
           getGuiLayer().setVisible(false);
       } 
       else if(layerToSee.equalsIgnoreCase("setting")){
           gameLayer.setVisible(false);
           splashLayer.setVisible(false);
           settingLayer.setVisible(true);
           helpLayer.setVisible(false);
           getGuiLayer().setVisible(false);
       } 
       else if(layerToSee.equalsIgnoreCase("return")){
           gameLayer.setVisible(true);
           splashLayer.setVisible(false);
           settingLayer.setVisible(false);
           helpLayer.setVisible(false);
           getGuiLayer().setVisible(true);
       }
   }
   
   public void buttonControl(GameMode mode){
       Button helpButton = guiButtons.get(HELP_TYPE);
       Button capitalButton = guiButtons.get(CAPITAL_TYPE);
       Button leaderButton = guiButtons.get(LEADER_TYPE);
       Button flagButton = guiButtons.get(FLAG_TYPE);
       Button regionButton = guiButtons.get(REGION_TYPE);
       switch(mode){
           case DISPLAY_MODE: 
               if(path.size() < 2){
                   System.out.println("path's size checking in button control: "+path.size());
                   regionButton.setDisable(false);
                   capitalButton.setDisable(true);
                   leaderButton.setDisable(true);
                   flagButton.setDisable(true);
               }
               else if(path.size() == 3){
                   capitalButton.setDisable(false);
                   regionButton.setDisable(false);
                   leaderButton.setDisable(true);
                   flagButton.setDisable(true);
               }
               else{
                   capitalButton.setDisable(false);
                   regionButton.setDisable(false);
                   leaderButton.setDisable(false);
                   flagButton.setDisable(false);
               }
               regionButton.setDisable(false);
               break;
           case REGION_MODE: case CAPITAL_MODE: case LEADER_MODE:
               helpButton.setDisable(true);
               regionButton.setDisable(true);
               capitalButton.setDisable(true);
               leaderButton.setDisable(true);
               flagButton.setDisable(true);
               break;
           case STOP_MODE:
               
       }
   }
   
   public void enterGame(){
       splashLayer.setVisible(false);
       backgroundLayer.setVisible(true);
       gameLayer.setVisible(true);
      // addGUIImage(gameLayer,WORLD_MAP_TYPE, loadImage(THE_WORLD_MAP), MAP_X, MAP_Y);
       
       //File worldFile = new File("TheWorldRegion.xml");
       //Boolean  yeah = worldDataManager.load(THE_WORLD_REGION);
       File schema = new File(REGION_SCHEMA);
       worldIO = new WorldIO(schema);
       
       worldDataManager.setWorldImporterExporter(worldIO);
       //worldDataManager.setRoot(null);
       reloadFile("The World");
       reloadMap("The World");
       guiLayer.setVisible(true);
       //System.out.println("Loaded in enter game? "+yeah);
      // System.out.println(worldDataManager.getAllRegions().isEmpty());
       
   }

   public void stop(){
       String selection = "";
       selection = confirmDialog.showYesNoCancel("STOP", "Are you sure you want to stop the game?");
       if(selection == "YES"){
           ((RegioVincoDataModel)data).stopMode();
       }
           
   }
   
}