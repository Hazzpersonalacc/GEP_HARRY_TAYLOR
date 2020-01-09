package core.game;

import core.game_engine.Sprite;
import core.game_engine.physics.BoxCollider2D;
import core.game_engine.physics.LayerTypes;
import processing.core.PApplet;
import processing.core.PVector;

public class CollectableThing extends Sprite {
    public PVector size;

    public CollectableThing(PApplet p, int x, int y, int w, int h){
        super(p, x, y, w, h);
        this.parent = p;
        this.size = new PVector(w,h,0);
        layerTypes = LayerTypes.INTERACTABLE;
        this.boxCollider2D = new BoxCollider2D(this, w, h);
    }

    @Override
    public void update(){
        super.update();
        if(!this.isActive)
            return;
        parent.pushMatrix();
        // platform rectangle
        parent.rectMode(PApplet.CENTER);
        parent.translate(this.position.x, this.position.y);
        this.parent.fill(0,200,0);
        this.parent.rect(0, 0, this.size.x, this.size.y);
        parent.popMatrix();
    }

}
