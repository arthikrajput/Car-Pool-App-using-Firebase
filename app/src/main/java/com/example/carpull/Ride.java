package com.example.carpull;

public class Ride {

    private String rideId;
    private String userId;
    private String source;
    private String destination;
    private String departureTime;
    private int seats;
    private String drivingPreference;
    private String genderPreference;
    private double cost;
    private String offerorName;
    private String offerorEmail;
    private String mobileNumber;

    // Default constructor for Firebase
    public Ride() {
    }

    // Constructor with all fields
    public Ride(String rideId, String userId, String source, String destination,
                String departureTime, int seats, String drivingPreference,
                String genderPreference, double cost, String offerorName,
                String offerorEmail, String mobileNumber) {
        this.rideId = rideId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.seats = seats;
        this.drivingPreference = drivingPreference;
        this.genderPreference = genderPreference;
        this.cost = cost;
        this.offerorName = offerorName;
        this.offerorEmail = offerorEmail;
        this.mobileNumber = mobileNumber;  // Initialize the field
    }

    // Add getter and setter for mobileNumber
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    // Getter and Setter methods for all fields

    public String getId() {
        return rideId;
    }

    public void setId(String rideId) {
        this.rideId = rideId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getDrivingPreference() {
        return drivingPreference;
    }

    public void setDrivingPreference(String drivingPreference) {
        this.drivingPreference = drivingPreference;
    }

    public String getGenderPreference() {
        return genderPreference;
    }

    public void setGenderPreference(String genderPreference) {
        this.genderPreference = genderPreference;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getOfferorName() {
        return offerorName;
    }

    public void setOfferorName(String offerorName) {
        this.offerorName = offerorName;
    }

    public String getOfferorEmail() {
        return offerorEmail;
    }

    public void setOfferorEmail(String offerorEmail) {
        this.offerorEmail = offerorEmail;
    }
}
