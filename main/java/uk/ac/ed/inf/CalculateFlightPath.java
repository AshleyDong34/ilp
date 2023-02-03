package uk.ac.ed.inf;


import java.util.ArrayList;
import java.lang.*;
import java.util.Arrays;

/**
 * This class creates an object using the constructor method that calculates the completed Arraylist of FlightPath and
 * ArrayList of Deliveries. The method to calculate the path is a greedy algorithm. It updates the incomplete deliveries
 * order outcome as the drone reaches the restaurant and back.
 * After finding the path to the restaurant, the drone will take the same path back to AppleTon tower to save computation time.
 * It contains private methods to find the opposite CompassDegree and a way to find the next best move with the greedy approach.
 */
public class CalculateFlightPath {

    static final double ATLng = -3.186874;
    static final double ATLat = 55.944494;

    public ArrayList<FlightPath> flightPaths;

    public ArrayList<Deliveries> completeDeliveries;

    /**
     * A constructor method to initialise the flightPaths and deliveries variable. It checks for valid delivery orders,
     * then finds the flight path for the valid orders until move runs out for the drone.
     * The path finding uses a greedy approach, and is only used for finding the way to the restaurant.
     * The path back to AT is just the opposite of the path taken to get to the restaurant.
     * Hover is also added whenever we reach the restaurant or AppleTon tower.
     * @param inCompleteDeliveries contains the incomplete orderOutcomes for all the orders for the day. The method will
     *                             try to deliver all the orders with the ValidButNotDelivered outcomes.
     * @param unsortedOrders contains the order details of the orders. Mainly used to get the pizza and then the restaurant the
     *               pizza is from.
     */
    public CalculateFlightPath(Deliveries[] inCompleteDeliveries, OrderDetails[] unsortedOrders) {

        // create AT lngLat coordinates
        LngLat ATCoordinates = new LngLat(ATLng, ATLat);
        // make the arraylist of FlightPath that will contain all the flight information of the drone.
        ArrayList<FlightPath> droneFlight = new ArrayList<>();
        // initialise the variables and counters needed for keeping track of the drone moves and delivery number
        int moves = 0;
        int i = 0;
        int movesToRestaurant;
        int movesForThisDelivery = 0;
        boolean hasLeftCentralArea = false;
        // get the noFlyZones information
        NoFlyZoneCoordinates[] noFlyZones = NoFlyZone.getNoFlyZone().getCoordinates();

        //get list of restaurants
        Restaurant[] restaurants = Restaurant.getRestaurantsFromRestServer(BaseAddressAndDate.getBaseAddress());
        //a wrapper class containing sorted orderDetails and sorted deliveries
        WrapperClass pair = sortByClosestDistance(unsortedOrders, inCompleteDeliveries, ATCoordinates, restaurants);
        OrderDetails[] orders = pair.orderDetails;
        inCompleteDeliveries = pair.deliveries;

        // try to deliver while we are not over or equal to drone moves, and we still have orders to be checked
        while (!(moves >= 2000) && i < inCompleteDeliveries.length) {
            movesForThisDelivery = 0;
            // if outcome is valid, we try to deliver
            if (inCompleteDeliveries[i].outcome == OrderOutcome.ValidButNotDelivered) {

                // hover over AT and add to flightPath
                FlightPath ATHover = new FlightPath(inCompleteDeliveries[i].orderNo, ATLng, ATLat, null, ATLng, ATLat, System.nanoTime());
                droneFlight.add(ATHover);
                //uses a move
                moves++;
                movesForThisDelivery++;
                //find the restaurant destination for this order
                Restaurant pickedRestaurant = Restaurant.findRestaurant(orders[i].orderItems);
                LngLat restaurantCoordinates = new LngLat(pickedRestaurant.longitude, pickedRestaurant.latitude);
                //create current coordinates
                LngLat currentPosition = ATCoordinates;
                // reset number of moves needed to get to restaurant
                movesToRestaurant = 0;
                //while we are not close to restaurant or moves have not yet ran out
                while (!restaurantCoordinates.closeTo(currentPosition)) {
                    //check if the drone has left the central area or not
                    if (!currentPosition.inCentralArea()) {
                        hasLeftCentralArea = true;
                    }
                    //find the next move based on previous move, current position and restaurant position and if drone has left central area or not
                    CompassDegrees angle = pickBestMove(currentPosition, restaurantCoordinates, droneFlight.get(droneFlight.size() - 1), noFlyZones, hasLeftCentralArea);
                    //create next position using new angle from current position
                    LngLat nextPosition = currentPosition.nextPosition(angle);
                    //add the move onto flight path
                    FlightPath nextMove = new FlightPath(inCompleteDeliveries[i].orderNo, currentPosition.lng(), currentPosition.lat(), angle, nextPosition.lng(), nextPosition.lat(), System.nanoTime());
                    droneFlight.add(nextMove);
                    moves++;
                    movesForThisDelivery++;
                    movesToRestaurant++;
                    //update current position
                    currentPosition = nextPosition;
                }

                //hover when close to restaurant, and add to flight path
                FlightPath RestaurantHover = new FlightPath(inCompleteDeliveries[i].orderNo, currentPosition.lng(), currentPosition.lat(), null, currentPosition.lng(), currentPosition.lat(), System.nanoTime());
                droneFlight.add(RestaurantHover);
                moves++;
                movesForThisDelivery++;
                int currentFlightPathSize = droneFlight.size();
                for (int j = 0; j < movesToRestaurant; j++) {

                    // find the right compass degree needed for the move backwards
                    CompassDegrees backToAT = oppositeDirection(droneFlight.get(currentFlightPathSize - 2 - j).angle);
                    // get the flightpath for the move to the restaurant
                    FlightPath droneMovement = droneFlight.get(currentFlightPathSize - 2 - j);
                    // create and add a new flightpath that is the opposite way
                    FlightPath moveBack = new FlightPath(inCompleteDeliveries[i].orderNo, droneMovement.toLongitude, droneMovement.toLatitude, backToAT, droneMovement.fromLongitude, droneMovement.toLatitude, System.nanoTime());
                    droneFlight.add(moveBack);
                    moves++;
                    movesForThisDelivery++;

                }
                //change the delivered order's OrderOutcome to Delivered
                inCompleteDeliveries[i].outcome = OrderOutcome.Delivered;
                //reset the drone to not have left the central area yet for the next delivery
                hasLeftCentralArea = false;
            }
            i++;
        }
        // once moves are over the limit of 2000
        if (moves > 2000) {
            // delete the flight paths for the delivery that was just simulated to be delivered but over 2000 moves
            droneFlight.subList((droneFlight.size() - movesForThisDelivery),(droneFlight.size())).clear();
            // change the outcome for that delivery back to be ValidButNotDelivered
            inCompleteDeliveries[i-1].outcome = OrderOutcome.ValidButNotDelivered;
            // add AtHover to deliveries again
            FlightPath ATHover = new FlightPath(inCompleteDeliveries[i-1].orderNo, ATLng, ATLat, null, ATLng, ATLat, System.nanoTime());
            droneFlight.add(ATHover);
            // delete the first dummy hover used for starting the loop
            droneFlight.remove(0);
        }

        // assign the calculated droneFLight and the completed deliveries to the object variables
        this.flightPaths = droneFlight;
        this.completeDeliveries = new ArrayList<>(Arrays.asList(inCompleteDeliveries));
    }

