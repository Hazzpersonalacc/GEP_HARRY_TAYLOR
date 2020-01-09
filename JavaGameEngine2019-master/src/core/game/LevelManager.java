package core.game;

import core.game_engine.GameManager;
import core.game_engine.data_management.DataManager;
import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class LevelManager {
    private PApplet parent;
    private GameManager gameManager;
    char currentKey = 'p';
    private String level_name = "Level 1";
    private int platformWidth = 50;
    private int platformHeight = 20;
    String itemType = "Platform";
    boolean mouse_down = false;
    DataManager dataManager;

    public LevelManager(PApplet p, GameManager g)
    {
        parent = p;
        gameManager = g;
        dataManager = new DataManager(this.parent);
        dataManager.load();
    }
    public void load_level(String _level_name){
        level_name = _level_name;
        if(dataManager.game_data !=null && dataManager.game_data.hasKey(level_name)){
            JSONArray levelItemsArray = dataManager.game_data.getJSONArray(level_name);
            for(int i = 0; i < levelItemsArray.size(); i++){
                JSONObject itemData = (JSONObject) levelItemsArray.get(i);
                itemType = itemData.getString("type");
                add_object(itemData.getInt("x"),
                        itemData.getInt("y"),
                        itemData.getInt("w"),
                        itemData.getInt("h"));
            }
        }
    }
    private void save_level(){
        dataManager.save(gameManager.getGame_objects(),level_name);
    }
    public void update()
    {
        if (parent.keyPressed){
            if(currentKey != parent.key){
                currentKey = parent.key;
                handle_key();
            }
        }
        if (parent.mousePressed){
            if(!mouse_down){
                mouse_down = true;
                handle_key();
            }
        }
        show_menu();
    }
    private void show_menu(){
        parent.pushMatrix();
        parent.rectMode(PApplet.CORNERS);
        parent.fill(255);
        parent.rect(0,0,parent.width,25);
        parent.fill(0);
        parent.textSize(12);
        parent.text("Edit mode: KEYS - Exit 1 | Add Platform P | Delete D | Add Collectible C | Save S", 5,15);
        parent.popMatrix();
    }
    private void add_object(int x, int y, int w, int h){
        switch (itemType){
            case "Platform":
                Platform gamePlatform = new Platform(this.parent,x,y,w,h);
                gameManager.add_game_object(gamePlatform);
                break;
            case "Collectable":
                break;
        }
    }
    private void handle_key(){
        switch (currentKey){
            case 'p':
            case 'P':
                System.out.println("Add Platform");
                add_object(parent.mouseX,parent.mouseY,platformWidth,platformHeight);

                break;
            case 'c':
            case 'C':
                System.out.println("Add Collectable");
                break;
            case 's':
            case 'S':
                System.out.println("Save All");
                save_level();
                break;
            case 'd':
            case 'D':
                System.out.println("Delete Platforms");
                break;
        }


    }
}
