package core.game;
import core.game_engine.GameManager;
import core.game_engine.data_management.DataManager;
import core.game_engine.input_commands.InputController;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.logging.Level;

public class TestGame {
    public PApplet parent;
    private GameManager game_manager;
    Platform gamePlatform;
    Player player;
    CollectableThing collectableThing;
    InputController playerInput;
    DataManager dataManager;
    GameMode gameMode = GameMode.START;
    LevelManager levelManager;
    private PApplet parent1;

    //LevelManager levelManager;
    public TestGame(PApplet p){
        this.parent = p;
    }
    public void start() {
        game_manager = new GameManager(this.parent);
        dataManager = new DataManager(this.parent);
        levelManager = new LevelManager(this.parent, game_manager);
        //dataManager.load();
        //load_game();
    }
    private void load_game(){
        this.game_manager.startup();
        dataManager.load();
        if(dataManager.game_data.hasKey("player")){
            JSONArray playersArray = dataManager.get_json_array("player");
            if(playersArray != null){
                for(int i = 0; i < playersArray.size(); i++){
                    JSONObject playerData = (JSONObject)playersArray.get(i);
                    Player savedPlayer = new Player(this.parent, playerData.getInt("x"),playerData.getInt("y"),20,20);
                    game_manager.add_game_object(savedPlayer);
                    player = savedPlayer;
                }
            }
        }else{
            player = new Player(this.parent, 300,100, 20, 20);
            game_manager.add_game_object(player);
            dataManager.save(game_manager.getGame_objects(), "player");
        }
        playerInput = new InputController(player);
        levelManager.load_level("Level 1");
    }

    private void welcome_screen(){
        parent.pushMatrix();
        parent.translate(parent.width/4,parent.height/4);
        parent.rectMode(PApplet.CORNERS);
        parent.fill(0,255,0);
        parent.textSize(32);
        parent.text("Welcome",0,0);
        parent.textSize(28);
        parent.text("Press any key to play",0,60);
        parent.text("Press 1 key to edit",0,120);
        parent.popMatrix();
    }

    public void update(){
        switch (gameMode) {
            case START:
                welcome_screen();
                break;
            case PLAY:
                playerInput.checkInput();
                game_manager.update();
                break;
            case EDIT:
                //level editor
                levelManager.update();
                game_manager.update();
                break;
            case RELOAD:
                //load a level
                load_game();
                gameMode = GameMode.PLAY;
                break;
        }
    }
    public void keyReleased(char key, int keycode) {
        switch (gameMode) {
            case START:
                switch (key) {
                    case '1':
                        System.out.println("Open edit mode");
                        gameMode = GameMode.EDIT;
                        game_manager.startup();
                        levelManager.load_level("Level 1");
                        break;
                    default:
                        gameMode = GameMode.RELOAD;
                        break;
                }
                break;
            case PLAY:
                playerInput.keyHandler(key, keycode, false);
                break;
            case EDIT:
                switch (key){
                    case '1':
                        gameMode = GameMode.START;
                }
                break;
            case RELOAD:
                break;
        }
    }
    public void keyPressed(char key, int keycode) {
        switch (gameMode) {
            case START:
                break;
            case PLAY:
                playerInput.keyHandler(key, keycode, true);
                break;
            case EDIT:
                break;
            case RELOAD:
                break;
        }
    }

}
