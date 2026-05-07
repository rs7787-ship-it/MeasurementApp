/**
 * UC7: Addition with Explicit Target Unit Specification
 * Features: Method Overloading, Private Utility Helpers, and Immutability.
 */

// Step 1: Enum with conversion factors relative to INCHES
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

// Step 2: The Quantity Class (Value Object)
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
     * Private Utility: Performs the actual calculation to avoid code duplication (DRY).
     */
    private static double calculateSumInTarget(Quantity q1, Quantity q2, LengthUnit target) {
        double baseSum = q1.unit.toBaseUnit(q1.value) + q2.unit.toBaseUnit(q2.value);
        return target.fromBaseUnit(baseSum);
    }

    /**
     * Method Overloading (UC6): Implicitly returns result in the unit of the first operand.
     */
    public Quantity add(Quantity other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null.");
        double resultValue = calculateSumInTarget(this, other, this.unit);
        return new Quantity(resultValue, this.unit);
    }

    /**
     * Method Overloading (UC7): Explicitly returns result in the specified target unit.
     */
    public Quantity add(Quantity other, LengthUnit targetUnit) {
        if (other == null || targetUnit == null) 
            throw new IllegalArgumentException("Operand and target unit must be non-null.");
        double resultValue = calculateSumInTarget(this, other, targetUnit);
        return new Quantity(resultValue, targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity that = (Quantity) o;
        // Epsilon check for floating-point accuracy
        return Math.abs(this.unit.toBaseUnit(this.value) - that.unit.toBaseUnit(that.value)) < 1e-6;
    }

    @Override
    public String toString() {
        return String.format("%.3f %s", value, unit);
    }
}

// Step 3: Application layer for testing and demonstration
public class QuantityMeasurementApp {

    public static void main(String[] args) {
        System.out.println("--- UC7: Addition with Target Unit Specification ---");

        Quantity oneFoot = new Quantity(1.0, LengthUnit.FEET);
        Quantity twelveInches = new Quantity(12.0, LengthUnit.INCH);

        // 1. Explicit Target Unit: Yards (1ft + 12in = 0.667 yards)
        System.out.println("1ft + 12in (Target: YARDS) = " + oneFoot.add(twelveInches, LengthUnit.YARD));

        // 2. Explicit Target Unit: Feet (1ft + 12in = 2 feet)
        System.out.println("1ft + 12in (Target: FEET)  = " + oneFoot.add(twelveInches, LengthUnit.FEET));

        // 3. Explicit Target Unit: Inches (1ft + 12in = 24 inches)
        System.out.println("1ft + 12in (Target: INCHES)= " + oneFoot.add(twelveInches, LengthUnit.INCH));

        // 4. Centimeters and Inches (Target: Centimeters)
        Quantity oneCm = new Quantity(2.54, LengthUnit.CENTIMETER);
        Quantity oneInch = new Quantity(1.0, LengthUnit.INCH);
        System.out.println("2.54cm + 1in (Target: CM)  = " + oneCm.add(oneInch, LengthUnit.CENTIMETER));

        // 5. Commutativity Check with Target Units
        Quantity sumA = oneFoot.add(twelveInches, LengthUnit.YARD);
        Quantity sumB = twelveInches.add(oneFoot, LengthUnit.YARD);
        System.out.println("Commutative property (A+B == B+A): " + sumA.equals(sumB));

        // 6. Zero value with Target Unit conversion
        Quantity zeroInches = new Quantity(0.0, LengthUnit.INCH);
        System.out.println("5ft + 0in (Target: YARDS)   = " + new Quantity(5.0, LengthUnit.FEET).add(zeroInches, LengthUnit.YARD));

        // 7. Negative values with Target Unit conversion
        Quantity negTwoFeet = new Quantity(-2.0, LengthUnit.FEET);
        System.out.println("5ft + (-2ft) (Target: INCHES)= " + new Quantity(5.0, LengthUnit.FEET).add(negTwoFeet, LengthUnit.INCH));
        
        // 8. Validation Test
        try {
            oneFoot.add(twelveInches, null);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation Caught: " + e.getMessage());
        }
    }
}
