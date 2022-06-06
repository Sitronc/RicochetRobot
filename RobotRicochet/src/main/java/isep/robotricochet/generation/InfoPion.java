package isep.robotricochet.generation;

import javafx.scene.paint.Color;
public class InfoPion {
    private Color color;
    private GenCellule currentGenCellule;
    private Coordonne oldCoordonne;

    public InfoPion(Color c){
        this.color = c;
    }
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public GenCellule getCurrentCell() {
        return currentGenCellule;
    }
    public void setCurrentCell(GenCellule currentGenCellule) {
        this.currentGenCellule = currentGenCellule;
    }
    public Coordonne getOldPosition() {
        return oldCoordonne;
    }
    public void setOldPosition(Coordonne oldCoordonne) {
        this.oldCoordonne = oldCoordonne;
    }
}