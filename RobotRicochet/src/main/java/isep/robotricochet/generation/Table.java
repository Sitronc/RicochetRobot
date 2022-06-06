package isep.robotricochet.generation;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class Table {

    private GenCellule[][] genCellules;
    private GenCellule[][][] miniBoards;
    private final List<Items> items = new ArrayList<>();
    private List<Items> goals = new ArrayList<>();
    public Table() {}
    public void makeCentralBox(){
        this.genCellules[9][7].addWalls(Direction.SOUTH);
        this.genCellules[9][10].addWalls(Direction.NORTH);
        this.genCellules[10][8].addWalls(Direction.WEST);
        this.genCellules[10][9].addWalls(Direction.WEST);
        this.genCellules[7][8].addWalls(Direction.EAST);
        this.genCellules[7][9].addWalls(Direction.EAST);
        this.genCellules[8][7].addWalls(Direction.SOUTH);
        this.genCellules[8][10].addWalls(Direction.NORTH);
        for(int i = 1; i < this.genCellules.length; i++){
            this.genCellules[i][1].addWalls(Direction.NORTH);
        }
        for(int i = 1; i < this.genCellules.length; i++){
            this.genCellules[1][i].addWalls(Direction.WEST);
        }
        for(int i = 1; i < this.genCellules.length; i++){
            this.genCellules[16][i].addWalls(Direction.EAST);
        }
        for(int i = 1; i < this.genCellules.length; i++){
            this.genCellules[i][16].addWalls(Direction.SOUTH);
        }
    }
    public void addRobotsToBoard() {
        for (int i = 0; i < 4; i++) {
            int randomRow = (int)(Math.random() * 16) + 1;
            int randomColumn = (int)(Math.random() * 16) + 1;
            while ((randomRow == 8 || randomRow == 9) && (randomColumn == 8 || randomColumn == 9)) {
                randomRow = (int)(Math.random() * 16) + 1;
                randomColumn = (int)(Math.random() * 16) + 1;
            }
            Color robotColor = Color.RED;
            switch (i) {
                case 1 -> robotColor = Color.BLUE;
                case 2 -> robotColor = Color.GREEN;
                case 3 -> robotColor = Color.YELLOW;
            }

            InfoPion infoPion = new InfoPion(robotColor);
            this.genCellules[randomRow][randomColumn].addRobot(infoPion);
            infoPion.setOldPosition(new Coordonne(randomRow, randomColumn));
        }
    }
    public void initMiniBoards() {
        this.miniBoards = new GenCellule[4][8][8];

        for (int b = 0; b < 4; b++) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    this.miniBoards[b][i][j] = new GenCellule(new Coordonne(i, j));
                }
            }
        }
    }
    private void addWallsToMiniBoards() {
        this.miniBoards[0][0][3].addWalls(Direction.EAST);
        this.miniBoards[0][2][5].addWalls(Direction.EAST);
        this.miniBoards[0][2][5].addWalls(Direction.SOUTH);
        this.miniBoards[0][4][0].addWalls(Direction.SOUTH);
        this.miniBoards[0][4][2].addWalls(Direction.EAST);
        this.miniBoards[0][4][2].addWalls(Direction.NORTH);
        this.miniBoards[0][5][7].addWalls(Direction.SOUTH);
        this.miniBoards[0][5][7].addWalls(Direction.WEST);
        this.miniBoards[0][6][1].addWalls(Direction.WEST);
        this.miniBoards[1][0][1].addWalls(Direction.EAST);
        this.miniBoards[1][1][5].addWalls(Direction.EAST);
        this.miniBoards[1][1][5].addWalls(Direction.EAST);
        this.miniBoards[1][1][7].addWalls(Direction.SOUTH);
        this.miniBoards[1][3][1].addWalls(Direction.NORTH);
        this.miniBoards[1][3][1].addWalls(Direction.WEST);
        this.miniBoards[1][4][6].addWalls(Direction.NORTH);
        this.miniBoards[1][4][6].addWalls(Direction.EAST);
        this.miniBoards[1][6][4].addWalls(Direction.SOUTH);
        this.miniBoards[1][6][4].addWalls(Direction.WEST);
        this.miniBoards[2][1][4].addWalls(Direction.SOUTH);
        this.miniBoards[2][1][4].addWalls(Direction.WEST);
        this.miniBoards[2][2][0].addWalls(Direction.SOUTH);
        this.miniBoards[2][2][6].addWalls(Direction.NORTH);
        this.miniBoards[2][2][6].addWalls(Direction.WEST);
        this.miniBoards[2][4][7].addWalls(Direction.NORTH);
        this.miniBoards[2][4][7].addWalls(Direction.EAST);
        this.miniBoards[2][5][1].addWalls(Direction.NORTH);
        this.miniBoards[2][5][1].addWalls(Direction.EAST);
        this.miniBoards[2][6][3].addWalls(Direction.EAST);
        this.miniBoards[2][6][3].addWalls(Direction.SOUTH);
        this.miniBoards[2][7][4].addWalls(Direction.EAST);
        this.miniBoards[3][1][5].addWalls(Direction.SOUTH);
        this.miniBoards[3][1][5].addWalls(Direction.WEST);
        this.miniBoards[3][3][1].addWalls(Direction.EAST);
        this.miniBoards[3][3][1].addWalls(Direction.SOUTH);
        this.miniBoards[3][3][7].addWalls(Direction.SOUTH);
        this.miniBoards[3][5][6].addWalls(Direction.NORTH);
        this.miniBoards[3][5][6].addWalls(Direction.EAST);
        this.miniBoards[3][6][2].addWalls(Direction.NORTH);
        this.miniBoards[3][6][2].addWalls(Direction.WEST);
        this.miniBoards[3][7][3].addWalls(Direction.EAST);
    }
    public void rotateMiniBoardAtIndexRight(int index, int numberOfRotations) {
        GenCellule[][] miniBoard = miniBoards[index];

        for (int n = 0; n < numberOfRotations; n++) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    GenCellule tempGenCellule = miniBoard[i][j];
                    miniBoard[i][j] = miniBoard[8 - 1 - j][i];
                    miniBoard[8 - 1 - j][i] = miniBoard[8 - 1 - i][8 - 1 - j];
                    miniBoard[8 - 1 - i][8 - 1 - j] = miniBoard[j][8 - 1 - i];
                    miniBoard[j][8 - 1 - i] = tempGenCellule;
                    GenCellule genCellule = miniBoard[i][j];
                    genCellule.rotateWallsRight(1);
                }
            }
        }
        miniBoards[index] = miniBoard;
    }
    public void constructBoardFromMiniBoards() {
        initMiniBoards();
        addWallsToMiniBoards();
        List<Integer> randomIndexes = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(randomIndexes);
        for (Integer index : randomIndexes) {
            int numberOfRotations = (int) (Math.random() * 4);
            rotateMiniBoardAtIndexRight(index, numberOfRotations);
        }
        this.genCellules = new GenCellule[17][17];
        for (int i = 1; i < 17; i++) {
            for (int j = 1; j < 17; j++) {
                this.genCellules[i][j] = new GenCellule(new Coordonne(i, j));
            }
        }
        for (int r = 1; r < 17; r++) {
            for (int c = 1; c < 17; c++) {
                int index;

                if (r <= 8) {
                    index = c <= 8 ? 0 : 1;
                } else {
                    index = c <= 8 ? 2 : 3;
                }
                GenCellule[][] miniBoard = miniBoards[randomIndexes.get(index)];
                GenCellule genCellule = miniBoard[r - 1 - (index < 2 ? 0 : 8)][c - 1 - (index % 2) * 8];
                List<GenMur> genMurs = genCellule.getWalls();

                if (genCellule.getIsThereWall()) {
                    this.genCellules[r][c].setWalls(genMurs);
                }
            }
        }
        makeCentralBox();
        setSymbols();
        setSymbolsOnCell();
    }

    public void setSymbols() {
        this.items.add(new Items(Color.GREEN, Objectif.GEAR, new Coordonne(2, 4)));
        this.items.add(new Items(Color.RED, Objectif.GEAR, new Coordonne(2, 13)));
        this.items.add(new Items(Color.YELLOW, Objectif.STAR, new Coordonne(4, 7)));
        this.items.add(new Items(Color.BLUE, Objectif.STAR, new Coordonne(4, 10)));
        this.items.add(new Items(Color.RED, Objectif.MOON, new Coordonne(5, 2)));
        this.items.add(new Items(Color.GREEN, Objectif.MOON, new Coordonne(5, 15)));
        this.items.add(new Items(Color.YELLOW, Objectif.PLANET, new Coordonne(6, 11)));
        this.items.add(new Items(Color.BLUE, Objectif.PLANET, new Coordonne(7, 5)));
        this.items.add(new Items(Color.BLACK, Objectif.VORTEX, new Coordonne(9, 13)));
        this.items.add(new Items(Color.BLUE, Objectif.MOON, new Coordonne(10, 11)));
        this.items.add(new Items(Color.BLUE, Objectif.GEAR, new Coordonne(10, 4)));
        this.items.add(new Items(Color.RED, Objectif.PLANET, new Coordonne(12, 6)));
        this.items.add(new Items(Color.YELLOW, Objectif.GEAR, new Coordonne(12, 10)));
        this.items.add(new Items(Color.GREEN, Objectif.PLANET, new Coordonne(13, 15)));
        this.items.add(new Items(Color.YELLOW, Objectif.MOON, new Coordonne(14, 2)));
        this.items.add(new Items(Color.GREEN, Objectif.STAR, new Coordonne(15, 7)));
        this.items.add(new Items(Color.RED, Objectif.STAR, new Coordonne(15, 14)));
    }
    public void setSymbolsOnCell(){
        this.genCellules[2][4].addSymbol(this.items.get(0));
        this.genCellules[2][13].addSymbol(this.items.get(1));
        this.genCellules[4][7].addSymbol(this.items.get(2));
        this.genCellules[4][10].addSymbol(this.items.get(3));
        this.genCellules[5][2].addSymbol(this.items.get(4));
        this.genCellules[5][15].addSymbol(this.items.get(5));
        this.genCellules[6][11].addSymbol(this.items.get(6));
        this.genCellules[7][5].addSymbol(this.items.get(7));
        this.genCellules[9][13].addSymbol(this.items.get(8));
        this.genCellules[10][11].addSymbol(this.items.get(9));
        this.genCellules[10][4].addSymbol(this.items.get(10));
        this.genCellules[12][6].addSymbol(this.items.get(11));
        this.genCellules[12][10].addSymbol(this.items.get(12));
        this.genCellules[13][15].addSymbol(this.items.get(13));
        this.genCellules[14][2].addSymbol(this.items.get(14));
        this.genCellules[15][7].addSymbol(this.items.get(15));
        this.genCellules[15][14].addSymbol(this.items.get(16));
    }
    public void setGoalList(){
        this.goals = this.items;
        Collections.shuffle(this.goals);
    }
    public GenCellule[][] getCells() {
        return this.genCellules;
    }
    public List<Items> getGoals() {
        return goals;
    }
    public GenCellule[][][] getMiniBoards() {
        return miniBoards;
    }
    public GenCellule getCell(Coordonne coordonne) {
        return this.genCellules[coordonne.getNumLigne()][coordonne.getNumColonne()];
    }
}
