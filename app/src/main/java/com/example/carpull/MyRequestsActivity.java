package com.example.carpull;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyRequestsAdapter myRequestsAdapter;
    private List<RideRequestWithDetails> myRequestsWithDetailsList;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_requests);

        recyclerView = findViewById(R.id.recycler_view_my_requests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        myRequestsWithDetailsList = new ArrayList<>();
        myRequestsAdapter = new MyRequestsAdapter(myRequestsWithDetailsList, this);
        recyclerView.setAdapter(myRequestsAdapter);

        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        fetchMyRequests();
    }

    private void fetchMyRequests() {
        db.collection("rideRequests")
                .whereEqualTo("requesterId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot document : querySnapshot) {
                            RideRequest rideRequest = document.toObject(RideRequest.class);

                            // Fetch details from the rides table using rideId
                            db.collection("rides")
                                    .document(rideRequest.getRideId())
                                    .get()
                                    .addOnSuccessListener(rideSnapshot -> {
                                        if (rideSnapshot.exists()) {
                                            Ride ride = rideSnapshot.toObject(Ride.class);

                                            // Fetch driver details
                                            db.collection("users")
                                                    .document(rideRequest.getDriverId()) // Fetch driver details using driverId
                                                    .get()
                                                    .addOnSuccessListener(driverSnapshot -> {
                                                        if (driverSnapshot.exists()) {
                                                            String driverName = driverSnapshot.getString("name");
                                                            String driverEmail = driverSnapshot.getString("email");
                                                            String driverMobile = driverSnapshot.getString("mobile");

                                                            // Create a combined object to pass to the adapter
                                                            RideRequestWithDetails requestWithDetails = new RideRequestWithDetails(
                                                                    rideRequest,
                                                                    ride.getSource(),
                                                                    ride.getDestination(),
                                                                    ride.getDepartureTime(),
                                                                    ride.getCost(),
                                                                    rideRequest.getStatus(),
                                                                    driverName,
                                                                    driverEmail,
                                                                    driverMobile
                                                            );

                                                            myRequestsWithDetailsList.add(requestWithDetails);
                                                            myRequestsAdapter.notifyDataSetChanged();
                                                        }
                                                    })
                                                    .addOnFailureListener(e ->
                                                            Toast.makeText(this, "Error fetching driver details", Toast.LENGTH_SHORT).show());
                                        }
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Error fetching ride details", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(this, "No requests found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error fetching requests", Toast.LENGTH_SHORT).show());
    }
}
