package uk.ac.ed.inf;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;

/**
 * This class retrieves all the no-fly-zone coordinates from the rest server,
 * as an array of NoFlyZoneCoordinates class
 */
public class NoFlyZone {
    private static NoFlyZone noFlyZone;
    private static final String noFlyZonesBaseUrl = "https://ilp-rest.azurewebsites.net/";
    private static final String noFlyZonesRestUrl = "noFlyZones/";
    private static NoFlyZoneCoordinates[] noFlyZonesCoordinates;
    private NoFlyZone() {
        setNoFlyZonesCoordinates(noFlyZonesBaseUrl);
    }

    /**
     * checks to see if an object of this class has already been initialised, if not, then create the object.
     * @return the only instance of the object from this class
     */
    public static NoFlyZone getNoFlyZone() {
        if (noFlyZone == null) {
            noFlyZone = new NoFlyZone();
        }
        return noFlyZone;
    }

    /**
     * This method uses the declared final url string centralAreaUrl to read and deserialize the json string to java class
     * of type Coordinates[]. This is then set to the static centralAreaCoordinates
     */
    private static void setNoFlyZonesCoordinates(String baseUrl){
        try {
            if (!baseUrl.endsWith ("/")) {
                baseUrl += "/";
            }
            //creates a pointer to the resource on the internet
            URL url = new URL(baseUrl+noFlyZonesRestUrl);
            //
            noFlyZonesCoordinates = (new ObjectMapper()).readValue(url, (NoFlyZoneCoordinates[].class));
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * A getter to retrieve the coordinates
     * @return an array of the coordinate object that contains the central area coordinate information
     */
    public static NoFlyZoneCoordinates[] getCoordinates() {
        return noFlyZonesCoordinates;
    }

}
