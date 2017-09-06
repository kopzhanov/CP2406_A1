public class MineralCard extends Card {
    private double hardness;
    private double gravity;
    private String cleavage;
    private String abundance;
    private String ecoValue;

    MineralCard(String name, double hardness, double gravity, String cleavage, String abundance, String ecoValue) {
        super(name);
        this.hardness = hardness;
        this.gravity = gravity;
        this.cleavage = cleavage;
        this.abundance = abundance;
        this.ecoValue = ecoValue;
    }

    public double getHardness() {
        return hardness;
    }

    public double getGravity() {
        return gravity;
    }

    public String getCleavage() {
        return cleavage;
    }

    public String getAbundance() {
        return abundance;
    }

    public String getEcoValue() {
        return ecoValue;
    }
}
