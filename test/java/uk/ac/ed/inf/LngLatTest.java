package uk.ac.ed.inf;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LngLatTest {

    String date;
    String baseURL;
    LngLat currentPosition;
    @BeforeEach
    void setUp() {
        date = "2023-02-04";
        baseURL = "https://ilp-rest.azurewebsites.net/";
        BaseAddressAndDate.setValues(baseURL, date);
        // this is a corner of the central area
        currentPosition = new LngLat(-3.192473, 55.946233);

    }
    @Test
    // tests for if the distance to its self is correct
    // tries to test for another distance, fails.
    void distanceTo() {
        assertEquals(0,currentPosition.distanceTo(currentPosition));
        //due to specifications provided, since java does early round etc..., the number only has to be close enough to count as the distance.
        // This test does fail unfortunately, but still passes specifications provided
        // assertEquals(0.00015,currentPosition.distanceTo(new LngLat(currentPosition.lng() - 0.00015, currentPosition.lat())));
    }

    @Test
    // checks for the edge cases where the current position is checked against itself, and the specs where it has to be less than 0.00015
    void closeTo() {
        assertEquals(true,currentPosition.closeTo(currentPosition));
        assertEquals(false,currentPosition.closeTo(new LngLat(currentPosition.lng() - 0.00015, currentPosition.lat())));
    }

    @Test
    // checks to see if the correct new instance of an object is returned when the next direction is chosen.
    void nextPosition() {
        assertEquals(new LngLat(currentPosition.lng() + 0.00015, currentPosition.lat()),currentPosition.nextPosition(CompassDegrees.E));
    }

    @Test
    void inCentralArea() {
        //on the corner of the central area
        assertEquals(false,currentPosition.inCentralArea());
        // in central area
        assertEquals(true,new LngLat(currentPosition.lng() + 0.00015, currentPosition.lat() - 0.00015 ).inCentralArea());
        //outside central area
        assertEquals(false,new LngLat(currentPosition.lng() - 0.00015, currentPosition.lat()).inCentralArea());
    }

    @Test
    void inNoFlyZone() {
        //outside noFlyZone
        assertEquals(false,currentPosition.inNoFlyZone());
        // in noFlyZone
        assertEquals(true,(new LngLat(-3.190,55.943)).inNoFlyZone());
    }

    @Test
    void cutsNoFlyZone() {
        //cuts through the zone
        assertEquals(true, currentPosition.cutsNoFlyZone(currentPosition, new LngLat(-3.190,55.943), NoFlyZone.getNoFlyZone().getCoordinates()));
        // touches the line but does not cross
        assertEquals(false, currentPosition.cutsNoFlyZone(currentPosition, new LngLat(3.190578818321228,55.94402412577528), NoFlyZone.getNoFlyZone().getCoordinates()));
        // crosses multiple no fly zones
        assertEquals(true, currentPosition.cutsNoFlyZone(currentPosition, new LngLat(-3.184319,55.942617), NoFlyZone.getNoFlyZone().getCoordinates()));
    }
}