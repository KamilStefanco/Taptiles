package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tuke.gamestudio.SpringClient;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringClient.class)
public class RatingServiceTest {
    @Autowired
    RatingService ratingService; //= new RatingServiceJDBC();

    @Test
    public void testGetRating() {
        ratingService.reset();

        ratingService.setRating(new Rating("Lubos","Taptiles",1,new Date()));
        ratingService.setRating(new Rating("Peter","Taptiles",5,new Date()));

        assertEquals(1, ratingService.getRating("Taptiles","Lubos"));
        assertEquals(5, ratingService.getRating("Taptiles","Peter"));

    }

    @Test
    public void testGetAverageRating() {
        ratingService.reset();

        ratingService.setRating(new Rating("Lubos","Taptiles",1,new Date()));
        ratingService.setRating(new Rating("Peter","Taptiles",5,new Date()));

        assertEquals(3, ratingService.getAverageRating("Taptiles"));
    }

    @Test
    public void testSetRating() {
        ratingService.reset();

        ratingService.setRating(new Rating("Lubos","Taptiles",2,new Date()));
        ratingService.setRating(new Rating("Peter","Taptiles",5,new Date()));
        ratingService.setRating(new Rating("Milan","Taptiles",3,new Date()));
        ratingService.setRating(new Rating("Hanca","Taptiles",4,new Date()));

        assertEquals(3, ratingService.getRating("Taptiles","Milan"));
        assertEquals(2, ratingService.getRating("Taptiles","Lubos"));
        assertEquals(5, ratingService.getRating("Taptiles","Peter"));
        assertEquals(4, ratingService.getRating("Taptiles","Hanca"));

    }

    @Test
    public void testReset() {
        ratingService.reset();

        assertEquals(0, ratingService.getAverageRating("Taptiles"));
    }
}
