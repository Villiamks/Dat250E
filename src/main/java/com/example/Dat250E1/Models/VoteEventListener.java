package com.example.Dat250E1.Models;

import com.example.Dat250E1.DTOs.VoteEvent;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class VoteEventListener {

    private final PollManager pollManager;

    public VoteEventListener(PollManager pollManager) {
        this.pollManager = pollManager;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "poll-votes-queue", durable = "true"),
            exchange = @Exchange(value = "poll-exchange", type = ExchangeTypes.TOPIC),
            key = "poll.*.votes"
    ))
    public void handleVoteEvent(VoteEvent voteEvent) {
        System.out.println("Received vote event: " + voteEvent);
        pollManager.processVoteEvent(voteEvent);
    }
}