package com.example.writer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IntegerRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.writer.Notes_click_UI;
import com.example.writer.R;
import com.example.writer.moduls.Notes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesList extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<Notes> list;
    Notes_click_UI listener;

    public NotesList(Context context, List<Notes> list, Notes_click_UI listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.note_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.title.setSelected(true);

        holder.note.setText(list.get(position).getNote());


        holder.date.setText(list.get(position).getDate());
        holder.date.setSelected(true);

        if (list.get(position).isPinned()) {
            holder.pinned.setImageResource(R.drawable.circle_pin);
        } else {
            holder.pinned.setImageResource(R.color.light_blue);
            holder.pinned.setColorFilter(ContextCompat.getColor(context, android.R.color.transparent));
        }
        int color = getColor();
        holder.notes_container.setCardBackgroundColor(holder.itemView.getResources().getColor(color, null));

        holder.notes_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
                return true;
            }
        });
    }

    private int getColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.light_blue);
        Random random = new Random();
        int not_random_color = random.nextInt(colorCode.size());
        //установка цвета
        return colorCode.get(not_random_color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> filtered_list) {
        list = filtered_list;
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notes_container;
    TextView title;
    TextView note;
    TextView date;
    ImageView pinned;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_container = itemView.findViewById(R.id.notes_container);
        note = itemView.findViewById(R.id.note);
        title = itemView.findViewById(R.id.text_title);
        date = itemView.findViewById(R.id.date);
        pinned = itemView.findViewById(R.id.pinned);
    }
}