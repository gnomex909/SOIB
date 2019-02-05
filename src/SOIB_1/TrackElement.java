package SOIB_1;

public class TrackElement {
    private double expectedDamping;
    private double variation;
    private int amount;
    private String elementName;

    public TrackElement() {
    }

    public TrackElement(double expectedDamping, double variation, int amount, String elementName) {
        this.expectedDamping = expectedDamping;
        this.variation = variation;
        this.amount = amount;
        this.elementName = elementName;
    }

    public double getExpectedDamping() {
        return expectedDamping;
    }

    public void setExpectedDamping(double expectedDamping) {
        this.expectedDamping = expectedDamping;
    }

    public double getVariation() {
        return variation;
    }

    public void setVariation(double variation) {
        this.variation = variation;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    @Override
    public String toString() {
        return "TrackElement{" +
                "expectedDamping=" + expectedDamping +
                ", variation=" + variation +
                ", amount=" + amount +
                ", elementName='" + elementName + '\'' +
                '}';
    }
}
