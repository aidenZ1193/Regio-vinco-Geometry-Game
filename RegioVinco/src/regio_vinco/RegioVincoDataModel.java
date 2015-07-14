package regio_vinco;

import audio_manager.AudioManager;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import javafx.scene.control.Label;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import pacg.PointAndClickGame;
import pacg.PointAndClickGameDataModel;
import static regio_vinco.RegioVinco.*;
import world_data.Region;
import world_data.WorldDataManager;

/**
 * This class manages the game data for the Regio Vinco game application. Note
 * that this game is built using the Point & Click Game Framework as its base. 
 * This class contains methods for managing game data and states.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class RegioVincoDataModel extends PointAndClickGameDataModel {

    // HELPER METHOD FOR MAKING A COLOR OBJECT
    public static Color makeColor(int r, int g, int b) {
        return Color.color(r/255.0, g/255.0, b/255.0);
    }
    // THIS IS THE MAP IMAGE THAT WE'LL USE
    private WritableImage mapImage;
    private PixelReader mapPixelReader;
    private PixelWriter mapPixelWriter;
    
    // AND OTHER GAME DATA
    private String regionName;
    private String subRegionsType;
    private HashMap<Color, String> colorToSubRegionMappings;
    private HashMap<String, Color> subRegionToColorMappings;
    private HashMap<String, ArrayList<int[]>> pixels;
    private LinkedList<String> redSubRegions;
    private LinkedList<MovableText> subRegionStack;
    private int subRegionsNumber;
    
    private Text timer = new Text();
    private Text regionFound = new Text();
    private Text regionNotFound = new Text();
    private Text wrongGuess = new Text();
    private int wrong = 0;
    private long start;
    private boolean added = false;
    
    private WorldDataManager worldDataManager;
   // private Font font = new Font("Verdana", Font.BOLD, 12);

    /**
     * Default constructor, it initializes all data structures for managing the
     * Sprites, including the map.
     */
    public RegioVincoDataModel() {
	// INITIALIZE OUR DATA STRUCTURES
	colorToSubRegionMappings = new HashMap();
	subRegionToColorMappings = new HashMap();
	subRegionStack = new LinkedList();
                    subRegionsNumber = 0;
	redSubRegions = new LinkedList();
                    // incase of null for worlddatamanager
                    worldDataManager = new WorldDataManager();
    }
    
    public void setMapImage(WritableImage initMapImage) {
	mapImage = initMapImage;
	mapPixelReader = mapImage.getPixelReader();
	mapPixelWriter = mapImage.getPixelWriter();
    }
