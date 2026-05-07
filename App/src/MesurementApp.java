/**
 * UC12: Subtraction and Division Operations
 * Part of the Generic Quantity Measurement System.
 */

interface IMeasurable {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
        // Rounding to 2 decimal places as per UC12 requirement
        this.value = Math.round(value * 100.0) / 100.0;
        this.unit = unit;
    }

    /**
     * SUBTRACTION: Implicit target unit (uses current unit)
     */
    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    /**
     * SUBTRACTION: Explicit target unit
     */
    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateCategory(other);
        double diffInBase = this.unit.convertToBaseUnit(this.value) - 
                            other.unit.convertToBaseUnit(other.value);
        
        double convertedValue = targetUnit.convertFromBaseUnit(diffInBase);
        return new Quantity<>(convertedValue, targetUnit);
    }

    /**
     * DIVISION: Returns a dimensionless scalar ratio
     */
    public double divide(Quantity<U> other) {
        validateCategory(other);
        double divisorBase = other.unit.convertToBaseUnit(other.value);
        
        if (Math.abs(divisorBase) < 1e-9) {
            throw new ArithmeticException("Cannot divide by zero quantity");
        }
        
        return this.unit.convertToBaseUnit(this.value) / divisorBase;
    }

    /**
     * Common validation for arithmetic operations
     */
    private void validateCategory(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Incompatible measurement categories (Cross-category operation prevented)");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> that = (Quantity<?>) o;
        if (this.unit.getClass() != that.unit.getClass()) return false;
        return Math.abs(this.unit.convertToBaseUnit(this.value) - 
               ((IMeasurable)that.unit).convertToBaseUnit(that.value)) < 1e-6;
    }

    @Override
    public String toString() {
        return value + " " + unit.getUnitName();
    }
}

// --- Example Units for Testing UC12 ---
enum LengthUnit implements IMeasurable {
    FEET(12.0), INCHES(1.0);
    private final double f;
    LengthUnit(double f) { this.f = f; }
    public double convertToBaseUnit(double v) { return v * f; }
    public double convertFromBaseUnit(double b) { return b / f; }
    public String getUnitName() { return name(); }
}
