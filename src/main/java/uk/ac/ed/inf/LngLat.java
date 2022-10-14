package uk.ac.ed.inf;

/**
 * This record class stores the coordinate points (longitude and latitude)
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

        double newLng = 0.00015*Math.cos(compassDegree.getCompassDegrees()) + lng;
        double newLat = 0.00015*Math.sin(compassDegree.getCompassDegrees()) + lat;

        return new LngLat(newLng,newLat);
    }

    /**
     * This uses a method called ray tracing to determine if a point is within a polygon(central area) or not.
     * It creates a ray that starts from the point and stretches out to infinity along the x-axis and
     * counts how many times the ray passes though an edge, if that number is odd, then the point is within the polygon, otherwise it is not.
     * This method allows us to check for convex polygons and well as rectangular ones.
     * This method is not original and is heavily inspired by a stackoverflow forum: <a href="https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon">https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon</a>
     * accessed: online 13th October 2022.
     * @return a boolean, true if the point is indeed within the polygon(central area), and false if it isn't
     */
    public Boolean inCentralArea() {
        int i;
        int j;

        Coordinates[] listOfPoints = CentralArea.getCentralArea().getCoordinates();

        // this is our checker for number of edges passed, each time and edge is passed through, the result will flip
        boolean result = false;

        // for loop that iterates through the list of points of the central area boundaries, j starts at the end of the array
        // but changes to being i - 1 for the rest of the for loop
        for (i = 0, j = listOfPoints.length - 1; i < listOfPoints.length; j = i++) {

            // Checks to see of the point is in between the two corner points and to see the point of intersection is to the left or the right of the
            // test point, which means we are required to find the x coordinate of the intersection
            if ((listOfPoints[i].latitude > lat) != (listOfPoints[j].latitude > lat) &&
                    (lng < (listOfPoints[j].longitude - listOfPoints[i].longitude) * (lat - listOfPoints[i].latitude) / (listOfPoints[j].latitude-listOfPoints[i].latitude) + listOfPoints[i].longitude)) {
                // flip result whenever we pass an intersection
                result = !result;
            }
        }
        return result;
    }
}
