package com.FlightPub.model;


/**
 * Haversine formula for finding distances between two coordinates
 */
public class HaversineCalculator {
    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    /**
     * Calculates distance between two coordinates
     * @param startLat Latitude of origin location
     * @param startLong Longitude of origin location
     * @param endLat Latitude of destination location
     * @param endLong Longitude of destination location
     * @return distance between two coordinates
     */
    public static double distance(double startLat, double startLong, double endLat, double endLong) {

        double dLat  = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat   = Math.toRadians(endLat);

        double a = haversine(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // <-- d
    }

    /**
     * Haversine Formula
     * @param val for formula
     * @return result of formula
     */
    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
