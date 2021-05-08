package com.android.cloud_project.bloodhelper.views;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.cloud_project.bloodhelper.R;
import com.android.cloud_project.bloodhelper.pages.SearchDonorPage;
import com.android.cloud_project.bloodhelper.models.DonorModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchDonorView extends Fragment {

    private View view;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Spinner bloodgroup, city;
    Button search_button;
    ProgressDialog progressDialog;
    List<DonorModel> donors;
    private RecyclerView recyclerview;

    private SearchDonorPage page;

    public SearchDonorView() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.search_donor_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);




        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("donors");

        bloodgroup = view.findViewById(R.id.btngetBloodGroup);
        city = view.findViewById(R.id.btnCity);
        search_button = view.findViewById(R.id.btnSearch);

        getActivity().setTitle("Find Blood Donor");

        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                donors = new ArrayList<>();
                donors.clear();
                page = new SearchDonorPage(donors);
                recyclerview = (RecyclerView) view.findViewById(R.id.showDonorList);
                recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
                RecyclerView.LayoutManager searchdonor = new LinearLayoutManager(getContext());
                recyclerview.setLayoutManager(searchdonor);
                recyclerview.setItemAnimator(new DefaultItemAnimator());
                recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
                recyclerview.setAdapter(page);
                Query qpath  = databaseReference.child(city.getSelectedItem().toString()).child(bloodgroup.getSelectedItem().toString());
                qpath.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       if(dataSnapshot.exists())
                       {
                           for(DataSnapshot singleitem : dataSnapshot.getChildren())
                           {
                               DonorModel donorModel = singleitem.getValue(DonorModel.class);
                               donors.add(donorModel);
                               page.notifyDataSetChanged();
                           }
                       }
                       else
                       {
                           Toast.makeText(getActivity(), "No records!",Toast.LENGTH_LONG).show();
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError databaseError) {
                       Log.d("User", databaseError.getMessage());
                   }
               });
               progressDialog.dismiss();
            }
        });
        return view;
    }

}
