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

    static boolean superTrumpCardInFirstRound = false;
    static boolean firstTurn = true;
    static ArrayList<Card> deck = new ArrayList<Card>();
    static Card lastPlayedCard;
    static ArrayList<Player> players = new ArrayList<Player>();
    static int dealerIndex;
    static int turnPlayerIndex;
    static int category = 0;
    static Scanner input;

    public static void main(String[] args) {
        input = new Scanner(System.in);
        int playerAmount;

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
        runGame();
    }

    private static void dealCards() {
        for (int i = 0; i < INITIALHAND; i++) {
            for (int y = 0; y < players.size(); y++) {
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
            br.close();
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

    private static void runGame() {
        nextPlayer(dealerIndex);
        while (players.size() != 1) {
            newRound();
        }
        System.out.println("Player " + players.get(0).getID() + " lost");
    }

    private static void nextPlayer(int index) {
        // return given int as index for player array
        if (players.size() - 1 <= index) {
            turnPlayerIndex = 0;
        } else {
            turnPlayerIndex = index + 1;
        }
        if (players.get(turnPlayerIndex).isPass()) {
            nextPlayer(turnPlayerIndex);
        }
    }

    private static void newRound() {
        System.out.println("*** NEW ROUND ***");
        for (Player player : players) {
            player.setPass(false);
        }
        while (!roundEnd()) {
            if (!firstTurn) {
                showLastPlayedCard();
            }
            players.get(turnPlayerIndex).playCard();
            nextPlayer(turnPlayerIndex);
            System.out.println();
        }
        firstTurn = true;
    }

    private static void showLastPlayedCard() {
        System.out.println("Previously played card: " + lastPlayedCard.getName());
        switch (category) {
            case 1: {
                System.out.println(lastPlayedCard.getName() + "'s Hardness:" + lastPlayedCard.getHardness());
                break;
            }
            case 2: {
                System.out.println(lastPlayedCard.getName() + "'s Specific Gravity:" + lastPlayedCard.getGravity());
                break;
            }
            case 3: {
                System.out.println(lastPlayedCard.getName() + "'s Cleavage:" + lastPlayedCard.getCleavage());
                break;
            }
            case 4: {
                System.out.println(lastPlayedCard.getName() + "'s Crustal Abundance:" + lastPlayedCard.getAbundance());
                break;
            }
            case 5: {
                System.out.println(lastPlayedCard.getName() + "'s Economic Value:" + lastPlayedCard.getEcoValue());
                break;
            }
        }
    }

    private static boolean roundEnd() {
        int playerPassed = 0;
        for (Player player : players) {
            if (player.isPass()) {
                playerPassed++;
            }
        }
        return playerPassed == players.size() - 1;
    }

    static void validCard(Card card) throws InvalidCardException {
        if (!superTrumpCardInFirstRound) {
            // Checking if the card is not SuperTrumpCard
            if (card.getInstruction() == null) {
                switch (category) {
                    case 1: {
                        if (card.getHardness() <= lastPlayedCard.getHardness()) {
                            throw new InvalidCardException("Hardness of " + card.getName() + " is not higher than of " + lastPlayedCard.getName());
                        }
                        break;
                    }
                    case 2: {
                        if (card.getGravity() <= lastPlayedCard.getGravity()) {
                            throw new InvalidCardException("Gravity of " + card.getName() + " is not higher than of " + lastPlayedCard.getName());
                        }
                        break;
                    }
                    case 3: {
                        int firstIndex = Arrays.binarySearch(RANKING_CLEAVAGE, card.getCleavage());
                        int secondIndex = Arrays.binarySearch(RANKING_CLEAVAGE, lastPlayedCard.getCleavage());
                        if (firstIndex <= secondIndex) {
                            throw new InvalidCardException("Cleavage of " + card.getName() + " is not higher than of " + lastPlayedCard.getName());
                        }
                        break;
                    }
                    case 4: {
                        int firstIndex = Arrays.binarySearch(RANKING_CRUSTAL_ABUNDANCE, card.getAbundance());
                        int secondIndex = Arrays.binarySearch(RANKING_CLEAVAGE, lastPlayedCard.getAbundance());
                        if (firstIndex <= secondIndex) {
                            throw new InvalidCardException("Crustal Abundance of " + card.getName() + " is not higher than of " + lastPlayedCard.getName());
                        }
                        break;
                    }
                    case 5: {
                        int firstIndex = Arrays.binarySearch(RANKING_ECONOMIC_VALUE, card.getEcoValue());
                        int secondIndex = Arrays.binarySearch(RANKING_ECONOMIC_VALUE, lastPlayedCard.getEcoValue());
                        if (firstIndex <= secondIndex) {
                            throw new InvalidCardException("Economic Value of " + card.getName() + " is not higher than of " + lastPlayedCard.getName());
                        }
                        break;
                    }
                }
            }
        }
    }

    static void trumpCardPlayed() {
        for (Player player : players) {
            if (player.isPass()) {
                player.setPass(false);
            }
        }
    }
}
