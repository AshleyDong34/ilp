package uk.ac.ed.inf;


import java.awt.geom.Line2D;
import java.util.ArrayList;


/**
 * This record class stores the coordinate points (longitude and latitude)
 * It contains several methods to check what the instance of the class is currently doing in relation to other
 * LngLat points or polygons.
 * @param lng is the x coordinate
 * @param lat is the y coordinate
 */
public record LngLat(double lng, double lat){

    /**
     * This method compares the points provides by the parameter and this object's points to find the
     * distance between the two using pythagoras's theorem.
     * @param point is the LngLat object that we compare this object to find the distance
     * @return the distance between the two objects in type double
     */
    public double distanceTo(LngLat point) {

        double x1 = point.lng;
        double x2 = lng;
        double y1 = point.lat;
        double y2 = lat;
        // We use the Math.sqrt() function to be able to apply pythagoras's theorem
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    /**
     * This method compares a point with this object's point and see if they are within
     * 0.00015 of each other
     * @param point the other object that is being compared to with this object's points
     * @return a boolean depending on if the two points are close relative to each other,
     * if they are within the required distance, then true is returned, otherwise false
     */
    public Boolean closeTo(LngLat point) {

        return distanceTo(point) < 0.00015;
    }

    /**
     * This method increases the position of this instance of LngLat by 0.00015 in the direction of the
     * compass degree entered. This can only be one of 16 compass directions set by the enum class CompassDegree.
     * We use Math.cos() and Math.sin() to calculate the lat and lng distance that is required.
     * @param compassDegree is an instance of the enum class CompassDegrees, where it contains 16 final double degrees that are allowed as input
     * @return a new instance of the LngLat class with new positions points for lng and lat
     */
    public LngLat nextPosition(CompassDegrees compassDegree) {

        double newLng;
        double newLat;
        if (compassDegree == null) {
            newLng = lng;
            newLat = lat;
        } else {
            newLng = 0.00015*Math.cos(compassDegree.getCompassDegrees()) + lng;
            newLat = 0.00015*Math.sin(compassDegree.getCompassDegrees()) + lat;
        }
        return new LngLat(newLng,newLat);
    }

    /**
     * This method checks if the instance of the LngLat class is in the central area or not by using
     * the isInPolygon() method
     * @return boolean stating if the LngLat point is in Central Area polygon or not
     */
    public Boolean inCentralArea() {
        Coordinates[] listOfPoints = CentralArea.getCentralArea().getCoordinates();
        // store and change points from the centralArea coordinates into the required type for the isInPolygon() method
        double[][] points = new double[listOfPoints.length][2];
        for (int i = 0; i< listOfPoints.length; i++) {
            points[i][0] = listOfPoints[i].longitude;
            points[i][1] = listOfPoints[i].latitude;
        }
        return isInPolygon(points);
    }

    /**
     * This method checks if the LngLat point is in any of the noFlyZone polygons and returns a boolean based if it is true or not.
     * This method requires the use of another method isInPolygon()
     * @return boolean isInNoFlyZone as true if the point is in one of the noFlyZones and false otherwise
     */
    public Boolean inNoFlyZone() {
        NoFlyZoneCoordinates[] noFlyZones = NoFlyZone.getNoFlyZone().getCoordinates();
        boolean isInNoFlyZone = false;
        for (NoFlyZoneCoordinates zone: noFlyZones ) {
            if (isInPolygon(zone.coordinates)) {
                isInNoFlyZone = true;
                break;
            }
        }
        return isInNoFlyZone;
    }

    /**
     * This method checks if the movement of the drone will cut across any of the corners of the noFlyZones
     * This uses the Line2D class to check if any intersecting happens.
     * We check for all adjacent coordinate points of each of the noFlyZones.
     * For each adjacent coordinate point, we create a line and check if the drone movement path line will intersect
     * that line.
     * @param currentPosition A LngLat point for the current position of the drone
     * @param testPosition A LngLat point for the test position of the drone
     * @param noFlyZones An array of NoFlyZoneCoordinates where each element of the array has their own double array of coordinates for that zone
     * @return boolean if the test LngLat position line with current position will intersect or not
     */
    public Boolean cutsNoFlyZone(LngLat currentPosition, LngLat testPosition, NoFlyZoneCoordinates[] noFlyZones) {
        // line of the drone test movement
        Line2D line = new Line2D.Double(currentPosition.lng, currentPosition.lat, testPosition.lng, testPosition.lat);
        // create an arrayList of all the coordinates of each noFlyZone polygons
        ArrayList<double[][]> noFlyZoneCoordinates = new ArrayList<>();
        for (NoFlyZoneCoordinates noFlyZone : noFlyZones) {
            noFlyZoneCoordinates.add(noFlyZone.coordinates);
        }
        // for each noFlyZone polygon coordinates
        for (double[][] noFlyZoneCoordinate: noFlyZoneCoordinates) {
            // for all the adjacent coordinates arrays in each noFlyZone (include those that wraps around)
            for (int i = 0, j = noFlyZoneCoordinate.length-1; i < noFlyZoneCoordinate.length; j = i++) {
                // create a new line to be tested
                Line2D noFlyZoneLine = new Line2D.Double(noFlyZoneCoordinate[i][0],noFlyZoneCoordinate[i][1],noFlyZoneCoordinate[j][0],noFlyZoneCoordinate[j][1]);
                // if the line does intersect
                if (line.intersectsLine(noFlyZoneLine)) {
                    return true;
                }
            }
        }return false;
    }

    /**
     * This uses a method called ray tracing to determine if a point is within a polygon or not.
     * It creates a ray that starts from the point and stretches out to infinity along the x-axis and
     * counts how many times the ray passes though an edge, if that number is odd, then the point is within the polygon, otherwise it is not.
     * This method allows us to check for convex polygons and well as rectangular ones.
     * This method is not original and is heavily inspired by a stackoverflow forum: <a href="https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon">https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon</a>
     * accessed: online 13th October 2022.
     * @return a boolean, true if the point is indeed within the polygon(central area), and false if it isn't
     */

    private Boolean isInPolygon(double[][] listOfPoints) {
        int i;
        int j;

        // this is our checker for number of edges passed, each time and edge is passed through, the result will flip
        boolean result = false;

        // for loop that iterates through the list of points of the area boundaries, j starts at the end of the array
        // but changes to being i - 1 for the rest of the for loop
        for (i = 0, j = listOfPoints.length - 1; i < listOfPoints.length; j = i++) {

            // Checks to see if the point is in between the two corner points and to see the point of intersection is to the left or the right of the
            // test point, which means we are required to find the x coordinate of the intersection
            if ((listOfPoints[i][1] > lat) != (listOfPoints[j][1] > lat) &&
                    (lng < (listOfPoints[j][0] - listOfPoints[i][0]) * (lat - listOfPoints[i][1]) / (listOfPoints[j][1]-listOfPoints[i][1]) + listOfPoints[i][0])) {
                // flip result whenever we pass an intersection
                result = !result;
            }
        }

        return result;
    }
}
