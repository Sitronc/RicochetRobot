package isep.robotricochet.generation;
public class GenMur {
    private Direction direction;
    public GenMur(Direction o){
        this.direction = o;
    }
    public Direction getOrientation() {
        return direction;
    }
    public void setOrientation(Direction direction) {
        this.direction = direction;
    }
}
