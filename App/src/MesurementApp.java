/**
 * UC6: Measurement Addition System
 * Supports cross-unit addition, maintaining immutability and type safety.
 */

// Step 1: Enum with conversion logic
enum LengthUnit {
    INCH(1.0),
    FEET(12.0),
    YARD(36.0),
    CENTIMETER(0.393701);

    public final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double toBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    public double fromBaseUnit(double value) {
        return value / this.conversionFactor;
    }
}

// Step 2: The Quantity Class with Addition logic
class Quantity {
    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        validate(value, unit);
        this.value = value;
        this.unit = unit;
    }

    private static void validate(double value, LengthUnit unit) {
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite.");
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null.");
    }

    /**
     * Instance method: Adds another quantity to this one.
     * The result is returned in the unit of the first operand (this).
     */
    public Quantity add(Quantity other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null.");
        
        // 1. Convert both to base unit (Inches)
        double baseSum = this.unit.toBaseUnit(this.value) + other.unit.toBaseUnit(other.value);
        
        // 2. Convert sum back to THIS unit
        double finalValue = this.unit.fromBaseUnit(baseSum);
        
        return new Quantity(finalValue, this.unit);
    }

    /**
     * Static method: Adds two quantities and returns result in a specific target unit.
     */
    public static Quantity add(Quantity q1, Quantity q2, LengthUnit targetUnit) {
        if (q1 == null || q2 == null || targetUnit == null) 
            throw new IllegalArgumentException("Operands and target unit must be non-null.");
            
        double baseSum = q1.unit.toBaseUnit(q1.value) + q2.unit.toBaseUnit(q2.value);
        double finalValue = targetUnit.fromBaseUnit(baseSum);
        
        return new Quantity(finalValue, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity that = (Quantity) o;
        return Math.abs(this.unit.toBaseUnit(this.value) - that.unit.toBaseUnit(that.value)) < 1e-6;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", value, unit);
    }
}

// Step 3: Application layer for testing and demonstration
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        System.out.println("--- UC6: Addition of Measurements ---");

        // 1. Same Unit Addition: 1 ft + 2 ft = 3 ft
        Quantity f1 = new Quantity(1.0, LengthUnit.FEET);
        Quantity f2 = new Quantity(2.0, LengthUnit.FEET);
        System.out.println("1 ft + 2 ft = " + f1.add(f2));

        // 2. Cross-Unit Addition (Result in first unit): 1 ft + 12 in = 2 ft
        Quantity twelveInches = new Quantity(12.0, LengthUnit.INCH);
        System.out.println("1 ft + 12 in = " + f1.add(twelveInches));

        // 3. Cross-Unit Addition (Result in first unit): 12 in + 1 ft = 24 in
        System.out.println("12 in + 1 ft = " + twelveInches.add(f1));

        // 4. Yards and Feet: 1 yard + 3 feet = 2 yards
        Quantity oneYard = new Quantity(1.0, LengthUnit.YARD);
        Quantity threeFeet = new Quantity(3.0, LengthUnit.FEET);
        System.out.println("1 yard + 3 feet = " + oneYard.add(threeFeet));

        // 5. Centimeters and Inches: 2.54 cm + 1 in = 5.08 cm
        Quantity cmVal = new Quantity(2.54, LengthUnit.CENTIMETER);
        Quantity inchVal = new Quantity(1.0, LengthUnit.INCH);
        System.out.println("2.54 cm + 1 in = " + cmVal.add(inchVal));

        // 6. Commutativity Test
        Quantity sum1 = f1.add(twelveInches); // Result in Feet
        Quantity sum2 = twelveInches.add(f1); // Result in Inches
        System.out.println("Sum 1 equals Sum 2 (Logic check): " + sum1.equals(sum2));

        // 7. Identity Element (Adding Zero)
        Quantity zeroInches = new Quantity(0.0, LengthUnit.INCH);
        System.out.println("5 ft + 0 in = " + new Quantity(5.0, LengthUnit.FEET).add(zeroInches));
        
        // 8. Negative Value Handling
        Quantity negTwoFeet = new Quantity(-2.0, LengthUnit.FEET);
        System.out.println("5 ft + (-2 ft) = " + new Quantity(5.0, LengthUnit.FEET).add(negTwoFeet));
    }
}
