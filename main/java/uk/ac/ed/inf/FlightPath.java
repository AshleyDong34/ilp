package uk.ac.ed.inf;

/**
 * This class stores the necessary information for each of the drone flight moves
 */
public class FlightPath {

    public String orderNo;
    public double fromLongitude;
    public double fromLatitude;
    public CompassDegrees angle;
    public double toLongitude;
    public double toLatitude;
    public long nanoSeconds;

    /**
     * The constructor method for this class and takes in the necessary variables and sets up the instance variables
     * @param orderNo the current delivery orderNo
     * @param fromLongitude the drone from Longitude
     * @param fromLatitude  the drone from Latitude
     * @param angle the drone CompassDegree taken
     * @param toLongitude the resulting Longitude
     * @param toLatitude the resulting Latitude
     * @param nanoSeconds the System tick run time
     */
    public FlightPath(String orderNo, double fromLongitude, double fromLatitude, CompassDegrees angle, double toLongitude, double toLatitude, long nanoSeconds) {
        this.orderNo = orderNo;
        this.fromLongitude = fromLongitude;
        this.fromLatitude = fromLatitude;
        this.angle = angle;
        this.toLongitude = toLongitude;
        this.toLatitude = toLatitude;
        this.nanoSeconds = nanoSeconds;
    }
}
