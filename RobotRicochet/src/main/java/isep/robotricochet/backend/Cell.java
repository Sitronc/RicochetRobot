package isep.robotricochet.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Case du plateau
 */
public class Cell {
    private Position position;
    private Robot currentRobot;
    private List<Wall> walls = new ArrayList<>();
    private boolean isThereWall;
    private boolean isThereARobot;
    private boolean isThereASymbol;
    private Symbol symbol;

    /**
     * Construction d'une case simple, en renseignant sa position
     * @param p Position de la case à définir
     */
    public Cell(Position p){
        this.position = p;
        this.isThereWall = false;
        this.isThereASymbol = false;
    }

    /**
     * Construction d'une case avec mur
     * @param p Position de la case à définir
     * @param w Liste de murs figurant sur la case à définir
     */
    Cell(Position p, List<Wall> w){
        this.position = p;
        this.walls = w;
        this.isThereWall = true;
    }

    //Getters/Setters

    /**
     * Getter retournant la position de la case
     * @return Position de la case
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Setter définissant la position de la case
     * @param position Position de la case
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Getter retournant la liste des murs posés sur la case
     * @return Liste des murs posés sur la case
     */
    public List<Wall> getWalls() {
        return this.walls;
    }

    /**
     * Setter définissant la liste des murs posés sur la case
     * @param walls Liste des murs posés sur la case
     */
    public void setWalls(List<Wall> walls) {
        this.walls = walls;
    }

    /**
     * Getter retournant s'il y a un mur sur la case
     * @return true s'il y a un mur sur la case, false dans le cas inverse
     */
    public boolean getIsThereWall() {
        return walls.size() > 0;
    }

    /**
     * Getter retournant s'il y a un robot sur la case
     * @return true s'il y a un robot sur la case, false dans le cas inverse
     */
    public boolean getIsThereARobot() {
        return isThereARobot;
    }

    /**
     * Getter retournant s'il y a un jeton objectif sur la case
     * @return true s'il y a un jeton objectif sur la case, false dans le cas inverse
     */
    public boolean getIsThereASymbol() {
        return isThereASymbol;
    }

    /**
     * Getter retournant le jeton objectif sur la case
     * @return le jeton objectif sur la case
     */
    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * Getter retournant le robot sur la case
     * @return Le robot sur la case
     */
    public Robot getCurrentRobot() {
        return currentRobot;
    }

    //Méthodes
    /**
     * Ajout d'un mur sur une case
     * @param orientation Orientation du mur à ajouter (nord, sud, est ou ouest)
     */
    public void addWalls(Orientation orientation) {
        this.walls.add(new Wall(orientation));
        this.isThereWall = true;
    }

    /**
     * Rotation de la case vers la droite à 90°
     * @param numberOfRotations Nombre de fois que l'on souhaite tourner la case
     */
    public void rotateWallsRight(int numberOfRotations) {
        for (Wall wall : walls) {
            for (int n = 0; n < numberOfRotations; n++) {
                switch (wall.getOrientation()) {
                    case NORTH -> wall.setOrientation(Orientation.EAST);
                    case SOUTH -> wall.setOrientation(Orientation.WEST);
                    case EAST -> wall.setOrientation(Orientation.SOUTH);
                    case WEST -> wall.setOrientation(Orientation.NORTH);
                }
            }
        }
    }

    /**
     * Ajout d'un jeton objectif sur la case
     * @param symbol Jeton objectif à ajouer sur la case
     */
    public void addSymbol(Symbol symbol){
        this.isThereASymbol = true;
        this.symbol = symbol;
    }

    /**
     * Ajout d'un robot sur la case
     * @param robot Robot à ajouter sur la case
     */
    public void addRobot(Robot robot) {
        this.currentRobot = robot;
        this.isThereARobot = true;
    }

    /**
     * Suppression du robot présent sur la case
     */
    public void removeRobot() {
        this.currentRobot = null;
        this.isThereARobot = false;
    }
}




