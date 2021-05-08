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
import android.widget.Toast;
import com.android.cloud_project.bloodhelper.R;
import com.android.cloud_project.bloodhelper.pages.DonationRequestPage;
import com.android.cloud_project.bloodhelper.models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class MyHomeView extends Fragment {

    private View view;
    private RecyclerView recent_posts;

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    private DonationRequestPage donation_request;
    private List<UserModel> request_lists;
    private ProgressDialog progressDialog;

    public MyHomeView() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.myhome_view_fragment, container, false);
        recent_posts = (RecyclerView) view.findViewById(R.id.recyleposts);
        recent_posts.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseReference = FirebaseDatabase.getInstance().getReference();
        request_lists = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        getActivity().setTitle("Blood Helper");
        donation_request = new DonationRequestPage(request_lists);
        RecyclerView.LayoutManager pmLayout = new LinearLayoutManager(getContext());
        recent_posts.setLayoutManager(pmLayout);
        recent_posts.setItemAnimator(new DefaultItemAnimator());
        recent_posts.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recent_posts.setAdapter(donation_request);
        displayRequests();
        return view;

    }
    private void displayRequests()
    {
        Query allposts = databaseReference.child("posts");
        progressDialog.show();
        allposts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    for (DataSnapshot singlepost : dataSnapshot.getChildren()) {
                        UserModel userModel = singlepost.getValue(UserModel.class);
                        request_lists.add(userModel);
                        donation_request.notifyDataSetChanged();
                    }
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(getActivity(), "No data to show!",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("User", databaseError.getMessage());

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
