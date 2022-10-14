package uk.ac.ed.inf;

import com . fasterxml . jackson . databind . ObjectMapper ;
import java . io . IOException ;
import java . net .URL;

/**
 * The CentralArea class is a singleton class, where there is only ever one instance of this class.
 * It contains the setCentralCoordinates() method which is called when an instance of this object is initialised,
 * and a method to get the coordinates from the object.
 */
public class CentralArea {
    private static CentralArea centralArea;
    private static final String centralAreaBaseUrl = "https://ilp-rest.azurewebsites.net/";
    private static final String centralAreaRestUrl = "centralArea/";
    private static Coordinates[] centralAreaCoordinates;
    private CentralArea() {
        setCentralCoordinates(centralAreaBaseUrl);
    }

    /**
     * checks to see if an object of this class has already been initialised, if not, then create the object.
     * @return the only instance of the object from this class
     */
    public static CentralArea getCentralArea() {
        if (centralArea == null) {
            centralArea = new CentralArea();
        }
        return centralArea;
    }

    /**
     * This method uses the declared final url string centralAreaUrl to read and deserialize the json string to java class
     * of type Coordinates[]. This is then set to the static centralAreaCoordinates
     */
    private static void setCentralCoordinates(String baseUrl){
        try {
            if (!baseUrl.endsWith ("/")) {
                baseUrl += "/";
            }
            //creates a pointer to the resource on the internet
            URL url = new URL(baseUrl+centralAreaRestUrl);
            //
            centralAreaCoordinates = (new ObjectMapper()).readValue(url, Coordinates[].class);
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * A getter to retrieve the coordinates
     * @return an array of the coordinate object that contains the central area coordinate information
     */
    public static Coordinates[] getCoordinates() {
        return centralAreaCoordinates;
    }

}
