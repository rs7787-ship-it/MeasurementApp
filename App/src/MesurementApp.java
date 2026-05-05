public class QuantityMeasurementApp {

    // Inner class to represent the measurement in Feet
    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            // Step 5.1: Check for same reference (Reflexive)
            if (this == obj) {
                return true;
            }

            // Step 5.2: Check for null or different class type
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }

            // Step 5.3: Type cast and compare values
            Feet that = (Feet) obj;

            // Using Double.compare to handle NaN and precise floating-point values
            return Double.compare(that.value, this.value) == 0;
        }
    }

    public static void main(String[] args) {
        // Step 6: Test instantiation and equality
        Feet firstFeet = new Feet(1.0);
        Feet secondFeet = new Feet(1.0);

        boolean isEqual = firstFeet.equals(secondFeet);

        System.out.println("Input: 1.0 ft and 1.0 ft");
        System.out.println("Output: Equal (" + isEqual + ")");
    }
}