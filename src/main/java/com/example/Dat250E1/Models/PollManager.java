package com.example.Dat250E1.Models;

import com.example.Dat250E1.DTOs.VoteEvent;
import com.example.Dat250E1.Services.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PollManager {

    @Autowired
    private PollService pollService;

    private final RabbitTemplate rabbitTemplate;

    private final HashMap<Integer, Users> users = new HashMap<>();
    private final HashMap<Integer, Poll> polls = new HashMap<>();
    private final HashMap<Integer, Vote> votes = new HashMap<>();
    private final HashMap<Integer, VoteOption> voteOptions = new HashMap<>();
    private final AtomicInteger userIdCounter = new AtomicInteger(0);
    private final AtomicInteger pollIdCounter = new AtomicInteger(0);
    private final AtomicInteger voteIdCounter = new AtomicInteger(0);
    private final AtomicInteger voteOptionIdCounter = new AtomicInteger(0);

    private static final String EXCHANGE_NAME = "poll-exchange";

    public PollManager(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

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
        //pollService.add(poll);

        createPollTopic(poll.getId());
        return poll;
    }

    public void createPollTopic(int pollId){
        String key = "poll."+pollId + ".votes";
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, key,
                new VoteEvent(pollId, null));
    }

    public void processVoteEvent(VoteEvent voteEvent) {
        if (voteEvent.getVoteOptionId() == null){
            return;
        }

        VoteOption voteOption = voteOptions.get(voteEvent.getVoteOptionId());
        Users user = null;
        if (voteEvent.getUserId() != null) {
            user = users.get(voteEvent.getUserId());
        }

        Vote vote = new Vote(user, voteOption);
        votes.put(vote.getId(), vote);
    }

    public void publishVote(int pollId, int voteOptionId, int userId){
        VoteEvent voteEvent = new VoteEvent(pollId, voteOptionId, userId);
        String key = "poll."+pollId + ".votes";
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, key, voteEvent);
    }

    public void deletePoll(int id) {
        Poll poll = getPollById(id);
        polls.remove(id);
        Users creator = poll.getCreatedBy();
        Map<Integer, Poll> tmp = creator.getCreated();
        tmp.remove(poll.getId());
        creator.setCreated(tmp);
        pollService.invalidatePoll(id);
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

    public Map<Integer, Vote> getVotesForPoll(Poll poll){
        Map< Integer, Vote> tmp = new HashMap<>();
        for (VoteOption vo : poll.getOptions().values()){
            for (Vote v : vo.getVotes().values()){
                tmp.put(v.getId(), v);
            }
        }
        return tmp;
    }

    public Integer getVoteCount(int pollId, Integer voteOptionId){
        boolean good = pollService.isPresent(pollId);
        if ( good ) {
            return Math.toIntExact(pollService.getVoteCount(pollId, voteOptionId, this));
        } else {
            int answ = getVotesForPoll(polls.get(pollId)).size();
            return answ;
        }
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

        pollService.invalidatePoll(poll.getId());
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
