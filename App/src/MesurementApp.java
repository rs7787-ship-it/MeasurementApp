/**
 * UC8: Consolidated Architecture
 * Responsibility: 
 * - LengthUnit: Logic for unit conversion.
 * - Quantity: Logic for measurement comparison and arithmetic.
 */

// Step 1: Standalone Enum with Conversion Responsibility
enum LengthUnit {
    FEET(1.0),
    INCHES(1.0 / 12.0),
    YARDS(3.0),
    CENTIMETERS(1.0 / 30.48);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    /**
     * Responsibility: Convert this unit's value to the base unit (FEET).
     */
    public double convertToBaseUnit(double value) {
        return value * this.conversionFactor;
    }

    /**
     * Responsibility: Convert a base unit value (FEET) to this unit.
     */
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.conversionFactor;
    }
}

// Step 2: Simplified Quantity Class (Delegates conversion to the Unit)
class Quantity {
    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null.");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite.");
        this.value = value;
        this.unit = unit;
    }

    /**
     * Equality Check: Normalizes both quantities to base unit via delegation.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity that = (Quantity) o;
        return Math.abs(this.unit.convertToBaseUnit(this.value) - 
                        that.unit.convertToBaseUnit(that.value)) < 1e-6;
    }

    /**
     * Conversion: Uses unit methods to transform value.
     */
    public Quantity convertTo(LengthUnit targetUnit) {
        double baseValue = this.unit.convertToBaseUnit(this.value);
        return new Quantity(targetUnit.convertFromBaseUnit(baseValue), targetUnit);
    }

    /**
     * Addition: Sums base values and converts result to target unit.
     */
    public Quantity add(Quantity other, LengthUnit targetUnit) {
        if (other == null || targetUnit == null) 
            throw new IllegalArgumentException("Operands and target unit must be non-null.");
            
        double baseSum = this.unit.convertToBaseUnit(this.value) + 
                         other.unit.convertToBaseUnit(other.value);
        return new Quantity(targetUnit.convertFromBaseUnit(baseSum), targetUnit);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s", value, unit);
    }
}

// Step 3: Application layer for standalone testing
public class QuantityMeasurementApp {
    public static void main(String[] args) {
        System.out.println("--- UC8 Standalone Architecture Results ---");

        // 1. Conversion via Delegation
        Quantity oneFoot = new Quantity(1.0, LengthUnit.FEET);
        System.out.println("1 Foot to Inches: " + oneFoot.convertTo(LengthUnit.INCHES));

        // 2. Equality check
        Quantity twelveInches = new Quantity(12.0, LengthUnit.INCHES);
        System.out.println("1 Foot equals 12 Inches: " + oneFoot.equals(twelveInches));

        // 3. Addition with Target Unit
        Quantity oneYard = new Quantity(1.0, LengthUnit.YARDS);
        Quantity threeFeet = new Quantity(3.0, LengthUnit.FEET);
        System.out.println("1 Yard + 3 Feet (in Yards): " + oneYard.add(threeFeet, LengthUnit.YARDS));

        // 4. Centimeter Conversion
        Quantity oneCm = new Quantity(2.54, LengthUnit.CENTIMETERS);
        System.out.println("2.54 CM to Inches: " + oneCm.convertTo(LengthUnit.INCHES));

        // 5. Unit-level responsibility check
        System.out.println("Raw Unit Logic (12 inches to feet): " + LengthUnit.INCHES.convertToBaseUnit(12.0));
    }
}