// I changed the return type to boolean from void
    public void removeAllButOneFromeStack(RegioVincoGame game) {
                    //for(int i = 0; i<subRegionStack.size(); i++){
                     //   String subRegion = subRegionStack.get(i).getText().getText();
                        //changeSubRegionColorOnMap()
                      
	while (subRegionStack.size() > 1) {
                         
                         //Label lla = subRegionStack.peek().getLabel();
//                        game.getGameLayer().getChildren().remove(lla);
                        
	    MovableText text = subRegionStack.removeFirst();
                       game.getGameLayer().getChildren().remove(text.getLabel());
	    String subRegionName = text.getText().getText();
               
	    // TURN THE TERRITORY GREEN
	    changeSubRegionColorOnMap(game, subRegionName, Color.GREEN);
	}
                     MovableText text = subRegionStack.peekFirst();
	    String subRegionName = text.getText().getText();
                    if(redSubRegions.contains(subRegionName)) 
                        changeSubRegionColorOnMap(game,subRegionName,getColorMappedToSubRegion(subRegionName));
                   subRegionStack.peek().getLabel().setStyle("-fx-background-color: green");
                   subRegionStack.peek().getText().setFill(Color.RED);
	startTextStackMovingDown();
    }

    public int getWrong(){
        return wrong;
        
    }
    public Text getTimer() {
        return timer;
    }

    public long setTimer() {
         long start = System.currentTimeMillis();
        return start;
    }

    public Text getRegionFound() {
        return regionFound;
    }

    public void setRegionFound(Text regionFound) {
        this.regionFound = regionFound;
    }

    public Text getRegionNotFound() {
        return regionNotFound;
    }

    public void setRegionNotFound(Text regionNotFound) {
        this.regionNotFound = regionNotFound;
    }

    public Text getWrongGuess() {
        return wrongGuess;
    }

    public long getStart(){
        return start;
    }
    public void setWrongGuess(Text wrongGuess) {
        this.wrongGuess = wrongGuess;
    }

    // ACCESSOR METHODS
    public String getRegionName() {
	return regionName;
    }

    public String getSubRegionsType() {
	return subRegionsType;
    }

    public void setRegionName(String initRegionName) {
	regionName = initRegionName;
    }

    public void setSubRegionsType(String initSubRegionsType) {
	subRegionsType = initSubRegionsType;
    }

    public WorldDataManager getWorldDataManager() {
        return worldDataManager;
    }

    public void setWorldDataManager(WorldDataManager worldDataManager) {
        this.worldDataManager = worldDataManager;
    }

    
    public String getSecondsAsTimeText(long numSeconds) {
	long numHours = numSeconds / 3600;
	numSeconds = numSeconds - (numHours * 3600);
	long numMinutes = numSeconds / 60;
	numSeconds = numSeconds - (numMinutes * 60);

	String timeText = "";
	if (numHours > 0) {
	    timeText += numHours + ":";
	}
	timeText += numMinutes + ":";
	if (numSeconds < 10) {
	    timeText += "0" + numSeconds;
	} else {
	    timeText += numSeconds;
	}
	return timeText;
    }

    public int getRegionsFound() {
	return colorToSubRegionMappings.keySet().size() - subRegionStack.size();
    }

    public int getRegionsNotFound() {
	return subRegionStack.size();
    }
    
    public LinkedList<MovableText> getSubRegionStack() {
	return subRegionStack;
    }
    
    public String getSubRegionMappedToColor(Color colorKey) {
	return colorToSubRegionMappings.get(colorKey);
    }
    
    public Color getColorMappedToSubRegion(String subRegion) {
	return subRegionToColorMappings.get(subRegion);
    }

    public boolean isAdded() {
        return added;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }

    
    // MUTATOR METHODS

    public void addColorToSubRegionMappings(Color colorKey, String subRegionName) {
	colorToSubRegionMappings.put(colorKey, subRegionName);
    }

    public void addSubRegionToColorMappings(String subRegionName, Color colorKey) {
	subRegionToColorMappings.put(subRegionName, colorKey);
    }

    public void respondToMapSelection(RegioVincoGame game, int x, int y) {
        // THIS IS WHERE WE'LL CHECK TO SEE IF THE
	// PLAYER CLICKED NO THE CORRECT SUBREGION
	Color pixelColor = mapPixelReader.getColor(x, y);
	String clickedSubRegion = colorToSubRegionMappings.get(pixelColor);
                //System.out.println("Clicked on(respondToMap): "+clickedSubRegion);
	if ((clickedSubRegion == null) || (subRegionStack.isEmpty())) {
                        System.out.println("Inisde the respondToMap method. Returning.");
	    return;
	}
	if (clickedSubRegion.equals(subRegionStack.get(0).getText().getText())) {
	    // YAY, CORRECT ANSWER
	    game.getAudio().play(SUCCESS, false);

	    // TURN THE TERRITORY GREEN
	    changeSubRegionColorOnMap(game, clickedSubRegion, Color.GREEN);

	    // REMOVE THE BOTTOM ELEMENT FROM THE STACK
                        Label lla = subRegionStack.peek().getLabel();
                        game.getGameLayer().getChildren().remove(lla);
	    subRegionStack.removeFirst();
                        if(subRegionStack.size() != 0){
                      
                            subRegionStack.peek().getLabel().setStyle("-fx-background-color: green");
                   subRegionStack.peek().getText().setFill(Color.RED);
                        }
                        else
                            return;
	    // AND LET'S CHANGE THE RED ONES BACK TO THEIR PROPER COLORS
	    for (String s : redSubRegions) {
		Color subRegionColor = subRegionToColorMappings.get(s);
		changeSubRegionColorOnMap(game, s, subRegionColor);
	    }
	    redSubRegions.clear();

	    startTextStackMovingDown();

	    if (subRegionStack.isEmpty()) {
		this.endGameAsWin();
		game.getAudio().stop(TRACKED_SONG);
		game.getAudio().play(AFGHAN_ANTHEM, false);
	    }
	} else {
	    if (!redSubRegions.contains(clickedSubRegion)) {
		// BOO WRONG ANSWER
		game.getAudio().play(FAILURE, false);

		// TURN THE TERRITORY TEMPORARILY RED
		changeSubRegionColorOnMap(game, clickedSubRegion, Color.RED);
		redSubRegions.add(clickedSubRegion);
                                        wrong++;
	    }
	}
    }
    
    public void respondToRegionSelection(RegioVincoGame game, int x, int y, ArrayList<String> path){
                   Color pixelColor = mapPixelReader.getColor(x, y);
	String clickedSubRegion = colorToSubRegionMappings.get(pixelColor);
         if ((clickedSubRegion == null)) {
	    return;
	}       
                    //System.out.println("clickedSubRegion name in respond method: "+clickedSubRegion);
                    game.reloadFile(clickedSubRegion);
                    game.reloadMap(clickedSubRegion);
                    resetRegion(game);
                    resetPinkRegions(game,path);
    }
    
    public void startTextStackMovingDown() {
	// AND START THE REST MOVING DOWN
	for (MovableText mT : subRegionStack) {
	    mT.setVelocityY(SUB_STACK_VELOCITY);
	}
    }

    public void changeSubRegionColorOnMap(RegioVincoGame game, String subRegion, Color color) {
        // THIS IS WHERE WE'LL CHECK TO SEE IF THE
	// PLAYER CLICKED NO THE CORRECT SUBREGION
	ArrayList<int[]> subRegionPixels = pixels.get(subRegion);
        //System.out.println("in changeColor: is pixels empty? "+pixels.isEmpty());
	for (int[] pixel : subRegionPixels) {
	    mapPixelWriter.setColor(pixel[0], pixel[1], color);
	}
    }

    public int getNumberOfSubRegions() {
	return colorToSubRegionMappings.keySet().size();
    }

    public void enter(RegioVincoGame game){
        game.enterGame();
       ArrayList<String> a = new ArrayList();
       a.add("The World");
        resetRegion(game);
        resetPinkRegions(game, a);
    }
    
    public void resetMapping(RegioVincoGame game, GameMode mode){
        //pixels = new HashMap();
        String key = "";   
                for (Region re : worldDataManager.getAllRegions()) {
                    if(!re.getName().equals(regionName)){
                      //System.out.println("names in reset region: "+re.getName());
                         int red = Integer.valueOf(re.getRed());
                         int green = Integer.valueOf(re.getGreen());
                         int blue = Integer.valueOf(re.getBlue());
                         
                        // System.out.println("capital in resetMapping: "+re.getCapital());
                         if(mode.equals(GameMode.REGION_MODE))       
                             key = re.getName();
                         else if(mode.equals(GameMode.LEADER_MODE))
                             key = re.getLeader();
                         else if(mode.equals(GameMode.CAPITAL_MODE))
                             key = re.getCapital();
                         else{
                             System.out.println("returning in resetMapping");
                             return;
                         }
                         
                       System.out.println("key in resetMapping: "+re.getName()+": "+key);
                         if(key == null)
                             changeSubRegionColorOnMap(game, re.getName(),Color.PINK);
                         else{
                            colorToSubRegionMappings.put(makeColor(red,green,blue), key);
                            subRegionToColorMappings.put(key, makeColor(red,green,blue));
                            changeSubRegionColorOnMap(game, re.getName(),makeColor(red,green,blue));
                         }
                    }
                }
    }
    
    public void resetRegion(RegioVincoGame game){
        subRegionStack.clear();
        subRegionToColorMappings.clear();
        redSubRegions.clear();
        colorToSubRegionMappings.clear();
        
        // LET'S RECORD ALL THE PIXELS
	pixels = new HashMap();
                    
                    Region root = worldDataManager.getWorld();
                    System.out.println("root from data manager's getWorld: "+root.getName());
                    regionName = root.getName();
	for (Region re : worldDataManager.getAllRegions()) {
	    if(!re.getName().equals(regionName)){
                                //System.out.println("names in reset region: "+re.getName());
                                int red = Integer.valueOf(re.getRed());
                                int green = Integer.valueOf(re.getGreen());
                                int blue = Integer.valueOf(re.getBlue());
                                colorToSubRegionMappings.put(makeColor(red,green,blue), re.getName());
                                subRegionToColorMappings.put(re.getName(), makeColor(red,green,blue));
                                pixels.put(re.getName(), new ArrayList());
                                
                                //System.out.println("colorToSubRegionMapping in resetRegion: "+colorToSubRegionMappings.get(makeColor(red,green,blue)));
                        }
	}

	for (int i = 0; i < mapImage.getWidth(); i++) {
	    for (int j = 0; j < mapImage.getHeight(); j++) {
		Color c = mapPixelReader.getColor(i, j);
		if (colorToSubRegionMappings.containsKey(c)) {
		    String subRegion = colorToSubRegionMappings.get(c);
		    ArrayList<int[]> subRegionPixels = pixels.get(subRegion);
		    int[] pixel = new int[2];
		    pixel[0] = i;
		    pixel[1] = j;
		    subRegionPixels.add(pixel);
		}
	    }
	}
        //for(Region a: colorToSubRegionMappings.get(root))
    }
    public void resetPinkRegions(RegioVincoGame game, ArrayList<String>path){
        int size;
        for(Region re: worldDataManager.getAllRegions()){
             String filePath = FILES_PATH;
             if(path.size()>1)
                   size = path.size()-1;
             else
                   size = path.size();
              for(int i = 0; i<path.size(); i++){
                   filePath+= path.get(i)+"/";
                            //System.out.println("folder path loop in pink region: "+path.get(i));
               }
            if(!re.getName().equals(worldDataManager.getWorld().getName())) {
                    filePath += (re.getName()+"/"+re.getName()+" Data.xml");           
                    //System.out.println("region fie path in change color: "+filePath);
            }else{
                filePath += (re.getName()+" Data.xml");
                game.loadAndPut(re.getName(),filePath);
            }
                     File toLoad = new File(filePath);
                     if(toLoad.exists()){
                        //System.out.println("region name in change color: "+re.getName());
                         game.loadAndPut(re.getName(),filePath);
                     }
                        else{
                            changeSubRegionColorOnMap(game, re.getName(),Color.PINK);
                     }
        }
    }
    
    /**
     * Resets all the game data so that a brand new game may be played.
     *
     * @param game the Zombiquarium game in progress
     */
    @Override
    public void reset(PointAndClickGame game) {
                   GameMode mode = ((RegioVincoGame)game).getGamingMode();
                   System.out.println("game mode passed to reset: "+mode.toString());
                   game.getDataModel().beginGame();
	// THIS GAME ONLY PLAYS AFGHANISTAN
	regionName = worldDataManager.getWorld().getName();
	//subRegionsType = "Provinces";

	// LET'S CLEAR THE DATA STRUCTURES
	colorToSubRegionMappings.clear();
	subRegionToColorMappings.clear();
	subRegionStack.clear();
	redSubRegions.clear();
                    
                   String key = "";   
       //            for(Region re: worldDataManager.getAllRegions())
      //                 System.out.println("capital in reset: "+re.getName()+" "+re.getCapital());
                   resetMapping((RegioVincoGame)game, mode);

	// REST THE MOVABLE TEXT
	Pane gameLayer = ((RegioVincoGame)game).getGameLayer();
                   // Pane backgroundLayer = ((RegioVincoGame)game).getBackgroundLayer();
	gameLayer.getChildren().clear();
        // where I add the header
                    Label la = new Label();
                    la.setPrefSize(290, 50);
                   
                    la.setLayoutX(STACK_X);
                    la.setLayoutY(160);
                    la.setStyle("-fx-background-color: black");
                   
                   Pane guiLayer = ((RegioVincoGame)game).getGuiLayer();
                    Text node = new Text(regionName);
                    node.setFont(Font.font("Book Antiqua",FontWeight.BOLD, 28));
                    node.setFill(Color.YELLOW);
                     la.setGraphic(node);
                    guiLayer.getChildren().add(la);
                    
	for (Color c : colorToSubRegionMappings.keySet()) {
	    String subRegion = colorToSubRegionMappings.get(c);
	    subRegionToColorMappings.put(subRegion, c);
	    Text textNode = new Text(subRegion);
            // changed my text font
                        textNode.setFont(Font.font("BookAntiqua",FontWeight.BOLD,28));
	    MovableText subRegionText = new MovableText(textNode);
                        subRegionText.getLabel().setStyle("-fx-background-color: rgb(" + (c.getRed()*255) + "," 
                                                + (c.getGreen()*255)+","+ (c.getBlue()*255) +")");
                        subRegionText.getLabel().setLayoutX(STACK_X);
                        subRegionText.getLabel().setGraphic(textNode);
                        subRegionText.getLabel().setPrefSize(290, 50);
                        subRegionText.getLabel().setEffect(null);
                        
                         gameLayer.getChildren().add(subRegionText.getLabel());
	    subRegionText.getText().setFill(Color.NAVY);
	    //textNode.setX(STACK_X);
	    subRegionStack.add(subRegionText);
	}
                    subRegionsNumber = subRegionStack.size();
                    System.out.println("subRegionStack size in reset: "+subRegionStack.size());
	Collections.shuffle(subRegionStack);
                   subRegionStack.peek().getLabel().setStyle("-fx-background-color: green");
                   subRegionStack.peek().getText().setFill(Color.RED);

	int y = STACK_INIT_Y;
	int yInc = STACK_INIT_Y_INC;
	// NOW FIX THEIR Y LOCATIONS
	for (MovableText mT : subRegionStack) {                                           
	    int tY = y + yInc;
                        mT.getLabel().setLayoutY(tY);
	    yInc -= 50;
	}
                   
	// RELOAD THE MAP
	//((RegioVincoGame) game).reloadMap(AFG_MAP_FILE_PATH);

	// LET'S RECORD ALL THE PIXELS
	pixels = new HashMap();
	for (MovableText mT : subRegionStack) {
	    pixels.put(mT.getText().getText(), new ArrayList());
	}

	for (int i = 0; i < mapImage.getWidth(); i++) {
	    for (int j = 0; j < mapImage.getHeight(); j++) {
		Color c = mapPixelReader.getColor(i, j);
		if (colorToSubRegionMappings.containsKey(c)) {
		    String subRegion = colorToSubRegionMappings.get(c);
		    ArrayList<int[]> subRegionPixels = pixels.get(subRegion);
		    int[] pixel = new int[2];
		    pixel[0] = i;
		    pixel[1] = j;
		    subRegionPixels.add(pixel);
		}
	    }
	}
                    
                    timer.setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                    timer.setLayoutX(20);
                    timer.setLayoutY(650);
                    timer.setFill(Color.AQUA);
                    start = System.currentTimeMillis();
                    
                    regionFound.setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                    regionFound.setLayoutX(150);
                    regionFound.setLayoutY(650);
                    regionFound.setFill(Color.AQUA);
                    
                    regionNotFound.setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                    regionNotFound.setLayoutX(420);
                    regionNotFound.setLayoutY(650);
                    regionNotFound.setFill(Color.AQUA);
                    
                    wrongGuess.setFont(Font.font("BookAntiqua",FontWeight.BOLD,20));
                    wrongGuess.setLayoutX(620);
                    wrongGuess.setLayoutY(650);
                    wrongGuess.setFill(Color.AQUA);
                    wrong = 0;
                    if(!added){
                    guiLayer.getChildren().add(timer);
                    guiLayer.getChildren().add(regionFound);
                    guiLayer.getChildren().add(regionNotFound);
                    guiLayer.getChildren().add(wrongGuess);
                    added = true;
                    }
                    else {
                        getRegionFound().setVisible(true);
                       getTimer().setVisible(true);
                        getRegionNotFound().setVisible(true);
                        getWrongGuess().setVisible(true);
                    }
	// RESET THE AUDIO
	AudioManager audio = ((RegioVincoGame) game).getAudio();
      //  System.out.println("audio name in dataModel's reset: "+audio. )
	audio.stop(AFGHAN_ANTHEM);

	if (!audio.isPlaying(BACKGROUND_SONG)) {
	    audio.play(BACKGROUND_SONG, true);
	}
	// LET'S GO
	beginGame();
    }
    
    // STATE TESTING METHODS
    // UPDATE METHODS
    // updateAll
    // updateDebugText
    /**
     * Called each frame, this thread already has a lock on the data. This
     * method updates all the game sprites as needed.
     *
     * @param game the game in progress
     */
    @Override
