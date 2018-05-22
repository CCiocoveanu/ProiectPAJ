package RazboiGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card {
    private String tip;
    private String numar;
    private int intNr;

    private Card(int nr, String tip){
        switch(nr){
            case 11: this.numar = "J";
                    break;
            case 12: this.numar = "Q";
                    break;
            case 13: this.numar = "K";
                    break;
            case 14: this.numar = "A";
                    break;
            default: this.numar = Integer.toString(nr);
                    break;
        }
        this.intNr = nr;
        this.tip = tip;
    }

    public static List<Card> getShuffledCardStack(){
        List<Card> list = new ArrayList<>();

        for(int i = 0; i<13; i++){
            list.add(new Card(i+2, "Hearts"));
            list.add(new Card(i+2, "Diamonds"));
            list.add(new Card(i+2, "Clubs"));
            list.add(new Card(i+2, "Spades"));
        }
        Collections.shuffle(list);
        return list;
    }

    int getIntNr() {
        return intNr;
    }

    @Override
    public String toString(){
        return this.numar+" / "+this.tip+"\n";
    }
}
