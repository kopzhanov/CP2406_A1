import java.util.InputMismatchException;

class SuperTrumpCard extends Card {
    SuperTrumpCard(String name, String instruction) {
        super(name, instruction);
    }

    static void playCard(Card card, boolean hasMagnetite, Player player) {
        switch (card.getName()) {
            case "The Miner": {
                MineralSupertrumps.category = 5;
                break;
            }
            case "The Petrologist": {
                MineralSupertrumps.category = 4;
                break;
            }
            case "The Gemmologist": {
                MineralSupertrumps.category = 1;
                break;
            }
            case "The Mineralogist": {
                MineralSupertrumps.category = 3;
                break;
            }
            case "The Geophysicist": {
                MineralSupertrumps.category = 2;
                if (hasMagnetite) {
                    System.out.println("Player " + player.getID() + "won the hand by playing combo of \"The Geophysicist\" and \"Magnetite\"");
                    MineralSupertrumps.players.remove(player);
                }
                break;
            }
            case "The Geologist": {
                int category;
                System.out.println("1.Hardness");
                System.out.println("2.Specific Gravity");
                System.out.println("3.Cleavage");
                System.out.println("4.Crustal Abundance");
                System.out.println("5.Economic Value");

                System.out.println("Choose a category according to its number");
                category = MineralSupertrumps.input.nextInt();
                while (category < 1 || category > 5) {
                    try {
                        System.out.println("Choose a category according to its number (Minimum - 0, Maximum - 5)");
                        category = MineralSupertrumps.input.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("A number is required, try again");
                        MineralSupertrumps.input.next();
                    }
                }
                MineralSupertrumps.category = category;
                break;
            }
        }
    }
}
