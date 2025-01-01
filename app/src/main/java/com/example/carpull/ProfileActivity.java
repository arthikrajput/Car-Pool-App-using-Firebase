package com.example.carpull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";  // Log tag for debugging

    private EditText nameEditText, mobileEditText, birthdateEditText,emailEditText;
    private RadioGroup genderRadioGroup;
    private Spinner designationSpinner;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private FirebaseUser user;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        birthdateEditText = findViewById(R.id.birthdateEditText);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        designationSpinner = findViewById(R.id.designationSpinner);
        saveButton = findViewById(R.id.saveButton);

        // Initialize Firebase Authentication and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get the current user
        user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            Log.e(TAG, "User is not logged in.");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();  // Close the activity if the user is not logged in
            return;
        }

        // Set up Designation Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.designation_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        designationSpinner.setAdapter(adapter);

        // Fetch user details from Firestore
        loadUserProfile();

        // Handle Save Button Click
        saveButton.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        // Log the user ID to ensure it's correct
        Log.d(TAG, "Loading user profile for userId: " + userId);

        try {
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User userDetails = documentSnapshot.toObject(User.class);

                            if (userDetails != null) {
                                // Set values to UI elements
                                emailEditText.setText(userDetails.getEmail());
                                nameEditText.setText(userDetails.getName());
                                mobileEditText.setText(userDetails.getMobile());
                                birthdateEditText.setText(userDetails.getBirthdate());
                                setGenderRadioButton(userDetails.getGender());
                                setDesignationSpinner(userDetails.getDesignation());
                            } else {
                                Log.e(TAG, "User details are null.");
                                Toast.makeText(ProfileActivity.this, "Failed to load user details.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "User document does not exist.");
                            Toast.makeText(ProfileActivity.this, "User details not found.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading user profile", e);
                        Toast.makeText(ProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error in loadUserProfile method", e);
        }
    }

    private void setGenderRadioButton(String gender) {
        try {
            switch (gender) {
                case "Male":
                    genderRadioGroup.check(R.id.maleRadioButton);
                    break;
                case "Female":
                    genderRadioGroup.check(R.id.femaleRadioButton);
                    break;
                default:
                    Log.e(TAG, "Invalid gender value: " + gender);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting gender radio button", e);
        }
    }

    private void setDesignationSpinner(String designation) {
        try {
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) designationSpinner.getAdapter();
            int position = adapter.getPosition(designation);
            designationSpinner.setSelection(position);
        } catch (Exception e) {
            Log.e(TAG, "Error setting designation spinner", e);
        }
    }

    private void saveUserProfile() {
        String email = emailEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();
        String birthdate = birthdateEditText.getText().toString().trim();
        String gender = getSelectedGender();
        String designation = designationSpinner.getSelectedItem().toString();

        // Validate inputs
        if (name.isEmpty() || mobile.isEmpty() || birthdate.isEmpty()) {
            Toast.makeText(ProfileActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create user details object
        User userDetails = new User(email, name, mobile, birthdate, gender, designation);

        try {
            // Update user data in Firestore
            db.collection("users")
                    .document(userId)
                    .set(userDetails)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error updating profile", e);
                        Toast.makeText(ProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error in saveUserProfile method", e);
        }
    }

    private String getSelectedGender() {
        int selectedId = genderRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.maleRadioButton) {
            return "Male";
        } else{
            return "Female";
        }
    }
}
