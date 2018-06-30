package alc.kofiamparbeng.ampjournal.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.entities.JournalEntry;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private LayoutInflater mInfater;
    private List<JournalFolder> mFolders;

    private FolderClickListener mFolderClickListener;

    public FolderAdapter(Context context) {
        mInfater = LayoutInflater.from(context);
    }

    public void setFolderClickListener(FolderClickListener newListener) {
        mFolderClickListener = newListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInfater.inflate(R.layout.folder_list_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mFolders != null) {
            JournalFolder current = mFolders.get(position);
            holder.doViewBindings(position, current);
        } else {
            // Covers the case of data not being ready yet.
            //holder.wordItemView.setText("No Word");
        }

    }

    @Override
    public int getItemCount() {
        return mFolders != null ? mFolders.size() : 0;
    }

    public void setFolders(List<JournalFolder> folders) {
        mFolders = folders;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView mFolderNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mFolderNameTextView = itemView.findViewById(R.id.tv_folder_name);

            itemView.setOnClickListener(this);
        }

        public void doViewBindings(int posittion, JournalFolder dataItem) {
            mFolderNameTextView.setText(dataItem.getName());
        }

        @Override
        public void onClick(View view) {
            if (mFolderClickListener != null) {
                JournalFolder currentEntry = mFolders.get(getAdapterPosition());
                mFolderClickListener.onFolderClicked(currentEntry.getName());
                mFolderClickListener.onFolderClickedId(currentEntry.getId());
            }
        }
    }

    public interface FolderClickListener {
        void onFolderClicked(String folderName);
        void onFolderClickedId(int folderId);
    }
}
