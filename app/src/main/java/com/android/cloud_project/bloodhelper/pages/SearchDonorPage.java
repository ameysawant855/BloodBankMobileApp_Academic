package com.android.cloud_project.bloodhelper.pages;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.cloud_project.bloodhelper.R;
import com.android.cloud_project.bloodhelper.models.DonorModel;
import java.util.List;

public class SearchDonorPage extends RecyclerView.Adapter<SearchDonorPage.Accumulator> {


    private List<DonorModel> donor_list;

    public class Accumulator extends RecyclerView.ViewHolder
    {
        TextView name, address, contact;

        public Accumulator(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.donorName);
            contact = itemView.findViewById(R.id.donorContact);
            address = itemView.findViewById(R.id.donorAddress);

        }
    }

    public SearchDonorPage(List<DonorModel> donor_list)
    {
        this.donor_list = donor_list;
    }

    @Override
    public Accumulator onCreateViewHolder(ViewGroup viewGroup, int i) {

        View listitem = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_donor_element, viewGroup, false);

        return new Accumulator(listitem);
    }

    @Override
    public void onBindViewHolder(Accumulator accumulator, int i) {

        if(i%2==0)
        {
            accumulator.itemView.setBackgroundColor(Color.parseColor("#C13F31"));
        }
        else
        {
            accumulator.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        DonorModel donorModel = donor_list.get(i);
        accumulator.name.setText("Name: "+ donorModel.getName());
        accumulator.contact.setText(donorModel.getContact());
        accumulator.address.setText("Address: "+ donorModel.getAddress());


    }

    @Override
    public int getItemCount() {
        return donor_list.size();
    }
}
