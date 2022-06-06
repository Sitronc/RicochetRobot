package isep.robotricochet.generation;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Items extends StackPane {

    private Color color;
    private Objectif objectif;
    private Coordonne coordonne;
    public Items(Color c, Objectif s, Coordonne p){
        this.color = c;
        this.objectif = s;
        this.coordonne = p;
    }
    public Coordonne getPosition() {
        return coordonne;
    }
    public void setPosition(Coordonne coordonne) {
        this.coordonne = coordonne;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Objectif getTheShape() {
        return this.objectif;
    }
    public void setShape(Objectif objectif) {
        this.objectif = objectif;
    }
}
