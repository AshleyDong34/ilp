package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class GeoJSONWriterTest {

    // checks to see if the required file is stored and created at the right place, with the correct name.
    @Test
    void writePathToGeoJSON() {

        String date = "2023-02-05";
        String baseURL = "https://ilp-rest.azurewebsites.net/";

        BaseAddressAndDate.setValues(baseURL, date);
        OrderDetails[] orders = OrderDetails.getOrderDetails(date, baseURL);
        Deliveries[] inCompleteDeliveries = Deliveries.getDeliveries(orders);
        CalculateFlightPath flightPath = new CalculateFlightPath(inCompleteDeliveries, orders);
        // write the GeoJson file of flightpath using an object of that class
        GeoJSONWriter writer = new GeoJSONWriter();
        writer.writePathToGeoJSON(flightPath.flightPaths);

        String path = System.getProperty("user.dir");
        File test = new File(path);
        // get to the ilp file path
        while (!test.getName().equals("ilp")) {
            test = test.getParentFile();
        }
        path = test.getAbsolutePath()  + "/resultfiles/" + "drone-" + date + ".geojson";
        test = new File(path);


        assertTrue(test.exists());
    }
}