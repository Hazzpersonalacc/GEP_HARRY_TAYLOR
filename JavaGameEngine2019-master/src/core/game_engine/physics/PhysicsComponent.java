package core.game_engine.physics;

import core.game_engine.Component;
import core.game_engine.GameObject;
import core.game_engine.Sprite;
import processing.core.PVector;

public class PhysicsComponent extends Component {
    private PVector velocity = new PVector(0,0,0);
    public float maxSpeed = 5f;
    private float friction = 0.9f;
    private float spacer = 0.3f;
    private float gravity = 1.1f;
    // box collider
    private BoxCollider2D boxCollider2D;
    public PhysicsComponent(GameObject g, BoxCollider2D b){
        super(g);
        this.boxCollider2D = b;

    }

    @Override
    protected void update() {
        if(this.velocity.mag() > this.maxSpeed){
            this.velocity.setMag(this.maxSpeed);
        }
       if(this.boxCollider2D.otherColliders.size() > 0){
           for(BoxCollider2D b : this.boxCollider2D.otherColliders){
               if(b.gameObject.layerTypes == LayerTypes.INTERACTABLE){
                   //do something else other than colliding
                   b.gameObject.isActive = false;
               } else {
                   setCollisionSide(b);
               }
               // move player relative to what it collided with
               //velocity.x = 0;
               //velocity.y = 0;
               //system.out.println("collided!");

           }
           this.boxCollider2D.otherColliders.clear();
       }
       this.velocity.mult(friction);
       this.gameObject.position = this.gameObject.next_position.copy();
        this.gameObject.next_position.add(this.velocity);
    }
    public void setVelocity(float x, float y){
        this.velocity.x += x;
        this.velocity.y += y;
    }
    private void setCollisionSide(BoxCollider2D otherBox2D){
        //find side from other box
        //todo get collision from bock

        Point otherTopRight = otherBox2D.getBounds().getTopRight();
        Point otherBottomLeft = otherBox2D.getBounds().getBottomLeft();
        this.boxCollider2D.findCollisionSide(otherBox2D);
        switch (this.boxCollider2D.getHitSide()){
            case TOP:
                this.gameObject.next_position.y = otherBottomLeft.getY() + this.boxCollider2D.getBounds().getHeight()/2 + spacer;
                break;

            case BOTTOM:
                //stop
                this.velocity.y=0;
                this.gameObject.next_position.y = otherTopRight.getY() - this.boxCollider2D.getBounds().getHeight()/2 - spacer;
                break;

            case LEFT:
                velocity.x=0;
                this.gameObject.next_position.x = otherBottomLeft.getX() - this.boxCollider2D.getBounds().getWidth() / 2f - spacer;

                break;
            case RIGHT:
                this.gameObject.next_position.x = otherTopRight.getX() + this.boxCollider2D.getBounds().getWidth() / 2f + spacer;
                break;
        }

    }
}
