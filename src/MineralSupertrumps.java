import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MineralSupertrumps {
    final static int INITIALHAND = 8;
    final static String[] RANKING_CLEAVAGE = {"none", "poor/none", "1 poor", "2 poor", "1 good", "1 good,1 poor", "2 good",
            "3 good", "1 perfect", "1 perfect,1 good", "1 perfect,2 good", "2 perfect,1 good", "3 perfect", "4 perfect", "6 perfect"};
    final static String[] RANKING_CRUSTAL_ABUNDANCE = {"ultratrace", "trace", "low", "moderate", "high", "very high"};
    final static String[] RANKING_ECONOMIC_VALUE = {"trivial", "low", "moderate", "high", "very high", "i'm rich!"};

    static boolean firstTurn = true;
    static ArrayList<Card> deck = new ArrayList<Card>();
    static ArrayList<Card> pile = new ArrayList<Card>();
    static ArrayList<Player> players = new ArrayList<Player>();
    static int playerAmount;
    static int dealerIndex;
    static int turnPlayerIndex;
    static int category = 0;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        parseCards();
        shuffleDeck();

        System.out.println("How many players? (Minimum - 3, Maximum - 5)");
        playerAmount = input.nextInt();
        while (playerAmount < 3 || playerAmount > 5) {
            System.out.println("Invalid number, 3 to 5 players are allowed only");
            playerAmount = input.nextInt();
        }

        setPlayers(playerAmount);
        dealCards();
        startGame();
    }

    private static void dealCards() {
        for (int i = 0; i < INITIALHAND; i++) {
            for (int y = 0; y < playerAmount; y++) {
                players.get(y).addCard(deck.get(0));
                deck.remove(0);
            }
        }
    }

    private static void setPlayers(int playerAmount) {
        for (int i = 0; i < playerAmount; i++) {
            Player player = new Player();
            players.add(player);
        }
        dealerIndex = ThreadLocalRandom.current().nextInt(0, playerAmount - 1);
    }

    private static void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private static void parseCards() {
        String file = "card.txt";
        BufferedReader br = null;
        String line;
        String delimiter = ",";

        try {
            br = new BufferedReader(new FileReader(file));
            br.readLine(); // To read over first line of column titles
            while ((line = br.readLine()) != null) {
                String[] card = line.split(delimiter);
                deck.add(new Card(card[0], Double.parseDouble(card[1]), Double.parseDouble(card[2]), card[3], card[4], card[5]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("File \"" + file + "\" has not found");
        } catch (IOException e) {
            System.out.println("General I/O exception, restart the program");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                System.out.println("General I/O exception, restart the program");
            }
        }
        //Since the are no supertrump cards in the given file, they are added manually below
        deck.add(new SuperTrumpCard("The Miner", "Change trumps category to \"Economic value\""));
        deck.add(new SuperTrumpCard("The Petrologist", "Change trumps category to \"Crustal abundance\""));
        deck.add(new SuperTrumpCard("The Gemmologist", "Change trumps category to \"Hardness\""));
        deck.add(new SuperTrumpCard("The Mineralogist", "Change trumps category to \"Cleavage\""));
        deck.add(new SuperTrumpCard("The Geophysicist", "Change trumps category to \"Specific gravity\", or throw \"Magnetite\""));
        deck.add(new SuperTrumpCard("The Geologist", "Change trumps category of your choice"));
    }

    private static void startGame() {
        nextPlayer(dealerIndex);
        newRound();
    }

    private static void nextPlayer(int index) {
        // return given int as index for player array
        if (playerAmount - 1 == index) {
            turnPlayerIndex = 0;
        } else {
            turnPlayerIndex = index + 1;
        }
        if (players.get(turnPlayerIndex).isPass()) {
            nextPlayer(turnPlayerIndex);
        }
    }

    static void newRound() {
        System.out.println("NEW ROUND");
        int playerPassed = 0;
        for (Player player :
                players) {
            player.setPass(false);
        }
        while (playerPassed != playerAmount - 1) {
            players.get(turnPlayerIndex).playCard();
            if (players.get(turnPlayerIndex).isPass()) {
                playerPassed++;
            }
            nextPlayer(turnPlayerIndex);
        }
        firstTurn = true;
        newRound();
    }

    static void validCard(Card card) throws InvalidCardException {
        switch (category) {
            case 1: {
                if (card.getHardness() < pile.get(pile.size() - 1).getHardness()) {
                    throw new InvalidCardException("Hardness of " + card.getName() + " is lower than of " + pile.get(pile.size() - 1).getName());
                }
                break;
            }
            case 2: {
                if (card.getGravity() < pile.get(pile.size() - 1).getGravity()) {
                    throw new InvalidCardException("Gravity of " + card.getName() + " is lower than of " + pile.get(pile.size() - 1).getName());
                }
                break;
            }
            case 3: {
                int firstIndex = Arrays.asList(RANKING_CLEAVAGE).indexOf(card.getCleavage());
                int secondIndex = Arrays.asList(RANKING_CLEAVAGE).indexOf(pile.get(pile.size() - 1).getCleavage());
                if (firstIndex < secondIndex) {
                    throw new InvalidCardException("Cleavage of " + card.getName() + " is lower than of " + pile.get(pile.size() - 1).getName());
                }
                break;
            }
            case 4: {
                int firstIndex = Arrays.asList(RANKING_CRUSTAL_ABUNDANCE).indexOf(card.getAbundance());
                int secondIndex = Arrays.asList(RANKING_CLEAVAGE).indexOf(pile.get(pile.size() - 1).getAbundance());
                if (firstIndex < secondIndex) {
                    throw new InvalidCardException("Crustal Abundance of " + card.getName() + " is lower than of " + pile.get(pile.size() - 1).getName());
                }
                break;
            }
            case 5: {
                int firstIndex = Arrays.asList(RANKING_ECONOMIC_VALUE).indexOf(card.getEcoValue());
                int secondIndex = Arrays.asList(RANKING_ECONOMIC_VALUE).indexOf(pile.get(pile.size() - 1).getEcoValue());
                if (firstIndex < secondIndex) {
                    throw new InvalidCardException("Economic Value of " + card.getName() + " is lower than of " + pile.get(pile.size() - 1).getName());
                }
                break;
            }
        }
    }
}
