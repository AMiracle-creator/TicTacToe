package View;

import Model.MenuItem;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class SubMenu extends VBox {
    public SubMenu(MenuItem... items) {
        setSpacing(15);
        setTranslateY(260);
        setTranslateX(100);
        setAlignment(Pos.CENTER);
        for (MenuItem item : items) {
            getChildren().addAll(item);
        }
    }
}