package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoFlyZoneCoordinatesTest {
    @Test
    void testForStorage() {
        NoFlyZoneCoordinates test = new NoFlyZoneCoordinates();

        test.coordinates = new double[][] {{46, 6524}, {57425,3724}};
        test.name = "Peggie Wei";

        assertEquals("Peggie Wei", test.name);
    }

}