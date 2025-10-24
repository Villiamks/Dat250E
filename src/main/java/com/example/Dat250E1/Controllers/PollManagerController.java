package com.example.Dat250E1.Controllers;

import com.example.Dat250E1.Models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class PollManagerController {

    @Autowired
    private PollManager pollManager;

    //User:

    @GetMapping("/api/users")
    public List<Users> getAllUsers() {
        return pollManager.getAllUsers();
    }

    @GetMapping("/api/users/find")
    public Users getUserById(@RequestParam(value = "id") int id) {
        Users user = pollManager.getUserById(id);
        if  (user == null) {
            return null;
        }
        return user;
    }

    @PostMapping("/api/users")
    public Users createUser(@RequestBody Map<String, String> inData) {
        Users user = new Users(inData.get("username"), inData.get("password"), inData.get("email"));
        return pollManager.createUser(user);
    }

    @DeleteMapping("/api/users/delete")
    public boolean deleteUser(@PathVariable(value="id") int id) {
        return pollManager.deleteUser(id);
    }


    //Polls:

    @GetMapping("/api/polls")
    public List<Poll> getAllPolls() {
        return pollManager.getAllPolls();
    }

    @GetMapping("/api/polls/find")
    public Poll getPollById(@PathVariable(value="id") int id){
        return pollManager.getPollById(id);
    }

    @PostMapping("/api/polls")
    public Poll createPoll(@RequestBody Map<String, String> inData) {
        Users user = pollManager.getUserById(Integer.parseInt(inData.get("userid")));
        Poll poll = new Poll(inData.get("question"), null, null, user);
        return pollManager.createPoll(poll);
    }

    @PostMapping("/api/polls/insert")
    public Poll insertOptions(@RequestBody Map<String, String> inData) {
        Poll poll = pollManager.getPollById( Integer.parseInt(inData.get("id")) );
        List<String> str = Arrays.asList(inData.get("options").split(",,"));
        pollManager.insertOptions(poll, str);
        return  poll;
    }

    @GetMapping("/api/polls/votes/{pollid}/{voteoptionid}")
    public Integer getVoteCount(@PathVariable(value="pollid") int pollId, @PathVariable(value = "voteoptionid") int voteOptionId ) {
        return pollManager.getVoteCount(pollId, voteOptionId);
    }

    @DeleteMapping("/api/polls/delete")
    public void deletePoll(@PathVariable(value="id") int id) {
        pollManager.deletePoll(id);
    }

    //Votes:

    @PostMapping("/api/votes")
    public String vote(@RequestBody Map<String, Integer> inData ) {
        Integer userId = inData.get("id");
        Integer voteOptionId = inData.get("voteoption");

        if (voteOptionId == null) {
            return "Vote option ID is required";
        }

        VoteOption voteOption = pollManager.getVoteOptionById(voteOptionId);
        if (voteOption == null) {
            return "Vote option ID is required";
        }

        int pollId = voteOption.getPoll().getId();
        pollManager.publishVote(pollId, voteOptionId, userId);
        return "Vote submitted";
    }

    @GetMapping("/api/votes")
    public List<Vote> getAllVotes() {
        return pollManager.getAllVotes();
    }

    @GetMapping("/api/votes/{id}")
    public Vote getVoteById(@PathVariable int id) {
        return pollManager.getVoteById(id);
    }

    @PutMapping("/api/votes")
    public Vote changeVote(@RequestBody Map<String, Integer> inData ) {
        Users user = pollManager.getUserById(inData.get("id"));
        VoteOption voteOption = pollManager.getVoteOptionById(inData.get("voteoption"));
        return pollManager.changeVote(user, voteOption);
    }

    // VoteOption:

    @GetMapping("/api/voteoption")
    public List<VoteOption> getAllVoteOptions() {
        return pollManager.getAllVoteOptions();
    }

    @GetMapping("/api/voteoption/find")
    public VoteOption getVoteOptionById(@RequestParam(value = "id") int id) {
        return pollManager.getVoteOptionById(id);
    }

    @PostMapping("/api/voteoption")
    public void createVoteOption(@RequestBody Map<String, String> inData) {
        Poll poll = pollManager.getPollById( Integer.parseInt(inData.get("id")));
        pollManager.createVoteOption(inData.get("caption"), Integer.parseInt(inData.get("presentationorder")), poll);
    }

    @DeleteMapping("/api/voteoption")
    public void deleteVoteOption(@RequestParam(value = "id") int id) {
        pollManager.deleteVoteOption(id);
    }
}