    /**
     * This method picks the best move from the available 16 compass directions and returns the corresponding CompassDegree
     * It picks the compass direction that is not the opposite direction to the previous move(move backwards), is the closest
     * to the destination, and does not cut or intersect the noFlyZones.
     * @param currentPosition A LngLat object of the drone current position
     * @param restaurantPosition A LngLat object of the selected restaurant position
     * @param lastFlight Last move instance of class FlightPath. Contains the details of the CompassDegrees last taken
     * @param noFlyZones An array of the NoFlyZoneCoordinates class. Which each contains the coordinates of the polygon of each noFlyZone
     * @return The best Compass degree to be taken by the drone for its next move.
     */
    private static CompassDegrees pickBestMove(LngLat currentPosition, LngLat restaurantPosition, FlightPath lastFlight, NoFlyZoneCoordinates[] noFlyZones, boolean hasLeftCentralArea) {

        //smallestDistance is current closest distance to the restaurant
        double smallestDistance = 1000000;
        //init the best move lng lat as null for now.
        CompassDegrees bestMove = null;
        //a lng lat for testing for smallest distance
        LngLat testPosition;
        //go through all the Compass Degrees
        for (CompassDegrees degrees: CompassDegrees.values()) {
            // only check if the angle is not the same as the previous move
            if (oppositeDirection(lastFlight.angle) != degrees) {
                // get lng lat for the compass degree we are testing
                testPosition = currentPosition.nextPosition(degrees);
                // once left centralArea, cannot go back when going to restaurant
                if (hasLeftCentralArea) {
                    // if the new distance is smaller than the previous smallest, not in a noFlyZone and does not cut through a noFlyZone
                    if (testPosition.distanceTo(restaurantPosition) < smallestDistance && !testPosition.inNoFlyZone() && !testPosition.cutsNoFlyZone(currentPosition, testPosition, noFlyZones) && !testPosition.inCentralArea()) {
                        // update the newest smallest distance and best CompassDegree choice
                        smallestDistance = testPosition.distanceTo(restaurantPosition);
                        bestMove = degrees;
                    }
                } else {
                    // if the new distance is smaller than the previous smallest, not in a noFlyZone and does not cut through a noFlyZone
                    if (testPosition.distanceTo(restaurantPosition) < smallestDistance && !testPosition.inNoFlyZone() && !testPosition.cutsNoFlyZone(currentPosition, testPosition, noFlyZones)) {
                        // update the newest smallest distance and best CompassDegree choice
                        smallestDistance = testPosition.distanceTo(restaurantPosition);
                        bestMove = degrees;
                    }
                }
            }
        }
        return bestMove;
    }

