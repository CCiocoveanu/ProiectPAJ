package RazboiFX;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


class CloseBox{

    private static boolean answer;

    static boolean display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Exit box");
        window.setMinWidth(250);
        Label label = new Label("Are you sure you want to exit?");

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox layout = new VBox(10);
        HBox buttonLayout = new HBox(10);
        buttonLayout.getChildren().addAll(yesButton, noButton);
        buttonLayout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(label, buttonLayout);

        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout, 250, 200);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}
