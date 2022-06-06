package isep.robotricochet;

import isep.robotricochet.generation.Game;
import isep.robotricochet.generation.Direction;
import isep.robotricochet.generation.InfoPion;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
public class LancementJeu extends javafx.application.Application {
    ControllerJeu controllerJeu;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LancementJeu.class.getResource("plateau.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Ricochet Robot");
        stage.setScene(scene);
        stage.show();
        controllerJeu = fxmlLoader.getController();
        initKeyEventHandler(scene);
    }
    private void initKeyEventHandler(Scene scene) {
        if(Game.Status != Game.Status.END_ROUND){
            scene.setOnKeyPressed(e -> {
                if (controllerJeu.itIsFinished()) {
                    return;
                } else if (controllerJeu.launchResetRobot()) {
                    return;
                }
                InfoPion selectedInfoPion = controllerJeu.selectedInfoPion;
                if (selectedInfoPion != null) {
                    switch (e.getCode()) {
                        case W -> {
                            controllerJeu.move(Direction.NORTH);
                            controllerJeu.setHits();
                        }
                        case S -> {
                            controllerJeu.move(Direction.SOUTH);
                            controllerJeu.setHits();
                        }
                        case D -> {
                            controllerJeu.move(Direction.EAST);
                            controllerJeu.setHits();
                        }
                        case A -> {
                            controllerJeu.move(Direction.WEST);
                            controllerJeu.setHits();
                        }
                    }
                }
                e.consume();
            });
        }
    }
    public static void main(String[] args) {
        launch();
    }
}
