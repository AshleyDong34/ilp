package uk.ac.ed.inf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderDetailsTest {

    @Test
    // checks if the order details are correct from the rest server for a random day
    void getOrderDetails() {
        OrderDetails[] testOrders = OrderDetails.getOrderDetails("2023-01-23","https://ilp-rest.azurewebsites.net/");
        assertEquals("5B232E28",testOrders[0].orderNo);
    }
}