package com.example.Dat250E1.Services;

import com.example.Dat250E1.Models.Poll;
import com.example.Dat250E1.Models.PollManager;
import com.example.Dat250E1.Models.VoteOption;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.valkey.JedisPool;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PollService {
    private static final String key = "poll:cache:";
    private final io.valkey.JedisPool jedisPool;
    private final io.valkey.Jedis jedis;
    private final int expire = 300;

    public PollService() {
        io.valkey.JedisPoolConfig jedisPoolConfig = new io.valkey.JedisPoolConfig();

        String valkeyHost = System.getProperty("valkey.host");

        jedisPoolConfig.setMaxTotal(32);
        jedisPoolConfig.setMaxIdle(32);
        jedisPoolConfig.setMinIdle(16);

        this.jedisPool = new JedisPool("localhost", 6379);
        this.jedis = jedisPool.getResource();
    }

    public Long getVoteCount(Integer pollId, Integer voteOptionId, PollManager pollManager) {
        return getPollVoteCounters(pollId, pollManager).get(voteOptionId);
    }

    public Map<Integer, Long> getPollVoteCounters(Integer pollId, PollManager pm){
        String pollkey = key + pollId;

        Map<String, String> cacheCounterStr = jedis.hgetAll(pollkey);

        if ( cacheCounterStr != null && !cacheCounterStr.isEmpty()){
            jedis.expire(pollkey, expire);
            return cacheCounterStr.entrySet().stream().collect(Collectors.toMap(
               e -> Integer.parseInt(e.getKey()),
               e -> Long.parseLong(e.getValue())
            ));
        } else {
            Poll poll = pm.getPollById(pollId);
            if ( poll == null ){
                return new  HashMap<>();
            }
            Map<Integer, Long> voteCounters = pm.getVotesForPoll(poll).values().stream()
                    .collect(Collectors.groupingBy(e -> e.getVotesOn().getId(), Collectors.counting()));
            for (VoteOption option : poll.getOptions().values()) {
                voteCounters.putIfAbsent(option.getId(), 0L);
            }

            if (!voteCounters.isEmpty()){
                Map<String, String> countsToCache = voteCounters.entrySet().stream()
                        .collect(Collectors.toMap(
                           e -> String.valueOf(e.getKey()),
                           e -> String.valueOf(e.getValue())
                        ));
                jedis.hset(pollkey, countsToCache);
                jedis.expire(pollkey, expire);
            }
            return voteCounters;
        }
    }

    public boolean isPresent(int pollId) {
        String pollKey = key + pollId;
        boolean good = jedis.exists(pollKey);
        return good;
    }

    public void invalidatePoll(int pollId) {
        String pollKey = key + pollId;
        jedis.del(pollKey);
    }

}
