/**
 * UC4: Extended Unit Support (Yards and Centimeters)
 * Proves scalability: Logic remains the same, only units are added.
 */

enum LengthUnit {
    // Base unit is INCH
    INCH(1.0),
    FEET(12.0),
    YARD(36.0),           // 1 Yard = 3 Feet = 36 Inches
    CENTIMETER(0.393701); // 1 cm = 0.393701 Inches

    public final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double convertToBase(double value) {
        return value * this.conversionFactor;
    }
}

class Quantity {
    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Quantity that = (Quantity) o;

        // Normalize both to Inches for comparison
        double value1 = this.unit.convertToBase(this.value);
        double value2 = that.unit.convertToBase(that.value);

        // Using a small epsilon for floating point comparison with CM
        return Math.abs(value1 - value2) < 1e-6;
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
