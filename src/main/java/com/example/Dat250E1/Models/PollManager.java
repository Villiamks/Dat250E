package com.example.Dat250E1.Models;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PollManager {
    private final HashMap<Integer, Users> users = new HashMap<>();
    private final HashMap<Integer, Poll> polls = new HashMap<>();
    private final HashMap<Integer, Vote> votes = new HashMap<>();
    private final HashMap<Integer, VoteOption> voteOptions = new HashMap<>();
    private final AtomicInteger userIdCounter = new AtomicInteger(0);
    private final AtomicInteger pollIdCounter = new AtomicInteger(0);
    private final AtomicInteger voteIdCounter = new AtomicInteger(0);
    private final AtomicInteger voteOptionIdCounter = new AtomicInteger(0);

    // User:

    public Users createUser(Users user) {
        user.setId(userIdCounter.getAndIncrement());
        users.put(user.getId(), user);
        return user;
    }

    public List<Users> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public Users getUserById(int id) {
        return users.get(id);
    }

    public boolean deleteUser(int id) {
        return users.remove(id) != null;
    }

    //Polls:

    public List<Poll> getAllPolls() {
        return new ArrayList<>(polls.values());
    }

    public Poll getPollById(int id){
        return polls.get(id);
    }

    public Poll createPoll(Poll poll) {
        poll.setId(pollIdCounter.getAndIncrement());
        polls.put(poll.getId(), poll);
        return poll;
    }

    public void deletePoll(int id) {
        Poll poll = getPollById(id);
        polls.remove(id);
        Users creator = poll.getCreatedBy();
        Map<Integer, Poll> tmp = creator.getCreated();
        tmp.remove(poll.getId());
        creator.setCreated(tmp);
    }

    public Poll insertOptions(Poll poll, List<String> options) {
        int y = poll.getOptions().size();
        for (int i = 0; i < options.size(); i++) {
            createVoteOption(options.get(i), y+i, poll);
        }
        updatePoll(poll);
        return poll;
    }

    private void updatePoll(Poll poll){
        LinkedHashMap<Integer, VoteOption>  options = new LinkedHashMap<>();

        for (int i = 0; i <= voteOptions.size(); i++){
            VoteOption tmp = voteOptions.get(i);
            if (tmp != null && tmp.getPoll().getId().equals(poll.getId())){
                options.put(tmp.getId(), tmp);
            }
        }
        poll.setOptions(options);
        polls.put(poll.getId(), poll);
    }

    //Votes:

    public Vote vote(Users user, VoteOption voteOption) {

        if (user != null && voteOption.getPoll().getVoteByUser(user) != null){
            return changeVote(user, voteOption);
        }

        Vote ny = new Vote(user, voteOption);

        ny.setId(voteIdCounter.getAndIncrement());
        votes.put(ny.getId(), ny);

        voteOption.getVotes().put(ny.getId(), ny);

        Poll poll = voteOption.getPoll();
        updatePoll(poll);
        return ny;
    }

    public List<Vote> getAllVotes() {
        return new ArrayList<>(votes.values());
    }

    public Vote getVoteById(int id) {
        return votes.get(id);
    }

    public Vote changeVote(Users user, VoteOption vo) {
        if (user != null){
            Vote old = vo.getPoll().getVoteByUser(user);
            Vote ny = new Vote(user, vo);
            ny.setId(voteIdCounter.getAndIncrement());

            votes.remove(old.getId());
            votes.put(ny.getId(), ny);
            old.getVotesOn().getVotes().remove(old.getId());
            vo.getVotes().put(ny.getId(), ny);

            updatePoll(vo.getPoll());
            return ny;
        }
        return  null;
    }

    // VoteOption;

    public List<VoteOption> getAllVoteOptions() {
        return new ArrayList<>(voteOptions.values());
    }

    public VoteOption getVoteOptionById(int id) {
        return voteOptions.get(id);
    }

    public void createVoteOption(String caption, int pO, Poll poll){
        VoteOption ny = new VoteOption(caption, pO, poll);
        ny.setId(voteOptionIdCounter.getAndIncrement());
        voteOptions.put(ny.getId(), ny);
    }

    public void deleteVoteOption(int id) {
        voteOptions.remove(id);
    }
}
