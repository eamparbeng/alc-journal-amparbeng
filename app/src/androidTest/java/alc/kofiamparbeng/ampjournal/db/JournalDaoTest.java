package alc.kofiamparbeng.ampjournal.db;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Date;
import java.util.List;


import alc.kofiamparbeng.ampjournal.entities.JournalEntry;

import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class JournalDaoTest {
    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private JournalDatabase journalDatabase;
    private JournalDao journalDao;

    @Mock
    private Observer<List<JournalEntry>> mockObserver;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        Context context = InstrumentationRegistry.getTargetContext();
        journalDatabase = Room.inMemoryDatabaseBuilder(context, JournalDatabase.class)
                .allowMainThreadQueries().build();
        journalDao = journalDatabase.journalDao();
    }

    @After
    public void tearDown() throws Exception {
        journalDatabase.close();
    }

    @Test
    public void insert_works() throws Exception {
        // given
        JournalEntry entry = new JournalEntry();
        entry.setTitle("Title");
        entry.setBody("Body");
        entry.setEntryDate(new Date());
        entry.setUpdatedDate(new Date());
        journalDao.getAllEntries().observeForever(mockObserver);
        // when
        journalDao.insert(entry);
        journalDao.getAllEntries();
        // then
        verify(mockObserver).onChanged(Collections.singletonList(entry));
    }

    @Test
    public void select_all_works() throws Exception {
        // given
        journalDao.getAllEntries().observeForever(mockObserver);
        // then
        verify(mockObserver).onChanged(Collections.<JournalEntry>emptyList());
    }
}
