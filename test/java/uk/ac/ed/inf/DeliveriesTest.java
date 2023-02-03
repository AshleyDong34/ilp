package uk.ac.ed.inf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/*
    There is only one public method that uses all the private methods provided
    All testing will be about creating valid and invalid order details and to check if the program passes all the unit tests we created.
 */


class DeliveriesTest {


    @BeforeEach
    void setUp() {
        String date = "2023-02-04";
        String baseURL = "https://ilp-rest.azurewebsites.net/";
        BaseAddressAndDate.setValues(baseURL, date);
    }
    // find if the number of invalid and valid orders correspond to what is expected when tested on all orders
    @Test
    void testOnAllOrders() {
        // checks for all orders and checks if the number of cases for each orderOutcome is correct.
        OrderDetails[] orders = OrderDetails.getOrderDetails("","https://ilp-rest.azurewebsites.net/");
        Deliveries[] inCompleteDeliveries = Deliveries.getDeliveries(orders);

        int validNotDelivered = 0, invalidExpiryDate = 0, invalidCardNumber = 0, invalidCvv = 0, invalidTotal = 0, invalidPizzaNotDefined = 0, invalidPizzaCount = 0, invalidPizzaCombinationMultipleSuppliers = 0;

        for (Deliveries order: inCompleteDeliveries) {
            switch (order.outcome) {
                case ValidButNotDelivered -> validNotDelivered++;
                case InvalidCvv -> invalidCvv++;
                case InvalidTotal -> invalidTotal++;
                case InvalidCardNumber -> invalidCardNumber++;
                case InvalidPizzaCount -> invalidPizzaCount++;
                case InvalidExpiryDate -> invalidExpiryDate++;
                case InvalidPizzaCombinationMultipleSuppliers -> invalidPizzaCombinationMultipleSuppliers++;
                case InvalidPizzaNotDefined -> invalidPizzaNotDefined++;
            }
        }

        assertEquals(6000, validNotDelivered);
        assertEquals(150, invalidCardNumber);
        assertEquals(150, invalidCvv);
        assertEquals(150, invalidExpiryDate);
        assertEquals(150, invalidPizzaCount);
        assertEquals(150, invalidTotal);
        assertEquals(150, invalidPizzaCombinationMultipleSuppliers);
        assertEquals(150, invalidPizzaNotDefined);
    }
    //checks if we can pick up different ways that the card number is incorrect
    @Test
    void testOnInvalidCardNumbers() {


        OrderDetails[] testCases = new OrderDetails[3];

        OrderDetails testWrongCardNumber = new OrderDetails();
        testWrongCardNumber.orderNo = "1AFFE082";
        testWrongCardNumber.orderDate = "2023-01-01";
        testWrongCardNumber.customer = "Gilberto Handshoe";
        testWrongCardNumber.creditCardNumber = "2402902";
        testWrongCardNumber.creditCardExpiry = "04/28";
        testWrongCardNumber.cvv = "922";
        testWrongCardNumber.priceTotalInPence = 2400;
        testWrongCardNumber.orderItems = new String[]{"Super Cheese","All Shrooms"};
        testCases[0] = testWrongCardNumber;

        OrderDetails testWrongCardNumberWrongDigits = new OrderDetails();
        testWrongCardNumberWrongDigits.orderNo = "1AFFE082";
        testWrongCardNumberWrongDigits.orderDate = "2023-01-01";
        testWrongCardNumberWrongDigits.customer = "Gilberto Handshoe";
        testWrongCardNumberWrongDigits.creditCardNumber = "2402902743821077";
        testWrongCardNumberWrongDigits.creditCardExpiry = "04/28";
        testWrongCardNumberWrongDigits.cvv = "922";
        testWrongCardNumberWrongDigits.priceTotalInPence = 2400;
        testWrongCardNumberWrongDigits.orderItems = new String[]{"Super Cheese","All Shrooms"};
        testCases[1] = testWrongCardNumberWrongDigits;

        OrderDetails testWrongCardNumberCorrect = new OrderDetails();
        testWrongCardNumberCorrect.orderNo = "1AFFE082";
        testWrongCardNumberCorrect.orderDate = "2023-01-01";
        testWrongCardNumberCorrect.customer = "Gilberto Handshoe";
        testWrongCardNumberCorrect.creditCardNumber = "4306685979511715";
        testWrongCardNumberCorrect.creditCardExpiry = "04/28";
        testWrongCardNumberCorrect.cvv = "922";
        testWrongCardNumberCorrect.priceTotalInPence = 2400;
        testWrongCardNumberCorrect.orderItems = new String[]{"Super Cheese","All Shrooms"};
        testCases[2] = testWrongCardNumberCorrect;

        OrderOutcome[] testExpectedDetails = new OrderOutcome[3];
        testExpectedDetails[0] = OrderOutcome.InvalidCardNumber;
        testExpectedDetails[1] = OrderOutcome.InvalidCardNumber;
        testExpectedDetails[2] = OrderOutcome.ValidButNotDelivered;

        Deliveries[] testDeliveries = Deliveries.getDeliveries(testCases);

        assertEquals(testExpectedDetails[0], testDeliveries[0].outcome);
        assertEquals(testExpectedDetails[1], testDeliveries[1].outcome);
        assertEquals(testExpectedDetails[2], testDeliveries[2].outcome);

    }

