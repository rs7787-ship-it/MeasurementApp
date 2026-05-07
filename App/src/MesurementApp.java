/**
 * UC10: THE UNIFIED GENERIC MEASUREMENT SYSTEM
 * -------------------------------------------------------------------------
 * This single codebase supports Length, Weight, and any future categories
 * by using Java Generics and a common Interface contract.
 */

// 1. The Interface Contract: Standardizes behavior for ALL measurement units
interface IMeasurable {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

// 2. Length Category implementation
enum LengthUnit implements IMeasurable {
    INCHES(1.0), FEET(12.0), YARDS(36.0), CM(0.393701);

    private final double factor;
    LengthUnit(double factor) { this.factor = factor; }

    @Override public double convertToBaseUnit(double v) { return v * factor; }
    @Override public double convertFromBaseUnit(double b) { return b / factor; }
    @Override public String getUnitName() { return name(); }
}

// 3. Weight Category implementation
enum WeightUnit implements IMeasurable {
    KG(1.0), GRAMS(0.001), LBS(0.453592);

    private final double factor;
    WeightUnit(double factor) { this.factor = factor; }

    @Override public double convertToBaseUnit(double v) { return v * factor; }
    @Override public double convertFromBaseUnit(double b) { return b / factor; }
    @Override public String getUnitName() { return name(); }
}

// 4. The Generic Quantity Class: One class to handle every category safely
class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null || !Double.isFinite(value)) 
            throw new IllegalArgumentException("Invalid value or unit.");
        this.value = value;
        this.unit = unit;
    }

    // Convert to a target unit within the same category
    public Quantity<U> convertTo(U targetUnit) {
        double base = this.unit.convertToBaseUnit(this.value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(base), targetUnit);
    }

    // Add two quantities of the same category
    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double totalBase = this.unit.convertToBaseUnit(this.value) + 
                           other.unit.convertToBaseUnit(other.value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(totalBase), targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> that = (Quantity<?>) o;

        // Category Safety: Prevent comparing Length to Weight at runtime
        if (this.unit.getClass() != that.unit.getClass()) return false;

        double v1 = this.unit.convertToBaseUnit(this.value);
        double v2 = ((IMeasurable) that.unit).convertToBaseUnit(that.value);
        return Math.abs(v1 - v2) < 1e-6;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit.getUnitName());
    }
}

// 5. Orchestration Layer
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("--- UC10 GENERIC SYSTEM DEMO ---");

        // Length Operations
        Quantity<LengthUnit> oneFt = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> twelveIn = new Quantity<>(12.0, LengthUnit.INCHES);
        System.out.println("Length Equality (1ft == 12in): " + oneFt.equals(twelveIn));
        System.out.println("Length Addition (1ft + 12in): " + oneFt.add(twelveIn, LengthUnit.FEET));

        // Weight Operations
        Quantity<WeightUnit> oneKg = new Quantity<>(1.0, WeightUnit.KG);
        Quantity<WeightUnit> grams = new Quantity<>(1000.0, WeightUnit.GRAMS);
        System.out.println("Weight Equality (1kg == 1000g): " + oneKg.equals(grams));

        // Cross-Category Safety
        System.out.println("Cross-Category (1ft == 1kg): " + oneFt.equals(oneKg));

        // Scalability Check: Adding Volume is now just an Enum away!
    }
}
