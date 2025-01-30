package sk.tuke.gamestudio.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tuke.gamestudio.SpringClient;
import sk.tuke.gamestudio.entity.Score;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringClient.class)
public class ScoreServiceTest {

    @Autowired
    private ScoreService scoreService;

    @Test
    public void testAddScore() {
        scoreService.reset();

        scoreService.addScore(new Score("Taptiles", "Kamil", 457, new Date()));

        List<Score> scores = scoreService.getTopScores("Taptiles");
        assertEquals(1, scores.size());
        assertEquals("Taptiles", scores.get(0).getGame());
        assertEquals("Kamil", scores.get(0).getPlayer());
        assertEquals(457, scores.get(0).getPoints());

    }

    @Test
    public void testGetTopScores() {
        scoreService.reset();
        Date date = new Date();
        scoreService.addScore(new Score("Taptiles", "Kamil", 120, date));
        scoreService.addScore(new Score("Taptiles", "Jano", 140, date));
        scoreService.addScore(new Score("Taptiles", "Zuzka", 180, date));
        scoreService.addScore(new Score("Taptiles", "Laura", 1000, date));
        scoreService.addScore(new Score("mines", "Janka", 148, date));

        List<Score> scores = scoreService.getTopScores("Taptiles");

        assertEquals(4, scores.size());

        assertEquals("Taptiles", scores.get(0).getGame());
        assertEquals("Laura", scores.get(0).getPlayer());
        assertEquals(1000, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());

        assertEquals("Taptiles", scores.get(1).getGame());
        assertEquals("Zuzka", scores.get(1).getPlayer());
        assertEquals(180, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedOn());

        assertEquals("Taptiles", scores.get(3).getGame());
        assertEquals("Kamil", scores.get(3).getPlayer());
        assertEquals(120, scores.get(3).getPoints());
        assertEquals(date, scores.get(3).getPlayedOn());

        List<Score> scores2 = scoreService.getTopScores("mines");

        assertEquals("mines", scores2.get(0).getGame());
        assertEquals("Janka", scores2.get(0).getPlayer());
        assertEquals(148, scores2.get(0).getPoints());
        assertEquals(date, scores2.get(0).getPlayedOn());
    }

    @Test
    public void testReset() {
        scoreService.reset();

        assertEquals(0, scoreService.getTopScores("Taptiles").size());
    }


}
