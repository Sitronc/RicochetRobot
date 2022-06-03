package isep.robotricochet.backend;

import javafx.scene.paint.Color;

/**
 * Robots (pions) à déplacer dans le jeu
 */
public class Robot {
    private Color color;
    private Cell currentCell;
    private Position oldPosition;                                                                                       //Position initiale du robot lors de la génération du plateau en début de partie

    /**
     * Constructeur du robot, avec sa couleur à définir
     * @param c couleur à définir pour le robot
     */
    public Robot(Color c){
        this.color = c;
    }

    //Getters/Setters

    /**
     * Getter retournant la couleur du robot
     * @return La couleur du robot
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter définissant la couleur du robot
     * @param color La couleur du robot
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter retournant la cellule sur laquelle se trouve le robot
     * @return Cellule sur laquelle se trouve le robot
     */
    public Cell getCurrentCell() {
        return currentCell;
    }

    /**
     * Setter définissant  la cellule sur laquelle se trouve le robot
     * @param currentCell Cellule sur laquelle se trouve le robot
     */
    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    /**
     * Getter retournant la position initiale du robot définie lors de la génération du plateau
     * @return Position initiale du robot définie lors de la génération du plateau
     */
    public Position getOldPosition() {
        return oldPosition;
    }

    /**
     * Getter définissant la position initiale du robot définie lors de la génération du plateau
     * @param oldPosition Position initiale du robot définie lors de la génération du plateau
     */
    public void setOldPosition(Position oldPosition) {
        this.oldPosition = oldPosition;
    }
}