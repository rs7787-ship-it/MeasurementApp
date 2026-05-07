/**
 * UC3: Consolidated Quantity Measurement System
 * This solution refactors Feet and Inches into a single Quantity class.
 * It uses an Enum to manage units and conversion factors.
 */

// Step 1: Define the Enum with conversion factors relative to a base unit (Inches)
enum LengthUnit {
    INCH(1.0),
    FEET(12.0);

    public final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double convertToBase(double value) {
        return value * this.conversionFactor;
    }
}

// Step 2: Create a generic Quantity class to eliminate code duplication
class Quantity {
    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        // Reference check (Reflexive property)
        if (this == o) return true;

        // Null and Class check (Type safety)
        if (o == null || getClass() != o.getClass()) return false;

        Quantity that = (Quantity) o;

        // Convert both values to the common base unit (Inches) for comparison
        double value1 = this.unit.convertToBase(this.value);
        double value2 = that.unit.convertToBase(that.value);

        // Value comparison using Double.compare to handle precision/special cases
        return Double.compare(value1, value2) == 0;
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}

// Main Application to demonstrate and test functionality
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        System.out.println("--- UC3: Generic Quantity Equality Results ---");

        // Test Case: Feet to Feet (Same Value)
        Quantity f1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity f2 = new Quantity(1.0, LengthUnit.FEET);
        System.out.println("1.0 ft == 1.0 ft: " + f1.equals(f2));

        // Test Case: Inch to Inch (Same Value)
        Quantity i1 = new Quantity(1.0, LengthUnit.INCH);
        Quantity i2 = new Quantity(1.0, LengthUnit.INCH);
        System.out.println("1.0 in == 1.0 in: " + i1.equals(i2));

        // Test Case: Feet to Inch (Equivalent Value - Cross Unit)
        Quantity feetVal = new Quantity(1.0, LengthUnit.FEET);
        Quantity inchVal = new Quantity(12.0, LengthUnit.INCH);
        System.out.println("1.0 ft == 12.0 in: " + feetVal.equals(inchVal));

        // Test Case: Inch to Feet (Symmetry check)
        System.out.println("12.0 in == 1.0 ft: " + inchVal.equals(feetVal));

        // Test Case: Different Values
        Quantity diffFeet = new Quantity(2.0, LengthUnit.FEET);
        System.out.println("1.0 ft == 2.0 ft: " + f1.equals(diffFeet));

        // Test Case: Null and Reference checks
        System.out.println("1.0 ft == null: " + f1.equals(null));
        System.out.println("Reflexive Check (f1 == f1): " + f1.equals(f1));
    }
}
