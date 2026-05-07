/**
 * UC5: Unit-to-Unit Conversion and Equality System
 * Features: Immutability, Enum-based conversion factors, Method Overloading, 
 * and robust input validation.
 */

// Step 1: Enum with centralized conversion logic and factors
enum LengthUnit {
    INCH(1.0),
    FEET(12.0),
    YARD(36.0),
    CENTIMETER(0.393701);

    public final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    // Helper to normalize any value to the base unit (Inches)
    public double toBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    // Helper to convert from the base unit back to the target unit
    public double fromBaseUnit(double value) {
        return value / this.conversionFactor;
    }
}

// Step 2: The Quantity Class (Value Object)
class Quantity {
    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        validate(value, unit);
        this.value = value;
        this.unit = unit;
    }

    // Private validation logic
    private static void validate(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite numeric number.");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null.");
        }
    }

    /**
     * Static Conversion API: Normalizes to base unit then converts to target.
     */
    public static double convert(double value, LengthUnit source, LengthUnit target) {
        validate(value, source);
        if (target == null) throw new IllegalArgumentException("Target unit cannot be null.");
        
        double baseInches = source.toBaseUnit(value);
        return target.fromBaseUnit(baseInches);
    }

    /**
     * Instance Conversion: Returns a new Quantity object in the target unit.
     */
    public Quantity convertTo(LengthUnit targetUnit) {
        double newValue = convert(this.value, this.unit, targetUnit);
        return new Quantity(newValue, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity that = (Quantity) o;
        // Use epsilon for floating-point equality (1e-6)
        return Math.abs(this.unit.toBaseUnit(this.value) - that.unit.toBaseUnit(that.value)) < 1e-6;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit);
    }
}

// Step 3: Application layer with Method Overloading
public class QuantityMeasurementApp {

    /**
     * Method Overloading 1: Direct value conversion
     */
    public static void demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {
        double result = Quantity.convert(value, from, to);
        System.out.printf("[Value Conversion] %.2f %s = %.4f %s%n", value, from, result, to);
    }

    /**
     * Method Overloading 2: Quantity object conversion
     */
    public static void demonstrateLengthConversion(Quantity lengthObj, LengthUnit targetUnit) {
        Quantity result = lengthObj.convertTo(targetUnit);
        System.out.println("[Object Conversion] " + lengthObj + " converted to " + result);
    }

    public static void main(String[] args) {
        System.out.println("--- UC5: Conversion and Equality Demonstration ---");

        // 1. Basic Unit Conversions (Feet <-> Inches)
        demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);   // Output: 12.0
        demonstrateLengthConversion(24.0, LengthUnit.INCH, LengthUnit.FEET); // Output: 2.0

        // 2. Cross-Unit Conversions (Yards <-> Inches)
        demonstrateLengthConversion(1.0, LengthUnit.YARD, LengthUnit.INCH);  // Output: 36.0
        demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARD); // Output: 1.0

        // 3. CM to Inches (Precision Handling)
        demonstrateLengthConversion(1.0, LengthUnit.CENTIMETER, LengthUnit.INCH); // Output: 0.3937

        // 4. Object-Based Conversion (Method Overloading)
        Quantity myYards = new Quantity(3.0, LengthUnit.YARD);
        demonstrateLengthConversion(myYards, LengthUnit.FEET); // Output: 9.0 FEET

        // 5. Equality Check (Preserving UC4 logic)
        Quantity oneYard = new Quantity(1.0, LengthUnit.YARD);
        Quantity thirtySixInches = new Quantity(36.0, LengthUnit.INCH);
        System.out.println("Equality Test (1 Yard == 36 Inches): " + oneYard.equals(thirtySixInches));

        // 6. Validation Test
        try {
            Quantity.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCH);
        } catch (IllegalArgumentException e) {
            System.out.println("Error Caught: " + e.getMessage());
        }
    }
}
