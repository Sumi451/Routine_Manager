package com.example.routinemanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.routinemanager.TimetableEntry; // Replace with your package name
import java.util.ArrayList;
import java.util.List;

/**
 * 8. RecyclerView Adapter
 * This adapter is responsible for binding the data from the TimetableEntry list
 * to the individual item views in the RecyclerView.
 */
public class TimetableAdapter extends RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder> {

    private List<TimetableEntry> timetableEntries = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(TimetableEntry entry);
    }

    public TimetableAdapter(OnItemClickListener listener) {
        this.listener = listener;
    }


    // This method is called to create new ViewHolders.
    @NonNull
    @Override
    public TimetableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timetable_entry_item, parent, false);
        return new TimetableViewHolder(itemView);
    }

    // This method binds the data to the ViewHolder.
    @Override
    public void onBindViewHolder(@NonNull TimetableViewHolder holder, int position) {
        if (timetableEntries != null) {
            TimetableEntry currentEntry = timetableEntries.get(position);
            holder.subjectNameTextView.setText(currentEntry.subjectName);
            holder.timeTextView.setText(currentEntry.startTime + " - " + currentEntry.endTime);
            holder.roomNumberTextView.setText("Room: " + currentEntry.roomNumber);
            holder.professorTextView.setText("Prof: " + currentEntry.professor);
        }
    }

    // Returns the total number of items in the data set.
    @Override
    public int getItemCount() {
        return timetableEntries.size();
    }

    // Public method to update the adapter's data.
    public void setTimetableEntries(List<TimetableEntry> entries) {
        this.timetableEntries = entries;
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class
     * Caches the item view components for efficiency.
     */
    class TimetableViewHolder extends RecyclerView.ViewHolder {
        private final TextView subjectNameTextView;
        private final TextView timeTextView;
        private final TextView roomNumberTextView;
        private final TextView professorTextView;

        public TimetableViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.subject_name_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            roomNumberTextView = itemView.findViewById(R.id.room_number_text_view);
            professorTextView = itemView.findViewById(R.id.professor_text_view);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(timetableEntries.get(position));
                }
            });
        }
    }
}

