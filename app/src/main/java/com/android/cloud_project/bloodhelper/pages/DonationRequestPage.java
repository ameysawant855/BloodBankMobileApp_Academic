package com.android.cloud_project.bloodhelper.pages;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.cloud_project.bloodhelper.R;
import com.android.cloud_project.bloodhelper.models.UserModel;
import java.util.List;

public class DonationRequestPage extends RecyclerView.Adapter<DonationRequestPage.Accumulator> {


    private List<UserModel> request_lists;

    public class Accumulator extends RecyclerView.ViewHolder
    {
        TextView Name, bloodgroup, Address, contact, posted, city;

        public Accumulator(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.reqstUser);
            contact = itemView.findViewById(R.id.targetCN);
            bloodgroup = itemView.findViewById(R.id.targetBG);
            Address = itemView.findViewById(R.id.reqstLocation);
            posted = itemView.findViewById(R.id.posted);

        }
    }

    public DonationRequestPage(List<UserModel> request_lists)
    {
        this.request_lists = request_lists;
    }

    @Override
    public Accumulator onCreateViewHolder(ViewGroup viewGroup, int i) {

        View list_item = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.request_list_element, viewGroup, false);

        return new Accumulator(list_item);
    }

    @Override
    public void onBindViewHolder(Accumulator accumulator, int i_1) {

        if(i_1%2==0)
        {
            accumulator.itemView.setBackgroundColor(Color.parseColor("#C13F31"));
        }
        else
        {
            accumulator.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        UserModel userModel = request_lists.get(i_1);
        accumulator.Name.setText("Requested by: "+ userModel.getName());
        accumulator.Address.setText("From: "+ userModel.getAddress());
        accumulator.bloodgroup.setText("Needs "+ userModel.getBloodGroup());
        accumulator.posted.setText("Request date: "+ userModel.getDate());
        accumulator.contact.setText(userModel.getContact());

    }

    @Override
    public int getItemCount() {
        return request_lists.size();
    }
}
