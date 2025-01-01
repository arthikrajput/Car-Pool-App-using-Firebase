package com.example.carpull;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.Calendar;

public class OfferRideActivity extends AppCompatActivity {

    private EditText sourceEditText, destinationEditText, seatsEditText, costEditText;
    private TextView departureTimeTextView;
    private RadioGroup drivingPreferenceGroup, genderPreferenceGroup;
    private Button saveButton;

    private FirebaseFirestore db;
    private String userId;
    private String offerorName;
    private String offerorEmail;

    private String offerorMobileNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);

        // Initialize views
        sourceEditText = findViewById(R.id.sourceEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        departureTimeTextView = findViewById(R.id.departureTimeTextView);
        seatsEditText = findViewById(R.id.seatsEditText);
        costEditText = findViewById(R.id.costEditText);
        drivingPreferenceGroup = findViewById(R.id.drivingPreferenceGroup);
        genderPreferenceGroup = findViewById(R.id.genderPreferenceGroup);
        saveButton = findViewById(R.id.saveButton);

        // Initialize Firestore and Auth
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Fetch the current user ID

        // Fetch the current user’s name and email for the offeror details
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        offerorName = documentSnapshot.getString("name");
                        offerorEmail = documentSnapshot.getString("email");
                        offerorMobileNumber = documentSnapshot.getString("mobile");
                    }
                });

        // Set up Date and Time Picker for departure time
        departureTimeTextView.setOnClickListener(v -> showDateTimePicker());

        // Handle Save Button Click
        saveButton.setOnClickListener(v -> saveRide());
    }

    private void showDateTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int selectedYear = calendar.get(Calendar.YEAR);
        int selectedMonth = calendar.get(Calendar.MONTH);
        int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        int selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = calendar.get(Calendar.MINUTE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, monthOfYear, dayOfMonth) -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                departureTimeTextView.setText(String.format("%02d/%02d/%04d %02d:%02d", dayOfMonth, monthOfYear + 1, year, hourOfDay, minute));
            }, selectedHour, selectedMinute, true);
            timePickerDialog.show();
        }, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }

    private void saveRide() {
        String source = sourceEditText.getText().toString().trim();
        String destination = destinationEditText.getText().toString().trim();
        String departureTime = departureTimeTextView.getText().toString().trim();

        if (source.isEmpty() || destination.isEmpty() || departureTime.isEmpty() || seatsEditText.getText().toString().trim().isEmpty() || costEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int seats = Integer.parseInt(seatsEditText.getText().toString().trim());
        double cost = Double.parseDouble(costEditText.getText().toString().trim());

        // Ensure either source or destination is "Nirma University"
        if (!(source.equalsIgnoreCase("Nirma University") || destination.equalsIgnoreCase("Nirma University"))) {
            Toast.makeText(this, "Either Source or Destination must be Nirma University", Toast.LENGTH_SHORT).show();
            return;
        }

        String drivingPreference = getSelectedDrivingPreference();
        String genderPreference = getSelectedGenderPreference();

        // Creating a new Ride object without the rideId (Firestore will generate it)
        Ride ride = new Ride(
                "",
                userId,
                source,
                destination,
                departureTime,
                seats,
                drivingPreference,
                genderPreference,
                cost,
                offerorName,
                offerorEmail,
                offerorMobileNumber  // Pass the mobile number
        );

        // Add the ride to Firestore and get the generated rideId
        db.collection("rides")
                .add(ride)
                .addOnSuccessListener(documentReference -> {
                    // After the ride is added, get the generated ride ID
                    String rideId = documentReference.getId();

                    // Set the rideId in the ride object
                    ride.setId(rideId);

                    // Now that the ride ID is set, update the ride document with the ID
                    db.collection("rides").document(rideId)
                            .update("id", rideId)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(OfferRideActivity.this, "Ride Offered Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(OfferRideActivity.this, "Error updating ride ID", Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OfferRideActivity.this, "Error Offering Ride", Toast.LENGTH_SHORT).show();
                });
    }


    private String getSelectedDrivingPreference() {
        int selectedId = drivingPreferenceGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.selfRadioButton) {
            return "Self";
        } else if (selectedId == R.id.otherRadioButton) {
            return "Other";
        } else {
            return "";  // Return empty string if no option is selected
        }
    }

    private String getSelectedGenderPreference() {
        int selectedId = genderPreferenceGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.maleRadioButton) {
            return "Male";
        } else if (selectedId == R.id.femaleRadioButton) {
            return "Female";
        } else if (selectedId == R.id.anyGenderRadioButton) {
            return "Any";
        } else {
            return "";  // Return empty string if no option is selected
        }
    }
}
