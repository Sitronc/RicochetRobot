package isep.robotricochet.backend;

/**
 * Position d'une cellule
 */
public class Position {
    private int row;
    private int column;

    /**
     * Constructeur de la position
     * @param r Numéro de ligne de la position
     * @param c Numéro de colonne de la position
     */
    public Position(int r, int c){
        this.row = r;
        this.column = c;
    }

    //Getters/Setters

    /**
     * Getter retournant le numéro de ligne de la position
     * @return Numéro de colonne de la position
     */
    public int getRow() {
        return row;
    }

    /**
     * Setter définissant le numéro de ligne de la position
     * @param row Numéro de ligne de la position
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Getter retournant le numéro de colonne de la position
     * @return Numéro de colonne de la position
     */
    public int getColumn() {
        return column;
    }

    /**
     * Setter définissant le numéro de colonne de la position
     * @param column Numéro de colonne de la position
     */
    public void setColumn(int column) {
        this.column = column;
    }

    //Méthodes
    /**
     * Déplacement de la position actuelle vers une position suivante (de 1 en 1)
     * Par exemple, si on a une position de (1, 1), si on se dirige vers le sud, notre nouvelle position sera de (2, 1)
     * @param direction Direction vers laquelle on souhaite s'engager (nord, sud, est ou ouest)
     * @return Retourne la nouvelle case sur laquelle on se trouvera après le mouvement
     */
    public Position nextPosition(Orientation direction) {
        Position nextCellPosition = new Position(this.row, this.column);

        switch (direction) {
            case NORTH -> nextCellPosition.setColumn(nextCellPosition.getColumn() - 1);
            case SOUTH -> nextCellPosition.setColumn(nextCellPosition.getColumn() + 1);
            case EAST -> nextCellPosition.setRow(nextCellPosition.getRow() + 1);
            case WEST -> nextCellPosition.setRow(nextCellPosition.getRow() - 1);
        }

        return nextCellPosition;
    }

}
