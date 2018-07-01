package alc.kofiamparbeng.ampjournal.data;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.PluralsRes;
import android.support.v4.content.ContextCompat;
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
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {
    private LayoutInflater mInfater;
    private List<JournalEntry> mEntries;
    private List<JournalFolder> mFolders;

    private static final SimpleDateFormat simpleDateFormatDayName = new SimpleDateFormat("EEEE");
    private static final SimpleDateFormat simpleDateFormatShortDate = new SimpleDateFormat("dd MMM");
    private static final SimpleDateFormat simpleDateFormatFullDate = new SimpleDateFormat("dd MMM yy");

    private JournalEntryEventListener mEntryEventListener;
    private final Context context;

    public JournalAdapter(Context context) {
        mInfater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setJournalEntryClickListener(JournalEntryEventListener newListener) {
        mEntryEventListener = newListener;
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
            holder.doViewBindings(position, current, context);
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

    public void setFolders(List<JournalFolder> folders) {
        mFolders = folders;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mJournalSubjectTextView;
        private final TextView mFolderNameTextView;
        private final TextView mJournalDateTextView;
        private final TextView mFullJournalDateTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mFolderNameTextView = itemView.findViewById(R.id.tv_journal_folder);
            mJournalSubjectTextView = itemView.findViewById(R.id.tv_journal_title);
            mJournalDateTextView = itemView.findViewById(R.id.tv_journal_date);
            mFullJournalDateTextView = itemView.findViewById(R.id.tv_date_full);

            itemView.setOnClickListener(this);
        }

        public void doViewBindings(int posittion, JournalEntry dataItem, Context context) {
            mJournalSubjectTextView.setText(dataItem.getTitle());
            mFolderNameTextView.setText(dataItem.getFolderName());

            mJournalDateTextView.setText(formatJournalEntryDate(dataItem.getEntryDate()));
            mFullJournalDateTextView.setText(simpleDateFormatFullDate.format(dataItem.getEntryDate()));

           /* int folderColor = ContextCompat.getColor(context, getFolderColor(dataItem.getFolderName()));
            mFolderNameTextView.setBackground(new ColorDrawable(folderColor));*/
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
            } else {
                return simpleDateFormatDayName.format(entryDate);
            }
        }

        @Override
        public void onClick(View view) {
            if (mEntryEventListener != null) {
                JournalEntry currentEntry = mEntries.get(getAdapterPosition());
                mEntryEventListener.onJournalEntryClicked(currentEntry.getId());
            }
        }

        public void onDelete() {
            int position = getAdapterPosition();
            JournalEntry currentEntry = mEntries.get(position);
            mEntryEventListener.onJournalEntrySwipedLeft(currentEntry, position);
        }
    }

    private int getFolderColor(String folderName) {
        int position = 0;
        for (int i = 0; i < mFolders.size(); i++) {
            if (mFolders.get(i).getName().equals(folderName)) {
                position = i;
                break;
            }
        }
        int backGroundColor = R.color.colorFolder1;

        switch (position % 10) {
            case 1:
                backGroundColor = R.color.colorFolder2;
                break;
            case 2:
                backGroundColor = R.color.colorFolder3;
                break;
            case 3:
                backGroundColor = R.color.colorFolder4;
                break;
            case 4:
                backGroundColor = R.color.colorFolder5;
                break;
            case 5:
                backGroundColor = R.color.colorFolder6;
                break;
            case 6:
                backGroundColor = R.color.colorFolder7;
                break;
            case 7:
                backGroundColor = R.color.colorFolder8;
                break;
            case 8:
                backGroundColor = R.color.colorFolder9;
                break;
            case 9:
                backGroundColor = R.color.colorFolder10;
                break;
        }
        return backGroundColor;
    }

    public interface JournalEntryEventListener {
        void onJournalEntryClicked(int journalEntryId);

        void onJournalEntrySwipedLeft(JournalEntry journalEntry, int position);
    }
}
