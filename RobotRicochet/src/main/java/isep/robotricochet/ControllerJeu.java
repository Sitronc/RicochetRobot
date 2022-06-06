package isep.robotricochet;

import isep.robotricochet.generation.*;
import isep.robotricochet.generation.GenCellule;
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
public class ControllerJeu implements Initializable {

    private static final Game game = new Game();
    private final Pane[][] board = new Pane[16][16];
    private final String filePathRoot = "src/main/resources/isep/robotricochet/";

    /**
     * Robot sélectionné par le joueur
     */
    public InfoPion selectedInfoPion;

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
    private Map<InfoPion, Integer> currentColum = new HashMap<>();
    private Map<InfoPion, Integer> currentRow = new HashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        game.play();
        this.scorePlayerOne.setText("");
        this.scorePlayerTwo.setText("");
        this.roundsWonPlayerOne.setText(String.valueOf(game.getPlayerOne().getWonRounds()));
        this.roundsWonPlayerTwo.setText(String.valueOf(game.getPlayerTwo().getWonRounds()));
        itIsWin = false;
        boardGeneration();
        Scene scene = boardPane.getScene();
        this.radioPlayerOne.setSelected(true);
        this.spinnerPlayerOne.setVisible(false);
        this.spinnerPlayerTwo.setVisible(false);
        this.radioPlayerOne.setVisible(false);
        this.radioPlayerTwo.setVisible(false);
        this.dotPlayerOne.setVisible(false);
        this.dotPlayerTwo.setVisible(false);
        game.Status = Game.Status.LAUNCH_TIMER;
    }
    @FXML
    public void handleGameBtn(){
        switch (game.Status) {
            case LAUNCH_TIMER -> {
                getPositionRobots();
                timeline.stop();
                game.reinitializePlayers();
                this.spinnerPlayerOne.setVisible(true);
                this.spinnerPlayerTwo.setVisible(true);
                hitsNumberChoicePlayerOne.setVisible(false);
                hitsNumberChoicePlayerTwo.setVisible(false);
                this.scorePlayerOne.setVisible(false);
                this.scorePlayerTwo.setVisible(false);
                this.gameBtn.setText("Confirmer le nombre de coups");
                game.Status = Game.Status.PREPARE_ROUND;
                launchSpinners();
                getFirstFinderPlayer();
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
                movePlayer();
                itIsFinished();
            }case PLAYER_TWO_TURN -> {
                this.scorePlayerOne.setVisible(true);
                this.scorePlayerTwo.setVisible(true);
                movePlayer();
                itIsFinished();

            }
            case END_ROUND -> {
                reinitializeRobot();
                selectedInfoPion = null;
                this.gameBtn.setText("Nouvelle manche");
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
            }
        }
    }
    private void boardGeneration(){
        Image cellImage = new Image(new File(filePathRoot + "planches/Cell.PNG").toURI().toString() , 44, 44, false, false);

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                StackPane stackPane = new StackPane();
                stackPane.setId(Integer.toString(i + 1) + "," + Integer.toString(j + 1));
                ImageView cellImageView = new ImageView(cellImage);
                stackPane.getChildren().add(cellImageView);
                GenCellule currentGenCellule = game.getBoard().getCells()[i + 1][j + 1];
                String wallImageFilename = null;
                if (currentGenCellule.getIsThereWall()) {
                    for (int w = 0; w < currentGenCellule.getWalls().size(); w++) {
                        switch (currentGenCellule.getWalls().get(w).getOrientation()) {
                            case NORTH -> wallImageFilename = "NorthWall.png";
                            case SOUTH -> wallImageFilename = "SouthWall.png";
                            case EAST -> wallImageFilename = "EastWall.png";
                            case WEST -> wallImageFilename = "WestWall.png";
                        }
                        Image wallImage = new Image(new File(filePathRoot + "planches/" + wallImageFilename).toURI().toString() , 44, 44, false, false);
                        ImageView wallImageView = new ImageView(wallImage);
                        stackPane.getChildren().add(wallImageView);
                    }
                }

                String symbolImageFilename = null;
                if(currentGenCellule.getIsThereASymbol()){
                    if (Color.BLUE.equals(currentGenCellule.getSymbol().getColor())) {
                        switch (currentGenCellule.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "BlueGear.png";
                            case MOON -> symbolImageFilename = "BlueMoon.png";
                            case PLANET -> symbolImageFilename = "BluePlanet.png";
                            case STAR -> symbolImageFilename = "BlueStar.png";
                        }
                    } else if (Color.GREEN.equals(currentGenCellule.getSymbol().getColor())) {
                        switch (currentGenCellule.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "GreenGear.png";
                            case MOON -> symbolImageFilename = "GreenMoon.png";
                            case PLANET -> symbolImageFilename = "GreenPlanet.png";
                            case STAR -> symbolImageFilename = "GreenStar.png";
                        }
                    } else if (Color.YELLOW.equals(currentGenCellule.getSymbol().getColor())) {
                        switch (currentGenCellule.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "YellowGear.png";
                            case MOON -> symbolImageFilename = "YellowMoon.png";
                            case PLANET -> symbolImageFilename = "YellowPlanet.png";
                            case STAR -> symbolImageFilename = "YellowStar.png";
                        }
                    } else if (Color.RED.equals(currentGenCellule.getSymbol().getColor())) {
                        switch (currentGenCellule.getSymbol().getTheShape()) {
                            case GEAR -> symbolImageFilename = "RedGear.png";
                            case MOON -> symbolImageFilename = "RedMoon.png";
                            case PLANET -> symbolImageFilename = "RedPlanet.png";
                            case STAR -> symbolImageFilename = "RedStar.png";
                        }
                    } else if (Color.BLACK.equals(currentGenCellule.getSymbol().getColor())) {
                        symbolImageFilename = "Vortex.png";
                    }
                }
                Image symbolImage = new Image(new File(filePathRoot + "objectifs/" + symbolImageFilename).toURI().toString() , 35, 35, false, false);
                ImageView symbolImageView = new ImageView(symbolImage);
                stackPane.getChildren().add(symbolImageView);
                if (game.getBoard().getCells()[i + 1][j + 1].getIsThereARobot()) {
                    InfoPion infoPion = game.getBoard().getCells()[i + 1][j + 1].getCurrentRobot();
                    String filename = getRobotImageFilename(infoPion.getColor());
                    Image robotImage = new Image(new File("src/main/resources/isep/robotricochet/robots/" + filename).toURI().toString() , 44, 44, false, false);
                    ImageView robotImageView = new ImageView(robotImage);
                    stackPane.getChildren().add(robotImageView);
                }
                if((i != 7 && i != 8) || (j != 7 && j != 8)){
                }else{
                    stackPane = new StackPane();
                    Image goalBox = new Image(new File("src/main/resources/isep/robotricochet/planches/GoalBox.png").toURI().toString() , 44, 44, false, false);
                    ImageView goalBoxView = new ImageView(goalBox);
                    stackPane.getChildren().add(goalBoxView);
                }
                this.board[i][j] = stackPane;
                this.boardPane.add(stackPane, i, j);
            }
        }

    }
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
    public void move(Direction direction) {
        GenCellule currentGenCellule = selectedInfoPion.getCurrentCell();
        if (selectedInfoPion != null) {
            while (game.isValidMove(currentGenCellule, direction)) {
                Coordonne oldCoordonne = currentGenCellule.getPosition();
                Coordonne newCoordonne = currentGenCellule.getPosition().nextPosition(direction);

                game.move(currentGenCellule, direction);
                updateRobotDisplay(oldCoordonne, newCoordonne, selectedInfoPion);
                currentGenCellule = game.getBoard().getCell(newCoordonne);
                selectedInfoPion.setCurrentCell(currentGenCellule);
            }
            selectedInfoPion = currentGenCellule.getCurrentRobot();
        }
    }
    private void getPositionRobots() {
        this.currentRow.clear();
        this.currentColum.clear();
        for (int i = 1; i < 17; i++) {
            for (int j = 1; j < 17; j++) {
                if(game.getBoard().getCells()[i][j].getIsThereARobot()){
                    InfoPion infoPion = game.getBoard().getCells()[i][j].getCurrentRobot();
                    int column = game.getBoard().getCells()[i][j].getPosition().getNumColonne();
                    int row = game.getBoard().getCells()[i][j].getPosition().getNumColonne();
                    this.currentColum.put(infoPion, column);
                    this.currentRow.put(infoPion, row);
                }
            }
        }
    }
    private void removeRobotFromCell(Coordonne coordonne) {
        int numberOfChildren = board[coordonne.getNumLigne() - 1][coordonne.getNumColonne() - 1].getChildren().size() - 1;
        board[coordonne.getNumLigne() - 1][coordonne.getNumColonne() - 1].getChildren().remove(numberOfChildren);
    }

    private void addRobotToCell(Coordonne coordonne, InfoPion infoPion) {
        String filename = getRobotImageFilename(infoPion.getColor());
        Image robotImage = new Image(new File("src/main/resources/isep/robotricochet/robots/" + filename).toURI().toString() , 44, 44, false, false);
        ImageView robotImageView = new ImageView(robotImage);
        board[coordonne.getNumLigne() - 1][coordonne.getNumColonne() - 1].getChildren().add(robotImageView);
    }
    private void updateRobotDisplay(Coordonne oldCoordonne, Coordonne newCoordonne, InfoPion infoPion) {
        removeRobotFromCell(oldCoordonne);
        addRobotToCell(newCoordonne, infoPion);
    }
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
        Image symbolImage = new Image(new File(filePathRoot + "objectifs/" + symbolImageFilename).toURI().toString() , 40, 40, false, false);
        this.currentImageGoal.setImage(symbolImage);
        this.currentImageGoal.setVisible(true);
    }
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
    private void movePlayer(){
        if (isTheTimerStopped) {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 16; j++) {
                    this.board[i][j].addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            String id = ((StackPane) event.getSource()).getId();
                            int[] coordinates = Stream.of(id.split(",")).mapToInt(Integer::parseInt).toArray();
                            GenCellule currentGenCellule = game.getBoard().getCells()[coordinates[0]][coordinates[1]];
                            if (currentGenCellule.getIsThereARobot()) {
                                selectedInfoPion = currentGenCellule.getCurrentRobot();
                                selectedInfoPion.setCurrentCell(currentGenCellule);
                            }
                            event.consume();
                        }
                    });
                }
            }
        }
    }
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
            itIsWin = game.itIsWin(selectedInfoPion);
        }else if(game.getPlayerTwo().getIsMyTurn() && game.getPlayerTwo().getHitsNumber() < game.getPlayerTwo().getHitsNumberChoice()) {
            game.getPlayerTwo().setHitsNumber(game.getPlayerTwo().getHitsNumber() + 1);
            scorePlayerTwo.setText(String.valueOf(game.getPlayerTwo().getHitsNumber()));
            itIsWin = game.itIsWin(selectedInfoPion);
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
    public void reinitializeRobot(){
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                if(game.getBoard().getCells()[i + 1][j + 1].getIsThereARobot()){
                    Coordonne oldCoordonne = game.getBoard().getCells()[i + 1][j + 1].getPosition();
                    InfoPion infoPion = game.getBoard().getCells()[i + 1][j + 1].getCurrentRobot();
                    infoPion.setCurrentCell(game.getBoard().getCells()[i + 1][j + 1]);
                    Coordonne newCoordonne = infoPion.getOldPosition();
                    game.getBoard().getCell(oldCoordonne).removeRobot();
                    game.getBoard().getCell(newCoordonne).addRobot(infoPion);
                    updateRobotDisplay(oldCoordonne, newCoordonne, infoPion);
                }
            }
        }
    }
    public boolean launchResetRobot(){
        if(game.getPlayerOne().getHitsNumber() == game.getPlayerOne().getHitsNumberChoice()){
            itIsWin = game.itIsWin(selectedInfoPion);
            reinitializeRobot();
            game.setNextTurn();
            game.getPlayerOne().setHitsNumber(0);
            return true;
        }else if (game.getPlayerTwo().getHitsNumber() == game.getPlayerTwo().getHitsNumberChoice()){
            itIsWin = game.itIsWin(selectedInfoPion);
            reinitializeRobot();
            game.setNextTurn();
            game.getPlayerTwo().setHitsNumber(0);
            return true;
        }else {
            return false;
        }
    }
}