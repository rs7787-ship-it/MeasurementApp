/**
 * UC11: Volume Measurement Implementation
 * Supports: Litres, Millilitres, and Gallons
 */

// 1. The Interface Contract (required for the Generic Quantity class)
interface IMeasurable {
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

// 2. UC11 Volume Unit Implementation
enum VolumeUnit implements IMeasurable {
    LITRE(1.0),
    MILLILITRE(0.001),
    GALLON(3.78541); // 1 Gallon ≈ 3.78541 Litres

    private final double factor;

    VolumeUnit(double factor) {
        this.factor = factor;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * this.factor;
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return baseValue / this.factor;
    }

    @Override
    public String getUnitName() {
        return this.name();
    }
}

// 3. Generic Quantity Engine (reused for Volume)
class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null || !Double.isFinite(value)) throw new IllegalArgumentException("Invalid input.");
        this.value = value;
        this.unit = unit;
    }

    public Quantity<U> convertTo(U targetUnit) {
        double baseValue = this.unit.convertToBaseUnit(this.value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(baseValue), targetUnit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double sumBase = this.unit.convertToBaseUnit(this.value) + 
                         other.unit.convertToBaseUnit(other.value);
        return new Quantity<>(targetUnit.convertFromBaseUnit(sumBase), targetUnit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quantity<?> that = (Quantity<?>) o;
        
        // Ensure we are comparing within the same unit category (Volume vs Volume)
        if (this.unit.getClass() != that.unit.getClass()) return false;

        return Math.abs(this.unit.convertToBaseUnit(this.value) - 
               ((IMeasurable)that.unit).convertToBaseUnit(that.value)) < 1e-6;
    }

    @Override
    public String toString() {
        return String.format("%.3f %s", value, unit.getUnitName());
    }
}

// 4. Main Demo for UC11
public class VolumeMeasurementApp {
    public static void main(String[] args) {
        System.out.println("--- UC11 VOLUME MEASUREMENT DEMO ---");

        // Equality: 1 Gallon vs 3.785 Litres
        Quantity<VolumeUnit> oneGallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> litres = new Quantity<>(3.78541, VolumeUnit.LITRE);
        System.out.println("1 Gallon == 3.78541 Litres: " + oneGallon.equals(litres));

        // Equality: 1 Litre vs 1000 mL
        Quantity<VolumeUnit> oneLitre = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> ml = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        System.out.println("1 Litre == 1000 mL: " + oneLitre.equals(ml));

        // Addition: 1L + 1000mL (Target: Litre)
        System.out.println("Sum (1L + 1000mL) in Litres: " + oneLitre.add(ml, VolumeUnit.LITRE));

        // Addition: 1 Gallon + 3.785L (Target: Gallon)
        System.out.println("Sum (1 Gallon + 3.78541L) in Gallons: " + oneGallon.add(litres, VolumeUnit.GALLON));
        
        // Conversion: 1 Gallon to mL
        System.out.println("1 Gallon converted to mL: " + oneGallon.convertTo(VolumeUnit.MILLILITRE));
    }
}
```</U>
