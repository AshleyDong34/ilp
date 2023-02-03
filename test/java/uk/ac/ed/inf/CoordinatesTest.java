package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {
    @Test
    // test for if the variable of a coordinate instance stores the correct information for casting from json
    void testForStorage() {
        Coordinates testCoords = new Coordinates();
        testCoords.name = "hi there";
        testCoords.latitude = -51.525325;
        testCoords.longitude = -3.1823;
        assertEquals("hi there", testCoords.name);
    }
}