package RazboiGame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainTest {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        String command;
        Random r = new Random();
        List<Card> cardPack = Card.getShuffledCardStack();
        List<Card> firstHalf = new ArrayList<>(cardPack.subList(0,26));
        List<Card> secondHalf = new ArrayList<>(cardPack.subList(26,52));

        Player player1 = new Player(firstHalf, "Player 1");
        Player player2 = new Player(secondHalf, "Player 2");

        System.out.println("Commands: ");
        System.out.println("play (each player draws a card from the deck and compares them)");
        System.out.println("calc (calculate points for each player)");
        System.out.println("show (display card piles for both players)");
        System.out.println("finish (declares a winner based on current score and ends the game)");

        while(true){
            System.out.println("Insert command: ");
            command = scanner.nextLine();
            switch(command){
                case "play":
                    try {
                        player1.goToWar(player2);
                    } catch (EmptyDeckException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "calc":
                    player1.calculatePoints();
                    player2.calculatePoints();
                    break;
                case "show":
                    player1.showPile();
                    player2.showPile();
                    break;
                case "finish":
                    if(player1.totalPoints() > player2.totalPoints()){
                        System.out.println(player1.getName()+" wins with a total of "+ player1.totalPoints()+" points");
                    } else if(player1.totalPoints() < player2.totalPoints()){
                        System.out.println(player2.getName()+" wins with a total of "+ player2.totalPoints()+" points");
                    } else {
                        System.out.println("Game ended in a draw. Both players have"+ player1.totalPoints()+" points");
                    }
                    System.out.println("Hope you had fun!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid command!");
                    break;
            }
        }



    }
}
