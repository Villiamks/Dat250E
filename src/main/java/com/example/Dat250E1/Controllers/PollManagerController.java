package com.example.Dat250E1.Controllers;

import com.example.Dat250E1.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class PollManagerController {

    @Autowired
    private PollManager pollManager;

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hello " + name;
    }

    //User:

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return pollManager.getAllUsers();
    }

    @GetMapping("/api/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        User user = pollManager.getUserById(id);
        if (user != null) return ResponseEntity.ok(user);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/api/users")
    public User createUser(@RequestBody User user) {
        return pollManager.createUser(user);
    }

    @DeleteMapping("/api/users/{id}")
    public boolean deleteUser(@PathVariable long id) {
        return pollManager.deleteUser(id);
    }

    //Polls:

    @GetMapping("/api/polls")
    public List<Poll> getAllPolls() {
        return pollManager.getAllPolls();
    }

    @GetMapping("/api/polls/{id}")
    public Poll getPollById(@PathVariable long id){
        return pollManager.getPollById(id);
    }

    @PostMapping("/api/polls")
    public Poll createPoll(@RequestBody Poll poll) {
        return pollManager.createPoll(poll);
    }

    @DeleteMapping("/api/polls")
    public void deletePoll(@RequestBody Poll poll) {
        pollManager.deletePoll(poll);
    }

    //Votes:

    @PostMapping("/api/votes")
    public void vote(@RequestBody User user, @RequestBody VoteOption voteOption) {
        pollManager.vote(user, voteOption);
    }

    @GetMapping("/api/votes")
    public List<Vote> getAllVotes() {
        return pollManager.getAllVotes();
    }

    @GetMapping("/api/votes/{id}")
    public Vote getVoteById(@PathVariable long id) {
        return pollManager.getVoteById(id);
    }

    @PutMapping("/api/votes")
    public void changeVote(@RequestBody User user, @RequestBody VoteOption voteOption) {
        pollManager.changeVote(user, voteOption);
    }
}
