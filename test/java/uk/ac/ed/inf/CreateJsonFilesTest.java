package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CreateJsonFilesTest {

    // checks to see if the correct file names has been created in the right folder space
    @Test
    void createJson() {
        String date = "2023-02-05";
        String baseURL = "https://ilp-rest.azurewebsites.net/";

        BaseAddressAndDate.setValues(baseURL, date);
        OrderDetails[] orders = OrderDetails.getOrderDetails(date, baseURL);
        Deliveries[] inCompleteDeliveries = Deliveries.getDeliveries(orders);
        CalculateFlightPath flightPath = new CalculateFlightPath(inCompleteDeliveries, orders);
        // write the GeoJson file of flightpath using an object of that class
        GeoJSONWriter writer = new GeoJSONWriter();
        CreateJsonFiles.createJson(flightPath.completeDeliveries, flightPath.flightPaths, date);

        String path = System.getProperty("user.dir");
        File test = new File(path);
        // get to the ilp file path
        while (!test.getName().equals("ilp")) {
            test = test.getParentFile();
        }
        path = test.getAbsolutePath()  + "/resultfiles/" + "deliveries-" + date + ".json";
        String path2 = test.getAbsolutePath() + "/resultfiles/" + "flightPath-" + date + ".json";
        test = new File(path);
        File test2 = new File(path2);

        assertTrue(test.exists());
        assertTrue(test2.exists());
    }
}