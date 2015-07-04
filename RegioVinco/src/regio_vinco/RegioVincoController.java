package regio_vinco;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pacg.KeyPressHook;

/**
 * This controller provides the apprpriate responses for all interactions.
 */
public class RegioVincoController implements KeyPressHook {
    RegioVincoGame game;
    
    public RegioVincoController(RegioVincoGame initGame) {
	game = initGame;
    }
    
    public void processEnterGameRequest(){
                    game.enterGame();
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