public void updateAll(PointAndClickGame game, double percentage) {
    for (MovableText mT : subRegionStack) {
        mT.update(percentage);
    }
   if(getRegionsFound() == subRegionsNumber){
             endGameAsWin();
             ((RegioVincoGame)game).setEndTime(System.currentTimeMillis());
              ((RegioVincoGame)game).addLabels();
              ((RegioVincoGame)game).setEnd(true);
           }     
    if (!subRegionStack.isEmpty()) {
        MovableText bottomOfStack = subRegionStack.get(0);
        double bottomY = bottomOfStack.getLabel().getLayoutY()+bottomOfStack.getLabel().getTranslateY();
        if (bottomY > FIRST_REGION_Y_IN_STACK) {
            double diffY = bottomY - FIRST_REGION_Y_IN_STACK;
            for (MovableText mT : subRegionStack) {
                mT.getText().setY(mT.getText().getY() - diffY);
                mT.setVelocityY(0);
            }
        }
    }
    timer.setText(getSecondsAsTimeText(System.currentTimeMillis()/1000-start/1000));
    regionFound.setText("Regions Found: "+String.valueOf(getRegionsFound()));
    regionNotFound.setText("Regions Left: "+String.valueOf(getRegionsNotFound()));
    wrongGuess.setText("Incorrect Guesses: "+wrong);
    
}

    /**
     * Called each frame, this method specifies what debug text to render. Note
     * that this can help with debugging because rather than use a
     * System.out.print statement that is scrolling at a fast frame rate, we can
     * observe variables on screen with the rest of the game as it's being
     * rendered.
     *
     * @return game the active game being played
     */
public void updateDebugText(PointAndClickGame game) {
	debugText.clear();
    }

public void stopMode(){
    if(!redSubRegions.isEmpty())
        redSubRegions.clear();
    
    
}
}
