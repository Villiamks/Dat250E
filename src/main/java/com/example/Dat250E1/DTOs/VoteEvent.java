package com.example.Dat250E1.DTOs;

public class VoteEvent {
    private Integer pollId;
    private Integer voteOptionId;
    private Integer userId;

    public VoteEvent() {}

    public VoteEvent(Integer pollId, Integer voteOptionId) {
        this.pollId = pollId;
        this.voteOptionId = voteOptionId;
        this.userId = null;
    }

    public VoteEvent(Integer pollId, Integer voteOptionId, Integer userId) {
        this.pollId = pollId;
        this.voteOptionId = voteOptionId;
        this.userId = userId;
    }

    // Getters and setters
    public Integer getPollId() { return pollId; }
    public void setPollId(Integer pollId) { this.pollId = pollId; }

    public Integer getVoteOptionId() { return voteOptionId; }
    public void setVoteOptionId(Integer voteOptionId) { this.voteOptionId = voteOptionId; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "VoteEvent{pollId=" + pollId + ", voteOptionId=" + voteOptionId +
                ", userId=" + userId + '}';
    }
}
