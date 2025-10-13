package Models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

public class Vote {
    private long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant publishedAt;

    private User user;
    private VoteOption voteOption;

    public Vote(User user, VoteOption voteOption) {
        this.user = user;
        this.voteOption = voteOption;
        this.publishedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public VoteOption getVoteOption() {
        return voteOption;
    }
    public void setVoteOption(VoteOption voteOption) {
        this.voteOption = voteOption;
    }
}
