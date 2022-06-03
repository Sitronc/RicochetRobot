package isep.robotricochet.backend;

/**
 * Classe gérant le déroulement du jeu
 */
public class Game {

    /**
     * Contexte du jeu
     */
    public static Game context;
    private Board board;
    private Symbol currentGoal;                                                                                         //Jeton objectif à atteindre
    private int goalCursor;                                                                                             //Variable à incrémenter pour sélectionner l'objectif actuel
    /**
     * État du jeu
     */
    public static Status Status;
    private Player playerOne = new Player("J1");
    private Player playerTwo = new Player("J2");

    /**
     * Création du jeu et du board
     */
    public Game(){
        this.board = new Board();
    }

    /**
     * Initialisation du jeu, création d'un contexte , du plateau, du roulement des jetons objectifs à atteindre
     */
    public void play() {
        if (Game.context != null) {
            throw new RuntimeException
                    ("Merci de ne pas exécuter plusieurs fois le jeu");
        }
        //Création d'un jeu
        Game.context = new Game();
        this.board.constructBoardFromMiniBoards();
        this.board.addRobotsToBoard();                                                                                  //Ajout des robots sur le plateau
        this.board.setGoalList();                                                                                       //Définition de l'ordre d'apparition des jetons objectif à atteindre
        this.goalCursor = 0;                                                                                            //Initialisation du curseur qui parcours la liste d'objectifs au fil de la partie
        this.currentGoal = this.board.getGoals().get(this.goalCursor);                                                  //Définition du jeton objectif à atteindre
    }

    //Getters/Setters
    /**
     * Getter du joueur 1
     * @return  Joueur 1
     */
    public Player getPlayerOne() {
        return playerOne;
    }

    /**
     * Getter du jeton objectif actuel à atteindre
     * @return Jeton objectif actuel à atteindre
     */
    public Symbol getCurrentGoal() {
        return currentGoal;
    }

    /**
     * Getter du joueur 2
     * @return Joueur 2
     */
    public Player getPlayerTwo() {
        return playerTwo;
    }

    /**
     * Setter du joueur 1
     * @param playerOne Joueur 1 à définir
     */
    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    /**
     * Setter du joueur 2
     * @param playerTwo Joueur 2 à définir
     */
    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    /**
     * Setter du jeton objectif actuel à atteindre
     * @param goalCursor jeton objectif actuel à atteindre
     */
    public void setGoalCursor(int goalCursor) {
        this.goalCursor = goalCursor;
    }

    /**
     * Getter du plateau de jeu
     * @return Plateau de jeu
     */
    public Board getBoard() {
        return this.board;
    }

    /**
     * État du jeu (déroulement du jeu)
     */
    public enum Status{
        /**
         * Statut activé quand le timer est lancé et que les joueurs doivent entrer le nombre de coups pour atteindre l'objectif
         */
        LAUNCH_TIMER,
        /**
         * Statut intermédiaire pour préparer une manche (entre LAUNCH_TIMER et PLAYER_ONE_TURN/PLAYER_TWO_TURN)
         */
        PREPARE_ROUND,
        /**
         * Tour du joueur 1
         */
        PLAYER_ONE_TURN,
        /**
         * Tour du joueur 2
         */
        PLAYER_TWO_TURN,

        /**
         * Fin d'une manche
         */
        END_ROUND,

        /**
         * Fin du jeu
         */
        GAME_OVER;
    }

    /**
     * Définition du premier tour (définit quel joueur jouera en premier dans un tour)
     */
    public void setFirstTurn(){
        if(this.playerOne.getIsIHaveTheNumberOfHitsFirst()){
            this.playerOne.setIsMyTurn(true);
            this.playerTwo.setIsMyTurn(false);
            Game.Status = Status.PLAYER_ONE_TURN;
        }else if(this.playerTwo.getIsIHaveTheNumberOfHitsFirst()){
            this.playerTwo.setIsMyTurn(true);
            this.playerOne.setIsMyTurn(false);
            Game.Status = Status.PLAYER_TWO_TURN;
        }
    }

    /**
     * Définition du prochain tour (fait passer le tour au prochain joueur si le joueur précédent a épuisé tous ses coups définis au préalable
     * ou passe à la fin du tour si les deux joueurs ont déjà joué)
     */

    public void setNextTurn(){
        if (this.playerOne.getIsMyTurn() && this.playerOne.getHitsNumber() >= this.playerOne.getHitsNumberChoice()){
            this.playerOne.setIsMyTurn(false);
            this.playerOne.setHaveAlreadyPlayed(true);
            this.playerTwo.setIsMyTurn(true);
            if (!this.playerTwo.isHaveAlreadyPlayed()){
                Game.Status = Status.PLAYER_TWO_TURN;
            }else {
                Game.Status = Status.END_ROUND;
            }
        }else if(this.playerTwo.getIsMyTurn() && this.playerTwo.getHitsNumber() >= this.playerTwo.getHitsNumberChoice()){
            this.playerTwo.setIsMyTurn(false);
            this.playerTwo.setHaveAlreadyPlayed(true);
            this.playerOne.setIsMyTurn(true);
            if (!this.playerOne.isHaveAlreadyPlayed()){
                Game.Status = Status.PLAYER_ONE_TURN;
            }else {
                Game.Status = Status.END_ROUND;
            }
        }
    }

