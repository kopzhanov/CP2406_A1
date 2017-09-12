import java.util.ArrayList;
import java.util.Scanner;

public class Player {
    public static final String COLOR_RESET = "\u001B[0m";
    public static final String COLOR_RED = "\u001B[31m";
    public static final String COLOR_BLUE = "\u001B[34m";

    private static int counter = 1;
    private int id = 1;
    private ArrayList<Card> hand = new ArrayList<Card>();
    private boolean pass;

    public Player() {
        this.id = counter++;
    }

    public int getID() {
        return id;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
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
                System.out.format(COLOR_BLUE + "%-10s%-20s%-15s\n" + COLOR_RESET, count, card.getName(), card.getInstruction());
            }
            count++;
        }
        System.out.format(COLOR_RED + "%-10sPass Turn\n" + COLOR_RESET, count);
    }

    public void playCard() {
        boolean validCard = false;
        int card = 0;
        System.out.println("It's player " + id + " turn");
        while ((card < 1 || card > hand.size() + 1) || !validCard) {
            try {
                MineralSupertrumps.input = new Scanner(System.in);
                showHand();
                System.out.println("Choose a number according to the table above");
                card = MineralSupertrumps.input.nextInt();
                if (card <= hand.size() && card > 0 && !MineralSupertrumps.firstTurn) {
                    MineralSupertrumps.validCard(hand.get(card - 1));
                } else {
                    validCard = true;
                }
                validCard = true;
            } catch (InvalidCardException e) {
                System.out.println(e.getMessage());
            }
        }

        //If pass turn chosen
        if (card == hand.size() + 1) {
            passTurn();
            //If Super Trump Card chosen
        } else if (hand.get(card - 1).getInstruction() != null) {
            card--;
            SuperTrumpCard.playCard(hand.get(card), hasMagnetite(), this);
            hand.remove(card);
            MineralSupertrumps.trumpCardPlayed();
            //If Mineral Card chosen
        } else {
            card--;
            System.out.println("Chosen Card: " + hand.get(card).getName());
            if (MineralSupertrumps.firstTurn) {
                int category;
                System.out.println("1.Hardness: " + hand.get(card).getHardness());
                System.out.println("2.Specific Gravity: " + hand.get(card).getGravity());
                System.out.println("3.Cleavage: " + hand.get(card).getCleavage());
                System.out.println("4.Crustal Abundance: " + hand.get(card).getAbundance());
                System.out.println("5.Economic Value: " + hand.get(card).getEcoValue());

                System.out.println("Choose a category according to its number");
                category = MineralSupertrumps.input.nextInt();
                while (category < 1 || category > 5) {
                    System.out.println("Wrong number, input number according to a category");
                    category = MineralSupertrumps.input.nextInt();
                }
                MineralSupertrumps.category = category;
                MineralSupertrumps.firstTurn = false;
            }
            MineralSupertrumps.lastPlayedCard = hand.get(card);
            hand.remove(card);
            if (hand.size() == 0) {
                MineralSupertrumps.players.remove(this);
            }
        }
    }

    public void passTurn() {
        pass = true;
        this.addCard(MineralSupertrumps.deck.get(0));
        MineralSupertrumps.deck.remove(0);
    }

    private boolean hasMagnetite() {
        boolean isMagnetite = false;
        for (Card card : hand) {
            if (card.getName().equals("Magnetite"))
                isMagnetite = true;
        }
        return isMagnetite;
    }
}
