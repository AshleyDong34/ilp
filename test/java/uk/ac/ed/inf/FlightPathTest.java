package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightPathTest {

    @Test
    //checks to see if all recorde information in this class is stored correctly.
    void testForInitialisation() {
        FlightPath testFlightPath = new FlightPath("cheese", 3.12, 4.89, CompassDegrees.NE, 3.2, 5.435, 0);
        assertEquals(3.12,testFlightPath.fromLongitude);
    }

}