    /**
     * Obtention d'un nouveau goal dans la liste ou si la fin de la liste de jetons objectifs a été atteinte, fin du jeu
     */
    public void nextGoalOrGameOver(){
        this.goalCursor++;                                                                                              //On incrémente l'index de la liste
        if(this.goalCursor >= 17){                                                                                      //Si l'index est supérieur ou égal à 17
            Game.Status = Status.GAME_OVER;                                                                             //C'est la fin du jeu
        }else{
            this.currentGoal = this.board.getGoals().get(this.goalCursor);                                              //Sinon on définit du nouvel objectif
            Game.Status = Game.Status.LAUNCH_TIMER;
        }
    }

    /**
     * Méthode vérifiant si le bon robot a atteint le bon jeton objectif (jeton objectif à atteindre en cours)
     * @param robot Robot vérifiant potentiellement la condition pour gagner (si le robot qui atteint le jeton objectif à atteindre a la même couleur que ce dernier)
     * @return true si le bon robot est sur le bon jeton à atteindre, false dans les autres cas
     */
    public boolean itIsWin(Robot robot){
        if((robot.getCurrentCell().getSymbol() == this.currentGoal) && (robot.getCurrentCell().getSymbol().getColor() == robot.getColor()) && (this.currentGoal.getColor() == robot.getColor())){
            if(this.playerOne.getIsMyTurn()){
                this.playerOne.addAnotherWonRound();
                this.playerOne.setRoundWon(true);
                System.out.println("WIIIIIIIIIIIIIIIIN" + this.playerOne.getWonRounds());
            }else if(this.playerTwo.getIsMyTurn()){
                this.playerTwo.addAnotherWonRound();
                this.playerTwo.setRoundWon(true);
                System.out.println("WIIIIIIIIIIIIIIIIIIN" + this.playerTwo.getWonRounds());
            }
            Game.Status = Status.END_ROUND;
            return true;
        }
        else {
            return false;
        }
    }


    /**
     * Méthode remettant à zéro les coups (coups définis et coups effectués) des joueurs et à false leurs propriétés (si la manche a été gagnée, s'ils ont déjà joué, si c'est à eux de jouer)
     */
    public void reinitializePlayers(){
        this.playerOne.setRoundWon(false);
        this.playerTwo.setRoundWon(false);
        this.playerOne.setHaveAlreadyPlayed(false);
        this.playerTwo.setHaveAlreadyPlayed(false);
        this.playerOne.setIsMyTurn(false);
        this.playerTwo.setIsMyTurn(false);
        this.playerOne.setHitsNumberChoice(0);
        this.playerTwo.setHitsNumberChoice(0);
        this.playerOne.setHitsNumber(0);
        this.playerTwo.setHitsNumber(0);
    }

    /**
     * Méthode vérifiant si le coup choisi par le joueur est valide
     * @param currentCell Case où le robot se trouve actuellement
     * @param direction Direction vers où le joueur souhaite déplacer le robot
     * @return true si le coup est possible à effectuer, false si le coup n'est pas possible à effectuer
     */

    public boolean isValidMove(Cell currentCell, Orientation direction) {
        Position nextCellPosition = currentCell.getPosition().nextPosition(direction);
        // If at the edge of board
        if (nextCellPosition.getColumn() > 16 || nextCellPosition.getRow() > 16) {
            return false;
        }

        Cell nextCell = this.board.getCell(nextCellPosition);
        // Check if valid move
        if (currentCell.getIsThereWall()) {
            for (Wall wall : currentCell.getWalls()) {
                if (wall.getOrientation() == direction) { return false; }
            }
        }

        if (nextCell.getIsThereARobot()) { return false; }

        if (nextCell.getIsThereWall()) {
            for (Wall wall : nextCell.getWalls()) {
                switch (wall.getOrientation()) {
                    case NORTH:
                        if (direction == Orientation.SOUTH) { return false; }
                        break;
                    case SOUTH:
                        if (direction == Orientation.NORTH) { return false; }
                        break;
                    case EAST:
                        if (direction == Orientation.WEST) { return false; }
                        break;
                    case WEST:
                        if (direction == Orientation.EAST) { return false; }
                        break;
                }
            }
        }

        return true;
    }

    /**
     * Méthode servant à déplacer le robot de case en case
     * @param currentCell Case où le robot se trouve actuellement
     * @param direction Direction vers laquelle le robot va avancer
     */
    public void move(Cell currentCell, Orientation direction) {
        Robot robot = currentCell.getCurrentRobot();

        // Remove robot from board
        Position currentPosition = currentCell.getPosition();
        board.getCell(currentPosition).removeRobot();

        // Add robot to board at new position
        Position newPosition = currentCell.getPosition().nextPosition(direction);
        Cell newCell = board.getCell(newPosition);
        newCell.addRobot(robot);
    }


    /*
    public void setInitialRobotPositionAtIndex(Position initialRobotPosition, Color robotColor) {
        //this.initialRobotPositions[i] = initialRobotPosition;
        //this.initialRobotPositions = new HashMap<>();
        this.initialRobotPositions.put(String.valueOf(robotColor), initialRobotPosition);
    }
     */
}
