package uk.ac.ed.inf;

/**
 * This class is used for deserializing the json file from the rest server for the no fly
 * zones. Contains their name and coordinates for their polygon
 */
public class NoFlyZoneCoordinates {
    public String name;
    public double[][] coordinates;
}
