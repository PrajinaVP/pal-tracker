package io.pivotal.pal.tracker;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;

        this.timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable(value = "id") long timeEntryId) {
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if (timeEntry == null) {
            return new ResponseEntity<>(timeEntry, HttpStatus.NOT_FOUND);
        }
        actionCounter.increment();
        return ResponseEntity.ok(timeEntry);
    }

    @GetMapping("")
    public ResponseEntity<List<TimeEntry>> list() {
        actionCounter.increment();
        return new ResponseEntity<List<TimeEntry>>(timeEntryRepository.list(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        return new ResponseEntity(createdTimeEntry, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable(value = "id") long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(timeEntryId, timeEntryToUpdate);
        if (updatedTimeEntry == null) {
            return new ResponseEntity<>(updatedTimeEntry, HttpStatus.NOT_FOUND);
        }

        actionCounter.increment();
        return new ResponseEntity<>(updatedTimeEntry, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if (timeEntry == null) {
            actionCounter.increment();
            return new ResponseEntity<>(timeEntry, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
