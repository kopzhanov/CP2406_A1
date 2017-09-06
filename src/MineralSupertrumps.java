import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MineralSupertrumps {
    static Card[] cards = new Card[54];

    public static void main(String[] args) {
        parseCards();
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
                cards[index] = new Card(card[0], Double.parseDouble(card[1]), Double.parseDouble(card[2]), card[3], card[4], card[5]);
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
    }
}
