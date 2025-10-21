package com.example.Dat250E1.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String question;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant publishedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant validUntil;

    @ManyToOne(cascade = CascadeType.ALL)
    private Users createdBy;

    @OneToMany(cascade = CascadeType.ALL)
    private Map<Integer, VoteOption> options;

    public Poll(String question, Instant pushedAt, Instant validUntil, Users createdBy) {
        this.question = question;
        this.publishedAt = pushedAt;
        this.validUntil = validUntil;
        this.createdBy = createdBy;
        options = new LinkedHashMap<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    public Map<Integer, VoteOption> getOptions() {
        return options;
    }

    public void setOptions(Map<Integer, VoteOption> options) {
        this.options = options;
    }

    public VoteOption addVoteOption(String caption, EntityManager em) {
        VoteOption vo = new VoteOption(caption, options.size(), this);
        em.persist(vo);
        options.put(vo.getId(), vo);
        return vo;
    }

    public List<Vote> findAllVotes(){
        List<Vote> votes = new ArrayList<>();

        for (int i = 0; i < options.size(); i++){
            VoteOption vo = options.get(i);
            for (Vote vote : vo.getVotes().values()){
                votes.add(vote);
            }
        }
        return votes;
    }

    public Vote getVoteByUser(Users user) {
        List<Vote> votes = findAllVotes();

        if (votes.isEmpty()){
            return null;
        }
        for (Vote tmp : votes) {
            if (tmp != null && tmp.getUser() == user) {
                return tmp;
            }
        }
        return null;
    }
}
