package com.example.carpull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RideRequestAdapter extends RecyclerView.Adapter<RideRequestAdapter.RideRequestViewHolder> {

    private List<RideRequest> rideRequestList;
    private Context context;
    private FirebaseFirestore db;

    public RideRequestAdapter(List<RideRequest> rideRequestList, Context context) {
        this.rideRequestList = rideRequestList;
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RideRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new RideRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideRequestViewHolder holder, int position) {
        RideRequest rideRequest = rideRequestList.get(position);

        // Fetch requester details using requesterId
        db.collection("users")
                .document(rideRequest.getRequesterId())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");
                        String mobile = documentSnapshot.getString("mobile");

                        holder.requesterNameTextView.setText("Name: " + name);
                        holder.requesterEmailTextView.setText("Email: " + email);
                        holder.requesterMobileTextView.setText("Mobile: " + mobile);
                    } else {
                        Toast.makeText(context, "Requester details not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to load requester details", Toast.LENGTH_SHORT).show());

        // Set other request details
        holder.seatsRequestedTextView.setText("Seats Requested: " + rideRequest.getSeatsRequested());
        holder.costTextView.setText("Cost: ₹" + rideRequest.getCost());
        holder.statusTextView.setText("Status: " + rideRequest.getStatus());

        // Set up button actions for accept and decline
        holder.acceptButton.setOnClickListener(v -> acceptRequest(rideRequest));
        holder.declineButton.setOnClickListener(v -> declineRequest(rideRequest));
    }

    @Override
    public int getItemCount() {
        return rideRequestList.size();
    }

    private void acceptRequest(RideRequest rideRequest) {
        db.collection("rideRequests")
                .document(rideRequest.getRequestId())
                .update("status", "accepted")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Request accepted", Toast.LENGTH_SHORT).show();
                    rideRequest.setStatus("accepted");
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to accept request", Toast.LENGTH_SHORT).show());
    }

    private void declineRequest(RideRequest rideRequest) {
        db.collection("rideRequests")
                .document(rideRequest.getRequestId())
                .update("status", "declined")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Request declined", Toast.LENGTH_SHORT).show();
                    rideRequest.setStatus("declined");
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Failed to decline request", Toast.LENGTH_SHORT).show());
    }

    public static class RideRequestViewHolder extends RecyclerView.ViewHolder {
        TextView requesterNameTextView, requesterEmailTextView, requesterMobileTextView;
        TextView seatsRequestedTextView, costTextView, statusTextView;
        Button acceptButton, declineButton;

        public RideRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            requesterNameTextView = itemView.findViewById(R.id.requesterNameTextView);
            requesterEmailTextView = itemView.findViewById(R.id.requesterEmailTextView);
            requesterMobileTextView = itemView.findViewById(R.id.requesterMobileTextView);
            seatsRequestedTextView = itemView.findViewById(R.id.seatsRequestedTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }
}
