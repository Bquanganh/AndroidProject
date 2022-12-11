package com.example.test.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.test.Common.Common;
import com.example.test.Model.TimeSlot;
import com.example.test.R;

import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyViewHolder> {
    Context context;
    List<TimeSlot> timeSLotList;

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
                    holder.card_time_slot.setCardBackgroundColor(context.getResources()
                    .getColor(R.color.purple_200));
                    holder.txt_time_slot_description.setText("Full");
                    holder.txt_time_slot_description.setTextColor(context.getResources()
                            .getColor(R.color.white));
                    holder.txt_time_slot.setTextColor(context.getResources()
                            .getColor(R.color.white));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description= itemView.findViewById(R.id.txt_time_slot_description);

        }
    }
}
