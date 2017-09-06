public class SuperTrumpCard extends Card {
    private String instruction;

    SuperTrumpCard(String name, String instruction) {
        super(name);
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }
}
