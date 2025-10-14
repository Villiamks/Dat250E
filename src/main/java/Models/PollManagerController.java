package Models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/PollManager")
public class PollManagerController {

    @Autowired
    private PollManager pollManager;

    //User:

    @GetMapping
    public List<User> getAllUsers() {
        return pollManager.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = pollManager.getUserById(id);
        if (user != null) return ResponseEntity.ok(user);
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return pollManager.createUser(user);
    }

    @DeleteMapping
    public boolean deleteUser(@RequestBody Long id) {
        return pollManager.deleteUser(id);
    }

    //Polls:

    @GetMapping
    public List<Poll> getAllPolls() {
        return pollManager.getAllPolls();
    }

    @PostMapping
    public Poll createPoll(@RequestBody Poll poll) {
        return pollManager.createPoll(poll);
    }

    @DeleteMapping
    public void deletePoll(@RequestBody Poll poll) {
        pollManager.deletePoll(poll);
    }

    //Votes:

    @PostMapping
    public void vote(@RequestBody Vote vote) {
        pollManager.vote(vote);
    }

    @GetMapping
    public List<Vote> getAllVotes() {
        return pollManager.getAllVotes();
    }

    @PutMapping
    public void changeVote(@RequestBody Vote vote, @RequestBody Vote old) {
        pollManager.changeVote(vote, old);
    }
}