    /**
     * This method return the opposite direction compass degree.
     * Since the hover move requires the angle to be null, we need to account for if the argument is null or not.
     * If the entered angle is null then the opposite direction will still be null.
     * @param angle is of CompassDegrees enum class.
     * @return the CompassDegree that points in the exact opposite direction from the argument
     */
    private static CompassDegrees oppositeDirection(CompassDegrees angle) {
        // initialise the return variable as null
        CompassDegrees oppositeDegree = null;

        if (angle != null) {
            // using modulus to find the directly opposite direction CompassDegree
            double oppositeAngle = ((angle.getCompassDegrees()) + 180) % 360;
            // check for which CompassDegrees are the same as calculated the oppositeAngle
            for (CompassDegrees degree : CompassDegrees.values()) {
                if (degree.getCompassDegrees() == oppositeAngle) {
                    oppositeDegree = degree;
                    break;
                }
            }
        }
        return oppositeDegree;
    }

    /**
     * This method takes in unsorted orders, deliveries and list of restaurants and AT coordinates to sort all
     * orders and their corresponding deliveries by how close their chosen restaurant is to AppleTon Tower.
     * This uses a bubble sort
     * @param orders unsorted orders
     * @param deliveries unsorted deliveries
     * @param AT LngLat AT point
     * @param restaurants list of restaurants from the rest server
     * @return a wrapper class that contains both the sorted orders and the sorted deliveries
     */
    private static WrapperClass sortByClosestDistance(OrderDetails[] orders, Deliveries[] deliveries, LngLat AT, Restaurant[] restaurants) {

        for (int i = 0; i < orders.length; i++) {

            for (int j = i+1; j < orders.length; j++) {

                //find the LngLat of the two points being compared
                Restaurant restaurant1 = findRestaurant(restaurants, orders[i].orderItems);
                Restaurant restaurant2 = findRestaurant(restaurants, orders[j].orderItems);

                LngLat tempLngLat1 = new LngLat(restaurant1.longitude, restaurant1.latitude);
                LngLat tempLngLat2 = new LngLat(restaurant2.longitude, restaurant2.latitude);
                //create temporary variables
                OrderDetails temp;
                Deliveries temp2;
                //if the point1 is larger than point2, then switch around both orders and deliveries
                if (tempLngLat1.distanceTo(AT) > tempLngLat2.distanceTo(AT)) {
                    temp = orders[i];
                    orders[i] = orders[j];
                    orders[j] = temp;

                    temp2 = deliveries[i];
                    deliveries[i] = deliveries[j];
                    deliveries[j] = temp2;

                }
            }

        }
        return new WrapperClass(deliveries,orders);
    }


    /**
     * This method returns the picked restaurant based on the first pizza chosen from the order
     * @param restaurants the list of participating restaurants
     * @param pizzas the String array of pizzas from this order
     * @return The chosen restaurant based on the first pizza
     */
    private static Restaurant findRestaurant(Restaurant[] restaurants, String[] pizzas) {
        String pizza = pizzas[0];
        Restaurant pickedRestaurant = null;
        for (Restaurant restaurant : restaurants) {

            for (int k = 0; k < restaurant.getMenu().length; k++) {

                // if pizza is the same as on this restaurant menu
                if (restaurant.getMenu()[k].name.equals(pizza)) {
                    pickedRestaurant = restaurant;
                }
            }
        }

        return pickedRestaurant;
    }

}
