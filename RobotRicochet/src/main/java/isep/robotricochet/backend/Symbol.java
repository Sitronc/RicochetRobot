package isep.robotricochet.backend;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * Jetons objectif
 */
public class Symbol extends StackPane {

    private Color color;
    private Shape shape;
    private Position position;

    /**
     * Constructeur du jeton objectif
     * @param c Couleur du jeton objectif
     * @param s Forme du jeton objectif
     * @param p Position du jeton objectif sur le plateau
     */
    public Symbol(Color c, Shape s, Position p){
        this.color = c;
        this.shape = s;
        this.position = p;
    }

    //Getters/Setters
    /**
     * Getter retournant la position du jeton objectif sur le plateau
     * @return Position du jeton objectif sur le plateau
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Setter définissant la position du jeton objectif sur le plateau
     * @param position Position du jeton objectif sur le plateau
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Getter retournant la couleur du jeton objectif
     * @return Couleur du jeton objectif
     */
    public Color getColor() {
        return color;
    }

    /**
     * Setter définissant la couleur du jeton objectif
     * @param color Couleur du jeton objectif
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Getter retournant la forme du jeton objectif
     * @return Forme du jeton objectif
     */
    public Shape getTheShape() {
        return this.shape;
    }

    /**
     * Setter définissant la forme du jeton objectif
     * @param shape Forme du jeton objectif
     */
    public void setShape(Shape shape) {
        this.shape = shape;
    }
}
