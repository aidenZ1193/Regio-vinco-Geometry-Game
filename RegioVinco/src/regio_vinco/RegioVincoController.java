package regio_vinco;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.sound.sampled.UnsupportedAudioFileException;
import pacg.KeyPressHook;

/**
 * This controller provides the apprpriate responses for all interactions.
 */
public class RegioVincoController implements KeyPressHook {
    RegioVincoGame game;
    
    public RegioVincoController(RegioVincoGame initGame) {
	game = initGame;
    }
    
    public void processEnterGameRequest() throws UnsupportedAudioFileException{
                    //game.enterGame();
               ((RegioVincoDataModel)game.getDataModel()).enter(game);
    }
    
    public void processHelpRequest(){
                    game.setLayerToVisible("help");
 //                   game.getGuiLayer().setVisible(false);
    }
    
    public void processSettingRequest(){
                    game.setLayerToVisible("setting");
  //                  game.getGuiLayer().setVisible(false);
    }
    
    public void processMuteSoundRequest(){
                    ((RegioVincoDataModel)game.getDataModel()).muteSound();
    }
    public void processMuteMusicRequest(){
        System.out.println("regionName get in controller: "+ ((RegioVincoDataModel)game.getDataModel()).getWorldDataManager().getWorld().getName());
                    ((RegioVincoDataModel)game.getDataModel()).muteMusic(game, 
                            ((RegioVincoDataModel)game.getDataModel()).getWorldDataManager().getWorld().getName());
    }
    
    public void processReturnRequest(){
                    game.setLayerToVisible("return");
  //                  game.getGuiLayer().setVisible(true);
    }
    
    public void processStopRequest() throws UnsupportedAudioFileException{
                    game.stop();
    }
    public void processStartGameRequest() {
	game.reset();
    }
    
    public void processExitGameRequest() {
	game.killApplication();
    }
    
    public void processMapClickRequest(int x, int y) {
	((RegioVincoDataModel)game.getDataModel()).respondToMapSelection(game, x, y);
    }
    
    public void processRegionClickRequest(int x, int y,ArrayList<String> path) throws UnsupportedAudioFileException{
                    ((RegioVincoDataModel)game.getDataModel()).respondToRegionSelection(game,x,y,path);
    }
    
    public void processMouseOverFlag(int x, int y, HashMap<String, Image>table){
                    game.processMouseOverRegion(x, y);
    }
        
    @Override
    public void processKeyPressHook(KeyEvent ke)
    {
        //boolean callWin = false;
        KeyCode keyCode = ke.getCode();
        if (keyCode == KeyCode.C)
        {
            try
            {    
                game.beginUsingData();
                RegioVincoDataModel dataModel = (RegioVincoDataModel)(game.getDataModel());
                if(dataModel.getRegionsFound()>=33)
                    return;
                dataModel.removeAllButOneFromeStack(game);     

               // dataModel.setGameState(WIN);
               // dataModel.endGameAsWin();
                
                game.updateGUI();
            }
            finally
            {
                game.endUsingData();
               // if(callWin)
                    
            }
        }
    }   
}
