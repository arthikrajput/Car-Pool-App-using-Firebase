package com.example.carpull;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRequestsAdapter extends RecyclerView.Adapter<MyRequestsAdapter.MyRequestsViewHolder> {

    private List<RideRequestWithDetails> myRequestsList;
    private Context context;

    public MyRequestsAdapter(List<RideRequestWithDetails> myRequestsList, Context context) {
        this.myRequestsList = myRequestsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyRequestsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_request, parent, false);
        return new MyRequestsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestsViewHolder holder, int position) {
        RideRequestWithDetails requestWithDetails = myRequestsList.get(position);

        holder.sourceTextView.setText("Source: " + requestWithDetails.getSource());
        holder.destinationTextView.setText("Destination: " + requestWithDetails.getDestination());
        holder.departureTimeTextView.setText("Departure Time: " + requestWithDetails.getDepartureTime());
        holder.costTextView.setText("Cost: ₹" + requestWithDetails.getCost());
        holder.statusTextView.setText("Status: " + requestWithDetails.getStatus());

        // Display driver details
        holder.driverNameTextView.setText("Driver: " + requestWithDetails.getDriverName());
        holder.driverEmailTextView.setText("Email: " + requestWithDetails.getDriverEmail());
        holder.driverMobileTextView.setText("Mobile: " + requestWithDetails.getDriverMobile());
    }

    @Override
    public int getItemCount() {
        return myRequestsList.size();
    }

    public static class MyRequestsViewHolder extends RecyclerView.ViewHolder {
        TextView sourceTextView, destinationTextView, departureTimeTextView, costTextView, statusTextView;
        TextView driverNameTextView, driverEmailTextView, driverMobileTextView;

        public MyRequestsViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            departureTimeTextView = itemView.findViewById(R.id.departureTimeTextView);
            costTextView = itemView.findViewById(R.id.costTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);

            // Driver details
            driverNameTextView = itemView.findViewById(R.id.driverNameTextView);
            driverEmailTextView = itemView.findViewById(R.id.driverEmailTextView);
            driverMobileTextView = itemView.findViewById(R.id.driverMobileTextView);
        }
    }
}
