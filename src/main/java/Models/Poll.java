package Models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Poll {
    private long id;
    private String question;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant publishedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant validUntil;

    private User creator;
    private List<VoteOption> voteOptions = new ArrayList<>();
    private List<Vote> votes = new ArrayList<>();

    public Poll(String question, Instant pushedAt, Instant validUntil, User creator) {
        this.question = question;
        this.publishedAt = pushedAt;
        this.validUntil = validUntil;
        this.creator = creator;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public List<VoteOption> getVoteOptions() {
        return voteOptions;
    }

    public void setVoteOptions(List<VoteOption> voteOptions) {
        this.voteOptions = voteOptions;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public Vote getVoteByUser(User user) {
        for (int i = 0; i <= votes.size(); i++) {
            Vote tmp = votes.get(i);
            if (tmp != null && tmp.getUser() == user) {
                return tmp;
            }
        }
        return null;
    }
}
