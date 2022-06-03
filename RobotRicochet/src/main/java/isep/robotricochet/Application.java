package isep.robotricochet;

import isep.robotricochet.backend.Game;
import isep.robotricochet.backend.Orientation;
import isep.robotricochet.backend.Robot;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Application
 */

public class Application extends javafx.application.Application {

    GameController gameController;

    /**
     * Méthode de chargement du fichier FXML principal dans la fenêtre du programme
     * @param stage Stage sur lequel dévoiler la scène
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("plateau.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Ricochet Robot");
        stage.setScene(scene);
        stage.show();

        // Get controller
        gameController = fxmlLoader.getController();
        // Init keystroke event handler
        initKeyEventHandler(scene);
    }

    /**
     * Méthode gérant les évènements lors des pressions des touches sur le clavier (W, S, D et A)
     * Pour déplacer un robot, cette méthode vérifie si le robot a été sélectionné à la souris dans un premier temps
     * @param scene Scène sur laquelle on pourra agir avec l'évènement du clavier
     */
    private void initKeyEventHandler(Scene scene) {
        if(Game.Status != Game.Status.END_ROUND){
            scene.setOnKeyPressed(e -> {
                if (gameController.itIsFinished()) {
                    return;
                } else if (gameController.launchResetRobot()) {
                    return;
                }
                Robot selectedRobot = gameController.selectedRobot;
                if (selectedRobot != null) {
                    switch (e.getCode()) {
                        case W -> {
                            gameController.move(Orientation.NORTH);
                            gameController.setHits();
                        }
                        case S -> {
                            gameController.move(Orientation.SOUTH);
                            gameController.setHits();
                        }
                        case D -> {
                            gameController.move(Orientation.EAST);
                            gameController.setHits();
                        }
                        case A -> {
                            gameController.move(Orientation.WEST);
                            gameController.setHits();
                        }
                    }
                }
                e.consume();
            });
        }
    }

    /**
     * Méthode Main pour lancer le programme
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}
