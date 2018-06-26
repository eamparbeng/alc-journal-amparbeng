package alc.kofiamparbeng.ampjournal.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    private LayoutInflater mInfater;
    private List<JournalEntry> mEntries;

    public JournalAdapter(Context context) {
        mInfater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInfater.inflate(R.layout.journal_list_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mEntries != null) {
            JournalEntry current = mEntries.get(position);
            holder.doViewBindings(position, current);
        } else {
            // Covers the case of data not being ready yet.
            //holder.wordItemView.setText("No Word");
        }

    }

    @Override
    public int getItemCount() {
        return mEntries != null ? mEntries.size() : 0;
    }

    public void setEntries(List<JournalEntry> entries) {
        mEntries = entries;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mJournalSubjectTextView;
        private final TextView mJournalBodyTextView;
        private final TextView mJournalDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mJournalBodyTextView = itemView.findViewById(R.id.tv_journal_body);
            mJournalSubjectTextView = itemView.findViewById(R.id.tv_journal_title);
            mJournalDateTextView = itemView.findViewById(R.id.tv_journal_date);

        }

        public void doViewBindings(int posittion, JournalEntry dataItem) {
            mJournalSubjectTextView.setText(dataItem.getTitle());
            mJournalBodyTextView.setText(dataItem.getBody());

            mJournalDateTextView.setText(formatJournalEntryDate(dataItem.getDate()));
        }

        private String formatJournalEntryDate(Date entryDate) {
            Date now = new Date();
            Calendar weekAgoCalendar = Calendar.getInstance();
            Calendar entryCalendar = Calendar.getInstance();
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(now);
            todayCalendar.set(todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DATE),0,0,0);
            entryCalendar.setTime(entryDate);
            weekAgoCalendar.setTime(now);
            weekAgoCalendar.add(Calendar.DATE, -7);
            if (todayCalendar.compareTo( entryCalendar)<=0){
                return "Today";
            }else if (weekAgoCalendar.compareTo( entryCalendar)<=0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE");
                return simpleDateFormat.format(entryDate);
            }
            else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM");
                return simpleDateFormat.format(entryDate);
            }
        }
    }
}
