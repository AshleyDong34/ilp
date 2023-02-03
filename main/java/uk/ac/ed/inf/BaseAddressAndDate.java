package uk.ac.ed.inf;

/**
 * This class contains a setter and getter methods for access to the date and the base address url Strings,
 * used so other classes can have easy access.
 * It also checks the user inputs and throws prints an error message if any input is invalid
 */
public class BaseAddressAndDate {
    private static String baseAddress;
    private static String date;

    /**
     * Sets the static String variables, date and baseAddress
     * @param address String input of the rest server base url
     * @param inputDate String input of the date we want to access the orders from the rest server
     */
    public static void setValues(String address, String inputDate) {
        checkInputs(inputDate, address);

        // checks if the base address contains the necessary slash, and adds on if it is not added.
        if (!address.endsWith ("/")) {
            address += "/";
        }
        // set the static variables from the arguments
        baseAddress = address;
        date = inputDate;
    }

    /**
     * getter that returns the set base address url
     * @return a String of the base address url
     */
    public static String getBaseAddress() {
        return baseAddress;
    }

    /**
     * getter that returns the set date
     * @return a String of the set date
     */
    public static String getDate() {
        return date;
    }

    /**
     * This method checks the user inputs to see if the date is within the required range
     * that the delivery orders are valid in the rest server and if the base address is correct or not
     * @param date the user input String
     * @param baseUrl the user input String of the rest server base url
     */
    private static void checkInputs(String date, String baseUrl) {

        // make integers of the year, month and date from the user input
        String[] splitDate = date.split("-");
        int year = Integer.parseInt(splitDate[0].trim());
        int month = Integer.parseInt(splitDate[1].trim());
        int day = Integer.parseInt(splitDate[2].trim());

        // check for invalid value in the date, month or year
        if ((year != 2023) || (month < 1) || (month > 5) || (isDateWrong(day, month))) {
            System.err.print("Invalid delivery date");
        }
        // check for if the url is correct
        else if (!baseUrl.equals("https://ilp-rest.azurewebsites.net/") && !baseUrl.equals("https://ilp-rest.azurewebsites.net")) {
            System.err.print("Invalid URL");
        }
    }

    /**
     * This method checks if the date and month are correct for each date, because
     * each month has a different number of dates
     * @param date integer type, between 1 and 31
     * @param month integer type, between 1 and 5
     * @return boolean true if date is wrong based on month and otherwise false
     */
    private static Boolean isDateWrong(int date, int month) {
        boolean flag = false;
        switch (month) {
            case 1, 3, 5 -> {
                if (date > 31) {
                    flag = true;
                }
            }
            case 2 -> {
                if (date > 28) {
                    flag = true;
                }
            }
            case 4 -> {
                if (date > 30) {
                    flag = true;
                }
            }
        }

        return flag;
    }
}
