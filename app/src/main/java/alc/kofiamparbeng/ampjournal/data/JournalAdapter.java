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

    private static final SimpleDateFormat simpleDateFormatDayName = new SimpleDateFormat("EEE");
    private static final SimpleDateFormat simpleDateFormatShortDate = new SimpleDateFormat("dd MMM");

    private JournalEntryClickListener mEntryClickListener;

    public JournalAdapter(Context context) {
        mInfater = LayoutInflater.from(context);
    }

    public void setJournalEntryClickListener(JournalEntryClickListener newListener) {
        mEntryClickListener = newListener;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mJournalSubjectTextView;
        private final TextView mJournalBodyTextView;
        private final TextView mJournalDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mJournalBodyTextView = itemView.findViewById(R.id.tv_journal_body);
            mJournalSubjectTextView = itemView.findViewById(R.id.tv_journal_title);
            mJournalDateTextView = itemView.findViewById(R.id.tv_journal_date);

            itemView.setOnClickListener(this);
        }

        public void doViewBindings(int posittion, JournalEntry dataItem) {
            mJournalSubjectTextView.setText(dataItem.getTitle());
            mJournalBodyTextView.setText(dataItem.getBody());

            mJournalDateTextView.setText(formatJournalEntryDate(dataItem.getEntryDate()));
        }

        private String formatJournalEntryDate(Date entryDate) {
            Date now = new Date();
            Calendar weekAgoCalendar = Calendar.getInstance();
            Calendar entryCalendar = Calendar.getInstance();
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(now);
            todayCalendar.set(todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DATE), 0, 0, 0);
            entryCalendar.setTime(entryDate);
            weekAgoCalendar.setTime(now);
            weekAgoCalendar.add(Calendar.DATE, -7);
            if (todayCalendar.compareTo(entryCalendar) <= 0) {
                return "Today";
            } else if (weekAgoCalendar.compareTo(entryCalendar) <= 0) {
                return simpleDateFormatDayName.format(entryDate);
            } else {
                return simpleDateFormatShortDate.format(entryDate);
            }
        }

        @Override
        public void onClick(View view) {
            if (mEntryClickListener != null) {
                JournalEntry currentEntry = mEntries.get(getAdapterPosition());
                mEntryClickListener.onJournalEntryClicked(currentEntry.getId());
            }
        }
    }

    public interface JournalEntryClickListener {
        void onJournalEntryClicked(int journalEntryId);
    }
}
