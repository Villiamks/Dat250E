package com.example.Dat250E1;

import Models.*;
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

        User user1 = new User("user1", "user1@gmail.com");
        pollManager.createUser(user1);
        assertTrue(pollManager.getAllUsers().contains(user1));

        User user2 = new User("user2", "user2@gmail.com");
        pollManager.createUser(user2);
        assertTrue(pollManager.getAllUsers().size() == 2 );

        Poll poll1 = new Poll("whats good?", Instant.now(), Instant.MAX, user1);
        VoteOption vo1 = new VoteOption("opt1", 1, poll1);
        VoteOption vo2 = new VoteOption("opt2", 2, poll1);
        List<VoteOption> vo = new ArrayList<>();
        vo.add(vo1);
        vo.add(vo2);
        poll1.setVoteOptions(vo);

        pollManager.createPoll(poll1);
        assertTrue(pollManager.getAllPolls().contains(poll1));

        Vote v1 = new Vote(user2, vo1);
        Vote v2 = new Vote(user2, vo2) ;

        pollManager.vote(v1);
        pollManager.changeVote(v2, v1);
        assertTrue(pollManager.getAllVotes().contains(v2) && !pollManager.getAllVotes().contains(v1));
	}

}
