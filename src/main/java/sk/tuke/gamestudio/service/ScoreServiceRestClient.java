package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Score;

import java.util.Arrays;
import java.util.List;

public class ScoreServiceRestClient implements ScoreService{

    private String url = "http://localhost:8080/api/score";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addScore(Score score) throws ScoreException {
        try{
            restTemplate.postForEntity(url,score,Score.class);
        }catch(Exception e){
            throw new ScoreException("addScore error",e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        try{
            return Arrays.asList(restTemplate.getForEntity(url + "/" + game, Score[].class).getBody());
        }catch(Exception e){
            throw new ScoreException("getTopScores error",e);
        }
    }

    @Override
    public void reset() throws ScoreException {
        throw new UnsupportedOperationException("Reset is not supported on web interface");
    }
}
