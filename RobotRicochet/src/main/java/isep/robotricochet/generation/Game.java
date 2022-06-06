package isep.robotricochet.generation;
public class Game {
    public static Game context;
    private Table table;
    private Items currentGoal;
    private int goalCursor;
    public static Status Status;
    private InfoJoueur infoJoueurOne = new InfoJoueur("J1");
    private InfoJoueur infoJoueurTwo = new InfoJoueur("J2");
    public Game(){
        this.table = new Table();
    }
    public void play() {
        if (Game.context != null) {
            throw new RuntimeException
                    ("Petit coquin tu as lancé plusieurs fois le jeu");
        }
        Game.context = new Game();
        this.table.constructBoardFromMiniBoards();
        this.table.addRobotsToBoard();
        this.table.setGoalList();
        this.goalCursor = 0;
        this.currentGoal = this.table.getGoals().get(this.goalCursor);
    }
    public InfoJoueur getPlayerOne() {
        return infoJoueurOne;
    }
    public Items getCurrentGoal() {
        return currentGoal;
    }
    public InfoJoueur getPlayerTwo() {
        return infoJoueurTwo;
    }
    public void setPlayerOne(InfoJoueur infoJoueurOne) {
        this.infoJoueurOne = infoJoueurOne;
    }
    public void setPlayerTwo(InfoJoueur infoJoueurTwo) {
        this.infoJoueurTwo = infoJoueurTwo;
    }
    public void setGoalCursor(int goalCursor) {
        this.goalCursor = goalCursor;
    }
    public Table getBoard() {
        return this.table;
    }
    public enum Status{LAUNCH_TIMER,PREPARE_ROUND,PLAYER_ONE_TURN,PLAYER_TWO_TURN,END_ROUND,GAME_OVER;}
    public void setFirstTurn(){
        if(this.infoJoueurOne.getIsIHaveTheNumberOfHitsFirst()){
            this.infoJoueurOne.setIsMyTurn(true);
            this.infoJoueurTwo.setIsMyTurn(false);
            Game.Status = Status.PLAYER_ONE_TURN;
        }else if(this.infoJoueurTwo.getIsIHaveTheNumberOfHitsFirst()){
            this.infoJoueurTwo.setIsMyTurn(true);
            this.infoJoueurOne.setIsMyTurn(false);
            Game.Status = Status.PLAYER_TWO_TURN;
        }
    }
    public void setNextTurn(){
        if (this.infoJoueurOne.getIsMyTurn() && this.infoJoueurOne.getHitsNumber() >= this.infoJoueurOne.getHitsNumberChoice()){
            this.infoJoueurOne.setIsMyTurn(false);
            this.infoJoueurOne.setHaveAlreadyPlayed(true);
            this.infoJoueurTwo.setIsMyTurn(true);
            if (!this.infoJoueurTwo.isHaveAlreadyPlayed()){
                Game.Status = Status.PLAYER_TWO_TURN;
            }else {
                Game.Status = Status.END_ROUND;
            }
        }else if(this.infoJoueurTwo.getIsMyTurn() && this.infoJoueurTwo.getHitsNumber() >= this.infoJoueurTwo.getHitsNumberChoice()){
            this.infoJoueurTwo.setIsMyTurn(false);
            this.infoJoueurTwo.setHaveAlreadyPlayed(true);
            this.infoJoueurOne.setIsMyTurn(true);
            if (!this.infoJoueurOne.isHaveAlreadyPlayed()){
                Game.Status = Status.PLAYER_ONE_TURN;
            }else {
                Game.Status = Status.END_ROUND;
            }
        }
    }
    public void nextGoalOrGameOver(){
        this.goalCursor++;                                                                                              //On incrémente l'index de la liste
        if(this.goalCursor >= 17){                                                                                      //Si l'index est supérieur ou égal à 17
            Game.Status = Status.GAME_OVER;                                                                             //C'est la fin du jeu
        }else{
            this.currentGoal = this.table.getGoals().get(this.goalCursor);                                              //Sinon on définit du nouvel objectif
            Game.Status = Game.Status.LAUNCH_TIMER;
        }
    }
    public boolean itIsWin(InfoPion infoPion){
        if((infoPion.getCurrentCell().getSymbol() == this.currentGoal) && (infoPion.getCurrentCell().getSymbol().getColor() == infoPion.getColor()) && (this.currentGoal.getColor() == infoPion.getColor())){
            if(this.infoJoueurOne.getIsMyTurn()){
                this.infoJoueurOne.addAnotherWonRound();
                this.infoJoueurOne.setRoundWon(true);
                System.out.println("WIIIIIIIIIIIIIIIIN" + this.infoJoueurOne.getWonRounds());
            }else if(this.infoJoueurTwo.getIsMyTurn()){
                this.infoJoueurTwo.addAnotherWonRound();
                this.infoJoueurTwo.setRoundWon(true);
                System.out.println("WIIIIIIIIIIIIIIIIIIN" + this.infoJoueurTwo.getWonRounds());
            }
            Game.Status = Status.END_ROUND;
            return true;
        }
        else {
            return false;
        }
    }
    public void reinitializePlayers(){
        this.infoJoueurOne.setRoundWon(false);
        this.infoJoueurTwo.setRoundWon(false);
        this.infoJoueurOne.setHaveAlreadyPlayed(false);
        this.infoJoueurTwo.setHaveAlreadyPlayed(false);
        this.infoJoueurOne.setIsMyTurn(false);
        this.infoJoueurTwo.setIsMyTurn(false);
        this.infoJoueurOne.setHitsNumberChoice(0);
        this.infoJoueurTwo.setHitsNumberChoice(0);
        this.infoJoueurOne.setHitsNumber(0);
        this.infoJoueurTwo.setHitsNumber(0);
    }
    public boolean isValidMove(GenCellule currentGenCellule, Direction direction) {
        Coordonne nextCellCoordonne = currentGenCellule.getPosition().nextPosition(direction);
        if (nextCellCoordonne.getNumColonne() > 16 || nextCellCoordonne.getNumLigne() > 16) {
            return false;
        }
        GenCellule nextGenCellule = this.table.getCell(nextCellCoordonne);
        if (currentGenCellule.getIsThereWall()) {
            for (GenMur genMur : currentGenCellule.getWalls()) {
                if (genMur.getOrientation() == direction) { return false; }
            }
        }

        if (nextGenCellule.getIsThereARobot()) { return false; }

        if (nextGenCellule.getIsThereWall()) {
            for (GenMur genMur : nextGenCellule.getWalls()) {
                switch (genMur.getOrientation()) {
                    case NORTH:
                        if (direction == Direction.SOUTH) { return false; }
                        break;
                    case SOUTH:
                        if (direction == Direction.NORTH) { return false; }
                        break;
                    case EAST:
                        if (direction == Direction.WEST) { return false; }
                        break;
                    case WEST:
                        if (direction == Direction.EAST) { return false; }
                        break;
                }
            }
        }

        return true;
    }
    public void move(GenCellule currentGenCellule, Direction direction) {
        InfoPion infoPion = currentGenCellule.getCurrentRobot();
        Coordonne currentCoordonne = currentGenCellule.getPosition();
        table.getCell(currentCoordonne).removeRobot();
        Coordonne newCoordonne = currentGenCellule.getPosition().nextPosition(direction);
        GenCellule newGenCellule = table.getCell(newCoordonne);
        newGenCellule.addRobot(infoPion);
    }
}
