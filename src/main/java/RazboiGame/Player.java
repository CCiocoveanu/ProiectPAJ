package RazboiGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    private List<Card> cardsInPile = new ArrayList<>();
    private List<Card> deck;
    private String name;
    private String lastCardDrawnMessage;

    public Player(List<Card> list, String name){
        this.deck = list;
        this.name = name;
    }

    public int calculatePoints(){
        int count = 0;
        for(Card c : cardsInPile){
            if(c.getIntNr() > 9){
                System.out.println(c);
                count++;
            }
        }
        System.out.println(this.name+ " total points: "+ count);
        return count;
    }

    public String getName() {
        return name;
    }

    int totalPoints(){
        int count = 0;
        for(Card c : cardsInPile){
            if(c.getIntNr() > 9){
                count++;
            }
        }
        return count;
    }

    private Card drawCard() throws EmptyDeckException{
        if(!deck.isEmpty()) {
            Card c = deck.get(0);
            deck.remove(0);
            return c;
        } else throw new EmptyDeckException("Your deck is empty! Calculate your points and end the game.");
    }

    void showPile(){
        System.out.println(this.name+ " cards in pile: ");
        System.out.println(cardsInPile);
    }

    private void addToPile(Card c){
        this.cardsInPile.add(c);
    }

    private void addToDeck(Card c){
        this.deck.add(c);
    }

    public String[] goToWar(Player player2) throws EmptyDeckException {
        Card card1 = this.drawCard();
        Card card2 = player2.drawCard();
        this.lastCardDrawnMessage = this.name + " has drawn: "+card1;
        player2.lastCardDrawnMessage = player2.name + " has drawn: "+card2;
        if(card1.getIntNr() > card2.getIntNr()){
            this.addToPile(card1);
            this.addToPile(card2);
            System.out.println(this.name+" wins!");
            return new String[]{this.lastCardDrawnMessage, player2.lastCardDrawnMessage, this.name+" wins!\n"+ card1.getIntNr()+" > "+card2.getIntNr()};
        } else if(card1.getIntNr() < card2.getIntNr()){
            player2.addToPile(card1);
            player2.addToPile(card2);
            System.out.println(player2.name+" wins!");
            return new String[]{this.lastCardDrawnMessage, player2.lastCardDrawnMessage, player2.name+" wins!\n"+ card2.getIntNr()+" > "+card1.getIntNr()};
        } else {
            this.addToDeck(card1);
            player2.addToDeck(card2);
            Collections.shuffle(this.deck);
            Collections.shuffle(player2.deck);
            System.out.println("It's a draw!");
            return new String[]{this.lastCardDrawnMessage, player2.lastCardDrawnMessage, "It's a draw!"};
        }
    }

    public List<Card> getCardsInPile() {
        return cardsInPile;
    }

    public void setName(String name) {
        this.name = name;
    }
}
