package uk.ac.ed.inf;

/**
 * This is an enum class that defines the final double variables that are allowed for the compass directions for the
 * nextPosition() method in the LntLat class.
 */
public enum CompassDegrees {

    E(0),
    ENE(22.5),
    NE (45),
    NNE(67.5),
    N(90),
    NNW(112.5),
    NW(135),
    WNW(157.5),
    W(180),
    WSW(202.5),
    SW(225),
    SSW(247.5),
    S(270),
    SSE(292.5),
    SE(315),
    ESE(337.5);

    private final double compassDegrees;
    CompassDegrees(final double degrees) {
        compassDegrees = degrees;
    }

    public double getCompassDegrees(){
        return compassDegrees;
    }
}
