package uk.ac.ed.inf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoFlyZoneTest {

    @Test
    @DisplayName("checks for the class type")
    void getNoFlyZone() {
        NoFlyZone testZones = NoFlyZone.getNoFlyZone();
        assertEquals(NoFlyZone.class, testZones.getClass());
    }

    @Test
    @DisplayName("checks the number of no fly zones")
    void getCoordinates() {
        NoFlyZoneCoordinates[] testCoordinates = NoFlyZone.getCoordinates();
        assertEquals(4, testCoordinates.length);
    }
}