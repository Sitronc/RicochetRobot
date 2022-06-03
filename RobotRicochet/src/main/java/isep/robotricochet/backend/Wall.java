package isep.robotricochet.backend;

/**
 * Murs à insérer sur certaines cases
 */

public class Wall {
    private Orientation orientation;

    /**
     * Constructeur du mur en indiquant son orientation
     * @param o Orientation du mur
     */
    public Wall(Orientation o){
        this.orientation = o;
    }

    //Getters/Setters
    /**
     * Getter retournant l'orientation du mur
     * @return Orientation du mur
     */
    public Orientation getOrientation() {
        return orientation;
    }

    /**
     * Setter définissant l'orientation du mur
     * @param orientation Orientation du mur
     */
    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
