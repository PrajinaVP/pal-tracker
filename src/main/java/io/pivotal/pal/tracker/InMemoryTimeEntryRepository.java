package io.pivotal.pal.tracker;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;

@Repository

    public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private long timeEntryId;
    HashMap<Long, TimeEntry> timeEntryHashMap = new HashMap<>();

    @Override
    public TimeEntry find(long timeEntryId) {
        TimeEntry timeEntry = timeEntryHashMap.get(timeEntryId);
        return timeEntry;
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {

        if (timeEntry != null) {
            timeEntryId++;
            timeEntry.setId(timeEntryId);
            timeEntryHashMap.put(timeEntryId, timeEntry);
        }

        return timeEntryHashMap.get(timeEntryId);
    }

    @Override
    public TimeEntry update(long timeEntryId, TimeEntry timeEntry) {

        if (timeEntry == null) {
            throw new RuntimeException("Error updating time entry... Null TimeEntry.");
        }

        TimeEntry timeEntryOriginal = timeEntryHashMap.get(timeEntryId);
        if (timeEntryOriginal != null) {
            timeEntry.setId(timeEntryId);
            timeEntryHashMap.replace(timeEntryId, timeEntryOriginal, timeEntry);
        }

        return timeEntryHashMap.get(timeEntryId);
    }

    @Override
    public void delete(long timeEntryId) {

        TimeEntry timeEntryOriginal = timeEntryHashMap.get(timeEntryId);
        if (timeEntryOriginal == null) {
            throw new RuntimeException("Error deleting time entry. TimeEntry with id " + timeEntryId + "not found.");
        }
        timeEntryHashMap.remove(timeEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> timeEntryList = new ArrayList<TimeEntry>(timeEntryHashMap.values());

        return timeEntryList;
    }
}
