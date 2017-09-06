import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MineralSupertrumps {
    static Card[] deck = new Card[60];
    static int players;

    public static void main(String[] args) {
        parseCards();
        Scanner input = new Scanner(System.in);
        System.out.println("How many players? (Minimum - 3, Maximum - 5)");
        players = input.nextInt();
        while (players < 3 || players > 5) {
            System.out.println("Invalid number, 3 to 5 players are allowed only");
            players = input.nextInt();
        }
        shuffleDeck();
    }

    private static void shuffleDeck() {
        Collections.shuffle(Arrays.asList(deck));
    }

    private static void parseCards() {
        String file = "card.txt";
        BufferedReader br = null;
        String line;
        String delimiter = ",";
        int index = 0;

        try {
            br = new BufferedReader(new FileReader(file));
            br.readLine(); // To read over first line of column titles
            while ((line = br.readLine()) != null) {
                String[] card = line.split(delimiter);
                deck[index] = new MineralCard(card[0], Double.parseDouble(card[1]), Double.parseDouble(card[2]), card[3], card[4], card[5]);
                index++;
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
                e.printStackTrace();
            }
        }
        //Since the are no supertrump cards in the given file, they are added manually below
        deck[index] = new SuperTrumpCard("The Miner", "Change trumps category to \"Economic value\"");
        index++;
        deck[index] = new SuperTrumpCard("The Petrologist", "Change trumps category to \"Crustal abundance\"");
        index++;
        deck[index] = new SuperTrumpCard("The Gemmologist", "Change trumps category to \"Hardness\"");
        index++;
        deck[index] = new SuperTrumpCard("The Mineralogist", "Change trumps category to \"Cleavage\"");
        index++;
        deck[index] = new SuperTrumpCard("The Geophysicist", "Change trumps category to \"Specific gravity\", or throw \"Magnetite\"");
        index++;
        deck[index] = new SuperTrumpCard("The Geologist", "Change trumps category of your choice");
    }
}
