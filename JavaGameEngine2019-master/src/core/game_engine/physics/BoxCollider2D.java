package core.game_engine.physics;

import core.game_engine.Component;
import core.game_engine.Sprite;

import java.util.ArrayList;

public class BoxCollider2D extends Component {
    // bounds rectangle
    public Rectangle bounds;
    private boolean hasCollided = false;
    private SIDES hitSideV = SIDES.NONE;
    private SIDES hitSide = SIDES.NONE;
    public SIDES getHitSide(){
        return hitSide;
    }
    public Rectangle getBounds(){
        return bounds;
    }
    public ArrayList<BoxCollider2D> otherColliders = new ArrayList<>();
    public BoxCollider2D(Sprite g, float w, float h){
        super(g);
        this.bounds = new Rectangle(gameObject.position.x, gameObject.position.y, w, h);
    }
    @Override
    protected void update() {
        this.bounds.updateBounds(gameObject.position.x, gameObject.position.y);
    }
    public void check_collisions(BoxCollider2D other){
        hasCollided = bounds.isOverLapping(other.bounds);
        if(hasCollided){
            this.otherColliders.add(other);
        }
    }
    public void findCollisionSide(BoxCollider2D otherBox2D){
        hitSideV = SIDES.NONE;
        boolean isTouchingAbove = this.bounds.getIsTouchingAbove(otherBox2D.getBounds());
        boolean isTouchingBelow = false;
        if(!isTouchingAbove){
            isTouchingBelow = this.bounds.getIsTouchingBelow(otherBox2D.getBounds());
        }

        if(isTouchingAbove){
            hitSideV = SIDES.BOTTOM;
        }else if (isTouchingBelow){
            hitSideV = SIDES.TOP;
        }
        hitSide = hitSideV;
        //do side?
        if(hitSideV == SIDES.NONE){
            boolean isTouchingRight = this.bounds.getIsTouchingRight(otherBox2D.getBounds());
            boolean isTouchingLeft = false;
            if(!isTouchingRight){
                isTouchingLeft = this.bounds.getIsTouchingLeft(otherBox2D.getBounds());
            }
            if(isTouchingLeft){
                hitSide = SIDES.LEFT;
            }else if(isTouchingRight){
                hitSide = SIDES.RIGHT;
            }
        }
    }
}
