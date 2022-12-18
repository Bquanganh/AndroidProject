package com.example.test.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.example.test.BookingDonationActivity;
import com.example.test.BookingFor2UsersActivity;
import com.example.test.Common.Common;
import com.example.test.Email.javaMailApi;
import com.example.test.Model.User;
import com.example.test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context context;
    private List<User> userList;




    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;

    }
    public void setData(List<User> list){
        this.userList=list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_displayed_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = userList.get(position);


        holder.type.setText(user.getType());

        if("donor".equalsIgnoreCase(user.getType())){
            holder.emailNow.setVisibility(View.VISIBLE);
        }
        holder.userEmail.setText(user.getEmail());
        holder.userName.setText(user.getName());
        holder.userIdNumber.setText(user.getIdNumber());
        holder.userBloodGroup.setText(user.getBloodGroup());


        Glide.with(context).load(user.getProfilepictureurl()).error(R.mipmap.ic_launcher).into(holder.userProfileImage);

        final String namOfTheReceiver = user.getName();
        final String idOfTheReceiver = user.getId();

        holder.emailNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookingDonationActivity.class);
                intent.putExtra("idOfRecipient",user.getId());
                holder.emailNow.setText(Common.statusEmail);
                context.startActivity(intent);
//                new AlertDialog.Builder(context)
//                        .setTitle("SEND EMAIL").setMessage("Send mail to" + user.getName()+ "?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
//                                        .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                reference.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                        String nameOfSender = snapshot.child("name").getValue().toString();
//                                        String email = snapshot.child("email").getValue().toString();
//                                        String phone = snapshot.child("idNumber").getValue().toString();
//                                        String blood = snapshot.child("bloodGroup").getValue().toString();
//
//                                        String mEmail = user.getEmail();
//                                        String mSubject = "BLOOD DONATION";
//                                        String mMessage = "Hello " + namOfTheReceiver+ ", "+nameOfSender+" would like blood donation from you. Here's his/her detail:\n"
//                                                + "Name: "+nameOfSender+"\n"+
//                                                "Phone Number: " +phone+"\n"+
//                                                "Email: "+email + "\n"+
//                                                "Blood Group: "+blood +"\n"+
//                                                "Kindly Reach out to him/her. Thank you!\n"+
//                                                "BLOOD DONATION APP -- DONATE BLOOD, SAVE LIVES";
//                                        javaMailApi JavaMaikApi = new javaMailApi(context,mEmail,mSubject,mMessage);
//                                        JavaMaikApi.execute();
//
//                                        DatabaseReference senderRef = FirebaseDatabase.getInstance().getReference("emails")
//                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                        senderRef.child(idOfTheReceiver).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if(task.isSuccessful()){
//                                                    DatabaseReference receiverRef = FirebaseDatabase.getInstance().getReference("emails")
//                                                            .child(idOfTheReceiver);
//                                                    receiverRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(true);
//
//                                                    addNotifications(idOfTheReceiver,FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                                }
//
//                                            }
//                                        });
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError error) {
//
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton("No", null)
//                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class  ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView userProfileImage;
        public TextView type,userName,userEmail,userIdNumber,userBloodGroup;
        public Button emailNow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfileImage = itemView.findViewById(R.id.userProfileImage);
            type = itemView.findViewById(R.id.type);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userIdNumber = itemView.findViewById(R.id.userIdNumber);
            userBloodGroup = itemView.findViewById(R.id.userBloodGroup);
            emailNow = itemView.findViewById(R.id.emailNow);
        }
    }

    private void  addNotifications(String receivedId, String senderId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("notifications").child(receivedId);
        String date = DateFormat.getDateInstance().format(new Date());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("receivedId",receivedId);
        hashMap.put("senderId",senderId);
        hashMap.put("text","Sent you an email, kindly check it out!");
        hashMap.put("date",date);

        reference.push().setValue(hashMap);
    }
}
