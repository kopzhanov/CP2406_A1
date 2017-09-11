import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";

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

    public void showHand() {
        int count = 1;
        System.out.format("%-10s%-20s%-10s%-20s%-20s%-20s%-15s\n", "Number", "Name", "Hardness", "Specific Gravity", "Cleavage", "Crustal Abundance", "Economic Value");
        for (Card card : this.hand) {
            if (card.getInstruction() == null) {
                System.out.format("%-10s%-20s%-10s%-20s%-20s%-20s%-15s\n", count, card.getName(), card.getHardness(), card.getGravity(), card.getCleavage(), card.getAbundance(), card.getEcoValue());
            } else {
                System.out.format(ANSI_BLUE + "%-10s%-20s%-15s\n" + ANSI_RESET, count, card.getName(), card.getInstruction());
            }
            count++;
        }
    }

    public void playCard() {
        int card;
        Scanner input = new Scanner(System.in);
        showHand();
        System.out.println("Card number:");
        card = input.nextInt();
        while (card < 1 || card > this.hand.size()) {
            System.out.println("Wrong number, input number according to a card number");
            card = input.nextInt();
        }
        card--;
        System.out.println("Chosen Card: " + this.hand.get(card).getName());
        if (MineralSupertrumps.firstTurn) {
            int category;
            System.out.println("1.Hardness: " + this.hand.get(card).getHardness());
            System.out.println("2.Specific Gravity " + this.hand.get(card).getGravity());
            System.out.println("3.Cleavage " + this.hand.get(card).getCleavage());
            System.out.println("4.Crustal Abundance " + this.hand.get(card).getAbundance());
            System.out.println("5.Economic Value " + this.hand.get(card).getEcoValue());

            System.out.println("Choose a category according to its number");
            category = input.nextInt();
            while (category < 1 || category > 5) {
                System.out.println("Wrong number, input number according to a category");
                category = input.nextInt();
            }
            MineralSupertrumps.category = category;
            MineralSupertrumps.firstTurn = false;
        }
        MineralSupertrumps.pile.add(this.hand.get(card));
        this.hand.remove(card);
    }
}
