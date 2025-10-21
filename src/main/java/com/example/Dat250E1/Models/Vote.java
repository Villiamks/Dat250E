package com.example.Dat250E1.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant publishedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Users user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private VoteOption votesOn;

    public Vote(Users user, VoteOption votesOn) {
        this.user = user;
        this.votesOn = votesOn;
        this.publishedAt = Instant.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public VoteOption getVotesOn() {
        return votesOn;
    }

    public void setVotesOn(VoteOption votesOn) {
        this.votesOn = votesOn;
    }
}
