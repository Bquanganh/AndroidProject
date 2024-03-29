package com.example.test.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.test.BookingDonationActivity;
import com.example.test.BookingFor2UsersActivity;
import com.example.test.Common.Common;
import com.example.test.DisplayActivity;
import com.example.test.Email.javaMailApi;
import com.example.test.Model.BookingInformation;
import com.example.test.Model.Hospital;
import com.example.test.Model.Notification;
import com.example.test.Model.User;
import com.example.test.R;
import com.example.test.SendEmailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep3Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    private  Context context;

    private TextView txt_hospital_name,txt_booking_time_text,txt_booking_location_text,txt_hospital_web,txt_hospital_phone_text,txt_hospital_open_hours,txt_hospital_address_text;
    private Button btn_confirm;
    public Hospital hospital;
    public User userSelected,userRecipient;
    private BookingFor2UsersActivity bookingFor2UsersActivity;
    private BookingDonationActivity bookingDonationActivity;
    private String idOfRecipient;
    private  String hospitalID;
    private ProgressDialog loader;



    BroadcastReceiver confirmBookingReceive = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }


    };
    private void setData() {
        if (hospitalID !=null)
        {

            txt_booking_location_text.setText(userSelected.getName());
            txt_hospital_phone_text.setText(hospital.getIdNumber());
            txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSLotToString(Common.currentTimeSlot))
                    .append(" at ")
                    .append(simpleDateFormat.format(Common.currentDate.getTime())));

            txt_hospital_address_text.setText(hospital.getAddress());
            txt_hospital_web.setText(hospital.getEmail());

            txt_hospital_name.setText(hospital.getName());
        }
        loader.setMessage("Loading...");
        loader.setCanceledOnTouchOutside(false);
        loader.show();
        Log.e("AAAAAA",hospitalID);
        txt_booking_location_text.setText(userRecipient.getName());
        txt_hospital_phone_text.setText(hospital.getIdNumber());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSLotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.currentDate.getTime())));

        txt_hospital_address_text.setText(hospital.getAddress());
        txt_hospital_web.setText(hospital.getEmail());

        txt_hospital_name.setText(hospital.getName());
        loader.dismiss();

    }



    static  BookingStep3Fragment instance;

    public static BookingStep3Fragment getInstance(){
        if (instance==null)
            instance= new BookingStep3Fragment();
        return instance;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceive,new IntentFilter(Common.KEY_CONFIRM_BOOKING));
        bookingFor2UsersActivity = (BookingFor2UsersActivity) getActivity();
        loader = new ProgressDialog(getContext());

         hospitalID = bookingFor2UsersActivity.getHospitalId();

        idOfRecipient = bookingFor2UsersActivity.getIdOfRecipient();
        if (hospitalID==null)
        {
            hospitalID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("hospitals").child(hospitalID);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Hospital allHospitals = snapshot.getValue(Hospital.class);

                hospital=allHospitals;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("HospiralData", String.valueOf(hospital));

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                userSelected=user;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (idOfRecipient!=null)
        {
            DatabaseReference recipientRef = FirebaseDatabase.getInstance().getReference().child("users").child(idOfRecipient);
            recipientRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    User user = snapshot.getValue(User.class);
                    userRecipient=user;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceive);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_booking_step_three,container,false);
        unbinder = ButterKnife.bind(this,view);



        txt_hospital_name= view.findViewById(R.id.txt_hospital_name);
        txt_booking_time_text= view.findViewById(R.id.txt_booking_time_text);
        txt_booking_location_text= view.findViewById(R.id.txt_booking_location_text);
        txt_hospital_web= view.findViewById(R.id.txt_hospital_web);
        txt_hospital_phone_text= view.findViewById(R.id.txt_hospital_phone_text);
        txt_hospital_open_hours= view.findViewById(R.id.txt_hospital_open_hours);
        txt_hospital_address_text=view.findViewById(R.id.txt_hospital_address_text);

        btn_confirm = view.findViewById(R.id.btn_confirm);


        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (idOfRecipient==null) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Confirm Information").setMessage("Do you want send mail to " +" "+ hospital.getName() + " ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String nameOfSender = snapshot.child("name").getValue().toString();
                                            String email = snapshot.child("email").getValue().toString();
                                            String phone = snapshot.child("idNumber").getValue().toString();
                                            String blood = snapshot.child("bloodGroup").getValue().toString();

                                            String mEmail = hospital.getEmail();
                                            String mSubject = "BLOOD DONATION";
                                            String mMessage = "Hello " + hospital.getName() + ", " + nameOfSender + " would like blood donation from you. Here's his/her detail:\n"
                                                    + "Name: " + nameOfSender + "\n" +
                                                    "Phone Number: " + phone + "\n" +
                                                    "Email: " + email + "\n" +
                                                    "Blood Group: " + blood + "\n" +
                                                    "Time slot: " + new StringBuilder(Common.convertTimeSLotToString(Common.currentTimeSlot))
                                                    .append(" at ")
                                                    .append(simpleDateFormat.format(Common.currentDate.getTime())) + "\n" +
                                                    "Kindly Reach out to him/her. Thank you!\n" +
                                                    "BLOOD DONATION APP -- DONATE BLOOD, SAVE LIVES";
                                            javaMailApi JavaMaikApi = new javaMailApi(view.getContext(), mEmail, mSubject, mMessage);
                                            JavaMaikApi.execute();

                                            DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("emails")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            senderRef.child(hospital.getId()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("hospitalEmails")
                                                                .child(hospital.getId());
                                                        receiverRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);

                                                    }

                                                }
                                            });



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
//                        getActivity().finish();// Close activity
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("AAAAa", idOfRecipient);
                    //Create booking information
                    BookingInformation bookingInformation = new BookingInformation();
                    Log.d("TestDatabase", hospital.getId());
                    bookingInformation.setHospitalName(hospital.getName());
                    bookingInformation.setHospitalId(hospital.getId());
                    bookingInformation.setCustomerName(userSelected.getName());
                    bookingInformation.setHospitalAddress(hospital.getAddress());
                    bookingInformation.setCustomerId(userSelected.getId());
                    bookingInformation.setRecipientName(userRecipient.getName());
                    bookingInformation.setCustomerPhone(userSelected.getIdNumber());
                    bookingInformation.setCustomerEmail(userSelected.getEmail());
                    bookingInformation.setRecipientId(userRecipient.getId());
                    bookingInformation.setRecipientPhone(userRecipient.getIdNumber());
                    bookingInformation.setStatus("");
                    bookingInformation.setTime(new StringBuilder(Common.convertTimeSLotToString(Common.currentTimeSlot))
                            .append(" at ")
                            .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
                    bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

                    new AlertDialog.Builder(view.getContext())
                            .setTitle("Confirm Information").setMessage("Do you want choose " + " "+hospital.getName() + " and " + " "+ userRecipient.getName() + " ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    senderRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            String nameOfSender = snapshot.child("name").getValue().toString();
                                            String email = snapshot.child("email").getValue().toString();
                                            String phone = snapshot.child("idNumber").getValue().toString();
                                            String blood = snapshot.child("bloodGroup").getValue().toString();

                                            String mEmail = userRecipient.getEmail();
                                            String mSubject = "BLOOD DONATION";
                                            String mMessage = "Hello " + userRecipient.getName() + ", " + nameOfSender + " would like blood donation from you. Here's his/her detail:\n"
                                                    + "Name: " + nameOfSender + "\n" +
                                                    "Phone Number: " + phone + "\n" +
                                                    "Email: " + email + "\n" +
                                                    "Blood Group: " + blood + "\n" +
                                                    "Time slot: " + new StringBuilder(Common.convertTimeSLotToString(Common.currentTimeSlot))
                                                    .append(" at ")
                                                    .append(simpleDateFormat.format(Common.currentDate.getTime())) + "\n" +
                                                    "Kindly Reach out to him/her. Thank you!\n" +
                                                    "BLOOD DONATION APP -- DONATE BLOOD, SAVE LIVES";
                                            javaMailApi JavaMaikApi = new javaMailApi(view.getContext(), mEmail, mSubject, mMessage);
                                            JavaMaikApi.execute();


                                            DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("emails")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            senderRef.child(hospital.getId()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("emails")
                                                                .child(userRecipient.getId()).child(userSelected.getName());
                                                        receiverRef.setValue(bookingInformation);



                                                    }

                                                }
                                            });

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }
                            })
                            .setNegativeButton("No", null)
                            .show();


                }


            }

        });



        return  view;
    }
    private void  addNotifications(String receivedId, String senderId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("notifications").child(receivedId);
        String date = DateFormat.getDateInstance().format(new Date());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("receivedId",receivedId);
        hashMap.put("senderId",senderId);
        hashMap.put("text","Sent you an email, kindly check it out!");
        hashMap.put("date",date);
        hashMap.put("status","Pending");

        reference.push().setValue(hashMap);
    }
}