    // checks to see if the method pics up on when the cvv number is incorrect
    @Test
    void testOnCvv() {

        OrderDetails[] testCases = new OrderDetails[3];

        OrderDetails testWrongCvv = new OrderDetails();
        testWrongCvv.orderNo = "67D62E36";
        testWrongCvv.orderDate = "2023-01-01";
        testWrongCvv.customer = "Mason Sporman";
        testWrongCvv.creditCardNumber = "4306685979511715";
        testWrongCvv.creditCardExpiry = "05/23";
        testWrongCvv.cvv = "8192";
        testWrongCvv.priceTotalInPence = 2400;
        testWrongCvv.orderItems = new String[]{"Proper Pizza","Pineapple & Ham & Cheese"};
        testCases[0] = testWrongCvv;

        OrderDetails testWrongCvv2 = new OrderDetails();
        testWrongCvv2.orderNo = "67D62E36";
        testWrongCvv2.orderDate = "2023-01-01";
        testWrongCvv2.customer = "Mason Sporman";
        testWrongCvv2.creditCardNumber = "4306685979511715";
        testWrongCvv2.creditCardExpiry = "05/23";
        testWrongCvv2.cvv = "8";
        testWrongCvv2.priceTotalInPence = 2400;
        testWrongCvv2.orderItems = new String[]{"Proper Pizza","Pineapple & Ham & Cheese"};
        testCases[1] = testWrongCvv2;

        OrderDetails testCorrectCvv = new OrderDetails();
        testCorrectCvv.orderNo = "67D62E36";
        testCorrectCvv.orderDate = "2023-01-01";
        testCorrectCvv.customer = "Mason Sporman";
        testCorrectCvv.creditCardNumber = "4306685979511715";
        testCorrectCvv.creditCardExpiry = "05/23";
        testCorrectCvv.cvv = "819";
        testCorrectCvv.priceTotalInPence = 2400;
        testCorrectCvv.orderItems = new String[]{"Proper Pizza","Pineapple & Ham & Cheese"};
        testCases[2] = testCorrectCvv;

        Deliveries[] testDeliveries = Deliveries.getDeliveries(testCases);

        OrderOutcome[] testExpectedDetails = new OrderOutcome[3];
        testExpectedDetails[0] = OrderOutcome.InvalidCvv;
        testExpectedDetails[1] = OrderOutcome.InvalidCvv;
        testExpectedDetails[2] = OrderOutcome.ValidButNotDelivered;

        assertEquals(testExpectedDetails[0], testDeliveries[0].outcome);
        assertEquals(testExpectedDetails[1], testDeliveries[1].outcome);
        assertEquals(testExpectedDetails[2], testDeliveries[2].outcome);

    }

    // checks to see if the order count is not over the limit test
    @Test
    void testOnOrderCount() {
        OrderDetails[] testCases = new OrderDetails[2];

        OrderDetails testWrongPizzaCount = new OrderDetails();
        testWrongPizzaCount.orderNo = "67D62E36";
        testWrongPizzaCount.orderDate = "2023-01-01";
        testWrongPizzaCount.customer = "Mason Sporman";
        testWrongPizzaCount.creditCardNumber = "4306685979511715";
        testWrongPizzaCount.creditCardExpiry = "05/23";
        testWrongPizzaCount.cvv = "812";
        testWrongPizzaCount.priceTotalInPence = 2400;
        testWrongPizzaCount.orderItems = new String[]{"Proper Pizza","Pineapple & Ham & Cheese","Proper Pizza","Pineapple & Ham & Cheese","Proper Pizza","Pineapple & Ham & Cheese","Proper Pizza","Pineapple & Ham & Cheese","Proper Pizza","Pineapple & Ham & Cheese"};
        testCases[0] = testWrongPizzaCount;

        OrderDetails testCorrectPizzaCount = new OrderDetails();
        testCorrectPizzaCount.orderNo = "67D62E36";
        testCorrectPizzaCount.orderDate = "2023-01-01";
        testCorrectPizzaCount.customer = "Mason Sporman";
        testCorrectPizzaCount.creditCardNumber = "4306685979511715";
        testCorrectPizzaCount.creditCardExpiry = "05/23";
        testCorrectPizzaCount.cvv = "819";
        testCorrectPizzaCount.priceTotalInPence = 2400;
        testCorrectPizzaCount.orderItems = new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"};
        testCases[1] = testCorrectPizzaCount;

        Deliveries[] testDeliveries = Deliveries.getDeliveries(testCases);

        OrderOutcome[] testExpectedDetails = new OrderOutcome[2];
        testExpectedDetails[0] = OrderOutcome.InvalidPizzaCount;
        testExpectedDetails[1] = OrderOutcome.ValidButNotDelivered;

        assertEquals(testExpectedDetails[0], testDeliveries[0].outcome);
        assertEquals(testExpectedDetails[1], testDeliveries[1].outcome);
        }

