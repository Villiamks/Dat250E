package com.example.Dat250E1;

import com.example.Dat250E1.Models.Poll;
import com.example.Dat250E1.Models.PollManager;
import com.example.Dat250E1.Models.Users;
import com.example.Dat250E1.Models.VoteOption;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class Dat250E1ApplicationTests {

	@Test
	void testPollManager() {
        PollManager pollManager = new PollManager();

        Users user1 = new Users("user1", "user1@gmail.com", "Pass1");
        testUser1(pollManager, user1);

        Users user2 = new Users("user2", "user2@gmail.com", "Pass2");
        testUser2(pollManager, user2);

        Poll poll1 = new Poll("whats good?", Instant.now(), Instant.MAX, user1);
        testAddPoll(pollManager, "opt1", "opt2", poll1);

        testChangeVoteAndVote(pollManager, user2, poll1);
	}

    void testUser1(PollManager pollManager, Users user1){
        pollManager.createUser(user1);
        assertTrue(pollManager.getAllUsers().contains(user1));
    }

    void testUser2(PollManager pollManager, Users user2){
        pollManager.createUser(user2);
        assertTrue(pollManager.getAllUsers().size() == 2 );
    }

    void testAddPoll(PollManager pollManager, String vo1, String vo2, Poll poll) {
        List<String> opt = new ArrayList<>();
        opt.add(vo1);
        opt.add(vo2);
        pollManager.insertOptions(poll, opt);
        pollManager.createPoll(poll);
        assertTrue(pollManager.getAllPolls().contains(poll));
    }

    void testChangeVoteAndVote(PollManager pollManager, Users user, Poll poll) {
        VoteOption vo1 = poll.getOptions().get(0);
        VoteOption vo2 = poll.getOptions().get(1);

        pollManager.vote(user, vo1);
        pollManager.changeVote(user, vo2);
        VoteOption tmpVO = pollManager.getPollById(poll.getId()).getVoteByUser(user).getVotesOn();
        assertTrue( tmpVO != vo1 && tmpVO == vo2);
    }

}
