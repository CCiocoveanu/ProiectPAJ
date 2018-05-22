package RazboiFX;

import RazboiGame.Card;
import RazboiGame.EmptyDeckException;
import RazboiGame.Player;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main extends Application{

    private Scene gameScene;
    private List<Card> cardPack = Card.getShuffledCardStack();
    private List<Card> firstHalf = new ArrayList<>(cardPack.subList(0,26));
    private List<Card> secondHalf = new ArrayList<>(cardPack.subList(26,52));

    private Player player1 = new Player(firstHalf, "player1");
    private Player player2 = new Player(secondHalf, "player2");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        //Enter player name scene
        //---------------------------------------------------------------
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);

        Label nameLabel1 = new Label("Player 1:");
        GridPane.setConstraints(nameLabel1, 0, 0);

        TextField nameInput1 = new TextField();
        nameInput1.setPromptText("john doe");
        GridPane.setConstraints(nameInput1, 1, 0);

        Label nameLabel2 = new Label("Player 2:");
        GridPane.setConstraints(nameLabel2, 0, 1);

        TextField nameInput2 = new TextField();
        nameInput2.setPromptText("johannah dohe");
        GridPane.setConstraints(nameInput2, 1, 1);

        Button loginButton = new Button("Start");
        GridPane.setConstraints(loginButton, 1, 2);

        grid.getChildren().addAll(nameLabel1, nameInput1, nameLabel2, nameInput2, loginButton);
        Label label2 = new Label();
        label2.setFont(new Font(20));
        loginButton.setOnAction(e -> {
            setPlayerNames(nameInput1.getText(), nameInput2.getText());
            label2.setText((nameInput1.getText().toUpperCase()+" VS "+nameInput2.getText()).toUpperCase());
            updateDB("INSERT INTO Players(nume) VALUES('"+player1.getName()+"'),('"+player2.getName()+"')");
            connectDB("SELECT * FROM Players");
            stage.setScene(gameScene);
        });


        Scene loginScene = new Scene(grid, 300,200);

        //Game scene
        //-------------------------------------------------------------
        Label label = new Label("Time to play the game!");
        label.setFont(new Font(20));

        HBox topBorder = new HBox(label, label2);
        topBorder.setPadding(new Insets(20));
        topBorder.setSpacing(10);
        topBorder.setStyle("-fx-background-color: #32D746;");

        Label info = new Label();
        info.setFont(new Font(18));
        Label card1 = new Label();
        card1.setFont(new Font(18));
        Label card2 = new Label();
        card2.setFont(new Font(18));
        VBox centerBorder = new VBox(card1, card2, info);
        centerBorder.setPadding(new Insets(20));
        centerBorder.setSpacing(10);

        Button buttonPlay = new Button("Play game");
        buttonPlay.setPrefSize(100, 20);
        buttonPlay.setOnAction(e -> {
            String[] ar = playTheGame(player1, player2);
            if(ar.length == 3) {
                card1.setText(ar[0]);
                card2.setText(ar[1]);
                info.setText(ar[2]);
            } else {
                card1.setText(ar[0]);
                card2.setText("");
                info.setText("");
            }
        });

        Label calc1 = new Label();
        calc1.setFont(new Font(18));
        Label calc2 = new Label();
        calc2.setFont(new Font(18));
        Button buttonCalc = new Button("Calculate");
        buttonCalc.setPrefSize(100, 20);
        buttonCalc.setOnAction(e -> {

            //calc1.setText(player1.getName() + " total points: "+player1.calculatePoints());
            //calc2.setText(player2.getName() + " total points: "+player2.calculatePoints());
            updateDB("insert into score (id_player, points) values ((select id_player from Players where nume = '"+player1.getName()+"'), "+player1.calculatePoints()+"),((select id_player from Players where nume = '"+player2.getName()+"'), "+player2.calculatePoints()+")");

            calc1.setText(player1.getName() + " total points: "+getMaxFromDB("select max(points) from score where id_player = (select id_player from players where nume = '"+player1.getName()+"')"));
            calc2.setText(player2.getName() + " total points: "+getMaxFromDB("select max(points) from score where id_player = (select id_player from players where nume = '"+player2.getName()+"')"));
        });
        GridPane bottomBorder = new GridPane();
        bottomBorder.setPadding(new Insets(10));
        bottomBorder.setVgap(15);
        bottomBorder.setHgap(15);

        GridPane.setConstraints(calc1, 2, 0);
        GridPane.setConstraints(calc2, 2, 1);
        bottomBorder.getChildren().addAll(calc1, calc2);


        Button buttonShow = new Button("Show Piles");
        buttonShow.setPrefSize(100, 20);

        stage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram(stage);
        });
        Button buttonExit = new Button("Exit");
        buttonExit.setPrefSize(100, 20);
        buttonExit.setOnAction(e -> closeProgram(stage));

        VBox leftBorder = new VBox(buttonPlay, buttonCalc, buttonShow, buttonExit);
        leftBorder.setPadding(new Insets(30, 10, 10, 30));
        leftBorder.setSpacing(10);

        ListView<String> listViewP1 = new ListView<>();
        ListView<String> listViewP2 = new ListView<>();
        Label player1Name = new Label();
        Label player2Name = new Label();
        VBox rightBorder1 = new VBox(player1Name, listViewP1);
        rightBorder1.setPadding(new Insets(20));
        rightBorder1.setSpacing(10);
        VBox rightBorder2 = new VBox(player2Name, listViewP2);
        rightBorder2.setPadding(new Insets(20));
        rightBorder2.setSpacing(10);
        HBox rightBorder = new HBox(rightBorder1, rightBorder2);
        rightBorder.setPadding(new Insets(10));
        rightBorder.setSpacing(10);

        buttonShow.setOnAction(e -> {
            player1Name.setText(label2.getText().split("\\s+")[0]+": ");
            player2Name.setText(label2.getText().split("\\s+")[2]+": ");
            listViewP1.getItems().clear();
            for(int i = 0; i < player1.getCardsInPile().size(); i++){
                    listViewP1.getItems().add(player1.getCardsInPile().get(i).toString());
            }
            listViewP2.getItems().clear();
            for(int i = 0; i < player2.getCardsInPile().size(); i++){
                    listViewP2.getItems().add(player2.getCardsInPile().get(i).toString());
            }
        });

        BorderPane border = new BorderPane();
        border.setLeft(leftBorder);
        border.setTop(topBorder);
        border.setCenter(centerBorder);
        border.setRight(rightBorder);
        border.setBottom(bottomBorder);

        gameScene = new Scene(border, 1100, 800);
        stage.setTitle("Razboi Game");
        stage.setScene(loginScene);
        stage.show();
    }

    private String[] playTheGame(Player player1, Player player2){
        try {
            return player1.goToWar(player2);

        } catch (EmptyDeckException e) {
            return new String[]{e.getMessage()};
        }
    }

    private void setPlayerNames(String name1, String name2){
        player1.setName(name1);
        player2.setName(name2);
    }

    private void closeProgram(Stage stage){
        Boolean answer = CloseBox.display();
        if(answer) stage.close();
    }

    private void connectDB(String query){

        Statement stmt = null;
        String url = "jdbc:mysql://localhost:3306/pajdb?autoReconnect=true&useSSL=false";
        Properties props = new Properties();
        props.setProperty("user","root");
        props.setProperty("password","root");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, props);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                //Retrieve by column name
                int id  = rs.getInt("id_player");
                String Name = rs.getString("nume");

                //Display values
                System.out.print("ID: " + id);
                System.out.print(", Name: " + Name);
                System.out.println();
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateDB(String query){

        Statement stmt = null;
        String url = "jdbc:mysql://localhost:3306/pajdb?autoReconnect=true&useSSL=false";
        Properties props = new Properties();
        props.setProperty("user","root");
        props.setProperty("password","root");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, props);
            stmt = conn.createStatement();
            stmt.executeUpdate(query);

            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getMaxFromDB(String query){

        int score = 0;
        Statement stmt = null;
        String url = "jdbc:mysql://localhost:3306/pajdb?autoReconnect=true&useSSL=false";
        Properties props = new Properties();
        props.setProperty("user","root");
        props.setProperty("password","root");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, props);
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) score = rs.getInt("max(points)");

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("frajer");
            e.printStackTrace();
        }
        return score;
    }
}

