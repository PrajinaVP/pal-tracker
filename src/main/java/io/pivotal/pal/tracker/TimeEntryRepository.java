package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.List;

public interface TimeEntryRepository {
    public TimeEntry find(long timeEntryId);


    public TimeEntry create(TimeEntry timeEntry);

    public TimeEntry update(long eq, TimeEntry timeEntry);

    public void delete(long timeEntryId) ;

    public List<TimeEntry> list();
}
