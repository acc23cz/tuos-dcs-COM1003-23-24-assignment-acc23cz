package uk.ac.sheffield.com1003.assignment2023.codeprovided.gui;

/**
 * Used to store the Minimum, Maximum, and Average values for each Song Property.
 * This class ensures that the minimum value is not greater than the maximum value,
 * throwing IllegalArgumentException if invalid values are provided.
 *
 * @author Maria-Cruz Villa-Uriol (m.villa-uriol@sheffield.ac.uk)
 * @author Ayeshmantha Wijayagunethilake (a.wijayagunethilake@sheffield.ac.uk)
 * @copyright Copyright (c) University of Sheffield 2023
 */
public class CustomChartAxisValues {
    private final double min;
    private final double max;
    private final double average;

    /**
     * Constructs a new instance with the specified minimum, maximum, and average values.
     *
     * @param min the minimum value, must not be greater than max
     * @param max the maximum value, must not be less than min
     * @param average the average value, logically between min and max
     * @throws IllegalArgumentException if min > max or average is not between min and max
     */
    public CustomChartAxisValues(double min, double max, double average) {
        double epsilon = 1e-9;
        if (min > max) {
            throw new IllegalArgumentException("Minimum value cannot exceed maximum value.");
        }
        if (max - min <= epsilon) {
            average = min;
        }
        if (average < min - epsilon || average > max + epsilon) {
            throw new IllegalArgumentException("Average value must be between minimum and maximum values.");
        }
        this.min = min;
        this.max = max;
        this.average = average;
    }

    /**
     * Returns the minimum value.
     * @return the minimum value
     */
    public double getMin() {
        return min;
    }

    /**
     * Returns the maximum value.
     * @return the maximum value
     */
    public double getMax() {
        return max;
    }

    /**
     * Returns the average value.
     * @return the average value
     */
    public double getAverage() {
        return average;
    }

    @Override
    public String toString() {
        return String.format("CustomChartAxisValues{min=%.2f, max=%.2f, average=%.2f}", min, max, average);
    }
}
