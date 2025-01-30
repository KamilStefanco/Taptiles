package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import java.util.Date;

@Entity
@NamedQuery(name = "Rating.updateRating", query = "UPDATE Rating r SET r.rating = :rating, r.rated_on = :rated_on WHERE r.game = :game AND r.player = :player")
public class Rating {
    private String player;
    private String game;
    private int rating;
    private Date rated_on;


    @Id
    @GeneratedValue
    private int ident;

    public Rating(){

    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return rated_on;
    }

    public void setRatedOn(Date ratedOn) {
        this.rated_on = ratedOn;
    }

    public Rating(String player, String game, int rating, Date ratedOn) {
        this.player = player;
        this.game = game;
        this.rating = rating;
        this.rated_on = ratedOn;
    }
}
