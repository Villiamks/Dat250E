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
    private final HashMap<Long, VoteOption> voteOptions = new HashMap<>();
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
        if (id == 0){
            return null;
        }
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
        //poll.getCreator().getCreatedPolls().add(poll);
        return poll;
    }

    public void deletePoll(long id) {
        Poll poll = getPollById(id);
        polls.remove(id);
        User creator = poll.getCreator();
        List<Poll> tmp = creator.getCreatedPolls();
        tmp.remove(poll);
        creator.setCreatedPolls(tmp);
    }

    public Poll insertOptions(Poll poll, List<String> options) {
        int y = poll.getVoteOptions().size();
        for (int i = 0; i < options.size(); i++) {
            createVoteOption(options.get(i), y+i, poll);
        }
        updatePoll(poll);
        return poll;
    }

    private void updatePoll(Poll poll){
        List<Vote> ny = new ArrayList<>();
        List<VoteOption>  options = new ArrayList<>();

        for (long i = 0; i <= votes.size(); i++){
            Vote tmp = votes.get(i);
            if (tmp != null && tmp.getVoteOption().getPoll().getId().equals(poll.getId() )){
                ny.add(tmp);
            }
        }
        for (long i = 0; i <= voteOptions.size(); i++){
            VoteOption tmp = voteOptions.get(i);
            if (tmp != null && tmp.getPoll().getId().equals(poll.getId())){
                options.add(tmp);
            }
        }
        poll.setVoteOptions(options);
        poll.setVotes(ny);
    }

    //Votes:

    public Vote vote(User user, VoteOption voteOption) {

        if (user != null && voteOption.getPoll().getVoteByUser(user) != null){
            return changeVote(user, voteOption);
        }

        Vote ny = new Vote(user, voteOption);

        ny.setId(voteIdCounter.getAndIncrement());
        votes.put(ny.getId(), ny);

        Poll poll = voteOption.getPoll();
        poll.getVotes().add(ny);
        updatePoll(poll);
        return ny;
    }

    public List<Vote> getAllVotes() {
        return new ArrayList<>(votes.values());
    }

    public Vote getVoteById(long id) {
        return votes.get(id);
    }

    public Vote changeVote(User user, VoteOption vo) {
        if (user != null){
            Vote old = vo.getPoll().getVoteByUser(user);
            Vote ny = new Vote(user, vo);
            ny.setId(old.getId());
            votes.remove(old.getId());
            votes.put(ny.getId(), ny);
            updatePoll(vo.getPoll());
            return ny;
        }
        return  null;
    }

    // VoteOption;

    public List<VoteOption> getAllVoteOptions() {
        return new ArrayList<>(voteOptions.values());
    }

    public VoteOption getVoteOptionById(long id) {
        return voteOptions.get(id);
    }

    public void createVoteOption(String caption, int pO, Poll poll){
        VoteOption ny = new VoteOption(caption, pO, poll);
        ny.setId(voteOptionIdCounter.getAndIncrement());
        voteOptions.put(ny.getId(), ny);
    }

    public void deleteVoteOption(long id) {
        voteOptions.remove(id);
    }
}
