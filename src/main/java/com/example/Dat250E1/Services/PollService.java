package com.example.Dat250E1.Services;

import com.example.Dat250E1.Models.Poll;
import com.example.Dat250E1.Models.VoteOption;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Service
public class PollService {
    private static final String key = "polls";
    private final io.valkey.JedisPool jedisPool;
    private final io.valkey.Jedis jedis;
    private final int expire = 3000;

    public PollService() {
        io.valkey.JedisPoolConfig jedisPoolConfig = new io.valkey.JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(32);
        jedisPoolConfig.setMaxIdle(32);
        jedisPoolConfig.setMinIdle(16);

        this.jedisPool = new io.valkey.JedisPool(jedisPoolConfig, "localhost", 6379);
        this.jedis = jedisPool.getResource();
    }

    public void add(Poll poll) {
        String pollKey = key + poll.getId();

        if (!isPresent(pollKey)) {
            jedis.hset(pollKey, "id", "" + poll.getId());
            jedis.hset(pollKey, "title", poll.getQuestion());

            for (VoteOption option : poll.getOptions().values()) {
                String optionKey = pollKey + ":option:" + option.getCaption();
                jedis.hset(optionKey, "id", option.getId() + "");
                jedis.hset(optionKey, "caption", option.getCaption());
                jedis.hset(optionKey, "voteCount", String.valueOf(option.getVotes().size()));
            }

            jedis.expire(pollKey, expire);
        }
    }

    public Map<String, String> getPollVotes(int pollId) {
        String pollKey = key + pollId;
        Map<String, String> voteCounts = new HashMap<>();

        Set<String> optionKeys = jedis.keys(pollKey + ":option:*");

        for (String optionKey : optionKeys) {
            String idstr = jedis.hget(optionKey, "id");
            String caption = jedis.hget(optionKey, "caption");
            String voteCountStr = jedis.hget(optionKey, "voteCount");

            if (idstr != null && voteCountStr != null) {
                int id = Integer.parseInt(idstr);
                try {
                    int voteCount = Integer.parseInt(voteCountStr);
                    voteCounts.put(""+id, "voteCount:" + voteCount + "caption:" + caption);
                } catch (NumberFormatException e) {
                    voteCounts.put(""+id, "0");
                }
            }
        }
        return voteCounts;
    }

    public boolean isPresent(String pollId) {
        String pollKey = key + pollId;
        boolean good = jedis.exists(pollKey);
        return good;
    }

    public Integer getVoteCount(int pollid , Integer voteOptid){
        Map<String, String> voteCounts = getPollVotes(pollid);
        String countstr =  voteCounts.get(""+voteOptid).split("voteCount:")[1].split("caption:")[0];
        return Integer.parseInt(countstr);
    }

    public void updatePoll(Poll poll) {
        String pollKey = key + poll.getId();
        invalidatePoll(poll.getId());
        add(poll);
    }

    public void invalidatePoll(int pollId) {
        String pollKey = key + pollId;
        jedis.del(pollKey);

        Set<String> optionKeys = jedis.keys(pollKey + ":option:*");
        if (!optionKeys.isEmpty()) {
            jedis.del(optionKeys.toArray(new String[0]));
        }

    }

}
