package alc.kofiamparbeng.ampjournal.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import alc.kofiamparbeng.ampjournal.R;
import alc.kofiamparbeng.ampjournal.entities.JournalFolder;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.ViewHolder> {
    private LayoutInflater mInfater;
    private List<JournalFolder> mFolders;

    private FolderEventsListener mFolderEventsListener;

    public FolderAdapter(Context context) {
        mInfater = LayoutInflater.from(context);
    }

    public void setFolderClickListener(FolderEventsListener newListener) {
        mFolderEventsListener = newListener;
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
            if (mFolderEventsListener != null) {
                JournalFolder currentEntry = mFolders.get(getAdapterPosition());
                mFolderEventsListener.onFolderClicked(currentEntry.getName());
                mFolderEventsListener.onFolderClickedId(currentEntry.getId());
            }
        }

        public void onDelete(){
            int position = getAdapterPosition();
            JournalFolder currentFolder = mFolders.get(position);
            mFolderEventsListener.onFolderSwipedLeft(currentFolder, position);
        }
    }

    public interface FolderEventsListener {
        void onFolderClicked(String folderName);
        void onFolderClickedId(int folderId);
        void onFolderSwipedLeft(JournalFolder folder, int position);
    }
}
