package isep.robotricochet;

import isep.robotricochet.backend.*;
import isep.robotricochet.backend.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * Contrôleur du jeu
 */
public class GameController implements Initializable {

    private static final Game game = new Game();
    private final Pane[][] board = new Pane[16][16];
    private final String filePathRoot = "src/main/resources/isep/robotricochet/";

    /**
     * Robot sélectionné par le joueur
     */
    public Robot selectedRobot;

    private Label timerLabel = new Label(), splitTimerLabel = new Label();
    private DoubleProperty timeSeconds = new SimpleDoubleProperty(), splitTimeSeconds = new SimpleDoubleProperty();
    private Duration time = Duration.ZERO, splitTime = Duration.ZERO;
    private Timeline timeline = new Timeline();

    @FXML
    private GridPane boardPane;
    @FXML
    private Label indication;
    @FXML
    private ImageView currentImageGoal;
    @FXML
    private Button gameBtn;
    @FXML
    private Label timerText;
    @FXML
    private Label scorePlayerOne;
    @FXML
    private Label scorePlayerTwo;
    @FXML
    private ImageView goalCenterImage;
    @FXML
    private Spinner<Integer> spinnerPlayerOne;
    @FXML
    private Spinner<Integer> spinnerPlayerTwo;
    @FXML
    private Label hitsNumberChoicePlayerOne;
    @FXML
    private Label hitsNumberChoicePlayerTwo;
    @FXML
    private RadioButton radioPlayerOne;
    @FXML
    private RadioButton radioPlayerTwo;
    @FXML
    private ToggleGroup radioGroup = new ToggleGroup();
    @FXML
    private Label dotPlayerOne;
    @FXML
    private Label dotPlayerTwo;
    @FXML
    private Text indicationNumberOfHits;
    @FXML
    private Text stateRound;
    @FXML
    private Label namePlayerOne;
    @FXML
    private Label namePlayerTwo;
    @FXML
    private Label roundsWonPlayerOne;
    @FXML
    private Label roundsWonPlayerTwo;
    @FXML
    private Label titlePlayerOne;
    @FXML
    private Label titlePlayerTwo;
    private int launchTimer = 30;
    private boolean isTheTimerStopped;
    private boolean itIsWin;
    private Map<Robot, Integer> currentColum = new HashMap<>();
    private Map<Robot, Integer> currentRow = new HashMap<>();

