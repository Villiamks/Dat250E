package com.example.Dat250E1.Controllers;

import com.example.Dat250E1.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class PollManagerController {

    @Autowired
    private PollManager pollManager;

    //User:

    @GetMapping("/api/users")
    public List<User> getAllUsers() {
        return pollManager.getAllUsers();
    }

    @GetMapping("/api/users/find")
    public User getUserById(@RequestParam(value = "id") long id) {
        User user = pollManager.getUserById(id);
        if  (user == null) {
            return null;
        }
        return user;
    }

    @PostMapping("/api/users")
    public User createUser(@RequestBody Map<String, String> inData) {
        User user = new User(inData.get("username"), inData.get("password"), inData.get("email"));
        return pollManager.createUser(user);
    }

    @DeleteMapping("/api/users/delete")
    public boolean deleteUser(@PathVariable(value="id") long id) {
        return pollManager.deleteUser(id);
    }

    //Polls:

    @GetMapping("/api/polls")
    public List<Poll> getAllPolls() {
        return pollManager.getAllPolls();
    }

    @GetMapping("/api/polls/find")
    public Poll getPollById(@PathVariable(value="id") long id){
        return pollManager.getPollById(id);
    }

    @PostMapping("/api/polls")
    public Poll createPoll(@RequestBody Map<String, String> inData) {
        User user = pollManager.getUserById(Integer.parseInt(inData.get("userid")));
        Poll poll = new Poll(inData.get("question"), null, null, user);
        return pollManager.createPoll(poll);
    }

    @DeleteMapping("/api/polls/delete")
    public void deletePoll(@PathVariable(value="id") long id) {
        pollManager.deletePoll(id);
    }

    //Votes:

    @PostMapping("/api/vote")
    public void vote(@RequestBody Map<String, Integer> inData ) {
        User user = pollManager.getUserById(inData.get("id"));
        VoteOption voteOption = pollManager.getVoteOptionById(inData.get("voteoption"));
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
    public void changeVote(@RequestBody Map<String, Integer> inData ) {
        User user = pollManager.getUserById(inData.get("id"));
        VoteOption voteOption = pollManager.getVoteOptionById(inData.get("voteoption"));
        pollManager.changeVote(user, voteOption);
    }

    // VoteOption:

    @GetMapping("/api/voteoption")
    public List<VoteOption> getAllVoteOptions() {
        return pollManager.getAllVoteOptions();
    }

    @GetMapping("/api/voteoption/find")
    public VoteOption getVoteOptionById(@RequestParam(value = "id") long id) {
        return pollManager.getVoteOptionById(id);
    }

    @PostMapping("/api/voteoption")
    public void createVoteOption(@RequestBody Map<String, String> inData) {
        Poll poll = pollManager.getPollById( Integer.parseInt(inData.get("id")));
        pollManager.createVoteOption(inData.get("caption"), Integer.parseInt(inData.get("presentationorder")), poll);
    }

    @DeleteMapping("/api/voteoption")
    public void deleteVoteOption(@RequestParam(value = "id") long id) {
        pollManager.deleteVoteOption(id);
    }
}
