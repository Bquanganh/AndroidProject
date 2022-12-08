package com.example.test.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Adapter.MyAllHospitalAdapter;
import com.example.test.Common.Dialog;
import com.example.test.Common.SpacesItemDecoration;
import com.example.test.Interface.AllLocation;
import com.example.test.Interface.HospitalsLoadListener;
import com.example.test.Model.AllHospitals;
import com.example.test.Model.Hospital;
import com.example.test.Model.User;
import com.example.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep1Fragment extends Fragment implements AllLocation, HospitalsLoadListener {

    private Uri resultUri;

    private ProgressDialog loader;
    private Context context;

    private FirebaseAuth mAuth;
    private DatabaseReference locationDatabase;

    CollectionReference allLocation;
    CollectionReference hospitalRef;
    DocumentReference allLocations;



    private  List<AllHospitals> allHospitals;
    Unbinder unbinder;




    AllLocation AllLocationLoadListener;
    HospitalsLoadListener hospitalsLoadListener;
    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.recycler_location)
    RecyclerView recycler_location;

    static  BookingStep1Fragment instance;

    public static  BookingStep1Fragment getInstance(){
        if (instance==null)
            instance= new BookingStep1Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allLocation = FirebaseFirestore.getInstance().collection("hospitals");
        locationDatabase = FirebaseDatabase.getInstance().getReference().child("hospitals");




        AllLocationLoadListener = this;
        hospitalsLoadListener = this;



    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView=  inflater.inflate(R.layout.fragment_booking_step_one,container,false);
        unbinder = ButterKnife.bind(this,itemView);
        initView();
        loadAllLocation();
        return itemView;
    }

    private void initView() {
        recycler_location.setHasFixedSize(true);
        recycler_location.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_location.addItemDecoration(new SpacesItemDecoration(4));
    }

    private void loadAllLocation() {
//
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("hospitals");
        locationDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> list = new ArrayList<>();
                list.add("Please choose location");
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String allHospitals = dataSnapshot.getKey();
                    list.add(allHospitals);
                }
                AllLocationLoadListener.onAllLocationLoadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onAllLocationLoadSuccess(List<String> areaNameList) {
            spinner.setItems(areaNameList);
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                @Override
                public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                    if (position>0){
                        loadHospitals(item.toString());
                    }
                    else
                        recycler_location.setVisibility(View.GONE);
                }
            });
    }

    private void loadHospitals(String cityName) {

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("hospitals").child(cityName).child("hospitals");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List<AllHospitals> list = new ArrayList<>();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        AllHospitals allHospitals = dataSnapshot.getValue(AllHospitals.class);
                        allHospitals.getHospitalID();
                        list.add(allHospitals);
                    }
                    hospitalsLoadListener.onHospitalsLoadSuccess(list);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }

    @Override
    public void onAllLocationLoadFailed(String message) {
        Toast.makeText(getActivity(),"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHospitalsLoadSuccess(List<AllHospitals> hospitalsList) {
        MyAllHospitalAdapter adapter = new MyAllHospitalAdapter(getActivity(),hospitalsList);
        recycler_location.setAdapter(adapter);
        recycler_location.setVisibility(View.VISIBLE);
    }

    @Override
    public void onHospitalsLoadFailed(String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();

    }




}
