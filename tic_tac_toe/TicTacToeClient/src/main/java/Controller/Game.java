package Controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Model.MenuItem;
import View.MenuBox;
import View.SubMenu;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Game extends Application {

    public boolean playable = true;
    public boolean turnX = true;
    public static Tile[][] board = new Tile[3][3];
    public List<Combo> combos = new ArrayList<>();
    public Client client;

    private Pane root = new Pane();

    public Parent createContent() {
        root.setPrefSize(600, 600);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Tile tile = new Tile();
                tile.setTranslateX(j * 200);
                tile.setTranslateY(i * 200);

                root.getChildren().add(tile);

                board[j][i] = tile;
            }
        }

        // horizontal
        for (int y = 0; y < 3; y++) {
            combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
        }

        // vertical
        for (int x = 0; x < 3; x++) {
            combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
        }

        // diagonals
        combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
        combos.add(new Combo(board[2][0], board[1][1], board[0][2]));

        return root;
    }

    public Scene createMenu() {
        //menu
        Pane root = new Pane();
        Image image = new Image("file:TicTacToeClient/src/main/resources/1.jpg");
        ImageView img = new ImageView(image);
        img.setFitHeight(600);
        img.setFitWidth(600);
        root.getChildren().add(img);

        MenuItem newGame = new MenuItem("НОВАЯ ИГРА");
        MenuItem multipalyer = new MenuItem("СЕТЕВАЯ ИГРА");
        MenuItem exitGame = new MenuItem("ВЫХОД");
        SubMenu mainMenu = new SubMenu(
                newGame, multipalyer, exitGame
        );

        MenuBox menuBox = new MenuBox(mainMenu);

        exitGame.setOnMouseClicked(event -> System.exit(0));
        newGame.setOnMouseClicked(event -> {
            root.getChildren().remove(menuBox);
            root.getChildren().remove(img);
            Parent content = createContent();
            root.getChildren().addAll(content);
        });
        multipalyer.setOnMouseClicked((event -> {
            root.getChildren().remove(menuBox);
            root.getChildren().remove(img);
            Parent content = createContent();
            System.out.println("ya tut1");
            this.client = new Client();
            System.out.println("ya tut2");
            root.getChildren().addAll(content);
        }));

        root.getChildren().addAll(menuBox);

        Scene scene = new Scene(root, 600, 600);

        FadeTransition ft = new FadeTransition(Duration.seconds(1), menuBox);
        if (!menuBox.isVisible()) {
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.play();
            menuBox.setVisible(true);
        } else {
            ft.setFromValue(1);
            ft.setToValue(0);
            ft.setOnFinished(evt -> menuBox.setVisible(false));
            ft.play();

        }

        return scene;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("TicTacToe");
        primaryStage.setScene(createMenu());
        primaryStage.show();
    }

    private void checkState() {
        for (Combo combo : combos) {
            if (combo.isComplete()) {
                playable = false;

                playWinAnimation(combo);
                break;
            }
        }
    }

    private void playWinAnimation(Combo combo) {
        Line line = new Line();
        line.setStartX(combo.tiles[0].getCenterX());
        line.setStartY(combo.tiles[0].getCenterY());
        line.setEndX(combo.tiles[0].getCenterX());
        line.setEndY(combo.tiles[0].getCenterY());

        root.getChildren().add(line);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()),
                new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
        timeline.play();
    }

    private class Combo {
        private Tile[] tiles;

        public Combo(Tile... tiles) {
            this.tiles = tiles;
        }

        public boolean isComplete() {
            if (tiles[0].getValue().isEmpty())
                return false;

            return tiles[0].getValue().equals(tiles[1].getValue())
                    && tiles[0].getValue().equals(tiles[2].getValue());
        }
    }

    public class Tile extends StackPane implements Serializable {
        private Text text = new Text();

        public Tile() {
            Rectangle border = new Rectangle(200, 200);
            border.setFill(null);
            border.setStroke(Color.BLACK);

            text.setFont(Font.font(72));

            setAlignment(Pos.CENTER);
            getChildren().addAll(border, text);

            setOnMouseClicked(event -> {
                if (!playable)
                    return;

                if (event.getButton() == MouseButton.PRIMARY) {
                    if (!turnX)
                        return;

                    drawX();
//                    try {
//                        client.sendMsg(board);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    turnX = false;
                    checkState();

                } else if (event.getButton() == MouseButton.SECONDARY) {
                    if (turnX)
                        return;

                    drawO();
//                    try {
//                        client.sendMsg(board);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    turnX = true;
                    checkState();
                }
            });
        }

        public double getCenterX() {
            return getTranslateX() + 100;
        }

        public double getCenterY() {
            return getTranslateY() + 100;
        }

        public String getValue() {
            return text.getText();
        }

        private void drawX() {
            text.setText("X");
        }

        private void drawO() {
            text.setText("O");
        }
    }
}
