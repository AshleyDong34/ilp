package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompassDegreesTest {
    // checks if we can receive the correct compass values assigned in the specs
    @Test
    void getCompassDegrees() {
        CompassDegrees test = CompassDegrees.NE;
        assertEquals(45, test.getCompassDegrees());
        assertEquals("N", CompassDegrees.N.name());
    }
}