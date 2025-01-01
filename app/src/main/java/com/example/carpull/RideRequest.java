package com.example.carpull;

public class RideRequest {
    private String requestId;
    private String requesterId;
    private String driverId;
    private String rideId;
    private int seatsRequested;
    private double cost;
    private String status;

    // Constructor including all required fields
    public RideRequest(String requestId, String requesterId, String driverId, String rideId, int seatsRequested, double cost, String status) {
        this.requestId = requestId;
        this.requesterId = requesterId;
        this.driverId = driverId;
        this.rideId = rideId;
        this.seatsRequested = seatsRequested;
        this.cost = cost;
        this.status = status;
    }

    // Default constructor for Firestore
    public RideRequest() {}

    // Getter and setter methods
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequesterId() {
        return requesterId;
    }

    public void setRequesterId(String requesterId) {
        this.requesterId = requesterId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public int getSeatsRequested() {
        return seatsRequested;
    }

    public void setSeatsRequested(int seatsRequested) {
        this.seatsRequested = seatsRequested;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
