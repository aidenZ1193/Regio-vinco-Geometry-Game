package regio_vinco;

import audio_manager.AudioManager;
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
    
    private Text timer = new Text();
    private Text regionFound = new Text();
    private Text regionNotFound = new Text();
    private Text wrongGuess = new Text();
    private int wrong = 0;
    private long start;
    private boolean added = false;
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
	redSubRegions = new LinkedList();
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
	if ((clickedSubRegion == null) || (subRegionStack.isEmpty())) {
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
	for (int[] pixel : subRegionPixels) {
	    mapPixelWriter.setColor(pixel[0], pixel[1], color);
	}
    }

    public int getNumberOfSubRegions() {
	return colorToSubRegionMappings.keySet().size();
    }

    /**
     * Resets all the game data so that a brand new game may be played.
     *
     * @param game the Zombiquarium game in progress
     */
    @Override
    public void reset(PointAndClickGame game) {
                   game.getDataModel().beginGame();
	// THIS GAME ONLY PLAYS AFGHANISTAN
	regionName = "Afghanistan";
	subRegionsType = "Provinces";

	// LET'S CLEAR THE DATA STRUCTURES
	colorToSubRegionMappings.clear();
	subRegionToColorMappings.clear();
	subRegionStack.clear();
	redSubRegions.clear();

        // INIT THE MAPPINGS - NOTE THIS SHOULD 
	// BE DONE IN A FILE, WHICH WE'LL DO IN
	// FUTURE HOMEWORK ASSIGNMENTS
	colorToSubRegionMappings.put(makeColor(200, 200, 200), "Badakhshan");
	colorToSubRegionMappings.put(makeColor(198, 198, 198), "Nuristan");
	colorToSubRegionMappings.put(makeColor(196, 196, 196), "Kunar");
	colorToSubRegionMappings.put(makeColor(194, 194, 194), "Laghman");
	colorToSubRegionMappings.put(makeColor(192, 192, 192), "Kapisa");
	colorToSubRegionMappings.put(makeColor(190, 190, 190), "Panjshir");
	colorToSubRegionMappings.put(makeColor(188, 188, 188), "Takhar");
	colorToSubRegionMappings.put(makeColor(186, 186, 186), "Kunduz");
	colorToSubRegionMappings.put(makeColor(184, 184, 184), "Baghlan");
	colorToSubRegionMappings.put(makeColor(182, 182, 182), "Parwan");
	colorToSubRegionMappings.put(makeColor(180, 180, 180), "Kabul");
	colorToSubRegionMappings.put(makeColor(178, 178, 178), "Nangrahar");
	colorToSubRegionMappings.put(makeColor(176, 176, 176), "Maidan Wardak");
	colorToSubRegionMappings.put(makeColor(174, 174, 174), "Logar");
	colorToSubRegionMappings.put(makeColor(172, 172, 172), "Paktia");
	colorToSubRegionMappings.put(makeColor(170, 170, 170), "Khost");
	colorToSubRegionMappings.put(makeColor(168, 168, 168), "Samangan");
	colorToSubRegionMappings.put(makeColor(166, 166, 166), "Balkh");
	colorToSubRegionMappings.put(makeColor(164, 164, 164), "Jowzjan");
	colorToSubRegionMappings.put(makeColor(162, 162, 162), "Faryab");
	colorToSubRegionMappings.put(makeColor(160, 160, 160), "Sar-e Pol");
	colorToSubRegionMappings.put(makeColor(158, 158, 158), "Bamyan");
	colorToSubRegionMappings.put(makeColor(156, 156, 156), "Ghazni");
	colorToSubRegionMappings.put(makeColor(154, 154, 154), "Paktika");
	colorToSubRegionMappings.put(makeColor(152, 152, 152), "Badghis");
	colorToSubRegionMappings.put(makeColor(150, 150, 150), "Ghor");
	colorToSubRegionMappings.put(makeColor(148, 148, 148), "Daykundi");
	colorToSubRegionMappings.put(makeColor(146, 146, 146), "Oruzgan");
	colorToSubRegionMappings.put(makeColor(144, 144, 144), "Zabul");
	colorToSubRegionMappings.put(makeColor(142, 142, 142), "Herat");
	colorToSubRegionMappings.put(makeColor(140, 140, 140), "Farah");
	colorToSubRegionMappings.put(makeColor(138, 138, 138), "Nimruz");
	colorToSubRegionMappings.put(makeColor(136, 136, 136), "Helmand");
	colorToSubRegionMappings.put(makeColor(134, 134, 134), "Kandahar");

	// REST THE MOVABLE TEXT
	Pane gameLayer = ((RegioVincoGame)game).getGameLayer();
                   // Pane backgroundLayer = ((RegioVincoGame)game).getBackgroundLayer();
	gameLayer.getChildren().clear();
        // where I add the header
                    Label la = new Label();
                    la.setPrefSize(290, 50);
                   
                    la.setLayoutX(STACK_X);
                    la.setLayoutY(150);
                    la.setStyle("-fx-background-color: black");
                   
                   Pane guiLayer = ((RegioVincoGame)game).getGuiLayer();
                    Text node = new Text(regionName+" "+subRegionsType);
                    node.setFont(Font.font("Book Antiqua",FontWeight.BOLD, 28));
                    node.setFill(Color.YELLOW);
                     la.setGraphic(node);
                    guiLayer.getChildren().add(la);
                    //guiLayer.getChildren().add(node);
                  //  MovableText re = new MovableText(node);
                    // need to change the color and size and bold
                   // re.getText().setFont(Font.font("Book Antiqua",FontWeight.BOLD, 28));
                   // re.getText().setFill(Color.YELLOW);
                   // node.setLayoutX(STACK_X);
                    //node.setLayoutY(180);

                    
	for (Color c : colorToSubRegionMappings.keySet()) {
	    String subRegion = colorToSubRegionMappings.get(c);
	    subRegionToColorMappings.put(subRegion, c);
	    Text textNode = new Text(subRegion);
            // changed my text font
                        textNode.setFont(Font.font("BookAntiqua",FontWeight.BOLD,28));
                       // textNode.setFill(Color.NAVY);    
                        
	    //gameLayer.getChildren().add(textNode);
                       
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
                       
                       // gameLayer.getChildren().add(r);
	}
	Collections.shuffle(subRegionStack);
                   subRegionStack.peek().getLabel().setStyle("-fx-background-color: green");
                   subRegionStack.peek().getText().setFill(Color.RED);

	int y = STACK_INIT_Y;
	int yInc = STACK_INIT_Y_INC;
	// NOW FIX THEIR Y LOCATIONS
	for (MovableText mT : subRegionStack) {
                    // Set a rectangle
                                            
	    int tY = y + yInc;
	    //mT.getText().setLayoutY(tY);
                        mT.getLabel().setLayoutY(tY);
	    yInc -= 50;
	}
                   
	// RELOAD THE MAP
	((RegioVincoGame) game).reloadMap();

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
            
        // timer labels and staff
//                    timer = new Text();
//                    regionFound = new Text();
//                    regionNotFound = new Text();
//                    wrongGuess = new Text();
                    
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
	audio.stop(AFGHAN_ANTHEM);

	if (!audio.isPlaying(TRACKED_SONG)) {
	    audio.play(TRACKED_SONG, true);
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

}
