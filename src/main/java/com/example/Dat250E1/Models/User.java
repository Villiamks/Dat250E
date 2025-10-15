package com.example.Dat250E1.Models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private long id;
    private String username;
    private String password;
    private String email;

    private List<Poll> createdPolls = new ArrayList<>();

    private List<Vote> votes = new ArrayList<>();

    public User(String name, String em, String pass) {
        username = name;
        email = em;
        password = pass;
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

    public List<Poll> getCreatedPolls() {
        return createdPolls;
    }

    public void setCreatedPolls(List<Poll> createdPolls) {
        this.createdPolls = createdPolls;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}
