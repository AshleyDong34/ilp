package uk.ac.ed.inf;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is used to write the GeoJSON files
 * needed as part of the specification
 */
public class GeoJSONWriter
{

    /**
     * this method writes the data for a given drone flight plan
     * for delivering orders for a given user date to the appropriate GeoJSON file
     * @param flightPaths the arraylist of FlightPath object containing the flight plan
     *              to be written
     */
    public void writePathToGeoJSON(ArrayList<FlightPath> flightPaths)
    {
        // the directory path to the user path
        String path = System.getProperty("user.dir");
        File test = new File(path);
        // get to the ilp file path
        while (!test.getName().equals("ilp")) {
            test = test.getParentFile();
        }
        path = test.getAbsolutePath()  + "/resultfiles";
        // get all the necessary information from the arrayList of FlightPath variable
        List<LngLat> finalCoordinates = new ArrayList<>();
        for (FlightPath flightPath: flightPaths) {
            finalCoordinates.add(new LngLat(flightPath.fromLongitude, flightPath.fromLatitude));
        }
        // get the user input date
        String fileDate = BaseAddressAndDate.getDate();
        try
        {
            // set up all the data for GeoJSON format
            ArrayList<Point> pointsList = new ArrayList<>();
            for (LngLat point : finalCoordinates) {
                pointsList.add(Point.fromLngLat(point.lng(), point.lat()));
            }
            LineString line = LineString.fromLngLats(pointsList);
            Feature feature = Feature.fromGeometry(line);
            FeatureCollection collection = FeatureCollection.fromFeature(feature);
            String fileName = "drone-" + fileDate + ".geojson";
            // and write it to the appropriate file

            Files.createDirectories(Path.of(path));

            FileWriter file = new FileWriter(path + "/" + fileName);
            file.write(collection.toJson());
            file.close();
        }
        catch (IOException e)
        {
            System.err.println("Exception occurred: \n" + e);
            System.exit(1);
  }
}

}