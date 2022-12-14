package com.example.test.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Common.Common;
import com.example.test.Interface.RecycleItemSelected;
import com.example.test.Model.TimeSlot;
import com.example.test.R;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {
    Context context;
    List<TimeSlot> timeSLotList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSLotList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSLotList = timeSlotList;
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_time_slot,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSLotToString(position)).toString());
        if(timeSLotList.size()==0) //If all position is available ,just show list
        {
            holder.card_time_slot.setCardBackgroundColor(context.getResources()
                    .getColor(R.color.white));
            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources()
            .getColor(R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources()
            .getColor(R.color.black));

        }else
        {
            for (TimeSlot slotValue : timeSLotList)
            {
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if (slot==position)
                {
                    holder.card_time_slot.setTag(Common.DISABLE_TAG);
                    holder.card_time_slot.setCardBackgroundColor(context.getResources()
                    .getColor(R.color.gray));
                    holder.txt_time_slot_description.setText("Full");
                    holder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(R.color.white));
                    holder.txt_time_slot.setTextColor(context.getResources()
                            .getColor(R.color.white));
                }else
                {
                    holder.card_time_slot.setCardBackgroundColor(context.getResources()
                            .getColor(R.color.white));
                    holder.txt_time_slot_description.setText("Available");
                    holder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(R.color.black));
                    holder.txt_time_slot.setTextColor(context.getResources()
                            .getColor(R.color.black));
                }
            }
        }
        if (!cardViewList.contains(holder.card_time_slot))
        {
            cardViewList.add(holder.card_time_slot);
        }

            holder.setRecycleItemSelected(new RecycleItemSelected() {
                @Override
                public void onItemSelectedListener(View view, int pos) {
                    for (CardView cardView: cardViewList)
                    {
                        if (cardView.getTag()==null)
                        {
                            cardView.setCardBackgroundColor(context.getResources()
                                    .getColor(R.color.white));
                        }
                    }
                    Common.currentTimeSlot=pos;
                    holder.card_time_slot.setCardBackgroundColor(context.getResources()
                            .getColor(R.color.teal_200));

                    Intent i = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                    i.putExtra(Common.KEY_TIME_SLOT,pos);
                    i.putExtra(Common.KEY_STEP,3);
                    localBroadcastManager.sendBroadcast(i);
                }
            });

    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;

        RecycleItemSelected recycleItemSelected;

        public RecycleItemSelected getRecycleItemSelected() {
            return recycleItemSelected;
        }

        public void setRecycleItemSelected(RecycleItemSelected recycleItemSelected) {
            this.recycleItemSelected = recycleItemSelected;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description= itemView.findViewById(R.id.txt_time_slot_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            recycleItemSelected.onItemSelectedListener(view, getAdapterPosition());
        }
    }
}
