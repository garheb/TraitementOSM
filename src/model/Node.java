package model;

public class Node {
    private final long _id; //id
    private final double _longitude;
    private final double _latitude;
    
    public Node(long id, double longitude, double latitude) {
        _id = id;
        _longitude = longitude;
        _latitude = latitude;
    }

    public long getId() {
        return _id;
    }

    public double getLongitude() {
        return _longitude;
    }

    public double getLatitude() {
        return _latitude;
    }
}
