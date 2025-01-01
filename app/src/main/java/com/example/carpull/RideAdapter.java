package com.example.carpull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RideAdapter extends RecyclerView.Adapter<RideAdapter.RideViewHolder> {

    private List<Ride> rideList;
    private FirebaseFirestore db;
    private String currentUserId;

    public RideAdapter(List<Ride> rideList) {
        this.rideList = rideList;
        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        Ride ride = rideList.get(position);

        holder.sourceTextView.setText("Source: " + ride.getSource());
        holder.destinationTextView.setText("Destination: " + ride.getDestination());
        holder.departureTimeTextView.setText("Departure Time: " + ride.getDepartureTime());
        holder.costTextView.setText("Cost: ₹" + ride.getCost());
        holder.seatsTextView.setText("Seats Available: " + ride.getSeats());
        holder.offerorNameTextView.setText("Name: " + ride.getOfferorName());
        holder.offerorEmailTextView.setText("Email: " + ride.getOfferorEmail());
        holder.offerorMobileTextView.setText("Mobile: " + ride.getMobileNumber());  // Set mobile number

        holder.requestRideButton.setEnabled(ride.getSeats() > 0);
        holder.requestRideButton.setOnClickListener(v -> requestRide(ride, holder));
    }

    @Override
    public int getItemCount() {
        return rideList.size();
    }

    private void requestRide(Ride ride, RideViewHolder holder) {
        RideRequest rideRequest = new RideRequest(
                null,                   // Initialize 'requestId' as null; it will be set by Firestore
                currentUserId,          // User requesting the ride
                ride.getUserId(),       // Driver ID (from Ride object)
                ride.getId(),           // Ride ID
                1,                      // Seats requested (adjustable as needed)
                ride.getCost(),         // Cost for the ride
                "pending"               // Initial status is "pending"
        );

        db.collection("rideRequests")
                .add(rideRequest)
                .addOnSuccessListener(documentReference -> {
                    String requestId = documentReference.getId();
                    Toast.makeText(holder.itemView.getContext(), "Ride Request Sent", Toast.LENGTH_SHORT).show();
                    holder.requestRideButton.setEnabled(false);

                    db.collection("rideRequests")
                            .document(requestId)
                            .update("requestId", requestId)
                            .addOnSuccessListener(aVoid -> {
                                // Successfully updated requestId if needed
                            })
                            .addOnFailureListener(e -> {
                                // Handle failure
                            });

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(holder.itemView.getContext(), "Error sending request", Toast.LENGTH_SHORT).show();
                });
    }

    public static class RideViewHolder extends RecyclerView.ViewHolder {
        TextView sourceTextView, destinationTextView, departureTimeTextView, costTextView, seatsTextView, offerorNameTextView, offerorEmailTextView, offerorMobileTextView;
        Button requestRideButton;

        public RideViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            departureTimeTextView = itemView.findViewById(R.id.departureTimeTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            seatsTextView = itemView.findViewById(R.id.seatsTextView);
            offerorNameTextView = itemView.findViewById(R.id.offerorNameTextView);
            offerorEmailTextView = itemView.findViewById(R.id.offerorEmailTextView);
            offerorMobileTextView = itemView.findViewById(R.id.offerorMobileTextView);  // Initialize mobile number view
            requestRideButton = itemView.findViewById(R.id.requestRideButton);
        }
    }
}