    @Test
    //checks to see if the validation checks and separates the invalid pizza names and the multiple pizzas from different suppliers
    void testOnValidPizzas() {

        OrderDetails[] testCases = new OrderDetails[4];

        OrderDetails testWrongPizzaCount = new OrderDetails();
        testWrongPizzaCount.orderNo = "67D62E36";
        testWrongPizzaCount.orderDate = "2023-01-01";
        testWrongPizzaCount.customer = "Mason Sporman";
        testWrongPizzaCount.creditCardNumber = "4306685979511715";
        testWrongPizzaCount.creditCardExpiry = "05/23";
        testWrongPizzaCount.cvv = "812";
        testWrongPizzaCount.priceTotalInPence = 2400;
        testWrongPizzaCount.orderItems = new String[]{"Proper Pizza","nice piazza"};
        testCases[0] = testWrongPizzaCount;

        OrderDetails testCorrectPizzaOrder = new OrderDetails();
        testCorrectPizzaOrder.orderNo = "67D62E36";
        testCorrectPizzaOrder.orderDate = "2023-01-01";
        testCorrectPizzaOrder.customer = "Mason Sporman";
        testCorrectPizzaOrder.creditCardNumber = "4306685979511715";
        testCorrectPizzaOrder.creditCardExpiry = "05/23";
        testCorrectPizzaOrder.cvv = "819";
        testCorrectPizzaOrder.priceTotalInPence = 2400;
        testCorrectPizzaOrder.orderItems = new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"};
        testCases[1] = testCorrectPizzaOrder;

        OrderDetails testMultiplePizzaSuppliers = new OrderDetails();
        testMultiplePizzaSuppliers.orderNo = "67D62E36";
        testMultiplePizzaSuppliers.orderDate = "2023-01-01";
        testMultiplePizzaSuppliers.customer = "Mason Sporman";
        testMultiplePizzaSuppliers.creditCardNumber = "4306685979511715";
        testMultiplePizzaSuppliers.creditCardExpiry = "05/23";
        testMultiplePizzaSuppliers.cvv = "812";
        testMultiplePizzaSuppliers.priceTotalInPence = 2400;
        testMultiplePizzaSuppliers.orderItems = new String[]{"Margarita","Calzone","Meat Lover"};
        testCases[2] = testMultiplePizzaSuppliers;

        OrderDetails testMultipleSamePizzas = new OrderDetails();
        testMultipleSamePizzas.orderNo = "67D62E36";
        testMultipleSamePizzas.orderDate = "2023-01-01";
        testMultipleSamePizzas.customer = "Mason Sporman";
        testMultipleSamePizzas.creditCardNumber = "4306685979511715";
        testMultipleSamePizzas.creditCardExpiry = "05/23";
        testMultipleSamePizzas.cvv = "819";
        testMultipleSamePizzas.priceTotalInPence = 3800;
        testMultipleSamePizzas.orderItems = new String[]{"Proper Pizza", "Pineapple & Ham & Cheese", "Proper Pizza"};
        testCases[3] = testMultipleSamePizzas;

        Deliveries[] testDeliveries = Deliveries.getDeliveries(testCases);

        OrderOutcome[] testExpectedDetails = new OrderOutcome[4];
        testExpectedDetails[0] = OrderOutcome.InvalidPizzaNotDefined;
        testExpectedDetails[1] = OrderOutcome.ValidButNotDelivered;
        testExpectedDetails[2] = OrderOutcome.InvalidPizzaCombinationMultipleSuppliers;
        testExpectedDetails[3] = OrderOutcome.ValidButNotDelivered;

        assertEquals(testExpectedDetails[0], testDeliveries[0].outcome);
        assertEquals(testExpectedDetails[1], testDeliveries[1].outcome);
        assertEquals(testExpectedDetails[2], testDeliveries[2].outcome);
        assertEquals(testExpectedDetails[3], testDeliveries[3].outcome);
    }

