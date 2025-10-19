package com.example.Dat250E1.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class VoteOption {
    private long id;
    private String caption;
    private int presentationOrder;

    @JsonIgnore
    private Poll poll;

    private List<Vote> votes = new ArrayList<>();

    public VoteOption(String caption, int presentationOrder, Poll poll) {
        this.caption = caption;
        this.presentationOrder = presentationOrder;
        this.poll = poll;
    }

    // Getters and setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }

    public int getPresentationOrder() { return presentationOrder; }
    public void setPresentationOrder(int presentationOrder) { this.presentationOrder = presentationOrder; }

    public Poll getPoll() { return poll; }
    public void setPoll(Poll poll) { this.poll = poll; }

    public List<Vote> getVotes() { return votes; }
    public void setVotes(List<Vote> votes) { this.votes = votes; }
}
