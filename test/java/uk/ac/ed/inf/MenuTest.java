package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {
    @Test
    void testForStorage() {
        Menu testMenu = new Menu();
        testMenu.priceInPence = 6794;
        testMenu.name = "mama mia";
        assertEquals(6794, testMenu.priceInPence);
    }
}