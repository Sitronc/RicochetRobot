package isep.robotricochet.backend;

/**
 * Contient des informations relatives aux joueurs
 */
public class Player {

    private String name;                                                                                                //Nom du joueur
    private int hitsNumber;                                                                                             //Nombre de coups faits dans une partie
    private int hitsNumberChoice;                                                                                       //Nombre de coups trouvés durant le décompte
    private boolean isMyTurn;                                                                                           //Définit si c'est au tour du joueur
    private boolean haveAlreadyPlayed;                                                                                  //Dit si le joueur a déjà joué
    private boolean iHaveTheNumberOfHitsFirst;                                                                          //Dit si le joueur est le premier a avoir trouvé le nombre de coups
    private int wonRounds;                                                                                              //Nombre de manches remportées
    private boolean roundWon;                                                                                           //Indique si la manche en cours a été gagnée par le joueur

    /**
     * Constructeur du joueur, en indiquant son nom
     * @param n Nom du joueur
     */
    public Player(String n){
        this.name = n;
        this.isMyTurn = false;
        this.iHaveTheNumberOfHitsFirst = false;
        this.haveAlreadyPlayed = false;
        this.hitsNumber = 0;
        this.wonRounds = 0;
    }

    //Méthodes
    /**
     * Incrémentation du nombre de manches gagnées
     */
    public void addAnotherWonRound(){
        this.wonRounds++;
    }


    //Getters/Setters

    /**
     * Getter signalant si c'est le tour du joueur
     * @return true si c'est bien le tour du joueur, false si ce n'est pas le tour du joueur
     */
    public boolean getIsMyTurn() {
        return isMyTurn;
    }

    /**
     * Setter définissant si c'est le tour du joueur
     * @param myTurn true pour définir qu'il s'agit du tour du joueur, false pour définir qu'il ne s'agit pas du tour du joueur
     */
    public void setIsMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

    /**
     * Getter pour obtenir le nombre de coups effectués par le joueurs
     * @return Nombre de coups effectués par le joueur
     */
    public int getHitsNumber() {
        return hitsNumber;
    }

    /**
     * Setter pour définir le nombre de coups effectués par le joueurs
     * @param hitsNumber Nombre de coups effectués par le joueur
     */
    public void setHitsNumber(int hitsNumber) {
        this.hitsNumber = hitsNumber;
    }

    /**
     * Getter retournant le nombre de coups à faire pour atteindre le jeton objectif définit par le joueur
     * @return Nombre de coups à faire pour atteindre le jeton objectif définit par le joueur
     */
    public int getHitsNumberChoice() {
        return hitsNumberChoice;
    }

    /**
     * Setter définissant le nombre de coups à faire pour atteindre le jeton objectif définit par le joueur
     * @param hitsNumberChoice Nombre de coups à faire pour atteindre le jeton objectif définit par le joueur
     */
    public void setHitsNumberChoice(int hitsNumberChoice) {
        this.hitsNumberChoice = hitsNumberChoice;
    }

    /**
     * Getter retournant si le joueur a trouvé en premier le nombre de coups à affectuer pour atteindre le jeton objectif
     * @return true si le joueur a trouvé en premier le nombre de coups à affectuer pour atteindre le jeton objectif, false dans le cas inverse
     */
    public boolean getIsIHaveTheNumberOfHitsFirst() {
        return iHaveTheNumberOfHitsFirst;
    }

    /**
     * Setter définissant si le joueur a trouvé en premier le nombre de coups à affectuer pour atteindre le jeton objectif
     * @param iHaveTheNumberOfHitsFirst true si le joueur a trouvé en premier le nombre de coups à affectuer pour atteindre le jeton objectif, false dans le cas inverse
     */
    public void setiHaveTheNumberOfHitsFirst(boolean iHaveTheNumberOfHitsFirst) {
        this.iHaveTheNumberOfHitsFirst = iHaveTheNumberOfHitsFirst;
    }

    /**
     * Getter retournant le nombre de manches gagnées par le joueur
     * @return Nombre de manches gagnées par le joueur
     */
    public int getWonRounds() {
        return wonRounds;
    }

    /**
     * Getter retournant si le joueur a déjà joué lors d'une manche
     * @return true si le joueur a déjà joué lors d'une manche, false dans le cas inverse
     */
    public boolean isHaveAlreadyPlayed() {
        return haveAlreadyPlayed;
    }

    /**
     * Setter définissant si le joueur a déjà joué lors d'une manche
     * @param haveAlreadyPlayed true si le joueur a déjà joué lors d'une manche, false dans le cas inverse
     */
    public void setHaveAlreadyPlayed(boolean haveAlreadyPlayed) {
        this.haveAlreadyPlayed = haveAlreadyPlayed;
    }

    /**
     * Getter retournant si le joueur a gagné la manche actuelle
     * @return true si le joueur a gagné la manche actuelle, false le cas inverse
     */
    public boolean isRoundWon() {
        return roundWon;
    }

    /**
     * Setter définissant si le joueur a gagné la manche actuelle
     * @param roundWon true si le joueur a gagné la manche actuelle, false le cas inverse
     */
    public void setRoundWon(boolean roundWon) {
        this.roundWon = roundWon;
    }

    /**
     * Getter retournant le nom du joueur
     * @return Le nom du joueur
     */
    public String getName() {
        return name;
    }

    /**
     * Setter définissant le nom du joueur
     * @param name Nom du joueur
     */
    public void setName(String name) {
        this.name = name;
    }
}
