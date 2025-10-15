package com.example.Dat250E1;

import com.example.Dat250E1.Models.Poll;
import com.example.Dat250E1.Models.PollManager;
import com.example.Dat250E1.Models.User;
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

        User user1 = new User("user1", "user1@gmail.com", "Pass1");
        testUser1(pollManager, user1);

        User user2 = new User("user2", "user2@gmail.com", "Pass2");
        testUser2(pollManager, user2);

        Poll poll1 = new Poll("whats good?", Instant.now(), Instant.MAX, user1);
        VoteOption vo1 = new VoteOption("opt1", 1, poll1);
        VoteOption vo2 = new VoteOption("opt2", 2, poll1);
        testAddPoll(pollManager, vo1, vo2, poll1);

        testChangeVoteAndVote(pollManager, user2, poll1, vo1, vo2);
	}

    void testUser1(PollManager pollManager, User user1){
        pollManager.createUser(user1);
        assertTrue(pollManager.getAllUsers().contains(user1));
    }

    void testUser2(PollManager pollManager, User user2){
        pollManager.createUser(user2);
        assertTrue(pollManager.getAllUsers().size() == 2 );
    }

    void testAddPoll(PollManager pollManager, VoteOption vo1, VoteOption vo2, Poll poll) {
        List<VoteOption> vo = new ArrayList<>();
        vo.add(vo1);
        vo.add(vo2);
        poll.setVoteOptions(vo);

        pollManager.createPoll(poll);
        assertTrue(pollManager.getAllPolls().contains(poll));
    }

    void testChangeVoteAndVote(PollManager pollManager, User user, Poll poll, VoteOption vo1, VoteOption vo2){
        pollManager.vote(user, vo1);
        pollManager.changeVote(user, vo2);
        VoteOption tmpVO = pollManager.getPollById(poll.getId()).getVoteByUser(user).getVoteOption();
        assertTrue( tmpVO != vo1 && tmpVO == vo2);
    }

}
