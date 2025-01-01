package com.example.carpull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Button profileButton, takeRideButton, offerRideButton, logoutButton,manageRequestsButton,myRequestsButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize FirebaseAuth and FirebaseFirestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize buttons
        profileButton = findViewById(R.id.profileButton);
        takeRideButton = findViewById(R.id.takeRideButton);
        offerRideButton = findViewById(R.id.offerRideButton);
        logoutButton = findViewById(R.id.logoutButton);
        manageRequestsButton = findViewById(R.id.manageRequestsButton);

        manageRequestsButton.setOnClickListener(v -> goToManageRequestsActivity());
        myRequestsButton = findViewById(R.id.button_my_requests);
        myRequestsButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MyRequestsActivity.class);
            startActivity(intent);
        });

        try {
            // Check if the user is logged in
            if (mAuth.getCurrentUser() == null) {
                // If the user is not logged in, redirect to LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                // If logged in, check if the profile exists in Firestore
                String userId = mAuth.getCurrentUser().getUid();
                db.collection("users").document(userId).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (!documentSnapshot.exists()) {
                                // If the profile does not exist, redirect to ProfileActivity
                                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(MainActivity.this, "Error checking profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        });
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error during initialization: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Set up Profile button to navigate to ProfileActivity
        profileButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error opening Profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Set up Take Ride button to navigate to TakeRideActivity
        takeRideButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, TakeRideActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error opening Take Ride: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Set up Offer Ride button to navigate to OfferRideActivity
        offerRideButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MainActivity.this, OfferRideActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "Error opening Offer Ride: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();  // Sign out the user
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear backstack
            startActivity(intent);
            finish();
        });
    }
    public void goToManageRequestsActivity() {
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, ManageRequestsActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
        }
    }
}
