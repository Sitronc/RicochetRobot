package isep.robotricochet.generation;

public class Coordonne {

    private int row;
    private int column;
    public Coordonne(int r, int c){
        this.row = r;
        this.column = c;
    }
    public int getNumLigne() {
        return row;
    }
    public void setNumLigne(int row) {
        this.row = row;
    }
    public int getNumColonne() {
        return column;
    }
    public void setNumColonne(int column) {
        this.column = column;
    }
    public Coordonne nextPosition(Direction direction) {
        Coordonne nextCellCoordonne = new Coordonne(this.row, this.column);

        switch (direction) {
            case NORTH -> nextCellCoordonne.setNumColonne(nextCellCoordonne.getNumColonne() - 1);
            case SOUTH -> nextCellCoordonne.setNumColonne(nextCellCoordonne.getNumColonne() + 1);
            case EAST -> nextCellCoordonne.setNumLigne(nextCellCoordonne.getNumLigne() + 1);
            case WEST -> nextCellCoordonne.setNumLigne(nextCellCoordonne.getNumLigne() - 1);
        }

        return nextCellCoordonne;
    }

}
