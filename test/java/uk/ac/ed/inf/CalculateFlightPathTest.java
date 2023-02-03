package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.*;

class CalculateFlightPathTest {

    CalculateFlightPath calculatedFlightPath;


    @BeforeEach
    void setUp() {
        BaseAddressAndDate.setValues("https://ilp-rest.azurewebsites.net/", "2023-02-04");
        OrderDetails[] orders = OrderDetails.getOrderDetails("2023-02-04", "https://ilp-rest.azurewebsites.net/");
        Deliveries[] inCompleteDeliveries = Deliveries.getDeliveries(orders);
        calculatedFlightPath = new CalculateFlightPath(inCompleteDeliveries, orders);
    }
    @Test
    @DisplayName("Check if the number of hovers is two per delivery")
    void flightPathNumber() {
        ListIterator<FlightPath> iterator = calculatedFlightPath.flightPaths.listIterator();
        ListIterator<Deliveries> iterator2 = calculatedFlightPath.completeDeliveries.listIterator();
        int i = 0;
        int j = 0;
        while (iterator.hasNext()){
            if(iterator.next().angle == null) {
                i++;
            }
        }
        while (iterator2.hasNext()) {
            if (iterator2.next().outcome == OrderOutcome.Delivered) {
                j++;
            }
        }
        assertEquals(j, i/2);
    }

    @Test
    @DisplayName("checks all the deliveries were delivered by closest distance")
    void testDistanceSort() {
        ListIterator<FlightPath> iterator = calculatedFlightPath.flightPaths.listIterator();
        ListIterator<Deliveries> iterator2 = calculatedFlightPath.completeDeliveries.listIterator();
        int shortestNumberOfMoves = 0;
        int numberOfMoves = 0;
        boolean flag = true;
        double lng;
        double lat;
        while (iterator2.hasNext()) {
            if (iterator2.next().outcome == OrderOutcome.Delivered) {
                while (iterator.hasNext()) {
                    lng = iterator.next().fromLongitude;
                    lat = iterator.next().fromLatitude;

                    if (lng == -3.186874 && lat == 55.944494) {
                        if (numberOfMoves >= shortestNumberOfMoves) {
                            shortestNumberOfMoves = numberOfMoves;
                        } else {
                            flag = false;
                            break;
                        }
                        numberOfMoves = 0;
                    } else{
                        numberOfMoves++;
                    }
                }
            } else {
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    void testForAtLeastOneDelivered () {
        ListIterator<Deliveries> iterator2 = calculatedFlightPath.completeDeliveries.listIterator();
        boolean flag = false;
        while (iterator2.hasNext()) {
            if (iterator2.next().outcome == OrderOutcome.Delivered) {
                flag = true;
                break;
            }
        }
        assertTrue(flag);
        assertTrue(calculatedFlightPath.flightPaths.size() > 0);
    }

}