package com.example.Dat250E1.Models;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class PollManager {
    private final HashMap<Long, User> users = new HashMap<>();
    private final HashMap<Long, Poll> polls = new HashMap<>();
    private final HashMap<Long, Vote> votes = new HashMap<>();
    private final AtomicLong userIdCounter = new AtomicLong(1);
    private final AtomicLong pollIdCounter = new AtomicLong(1);
    private final AtomicLong voteIdCounter = new AtomicLong(1);
    private final AtomicLong voteOptionIdCounter = new AtomicLong(1);

    // User:

    public User createUser(User user) {
        user.setId(userIdCounter.getAndIncrement());
        users.put(user.getId(), user);
        return user;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(long id) {
        return users.get(id);
    }

    public boolean deleteUser(long id) {
        return users.remove(id) != null;
    }

    //Polls:

    public List<Poll> getAllPolls() {
        return new ArrayList<>(polls.values());
    }

    public Poll getPollById(long id){
        return polls.get(id);
    }

    public Poll createPoll(Poll poll) {
        poll.setId(pollIdCounter.getAndIncrement());
        for (VoteOption option : poll.getVoteOptions()) {
            option.setId(voteOptionIdCounter.getAndIncrement());
            option.setPoll(poll);
        }
        polls.put(poll.getId(), poll);
        poll.getCreator().getCreatedPolls().add(poll);
        return poll;
    }

    public void deletePoll(Poll poll) {
        polls.remove(poll.getId());
        User creator = poll.getCreator();
        List<Poll> tmp = creator.getCreatedPolls();
        tmp.remove(poll);
        creator.setCreatedPolls(tmp);
    }
    private void updatePoll(Poll poll){
        List<Vote> ny = new ArrayList<>();

        for (long i = 0; i <= votes.size(); i++){
            Vote tmp = votes.get(i);
            if (tmp != null && tmp.getVoteOption().getPoll().getId().equals(poll.getId() )){
                ny.add(tmp);
            }
        }
        poll.setVotes(ny);
    }

    //Votes:

    public void vote(User user, VoteOption voteOption) {

        if (user != null && voteOption.getPoll().getVoteByUser(user) != null){
            changeVote(user, voteOption);
        } else {

            Vote ny = new Vote(user, voteOption);

            ny.setId(voteIdCounter.getAndIncrement());
            votes.put(ny.getId(), ny);

            Poll poll = voteOption.getPoll();
            poll.getVotes().add(ny);
        }
    }

    public List<Vote> getAllVotes() {
        return new ArrayList<>(votes.values());
    }

    public Vote getVoteById(long id) {
        return votes.get(id);
    }

    public void changeVote(User user, VoteOption vo) {
        if (user != null){
            Vote old = vo.getPoll().getVoteByUser(user);
            Vote ny = new Vote(user, vo);
            ny.setId(old.getId());
            votes.remove(old.getId());
            votes.put(ny.getId(), ny);
            updatePoll(vo.getPoll());
        }
    }
}
