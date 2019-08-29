package org.foi.common;

public class MathHelper  {

    public static float round(float value, int nDigits) {
        return Math.round(value * (10 ^ nDigits)) / (float) (10 ^ nDigits);
    }

    public static double round(double value, int nDigits) {
        return Math.round(value * (10 ^ nDigits)) / (double) (10 ^ nDigits);
    }
}
