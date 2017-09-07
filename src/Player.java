import java.util.ArrayList;

public class Player {
    private static int counter = 1;
    private int id = 1;
    private boolean dealer = false;
    private ArrayList<Card> hand = new ArrayList<Card>();

    public Player() {
        this.id = counter++;
    }

    public int getID() {
        return id;
    }

    public boolean isDealer() {
        return dealer;
    }

    public void setDealer() {
        this.dealer = true;
    }

    public void addCard(Card card) {
        this.hand.add(card);
    }
}
