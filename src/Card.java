public class Card {
    private String name;
    private double hardness;
    private double gravity;
    private String cleavage;
    private String abundance;
    private String ecoValue;

    Card(String name, double hardness, double gravity, String cleavage, String abundance, String ecoValue) {
        this.name = name;
        this.hardness = hardness;
        this.gravity = gravity;
        this.cleavage = cleavage;
        this.abundance = abundance;
        this.ecoValue = ecoValue;
    }

    public String getName() {
        return name;
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
