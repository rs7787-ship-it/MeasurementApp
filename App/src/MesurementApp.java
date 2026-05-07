/**
 * UC2: Feet and Inches Measurement Equality
 * This implementation provides separate classes for Feet and Inches
 * to ensure type-safe equality checks.
 */

class Feet {
    private final Double value;

    public Feet(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        // 1. Same Reference Check
        if (this == o) return true;
        
        // 2. Null and Type Check
        if (o == null || getClass() != o.getClass()) return false;
        
        // 3. Value Comparison
        Feet feet = (Feet) o;
        return Double.compare(feet.value, value) == 0;
    }
}

class Inches {
    private final Double value;

    public Inches(Double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        // 1. Same Reference Check
        if (this == o) return true;
        
        // 2. Null and Type Check
        if (o == null || getClass() != o.getClass()) return false;
        
        // 3. Value Comparison
        Inches inches = (Inches) o;
        return Double.compare(inches.value, value) == 0;
    }
}

public class QuantityMeasurementApp {

    // Static method for Feet equality check
    public static boolean compareFeet(Double f1, Double f2) {
        if (f1 == null || f2 == null) return false;
        Feet firstFeet = new Feet(f1);
        Feet secondFeet = new Feet(f2);
        return firstFeet.equals(secondFeet);
    }

    // Static method for Inches equality check
    public static boolean compareInches(Double i1, Double i2) {
        if (i1 == null || i2 == null) return false;
        Inches firstInches = new Inches(i1);
        Inches secondInches = new Inches(i2);
        return firstInches.equals(secondInches);
    }

    public static void main(String[] args) {
        // Test Case 1: Same Value Inches
        System.out.println("Input: 1.0 inch and 1.0 inch -> Equal: " + compareInches(1.0, 1.0));

        // Test Case 2: Different Value Inches
        System.out.println("Input: 1.0 inch and 2.0 inch -> Equal: " + compareInches(1.0, 2.0));

        // Test Case 3: Same Value Feet
        System.out.println("Input: 1.0 ft and 1.0 ft -> Equal: " + compareFeet(1.0, 1.0));

        // Test Case 4: Different Value Feet
        System.out.println("Input: 1.0 ft and 2.0 ft -> Equal: " + compareFeet(1.0, 2.0));
        
        // Demonstration of Type Safety (Inches cannot be compared to Feet)
        Inches inchObj = new Inches(1.0);
        Feet feetObj = new Feet(1.0);
        System.out.println("Input: 1.0 inch and 1.0 ft (Cross-type) -> Equal: " + inchObj.equals(feetObj));
    }
}
