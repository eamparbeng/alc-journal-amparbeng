package alc.kofiamparbeng.io.ampjournal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import alc.kofiamparbeng.io.ampjournal.data.JournalAdapter;

public class MainActivity extends AppCompatActivity {
    private JournalAdapter mJournalAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareRecyclerView();
    }

    private  void prepareRecyclerView(){
        mJournalAdapter = new JournalAdapter();
        mRecyclerView = (RecyclerView)findViewById(R.id.journal_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mJournalAdapter);
        mRecyclerView.setLayoutManager(layoutManager);
    }
}
