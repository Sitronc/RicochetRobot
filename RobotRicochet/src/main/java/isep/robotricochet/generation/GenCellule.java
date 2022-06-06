package isep.robotricochet.generation;

import java.util.ArrayList;
import java.util.List;
public class GenCellule {
    private Coordonne coordonne;
    private InfoPion currentInfoPion;
    private List<GenMur> genMurs = new ArrayList<>();
    private boolean isThereWall;
    private boolean isThereARobot;
    private boolean isThereASymbol;
    private Items items;
    public GenCellule(Coordonne p){
        this.coordonne = p;
        this.isThereWall = false;
        this.isThereASymbol = false;
    }
    GenCellule(Coordonne p, List<GenMur> w){
        this.coordonne = p;
        this.genMurs = w;
        this.isThereWall = true;
    }
    public Coordonne getPosition() {
        return coordonne;
    }
    public void setPosition(Coordonne coordonne) {
        this.coordonne = coordonne;
    }
    public List<GenMur> getWalls() {
        return this.genMurs;
    }
    public void setWalls(List<GenMur> genMurs) {
        this.genMurs = genMurs;
    }
    public boolean getIsThereWall() {
        return genMurs.size() > 0;
    }
    public boolean getIsThereARobot() {
        return isThereARobot;
    }
    public boolean getIsThereASymbol() {
        return isThereASymbol;
    }
    public Items getSymbol() {
        return items;
    }
    public InfoPion getCurrentRobot() {
        return currentInfoPion;
    }
    public void addWalls(Direction direction) {
        this.genMurs.add(new GenMur(direction));
        this.isThereWall = true;
    }
    public void rotateWallsRight(int numberOfRotations) {
        for (GenMur genMur : genMurs) {
            for (int n = 0; n < numberOfRotations; n++) {
                switch (genMur.getOrientation()) {
                    case NORTH -> genMur.setOrientation(Direction.EAST);
                    case SOUTH -> genMur.setOrientation(Direction.WEST);
                    case EAST -> genMur.setOrientation(Direction.SOUTH);
                    case WEST -> genMur.setOrientation(Direction.NORTH);
                }
            }
        }
    }
    public void addSymbol(Items items){
        this.isThereASymbol = true;
        this.items = items;
    }
    public void addRobot(InfoPion infoPion) {
        this.currentInfoPion = infoPion;
        this.isThereARobot = true;
    }
    public void removeRobot() {
        this.currentInfoPion = null;
        this.isThereARobot = false;
    }
}




