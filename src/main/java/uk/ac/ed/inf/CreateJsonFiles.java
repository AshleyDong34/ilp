package uk.ac.ed.inf;
import java.io.IOException;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;

/**
 * This class converts to the equivalent json files for the Arraylist for Deliveries and FlightPath.
 * It also finds the right folder and path to place the newly created json files.
 * We use jackson to convert to json and the date and the Java.io.File import to create the file name and contents
 */
public class CreateJsonFiles {

    /**
     * This method finds the correct path to create the equivalent json file of the ArrayLists for the flightPaths
     * and the deliveries. It finds the correct path, the root, and creates the json file based on the date provided
     * @param deliveries is the final array list of completed Deliveries to be converted to json
     * @param flightPaths is the final array list of completed FlightPath to be converted to json
     * @param date is the user input date where the orders for the deliveries and flightpath come from.
     */
    public static void createJson(ArrayList<Deliveries> deliveries, ArrayList<FlightPath> flightPaths, String date) {

        ObjectMapper mapper = new ObjectMapper();
        // find the current path user position
        String path = System.getProperty("user.dir");
        //create a new file based on the path
        File test = new File(path);
        // go back in the path until we find the folder named ilp
        while (!test.getName().equals("ilp")) {
            test = test.getParentFile();
        }
        // the path is updated to the correct folder for the files to be made in
        path = test.getAbsolutePath()  + "/resultfiles";
        try {
            // make the json files based on the ArrayLists
            mapper.writeValue(new File(path +"/deliveries-"+ date+".json"), deliveries);
            mapper.writeValue(new File(path + "/flightPath-"+ date+".json"), flightPaths);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
