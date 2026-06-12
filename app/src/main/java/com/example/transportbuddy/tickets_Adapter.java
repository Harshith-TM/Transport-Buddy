package com.example.transportbuddy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class tickets_Adapter extends RecyclerView.Adapter<tickets_Adapter.TicketViewHolder> {
    Context context;
    List<busTicketDetails> ticketList;

    public tickets_Adapter(Context context, List<busTicketDetails> ticketList) {
        this.context = context;
        this.ticketList = ticketList;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rcview_tickets, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tickets_Adapter.TicketViewHolder holder, int position) {
        busTicketDetails ticket = ticketList.get(position);

        holder.time.setText(ticket.getBT_time());
        holder.date.setText(ticket.getBT_date());
        String capsFrom = capitalizeWord(ticket.getBT_fromPlace());
        holder.fromP.setText(capsFrom);
        String capsTo = capitalizeWord(ticket.getBT_toPlace());
        holder.toP.setText(capsTo);
        String fare1 = "Rs:"+ticket.getBT_amount();
        holder.fare.setText(fare1);
        holder.mode.setText(ticket.getMode());
        holder.ticketId.setText(ticket.getBT_Id());

        holder.itemView.setOnClickListener(v->{
            Intent intent = new Intent(context, activity_Bus_Ticket.class);
            intent.putExtra("BTId", ticket.getBT_Id());
            intent.putExtra("BTTime", ticket.getBT_time());
            intent.putExtra("BTDate", ticket.getBT_date());
            intent.putExtra("BTFrom", ticket.getBT_fromPlace());
            intent.putExtra("BTTo", ticket.getBT_toPlace());
            intent.putExtra("BTFare", ticket.getBT_amount());
            intent.putExtra("BTNumber", ticket.getBT_busNum());
            intent.putExtra("BTCount", ticket.getBT_count());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView time,date,fromP,toP,fare,mode,ticketId;

        public TicketViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.rc_txt_time1);
            date = itemView.findViewById(R.id.rc_txt_date1);
            fromP = itemView.findViewById(R.id.rc_txt_place1);
            toP = itemView.findViewById(R.id.rc_txt_place2);
            fare = itemView.findViewById(R.id.rc_txt_fare1);
            mode = itemView.findViewById(R.id.rc_txt_mode1);
            ticketId = itemView.findViewById(R.id.rc_txt_ticketId);
        }
    }

    private String capitalizeWord(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        String[] words = input.toLowerCase().split("\\s+");
        StringBuilder capitalized = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return capitalized.toString().trim();
    }
}
