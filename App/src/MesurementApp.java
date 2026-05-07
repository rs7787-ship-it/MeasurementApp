import java.util.function.DoubleBinaryOperator;

/**
 * UC13: CENTRALIZED ARITHMETIC LOGIC
 * Refactored to eliminate duplication and enforce DRY principles.
 */

// --- UNIT CONTRACT ---
interface IMeasurable {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

// --- OPERATION DISPATCHER ---
enum ArithmeticOperation {
    ADD((a, b) -> a + b),
    SUBTRACT((a, b) -> a - b),
    DIVIDE((a, b) -> {
        if (Math.abs(b) < 1e-9) throw new ArithmeticException("Division by zero quantity");
        return a / b;
    });

    private final DoubleBinaryOperator operator;

    ArithmeticOperation(DoubleBinaryOperator operator) {
        this.operator = operator;
    }

    public double compute(double v1, double v2) {
        return operator.applyAsDouble(v1, v2);
    }
}

// --- REFACTORED QUANTITY ENGINE ---
class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null) throw new IllegalArgumentException("Unit is null");
        this.unit = unit;
        // Standardized rounding for storage
        this.value = round(value);
    }

    // --- PUBLIC API (Signatures preserved from UC12) ---

    public Quantity<U> add(Quantity<U> other) { return add(other, this.unit); }
    
    public Quantity<U> add(Quantity<U> other, U target) {
        double resultBase = performArithmetic(other, ArithmeticOperation.ADD);
        return new Quantity<>(target.convertFromBaseUnit(resultBase), target);
    }

    public Quantity<U> subtract(Quantity<U> other) { return subtract(other, this.unit); }

    public Quantity<U> subtract(Quantity<U> other, U target) {
        double resultBase = performArithmetic(other, ArithmeticOperation.SUBTRACT);
        return new Quantity<>(target.convertFromBaseUnit(resultBase), target);
    }

    public double divide(Quantity<U> other) {
        return performArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    // --- PRIVATE DRY HELPERS ---

    /**
     * The heart of UC13: Centralized logic for all arithmetic.
     * Handles Validation, Base-Unit Normalization, and Computation.
     */
    private double performArithmetic(Quantity<U> other, ArithmeticOperation op) {
        validate(other);
        double v1Base = this.unit.convertToBaseUnit(this.value);
        double v2Base = other.unit.convertToBaseUnit(other.value);
        return op.compute(v1Base, v2Base);
    }

    private void validate(Quantity<U> other) {
        if (other == null) throw new IllegalArgumentException("Operand is null");
        if (this.unit.getClass() != other.unit.getClass()) {
            throw new IllegalArgumentException("Cross-category arithmetic is not allowed");
        }
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    @Override
    public String toString() { return value + " " + unit.getUnitName(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> that = (Quantity<?>) o;
        if (this.unit.getClass() != that.unit.getClass()) return false;
        return Math.abs(this.unit.convertToBaseUnit(this.value) - 
               ((IMeasurable)that.unit).convertToBaseUnit(that.value)) < 1e-6;
    }
}
