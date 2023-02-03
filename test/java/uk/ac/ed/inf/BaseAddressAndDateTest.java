package uk.ac.ed.inf;

import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BaseAddressAndDateTest {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    @DisplayName("Test for error messages for incorrect urls")
    void setValues() {
        // will give error if invalid url is given
        BaseAddressAndDate.setValues("bruh", "2023-01-23");
        assertEquals("Invalid URL", errContent.toString());
    }

    @Test
    void testForInvalidYear() {
        // check if invalid date is given - year
        BaseAddressAndDate.setValues("https://ilp-rest.azurewebsites.net/", "2025-01-01");
        assertEquals("Invalid delivery date", errContent.toString());
    }

    @Test
    void testForInvalidMonth() {
        // check if invalid date is given - month
        BaseAddressAndDate.setValues("https://ilp-rest.azurewebsites.net/", "2023-00-01");
        assertEquals("Invalid delivery date", errContent.toString());
    }

    @Test
    void testForInvalidDate() {
        // check if invalid date is given - date
        BaseAddressAndDate.setValues("https://ilp-rest.azurewebsites.net/", "2023-02-29");
        assertEquals("Invalid delivery date", errContent.toString());
    }


    @Test
    void getBaseAddress() {
        BaseAddressAndDate.setValues("https://ilp-rest.azurewebsites.net/", "2023-01-23");
        // checks if the base url is correct based on two correct inputs
        String url = BaseAddressAndDate.getBaseAddress();
        assertEquals("https://ilp-rest.azurewebsites.net/", url);
        BaseAddressAndDate.setValues("https://ilp-rest.azurewebsites.net", "2023-01-01");
        String url2 = BaseAddressAndDate.getBaseAddress();
        assertEquals("https://ilp-rest.azurewebsites.net/", url2);
    }

    @Test
    void getDate() {
        // see fi correct date is given back;
        BaseAddressAndDate.setValues("https://ilp-rest.azurewebsites.net/", "2023-01-23");
        String date = BaseAddressAndDate.getDate();
        assertEquals("2023-01-23", date);
    }
}