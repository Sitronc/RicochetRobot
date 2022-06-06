package isep.robotricochet.generation;
public class InfoJoueur {

    private String name;
    private int hitsNumber;
    private int hitsNumberChoice;
    private boolean isMyTurn;
    private boolean haveAlreadyPlayed;
    private boolean iHaveTheNumberOfHitsFirst;
    private int wonRounds;
    private boolean roundWon;

    public InfoJoueur(String n){
        this.name = n;
        this.isMyTurn = false;
        this.iHaveTheNumberOfHitsFirst = false;
        this.haveAlreadyPlayed = false;
        this.hitsNumber = 0;
        this.wonRounds = 0;
    }
    public void addAnotherWonRound(){
        this.wonRounds++;
    }
    public boolean getIsMyTurn() {
        return isMyTurn;
    }
    public void setIsMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }
    public int getHitsNumber() {
        return hitsNumber;
    }
    public void setHitsNumber(int hitsNumber) {
        this.hitsNumber = hitsNumber;
    }
    public int getHitsNumberChoice() {
        return hitsNumberChoice;
    }
    public void setHitsNumberChoice(int hitsNumberChoice) {
        this.hitsNumberChoice = hitsNumberChoice;
    }
    public boolean getIsIHaveTheNumberOfHitsFirst() {
        return iHaveTheNumberOfHitsFirst;
    }
    public void setiHaveTheNumberOfHitsFirst(boolean iHaveTheNumberOfHitsFirst) {
        this.iHaveTheNumberOfHitsFirst = iHaveTheNumberOfHitsFirst;
    }
    public int getWonRounds() {
        return wonRounds;
    }
    public boolean isHaveAlreadyPlayed() {
        return haveAlreadyPlayed;
    }
    public void setHaveAlreadyPlayed(boolean haveAlreadyPlayed) {
        this.haveAlreadyPlayed = haveAlreadyPlayed;
    }
    public boolean isRoundWon() {
        return roundWon;
    }
    public void setRoundWon(boolean roundWon) {
        this.roundWon = roundWon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
