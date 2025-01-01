package com.example.carpull;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class TakeRideActivity extends AppCompatActivity {

    private EditText sourceEditText, destinationEditText;
    private RadioGroup genderPreferenceGroup, riderPreferenceGroup;
    private RecyclerView rideRecyclerView;
    private RideAdapter rideAdapter;
    private List<Ride> rideList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_ride);

        // Initialize views
        sourceEditText = findViewById(R.id.sourceEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        genderPreferenceGroup = findViewById(R.id.genderPreferenceGroup);
        riderPreferenceGroup = findViewById(R.id.riderPreferenceGroup);
        rideRecyclerView = findViewById(R.id.rideRecyclerView);

        // Initialize Firestore and RecyclerView
        db = FirebaseFirestore.getInstance();
        rideList = new ArrayList<>();
        rideAdapter = new RideAdapter(rideList);
        rideRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rideRecyclerView.setAdapter(rideAdapter);

        // Set up listeners for dynamic filtering
        setupDynamicFilters();
    }

    private void setupDynamicFilters() {
        // Listen for changes in text fields
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fetchRides();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        sourceEditText.addTextChangedListener(textWatcher);
        destinationEditText.addTextChangedListener(textWatcher);

        // Listen for changes in radio groups
        genderPreferenceGroup.setOnCheckedChangeListener((group, checkedId) -> fetchRides());
        riderPreferenceGroup.setOnCheckedChangeListener((group, checkedId) -> fetchRides());
    }

    private void fetchRides() {
        String source = sourceEditText.getText().toString().trim();
        String destination = destinationEditText.getText().toString().trim();

        // Ensure source and destination are not empty
        if (source.isEmpty() || destination.isEmpty()) {
            Toast.makeText(this, "Please enter both source and destination", Toast.LENGTH_SHORT).show();
            return;
        }

        String genderPreference = getSelectedGenderPreference();
        String riderPreference = getSelectedRiderPreference();

        // Query Firestore based on user input fields with a real-time listener
        db.collection("rides")
                .whereEqualTo("source", source)
                .whereEqualTo("destination", destination)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(this, "Error fetching rides", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (queryDocumentSnapshots != null) {
                        rideList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            String rideUserId = document.getString("userId");
                            checkUserDesignation(rideUserId, riderPreference, genderPreference, document);
                        }
                    }
                });
    }

    private void checkUserDesignation(String userId, String riderPreference, String genderPreference, DocumentSnapshot rideDocument) {
        db.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(userDoc -> {
                    if (userDoc.exists()) {
                        String userDesignation = userDoc.getString("designation");
                        String rideGenderPreference = rideDocument.getString("genderPreference");

                        // Allow "Any" gender preference to match both "Male" and "Female"
                        boolean matchesGender = genderPreference.equals("Any") || genderPreference.equals(rideGenderPreference);

                        if (riderPreference.equals(userDesignation) && matchesGender) {
                            Ride ride = rideDocument.toObject(Ride.class);
                            if (ride != null && ride.getSeats() > 0) { // Ensure seats > 0
                                rideList.add(ride);
                                rideAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error checking user details", Toast.LENGTH_SHORT).show();
                });
    }

    private String getSelectedGenderPreference() {
        int selectedId = genderPreferenceGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.maleRadioButton) {
            return "Male";
        } else if (selectedId == R.id.femaleRadioButton) {
            return "Female";
        } else {
            return "Any"; // Assuming there's a "Any" option in the RadioGroup
        }
    }

    private String getSelectedRiderPreference() {
        int selectedId = riderPreferenceGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.studentRadioButton) {
            return "Student";
        } else if (selectedId == R.id.staffRadioButton) {
            return "Staff";
        } else {
            return "Faculty";
        }
    }
}
