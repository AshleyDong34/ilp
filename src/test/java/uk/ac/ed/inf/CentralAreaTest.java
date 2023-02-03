package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CentralAreaTest {

    @BeforeEach
    void setUp() {
        BaseAddressAndDate.setValues("https://ilp-rest.azurewebsites.net/", "2023-02-04");
    }
    //checks if the central area instance returned is the correct one
    @Test
    void getCentralArea() {
        CentralArea centralArea = CentralArea.getCentralArea();
        assertEquals(CentralArea.class, centralArea.getClass());
    }
    // checks to see if the number of central area points received is from rest server is correct
    @Test
    void getCoordinates() {
        Coordinates[] centralCoords = CentralArea.getCoordinates();
        assertEquals(4, centralCoords.length);
    }
}