    /**
     * Initialisation du contrôleur
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Lancement du jeu
        game.play();
        this.scorePlayerOne.setText("");
        this.scorePlayerTwo.setText("");
        this.namePlayerOne.setText(game.getPlayerOne().getName());
        this.namePlayerTwo.setText(game.getPlayerTwo().getName());
        this.roundsWonPlayerOne.setText(String.valueOf(game.getPlayerOne().getWonRounds()));
        this.roundsWonPlayerTwo.setText(String.valueOf(game.getPlayerTwo().getWonRounds()));
        itIsWin = false;
        //Création du plateau en frontend
        boardGeneration();
        Scene scene = boardPane.getScene();
        this.radioPlayerOne.setSelected(true);
        this.stateRound.setVisible(false);
        this.spinnerPlayerOne.setVisible(false);
        this.spinnerPlayerTwo.setVisible(false);
        this.radioPlayerOne.setVisible(false);
        this.radioPlayerTwo.setVisible(false);
        this.dotPlayerOne.setVisible(false);
        this.dotPlayerTwo.setVisible(false);
        this.indicationNumberOfHits.setVisible(false);
        game.Status = Game.Status.LAUNCH_TIMER;
    }

    /**
     * Méthode gérant l'activation du bouton principal (et par la même occasion met à jour l'état du jeu)
     */
    @FXML
    public void handleGameBtn(){
        switch (game.Status) {
            case LAUNCH_TIMER -> {
                getPositionRobots();
                timeline.stop();
                game.reinitializePlayers();                                                                             //Remet à 0 le nombre de coups fait précédéments, le nombre de coups choisis etc.
                //reinitializeRobot();
                this.stateRound.setText("Entrez le plus petit nombre de coups");
                this.stateRound.setVisible(true);
                this.spinnerPlayerOne.setVisible(true);
                this.spinnerPlayerTwo.setVisible(true);
                this.indicationNumberOfHits.setVisible(false);
                hitsNumberChoicePlayerOne.setVisible(false);
                hitsNumberChoicePlayerTwo.setVisible(false);
                this.scorePlayerOne.setVisible(false);
                this.scorePlayerTwo.setVisible(false);
                this.gameBtn.setText("Confirmer le nombre de coups");
                game.Status = Game.Status.PREPARE_ROUND;
                /*launchSpinners();
                getFirstFinderPlayer();*/
                timer();
                movePlayer();
                displayGoal();
            }
            case PREPARE_ROUND -> {
                this.scorePlayerOne.setText("");
                this.scorePlayerTwo.setText("");
                launchTimer = 0;
                this.spinnerPlayerOne.setVisible(false);
                this.spinnerPlayerTwo.setVisible(false);
                this.radioPlayerOne.setVisible(false);
                this.radioPlayerTwo.setVisible(false);
                this.indicationNumberOfHits.setVisible(false);
                if (game.getPlayerOne().getIsIHaveTheNumberOfHitsFirst()){
                    this.dotPlayerOne.setVisible(true);
                    this.dotPlayerTwo.setVisible(false);
                } else if (game.getPlayerTwo().getIsIHaveTheNumberOfHitsFirst()) {
                    this.dotPlayerTwo.setVisible(true);
                    this.dotPlayerOne.setVisible(false);
                }
                this.gameBtn.setVisible(false);
                this.hitsNumberChoicePlayerOne.setVisible(true);
                this.hitsNumberChoicePlayerTwo.setVisible(true);
                game.setFirstTurn();
                handleGameBtn();
            }
            case PLAYER_ONE_TURN -> {
                this.scorePlayerOne.setVisible(true);
                this.scorePlayerTwo.setVisible(true);
                this.stateRound.setVisible(true);
                this.stateRound.setText("Tour du joueur 1");
                movePlayer();
                itIsFinished();
            }case PLAYER_TWO_TURN -> {
                this.scorePlayerOne.setVisible(true);
                this.scorePlayerTwo.setVisible(true);
                this.stateRound.setVisible(true);
                this.stateRound.setText("Tour du joueur 2");
                movePlayer();
                itIsFinished();

            }
            case END_ROUND -> {
                this.stateRound.setVisible(true);
                reinitializeRobot();
                selectedRobot = null;
                this.gameBtn.setText("Nouvelle manche");
                if(game.getPlayerOne().isRoundWon()){
                    this.stateRound.setText(game.getPlayerOne().getName() + " a gagné la manche");
                }else if(game.getPlayerTwo().isRoundWon()){
                    this.stateRound.setText(game.getPlayerTwo().getName() + " a gagné la manche");
                }
                else {
                    this.stateRound.setText("Personne n'a gagné");
                }
                itIsWin = false;
                game.reinitializePlayers();
                this.roundsWonPlayerOne.setText(String.valueOf(game.getPlayerOne().getWonRounds()));
                this.roundsWonPlayerTwo.setText(String.valueOf(game.getPlayerTwo().getWonRounds()));
                this.gameBtn.setVisible(true);
                game.nextGoalOrGameOver();
            }case GAME_OVER -> {
                timeline.stop();
                this.gameBtn.setVisible(false);
                this.boardPane.setVisible(false);
                this.scorePlayerOne.setVisible(false);
                this.scorePlayerTwo.setVisible(false);
                this.indication.setVisible(false);
                this.currentImageGoal.setVisible(false);
                this.titlePlayerOne.setVisible(false);
                this.titlePlayerTwo.setVisible(false);
                this.hitsNumberChoicePlayerOne.setVisible(false);
                this.hitsNumberChoicePlayerTwo.setVisible(false);
                this.dotPlayerOne.setVisible(false);
                this.dotPlayerTwo.setVisible(false);
                if(game.getPlayerOne().getWonRounds() > game.getPlayerTwo().getWonRounds()){
                    this.stateRound.setText(game.getPlayerOne().getName() + " a gagné");
                } else if (game.getPlayerOne().getWonRounds() == game.getPlayerTwo().getWonRounds()) {
                    this.stateRound.setText("Personne n'a gagné");
                }else {
                    this.stateRound.setText(game.getPlayerTwo().getName() + " a gagné");
                }
            }
        }
    }

