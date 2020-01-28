package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    @Autowired
    JdbcTimeEntryRepository jdbcTimeEntryRepository;

    public TimeEntryController() {
    }

    public TimeEntryController(JdbcTimeEntryRepository jdbcTimeEntryRepository) {
        this.jdbcTimeEntryRepository = jdbcTimeEntryRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable(value = "id") long timeEntryId) {
        TimeEntry timeEntry = jdbcTimeEntryRepository.find(timeEntryId);
        if (timeEntry == null) {
            return new ResponseEntity<>(timeEntry, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(timeEntry);
    }

    @GetMapping("")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntryList = jdbcTimeEntryRepository.list();
        return ResponseEntity.ok(timeEntryList);
    }

    @PostMapping("")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry timeEntry = jdbcTimeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity(timeEntry, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(value = "id") long timeEntryId, @RequestBody TimeEntry timeEntryToUpdate) {
        TimeEntry timeEntry = jdbcTimeEntryRepository.update(timeEntryId, timeEntryToUpdate);
        if (timeEntry == null) {
            return new ResponseEntity<>(timeEntry, HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(timeEntry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") long timeEntryId) {
        jdbcTimeEntryRepository.delete(timeEntryId);
        TimeEntry timeEntry = jdbcTimeEntryRepository.find(timeEntryId);
        if (timeEntry == null) {
            return new ResponseEntity<>(timeEntry, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
