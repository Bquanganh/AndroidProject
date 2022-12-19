package com.example.test.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test.BookingDonationActivity;
import com.example.test.Common.Common;
import com.example.test.Email.javaMailApi;
import com.example.test.Model.BookingInformation;
import com.example.test.Model.RequestDonation;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestDonationAdapter extends RecyclerView.Adapter<RequestDonationAdapter.ViewHolder> {
    private Context context;
    private List<RequestDonation> userList;
    private String customerPhone,customerEmail;
    private List listUser;
    SimpleDateFormat simpleDateFormat;




    public RequestDonationAdapter(Context context, List<RequestDonation> userList) {
        this.context = context;
        this.userList = userList;

    }
    public void setData(List<RequestDonation> list){
        this.userList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RequestDonationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_accept_user,parent,false);
        return new RequestDonationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestDonationAdapter.ViewHolder holder, int position) {

        RequestDonation user = userList.get(position);
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DatabaseReference checkStatus = FirebaseDatabase.getInstance().getReference("emails")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getCustomerName()).child("status");
        checkStatus.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    if(snapshot.getValue().equals("Confirm"))
                    {
                        holder.btn_confirm.setVisibility(View.GONE);
                        holder.btn_refuse.setVisibility(View.GONE);
                        holder.btn_status.setText("Completed");
                        holder.btn_status.setVisibility(View.VISIBLE);
                    }else if(snapshot.getValue().equals("refused"))
                    {
                        holder.btn_confirm.setVisibility(View.GONE);
                        holder.btn_refuse.setVisibility(View.GONE);
                        holder.btn_status.setText("Refused");
                        holder.btn_status.setVisibility(View.VISIBLE);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.txt_hospital_name.setText(user.getHospitalName());
        holder.btn_confirm.setVisibility(View.VISIBLE);
        holder.txt_booking_time_text.setText(user.getTime());
        holder.txt_booking_location_text.setText(user.getCustomerName());
        holder.txt_hospital_web.setText(user.getCustomerEmail());
        holder.txt_hospital_phone_text.setText(user.getCustomerPhone());
        holder.txt_hospital_address_text.setText(user.getHospitalAddress());

        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookingInformation bookingInformation = new BookingInformation();
                bookingInformation.setHospitalName(user.getHospitalName());
                bookingInformation.setHospitalId(user.getHospitalId());
                bookingInformation.setCustomerName(user.getCustomerName());
                bookingInformation.setHospitalAddress(user.getHospitalAddress());
                bookingInformation.setCustomerId(user.getCustomerId());
                bookingInformation.setRecipientName(user.getRecipientName());
                bookingInformation.setCustomerPhone(user.getCustomerPhone());
                bookingInformation.setCustomerEmail(user.getCustomerEmail());
                bookingInformation.setRecipientId(user.getRecipientId());
                bookingInformation.setRecipientPhone(user.getRecipientPhone());
                bookingInformation.setStatus("Confirm");
                bookingInformation.setTime(new StringBuilder(Common.convertTimeSLotToString(Common.currentTimeSlot))
                        .append(" at ")
                        .append(simpleDateFormat.format(Common.currentDate.getTime())).toString());
                bookingInformation.setSlot(user.getSlot());

                //Submit to hospital document
                DatabaseReference bookDate = FirebaseDatabase.getInstance().getReference()
                        .child("hospitals").child(user.getHospitalId())
                        .child(Common.simpleFormat.format(Common.currentDate.getTime()))
                        .child(String.valueOf(Common.currentTimeSlot));
                bookDate.setValue(bookingInformation).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        new AlertDialog.Builder(context)
                                .setTitle("Accept").setMessage("Do you accept this request of" + user.getCustomerName()+ "?")
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

                                                String mEmail = user.getCustomerEmail();
                                                String mSubject = "BLOOD DONATION";
                                                String mMessage = "Hello " + user.getCustomerName()+ ", "+nameOfSender+" have agreed to your blood donation schedule :\n"
                                                        +" and this is his/her details : \n"
                                                        + "Name: "+nameOfSender+"\n"+
                                                        "Phone Number: " +phone+"\n"+
                                                        "Email: "+email + "\n"+
                                                        "Blood Group: "+blood +"\n"+
                                                        "Time: " + user.getTime() +"\n"+
                                                        "Hospital: " + user.getHospitalName() + "\n"+
                                                        "Hospital Address: " + user.getHospitalAddress() +"\n"+
                                                        "Kindly Reach out to him/her. Thank you!\n"+
                                                        "BLOOD DONATION APP -- DONATE BLOOD, SAVE LIVES";
                                                javaMailApi JavaMaikApi = new javaMailApi(context,mEmail,mSubject,mMessage);
                                                JavaMaikApi.execute();

                                                holder.btn_confirm.setVisibility(View.GONE);
                                                holder.btn_refuse.setVisibility(View.GONE);
                                                holder.btn_status.setText("Completed");
                                                holder.btn_status.setVisibility(View.VISIBLE);
                                                addNotifications(user.getCustomerId(),FirebaseAuth.getInstance().getCurrentUser().getUid(),"Accepted");
                                                FirebaseDatabase.getInstance().getReference("emails")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getCustomerName()).child("status").setValue("Confirm");

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
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

        });
        holder.btn_refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Refuse").setMessage("Do you refuse this request of" + user.getCustomerName()+ "?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("emails")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getCustomerName()).child("status").setValue("refused");

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String nameOfSender = snapshot.child("name").getValue().toString();
                                        String email = snapshot.child("email").getValue().toString();
                                        String phone = snapshot.child("idNumber").getValue().toString();
                                        String blood = snapshot.child("bloodGroup").getValue().toString();

                                        String mEmail = user.getCustomerEmail();
                                        String mSubject = "BLOOD DONATION";
                                        String mMessage = "Hello " + user.getCustomerName()+ ", "+nameOfSender+" have refuse to your blood donation schedule :\n"
                                                +" and this is his/her details : \n"
                                                + "Name: "+nameOfSender+"\n"+
                                                "Phone Number: " +phone+"\n"+
                                                "Email: "+email + "\n"+
                                                "Blood Group: "+blood +"\n"+
                                                "Time: " + user.getTime() +"\n"+
                                                "Hospital: " + user.getHospitalName() + "\n"+
                                                "Hospital Address: " + user.getHospitalAddress() +"\n"+
                                                "Sorry about this. Thank you!\n"+
                                                "BLOOD DONATION APP -- DONATE BLOOD, SAVE LIVES";
                                        javaMailApi JavaMaikApi = new javaMailApi(context,mEmail,mSubject,mMessage);
                                        JavaMaikApi.execute();

                                        holder.btn_confirm.setVisibility(View.GONE);
                                        holder.btn_refuse.setVisibility(View.GONE);
                                        holder.btn_status.setText("Refused");
                                        holder.btn_status.setVisibility(View.VISIBLE);
                                        addNotifications(user.getCustomerId(),FirebaseAuth.getInstance().getCurrentUser().getUid(),"Refused");
                                        FirebaseDatabase.getInstance().getReference("emails")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(user.getCustomerName()).child("status").setValue("refused");

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
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView userProfileImage;
        private TextView txt_hospital_name,txt_booking_time_text,txt_booking_location_text,txt_hospital_web,txt_hospital_phone_text,txt_hospital_open_hours,txt_hospital_address_text;
        private Button btn_confirm,btn_refuse,btn_status;
        public Button emailNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_hospital_name= itemView.findViewById(R.id.txt_hospital_name);
            txt_booking_time_text= itemView.findViewById(R.id.txt_booking_time_text);
            txt_booking_location_text= itemView.findViewById(R.id.txt_booking_location_text);
            txt_hospital_web= itemView.findViewById(R.id.txt_hospital_web);
            txt_hospital_phone_text= itemView.findViewById(R.id.txt_hospital_phone_text);
            txt_hospital_open_hours= itemView.findViewById(R.id.txt_hospital_open_hours);
            txt_hospital_address_text=itemView.findViewById(R.id.txt_hospital_address_text);

            btn_confirm = itemView.findViewById(R.id.btn_confirm);
            btn_refuse = itemView.findViewById(R.id.btn_refuse);
            btn_status = itemView.findViewById(R.id.btn_status);

        }
    }

    private void  addNotifications(String receivedId, String senderId, String status){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("notifications").child(receivedId);
        String date = DateFormat.getDateInstance().format(new Date());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("receivedId",receivedId);
        hashMap.put("senderId",senderId);
        hashMap.put("text","Sent you an email, kindly check it out!");
        hashMap.put("date",date);
        hashMap.put("status",status);

        reference.push().setValue(hashMap);
    }
}

