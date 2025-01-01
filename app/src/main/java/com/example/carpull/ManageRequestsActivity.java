package com.example.carpull;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManageRequestsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RideRequestAdapter rideRequestAdapter;
    private List<RideRequest> rideRequestList;
    private FirebaseFirestore db;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_requests);

        recyclerView = findViewById(R.id.recycler_view_ride_requests);
        rideRequestList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        rideRequestAdapter = new RideRequestAdapter(rideRequestList, this);
        recyclerView.setAdapter(rideRequestAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch ride requests
        fetchRideRequests();
    }

    private void fetchRideRequests() {
        db.collection("rideRequests")
                .whereEqualTo("driverId",userId)
//                .whereEqualTo("status", "pending")  // Filter for pending requests
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            RideRequest rideRequest = document.toObject(RideRequest.class);
                            rideRequestList.add(rideRequest);
                        }
                        rideRequestAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ManageRequestsActivity.this, "Error fetching ride requests", Toast.LENGTH_SHORT).show();
                });
    }
}
