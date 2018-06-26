package alc.kofiamparbeng.io.ampjournal.data;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import alc.kofiamparbeng.io.ampjournal.R;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.ViewHolder> {

    public JournalAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_list_item_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.doViewBindings(position, new Object());
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mJournalSubjectTextView;
        private final  TextView mJournalBodyTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            mJournalBodyTextView=itemView.findViewById(R.id.tv_journal_body);
            mJournalSubjectTextView=itemView.findViewById(R.id.tv_journal_title);
        }

        public void doViewBindings(int posittion, Object dataItem){
            mJournalSubjectTextView.setText(String.format("%d.",posittion));
            mJournalBodyTextView.setText("Yadayaydyayayayayayayayayyayayayayayyayayayyayayayayayayyayayayayayayayayayayayayayayayayayayayayayayayayayayaya");
        }
    }
}
