/**
 * UNIFIED QUANTITY MEASUREMENT SYSTEM
 * Supported Categories: Length (Inches, Feet, Yards, CM) and Weight (KG, Grams, Pounds)
 * Features: Standalone Enums, SRP, Type Safety, and Immutability.
 */

// --- CATEGORY 1: LENGTH UNITS ---
enum LengthUnit {
    INCHES(1.0), 
    FEET(12.0), 
    YARDS(36.0), 
    CENTIMETERS(0.393701);

    private final double factor;
    LengthUnit(double factor) { this.factor = factor; }

    public double toBase(double val) { return val * factor; }
    public double fromBase(double val) { return val / factor; }
}

// --- CATEGORY 2: WEIGHT UNITS ---
enum WeightUnit {
    KILOGRAMS(1.0), 
    GRAMS(0.001), 
    POUNDS(0.453592);

    private final double factor;
    WeightUnit(double factor) { this.factor = factor; }

    public double toBase(double val) { return val * factor; }
    public double fromBase(double val) { return val / factor; }
}

// --- LENGTH QUANTITY ---
class Length {
    private final double value;
    private final LengthUnit unit;

    public Length(double value, LengthUnit unit) {
        if (unit == null || !Double.isFinite(value)) throw new IllegalArgumentException("Invalid Input");
        this.value = value;
        this.unit = unit;
    }

    public Length convertTo(LengthUnit target) {
        return new Length(target.fromBase(this.unit.toBase(this.value)), target);
    }

    public Length add(Length other, LengthUnit target) {
        double sumBase = this.unit.toBase(this.value) + other.unit.toBase(other.value);
        return new Length(target.fromBase(sumBase), target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Length other = (Length) o;
        return Math.abs(this.unit.toBase(this.value) - other.unit.toBase(other.value)) < 1e-6;
    }

    @Override
    public String toString() { return String.format("%.2f %s", value, unit); }
}

// --- WEIGHT QUANTITY ---
class Weight {
    private final double value;
    private final WeightUnit unit;

    public Weight(double value, WeightUnit unit) {
        if (unit == null || !Double.isFinite(value)) throw new IllegalArgumentException("Invalid Input");
        this.value = value;
        this.unit = unit;
    }

    public Weight convertTo(WeightUnit target) {
        return new Weight(target.fromBase(this.unit.toBase(this.value)), target);
    }

    public Weight add(Weight other, WeightUnit target) {
        double sumBase = this.unit.toBase(this.value) + other.unit.toBase(other.value);
        return new Weight(target.fromBase(sumBase), target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false; // Category Safety
        Weight other = (Weight) o;
        return Math.abs(this.unit.toBase(this.value) - other.unit.toBase(other.value)) < 1e-6;
    }

    @Override
    public String toString() { return String.format("%.2f %s", value, unit); }
}

// --- MAIN APPLICATION ---
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("=== 1 CODE SYSTEM: LENGTH & WEIGHT ===");

        // Length Tests
        Length mile = new Length(1, LengthUnit.YARDS);
        Length feet = new Length(3, LengthUnit.FEET);
        System.out.println("1 Yard + 3 Feet in Yards: " + mile.add(feet, LengthUnit.YARDS));

        // Weight Tests
        Weight kg = new Weight(1, WeightUnit.KILOGRAMS);
        Weight grams = new Weight(1000, WeightUnit.GRAMS);
        System.out.println("1 KG equals 1000 Grams: " + kg.equals(grams));

        // Conversion Test
        Weight pounds = new Weight(1, WeightUnit.POUNDS);
        System.out.println("1 Pound in Grams: " + pounds.convertTo(WeightUnit.GRAMS));

        // Category Incompatibility Check (Weight vs Length)
        System.out.println("Is 1 KG equal to 1 Yard? " + kg.equals(mile)); 
    }
}
