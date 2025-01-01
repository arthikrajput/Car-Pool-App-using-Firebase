package com.example.carpull;

public class RideRequestWithDetails {
    private RideRequest rideRequest;
    private String source;
    private String destination;
    private String departureTime;
    private double cost;
    private String status;

    // Added fields for driver's details
    private String driverName;
    private String driverEmail;
    private String driverMobile;

    // Constructor including driver details
    public RideRequestWithDetails(RideRequest rideRequest, String source, String destination, String departureTime,
                                  double cost, String status, String driverName, String driverEmail, String driverMobile) {
        this.rideRequest = rideRequest;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.cost = cost;
        this.status = status;
        this.driverName = driverName;
        this.driverEmail = driverEmail;
        this.driverMobile = driverMobile;
    }

    // Getter methods
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getDepartureTime() { return departureTime; }
    public double getCost() { return cost; }
    public String getStatus() { return status; }
    public String getDriverName() { return driverName; }
    public String getDriverEmail() { return driverEmail; }
    public String getDriverMobile() { return driverMobile; }
}