    /**
     * Méthode de génération de l'affichage du plateau principale (cases, murs, jetons objectifs et robots)
     */
    private void boardGeneration(){
        Image cellImage = new Image(new File(filePathRoot + "boards/Cell.PNG").toURI().toString() , 44, 44, false, false);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                StackPane stackPane = new StackPane();

                // Ajouter un identifiant
                stackPane.setId(Integer.toString(i + 1) + "," + Integer.toString(j + 1));

                // Ajouter image de cellule
                ImageView cellImageView = new ImageView(cellImage);
                stackPane.getChildren().add(cellImageView);

                Cell currentCell = game.getBoard().getCells()[i + 1][j + 1];

                //Ajout des murs, s'il y en a
                String wallImageFilename = null;
                if (currentCell.getIsThereWall()) {
                    for (int w = 0; w < currentCell.getWalls().size(); w++) {
                        switch (currentCell.getWalls().get(w).getOrientation()) {
                            case NORTH -> wallImageFilename = "NorthWall.png";
                            case SOUTH -> wallImageFilename = "SouthWall.png";
                            case EAST -> wallImageFilename = "EastWall.png";
                            case WEST -> wallImageFilename = "WestWall.png";
                        }

                        // Add wall image to cell
                        Image wallImage = new Image(new File(filePathRoot + "boards/" + wallImageFilename).toURI().toString() , 44, 44, false, false);
                        ImageView wallImageView = new ImageView(wallImage);
                        stackPane.getChildren().add(wallImageView);
                    }
                }

                String symbolImageFilename = null;
                if(currentCell.getIsThereASymbol()){
                    if (Color.BLUE.equals(currentCell.getSymbol().getColor())) {
                        switch (currentCell.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "BlueGear.png";
                            case MOON -> symbolImageFilename = "BlueMoon.png";
                            case PLANET -> symbolImageFilename = "BluePlanet.png";
                            case STAR -> symbolImageFilename = "BlueStar.png";
                        }
                    } else if (Color.GREEN.equals(currentCell.getSymbol().getColor())) {
                        switch (currentCell.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "GreenGear.png";
                            case MOON -> symbolImageFilename = "GreenMoon.png";
                            case PLANET -> symbolImageFilename = "GreenPlanet.png";
                            case STAR -> symbolImageFilename = "GreenStar.png";
                        }
                    } else if (Color.YELLOW.equals(currentCell.getSymbol().getColor())) {
                        switch (currentCell.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "YellowGear.png";
                            case MOON -> symbolImageFilename = "YellowMoon.png";
                            case PLANET -> symbolImageFilename = "YellowPlanet.png";
                            case STAR -> symbolImageFilename = "YellowStar.png";
                        }
                    } else if (Color.RED.equals(currentCell.getSymbol().getColor())) {
                        switch (currentCell.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "RedGear.png";
                            case MOON -> symbolImageFilename = "RedMoon.png";
                            case PLANET -> symbolImageFilename = "RedPlanet.png";
                            case STAR -> symbolImageFilename = "RedStar.png";
                        }
                    } else if (Color.BLACK.equals(currentCell.getSymbol().getColor())) {
                        symbolImageFilename = "Vortex.png";
                    }
                }

                Image symbolImage = new Image(new File(filePathRoot + "goals/" + symbolImageFilename).toURI().toString() , 35, 35, false, false);
                ImageView symbolImageView = new ImageView(symbolImage);
                stackPane.getChildren().add(symbolImageView);

                // Afficher robots
                if (game.getBoard().getCells()[i + 1][j + 1].getIsThereARobot()) {
                    Robot robot = game.getBoard().getCells()[i + 1][j + 1].getCurrentRobot();

                    String filename = getRobotImageFilename(robot.getColor());

                    Image robotImage = new Image(new File("src/main/resources/isep/robotricochet/robots/" + filename).toURI().toString() , 44, 44, false, false);
                    ImageView robotImageView = new ImageView(robotImage);
                    stackPane.getChildren().add(robotImageView);
                }

                if((i != 7 && i != 8) || (j != 7 && j != 8)){

                }else{

                    stackPane = new StackPane();
                    Image goalBox = new Image(new File("src/main/resources/isep/robotricochet/boards/GoalBox.png").toURI().toString() , 44, 44, false, false);
                    ImageView goalBoxView = new ImageView(goalBox);
                    stackPane.getChildren().add(goalBoxView);
                }
                // Ajouter stackPane au board
                this.board[i][j] = stackPane;
                this.boardPane.add(stackPane, i, j);

            }
        }

    }

    /**
     * Méthode renvoyant le nom d'un fichier de ressource d'image de robot
     * @param robotColor Couleur du robot en question
     * @return Renvoie le nom du fichier de l'image du robot correspondant à la couleur renseignée
     */

    private String getRobotImageFilename(Color robotColor) {
        if (robotColor.equals(Color.RED)) {
            return "robotRed.png";
        } else if (robotColor.equals(Color.BLUE)) {
            return "robotBlue.png";
        } else if (robotColor.equals(Color.GREEN)) {
            return "robotGreen.png";
        } else {
            return "robotYellow.png";
        }
    }

    /**
     * Méthode effectuant le déplacement du robot par le joueur
     * @param direction Direction vers laquelle le robot peut se déplacer (nord, sud, est ou ouest)
     */
    public void move(Orientation direction) {
        Cell currentCell = selectedRobot.getCurrentCell();

        if (selectedRobot != null) {
            while (game.isValidMove(currentCell, direction)) {
                Position oldPosition = currentCell.getPosition();
                Position newPosition = currentCell.getPosition().nextPosition(direction);

                game.move(currentCell, direction);
                updateRobotDisplay(oldPosition, newPosition, selectedRobot);
                currentCell = game.getBoard().getCell(newPosition);
                selectedRobot.setCurrentCell(currentCell);
            }

            // Update selected robot
            selectedRobot = currentCell.getCurrentRobot();

        }
    }

    /**
     *
     */

    private void getPositionRobots() {
        this.currentRow.clear();
        this.currentColum.clear();
        for (int i = 1; i < 17; i++) {
            for (int j = 1; j < 17; j++) {
                if(game.getBoard().getCells()[i][j].getIsThereARobot()){
                    Robot robot = game.getBoard().getCells()[i][j].getCurrentRobot();
                    int column = game.getBoard().getCells()[i][j].getPosition().getColumn();
                    int row = game.getBoard().getCells()[i][j].getPosition().getColumn();
                    this.currentColum.put(robot, column);
                    this.currentRow.put(robot, row);
                }
            }
        }
    }

    /**
     * Suppression du robot de la case
     * @param position Position actuelle du robot
     */

    private void removeRobotFromCell(Position position) {
        int numberOfChildren = board[position.getRow() - 1][position.getColumn() - 1].getChildren().size() - 1;
        board[position.getRow() - 1][position.getColumn() - 1].getChildren().remove(numberOfChildren);
    }

    private void addRobotToCell(Position position, Robot robot) {

        String filename = getRobotImageFilename(robot.getColor());

        // Get stackPane
        Image robotImage = new Image(new File("src/main/resources/isep/robotricochet/robots/" + filename).toURI().toString() , 44, 44, false, false);
        ImageView robotImageView = new ImageView(robotImage);
        board[position.getRow() - 1][position.getColumn() - 1].getChildren().add(robotImageView);
    }

    /**
     * Mise à jour de l'affichage de la position du robot
     * @param oldPosition Position précédente du robot avant son déplacement
     * @param newPosition Nouvelle position du robot atteinte après son déplacement
     * @param robot Le robot à mettre à jour en question
     */
    private void updateRobotDisplay(Position oldPosition, Position newPosition, Robot robot) {
        removeRobotFromCell(oldPosition);
        addRobotToCell(newPosition, robot);
    }

    /**
     * Méthode d'affichage de l'image du jeton objectif à atteindre à droite de l'écran, en dehors du plateau
     */
    private void displayGoal(){
        String symbolImageFilename = null;
        if (Color.BLUE.equals(game.getCurrentGoal().getColor())){
            switch (game.getCurrentGoal().getTheShape()) {
                case GEAR -> symbolImageFilename = "BlueGear.png";
                case MOON -> symbolImageFilename = "BlueMoon.png";
                case PLANET -> symbolImageFilename = "BluePlanet.png";
                case STAR -> symbolImageFilename = "BlueStar.png";
            }
        } else if (Color.RED.equals(game.getCurrentGoal().getColor())) {
            switch (game.getCurrentGoal().getTheShape()) {
                case GEAR -> symbolImageFilename = "RedGear.png";
                case MOON -> symbolImageFilename = "RedMoon.png";
                case PLANET -> symbolImageFilename = "RedPlanet.png";
                case STAR -> symbolImageFilename = "RedStar.png";
            }
        } else if (Color.GREEN.equals(game.getCurrentGoal().getColor())) {
            switch (game.getCurrentGoal().getTheShape()) {
                case GEAR -> symbolImageFilename = "GreenGear.png";
                case MOON -> symbolImageFilename = "GreenMoon.png";
                case PLANET -> symbolImageFilename = "GreenPlanet.png";
                case STAR -> symbolImageFilename = "GreenStar.png";
            }
        } else if (Color.YELLOW.equals(game.getCurrentGoal().getColor())) {
            switch (game.getCurrentGoal().getTheShape()) {
                case GEAR -> symbolImageFilename = "YellowGear.png";
                case MOON -> symbolImageFilename = "YellowMoon.png";
                case PLANET -> symbolImageFilename = "YellowPlanet.png";
                case STAR -> symbolImageFilename = "YellowStar.png";
            }
        } else if (Color.BLACK.equals(game.getCurrentGoal().getColor())) {
            symbolImageFilename = "Vortex.png";
        }
        Image symbolImage = new Image(new File(filePathRoot + "goals/" + symbolImageFilename).toURI().toString() , 40, 40, false, false);
        this.currentImageGoal.setImage(symbolImage);
        this.currentImageGoal.setVisible(true);
    }

    /**
     * Lancement du sablier de 30 secondes
     */
    private void timer(){
        launchTimer = 30;
        isTheTimerStopped = false;
        timerText.setVisible(true);
        timerText.setText(String.valueOf(launchTimer));
        timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), e ->{
            if (launchTimer == 0) {
                if(!isTheTimerStopped) {
                    timerText.setText("");
                    launchTimer = 0;
                    isTheTimerStopped = true;
                    game.Status = Game.Status.PREPARE_ROUND;
                    handleGameBtn();
                }
            }else {
                launchTimer--;
                timerText.setText(String.valueOf(launchTimer));
            }
            e.consume();
        }));
        timeline.setCycleCount(31);
        timeline.play();
    }

    /**
     * Méthode pour gérer les mouvements du robot sélectionné
     */

    private void movePlayer(){
        if (isTheTimerStopped) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    this.board[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            String id = ((StackPane) event.getSource()).getId();

                            // Get robot from cell
                            int[] coordinates = Stream.of(id.split(",")).mapToInt(Integer::parseInt).toArray();
                            Cell currentCell = game.getBoard().getCells()[coordinates[0]][coordinates[1]];

                            if (currentCell.getIsThereARobot()) {
                                selectedRobot = currentCell.getCurrentRobot();
                                selectedRobot.setCurrentCell(currentCell);
                            }
                            event.consume();
                        }
                    });
                }
            }
        }
    }

    /**
     * Méthode pour initialiser les spinners afin d'y entrer le nombre de coups choisis
     */

    private void launchSpinners(){
        SpinnerValueFactory<Integer> valueFactoryPlayerOne = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        valueFactoryPlayerOne.setValue(1);
        SpinnerValueFactory<Integer> valueFactoryPlayerTwo = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100);
        valueFactoryPlayerTwo.setValue(1);
        spinnerPlayerOne.setValueFactory(valueFactoryPlayerOne);
        spinnerPlayerTwo.setValueFactory(valueFactoryPlayerTwo);
        game.getPlayerOne().setHitsNumberChoice(spinnerPlayerOne.getValue());
        game.getPlayerTwo().setHitsNumberChoice(spinnerPlayerTwo.getValue());
        hitsNumberChoicePlayerOne.setText(String.valueOf("Nombre de coups choisi : " + game.getPlayerOne().getHitsNumberChoice()));
        hitsNumberChoicePlayerTwo.setText(String.valueOf("Nombre de coups choisi : " + game.getPlayerTwo().getHitsNumberChoice()));
        this.spinnerPlayerOne.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                game.getPlayerOne().setHitsNumberChoice(spinnerPlayerOne.getValue());
                hitsNumberChoicePlayerOne.setText(String.valueOf("Nombre de coups choisi : " + game.getPlayerOne().getHitsNumberChoice()));
            }
        });
        this.spinnerPlayerTwo.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observableValue, Integer integer, Integer t1) {
                game.getPlayerTwo().setHitsNumberChoice(spinnerPlayerTwo.getValue());
                hitsNumberChoicePlayerTwo.setText(String.valueOf("Nombre de coups choisi : " + game.getPlayerTwo().getHitsNumberChoice()));
            }
        });
    }

    /**
     * Méthode de gestion des radio boxes (servant à définir quel joueur à trouvé le plus petit nombre de coups en premier)
     */

    public void getFirstFinderPlayer(){
        this.radioPlayerOne.setToggleGroup(this.radioGroup);
        this.radioPlayerTwo.setToggleGroup(this.radioGroup);
        this.radioPlayerOne.setVisible(true);
        this.radioPlayerTwo.setVisible(true);
        game.getPlayerOne().setiHaveTheNumberOfHitsFirst(true);
        game.getPlayerTwo().setiHaveTheNumberOfHitsFirst(false);
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> observableValue, Toggle toggle, Toggle t1) {
                if (radioGroup.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) radioGroup.getSelectedToggle();
                    if (button == radioPlayerOne){
                        game.getPlayerOne().setiHaveTheNumberOfHitsFirst(true);
                        game.getPlayerTwo().setiHaveTheNumberOfHitsFirst(false);
                    }else if(button == radioPlayerTwo){
                        game.getPlayerTwo().setiHaveTheNumberOfHitsFirst(true);
                        game.getPlayerOne().setiHaveTheNumberOfHitsFirst(false);
                    }
                }else {
                    game.getPlayerOne().setiHaveTheNumberOfHitsFirst(true);
                    game.getPlayerTwo().setiHaveTheNumberOfHitsFirst(false);
                }
            }
        });
    }

    /**
     * Méthode mettant à jour le comptage des coups effectués par les joueurs, et passant automatiquement le tour au joueur suivant si le précédent joueur a épuisé son nombre de coups fixé
     */

    public void setHits(){

        if (game.getPlayerOne().getIsMyTurn() && (game.getPlayerOne().getHitsNumber() >= game.getPlayerOne().getHitsNumberChoice())){
            game.setNextTurn();
            handleGameBtn();
        } else if (game.getPlayerTwo().getIsMyTurn() && (game.getPlayerTwo().getHitsNumber() >= game.getPlayerTwo().getHitsNumberChoice())) {
            game.setNextTurn();
            handleGameBtn();
        }

        if (game.getPlayerOne().getIsMyTurn() && game.getPlayerOne().getHitsNumber() < game.getPlayerOne().getHitsNumberChoice()){
            game.getPlayerOne().setHitsNumber(game.getPlayerOne().getHitsNumber() + 1);
            scorePlayerOne.setText(String.valueOf(game.getPlayerOne().getHitsNumber()));
            itIsWin = game.itIsWin(selectedRobot);
        }else if(game.getPlayerTwo().getIsMyTurn() && game.getPlayerTwo().getHitsNumber() < game.getPlayerTwo().getHitsNumberChoice()) {
            game.getPlayerTwo().setHitsNumber(game.getPlayerTwo().getHitsNumber() + 1);
            scorePlayerTwo.setText(String.valueOf(game.getPlayerTwo().getHitsNumber()));
            itIsWin = game.itIsWin(selectedRobot);
        }
    }

    /**
     * Méthode définissant si la manche est finie ou pas
     * @return true si la manche est finie, false dans le cas inverse
     */
    public boolean itIsFinished(){
        if((Game.Status != Game.Status.PLAYER_ONE_TURN &&  Game.Status != Game.Status.PLAYER_TWO_TURN) || itIsWin || (game.getPlayerOne().getHitsNumber() + game.getPlayerTwo().getHitsNumber() == game.getPlayerOne().getHitsNumberChoice() + game.getPlayerTwo().getHitsNumberChoice())){
            itIsWin = false;
            Game.Status = Game.Status.END_ROUND;
            handleGameBtn();
            return true;
        }else {
            return false;
        }
    }

    /**
     * Méthode pour réinitialiser la position des robots à chaque nouveau tour
     */
    public void reinitializeRobot(){
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if(game.getBoard().getCells()[i + 1][j + 1].getIsThereARobot()){
                    Position oldPosition = game.getBoard().getCells()[i + 1][j + 1].getPosition();
                    Robot robot = game.getBoard().getCells()[i + 1][j + 1].getCurrentRobot();
                    robot.setCurrentCell(game.getBoard().getCells()[i + 1][j + 1]);
                    Position newPosition = robot.getOldPosition();
                    game.getBoard().getCell(oldPosition).removeRobot();
                    game.getBoard().getCell(newPosition).addRobot(robot);
                    updateRobotDisplay(oldPosition, newPosition, robot);
                }
            }
        }
    }

    /**
     * Vérifie les conditions nécessaires pour lancer la réinitialisation des robots
     * @return true s'il faut réinitialiser la position des robots (quand l'un des joueurs a atteint le nombre de coups qu'il a fixé), false dans les autres cas
     */

    public boolean launchResetRobot(){
        if(game.getPlayerOne().getHitsNumber() == game.getPlayerOne().getHitsNumberChoice()){                           //Si le joueur 1 a épuisé son nombre de coups
            itIsWin = game.itIsWin(selectedRobot);                                                                      //On vérifie s'il a gagné
            reinitializeRobot();                                                                                        //Réinitialisation des robots
            game.setNextTurn();                                                                                         //Définition du tour suivant
            game.getPlayerOne().setHitsNumber(0);                                                                       //Mise à 0 des coups  du joueur 1
            return true;
        }else if (game.getPlayerTwo().getHitsNumber() == game.getPlayerTwo().getHitsNumberChoice()){                    //Si le joueur 2 a épuisé son nombre de coups
            itIsWin = game.itIsWin(selectedRobot);                                                                      //On vérifie s'il a gagné
            reinitializeRobot();                                                                                        //Réinitialisation des robots
            game.setNextTurn();                                                                                         //Définition du tour suivant
            game.getPlayerTwo().setHitsNumber(0);                                                                       //Mise à 0 des coups  du joueur 2
            return true;
        }else {                                                                                                         //Sinon on retourne faux
            return false;
        }
    }
}