package com.example.Dat250E1.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.*;
import java.util.Map;

@Entity
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Map<Integer, Poll> created;

    @OneToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Map<Integer, Vote> votes;

    public Users(){}

    public Users(String name, String em, String pass) {
        username = name;
        email = em;
        password = pass;
        created = new LinkedHashMap<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Map<Integer, Poll> getCreated() {
        return created;
    }

    public void getCreated(Map<Integer, Poll> created) {
        this.created = created;
    }

    public void setCreated(Map<Integer, Poll> createdPolls) {
        this.created = createdPolls;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Poll createPoll(String question) {
        return new Poll(question, null, null, this);
    }

    public Vote voteFor(VoteOption voteOption) {
        Vote vote = new Vote(this, voteOption);
        return vote;
    }

    public Map<Integer, Vote> getVotes() {
        return votes;
    }

    public void setVotes(Map<Integer, Vote> votes) {
        this.votes = votes;
    }
}