    @Test
    // checks to see if the total cost is different from expected.
    void testInvalidTotal() {

        OrderDetails[] testCases = new OrderDetails[2];

        OrderDetails testWrongCost = new OrderDetails();
        testWrongCost.orderNo = "67D62E36";
        testWrongCost.orderDate = "2023-01-01";
        testWrongCost.customer = "Mason Sporman";
        testWrongCost.creditCardNumber = "4306685979511715";
        testWrongCost.creditCardExpiry = "05/23";
        testWrongCost.cvv = "812";
        testWrongCost.priceTotalInPence = 0;
        testWrongCost.orderItems = new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"};
        testCases[0] = testWrongCost;

        OrderDetails testCorrectTotal = new OrderDetails();
        testCorrectTotal.orderNo = "67D62E36";
        testCorrectTotal.orderDate = "2023-01-01";
        testCorrectTotal.customer = "Mason Sporman";
        testCorrectTotal.creditCardNumber = "4306685979511715";
        testCorrectTotal.creditCardExpiry = "05/23";
        testCorrectTotal.cvv = "819";
        testCorrectTotal.priceTotalInPence = 2400;
        testCorrectTotal.orderItems = new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"};
        testCases[1] = testCorrectTotal;

        Deliveries[] testDeliveries = Deliveries.getDeliveries(testCases);

        OrderOutcome[] testExpectedDetails = new OrderOutcome[2];
        testExpectedDetails[0] = OrderOutcome.InvalidTotal;
        testExpectedDetails[1] = OrderOutcome.ValidButNotDelivered;

        assertEquals(testExpectedDetails[0], testDeliveries[0].outcome);
        assertEquals(testExpectedDetails[1], testDeliveries[1].outcome);
    }

    @Test
    // check for if the test will pick up on different ways of expiry date, regarding the order date and card expiry
    void testInvalidExpiry() {

        OrderDetails[] testCases = new OrderDetails[3];

        OrderDetails testYearExpired = new OrderDetails();
        testYearExpired.orderNo = "67D62E36";
        testYearExpired.orderDate = "2023-01-01";
        testYearExpired.customer = "Mason Sporman";
        testYearExpired.creditCardNumber = "4306685979511715";
        testYearExpired.creditCardExpiry = "05/20";
        testYearExpired.cvv = "812";
        testYearExpired.priceTotalInPence = 2400;
        testYearExpired.orderItems = new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"};
        testCases[0] = testYearExpired;

        OrderDetails testCorrectExpiry = new OrderDetails();
        testCorrectExpiry.orderNo = "67D62E36";
        testCorrectExpiry.orderDate = "2023-01-02";
        testCorrectExpiry.customer = "Mason Sporman";
        testCorrectExpiry.creditCardNumber = "4306685979511715";
        testCorrectExpiry.creditCardExpiry = "01/23";
        testCorrectExpiry.cvv = "819";
        testCorrectExpiry.priceTotalInPence = 2400;
        testCorrectExpiry.orderItems = new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"};
        testCases[1] = testCorrectExpiry;

        OrderDetails testWrongMonth = new OrderDetails();
        testWrongMonth.orderNo = "67D62E36";
        testWrongMonth.orderDate = "2023-02-01";
        testWrongMonth.customer = "Mason Sporman";
        testWrongMonth.creditCardNumber = "4306685979511715";
        testWrongMonth.creditCardExpiry = "01/23";
        testWrongMonth.cvv = "819";
        testWrongMonth.priceTotalInPence = 2400;
        testWrongMonth.orderItems = new String[]{"Proper Pizza", "Pineapple & Ham & Cheese"};
        testCases[2] = testWrongMonth;

        Deliveries[] testDeliveries = Deliveries.getDeliveries(testCases);

        OrderOutcome[] testExpectedDetails = new OrderOutcome[3];
        testExpectedDetails[0] = OrderOutcome.InvalidExpiryDate;
        testExpectedDetails[1] = OrderOutcome.ValidButNotDelivered;
        testExpectedDetails[2] = OrderOutcome.InvalidExpiryDate;

        assertEquals(testExpectedDetails[0], testDeliveries[0].outcome);
        assertEquals(testExpectedDetails[1], testDeliveries[1].outcome);
        assertEquals(testExpectedDetails[2], testDeliveries[2].outcome);
        }
    }