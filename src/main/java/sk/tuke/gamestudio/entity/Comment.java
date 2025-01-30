package sk.tuke.gamestudio.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Comment {

    private String player;
    private String game;
    private String comment;
    private Date commentedOn;

    @Id
    @GeneratedValue
    private int ident;

    public Comment(){

    }

    public Comment(String player, String game, String comment, Date commentedAt) {
        this.player = player;
        this.game = game;
        this.comment = comment;
        this.commentedOn = commentedAt;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCommentedOn() {
        return commentedOn;
    }

    public void setCommentedOn(Date commentedOn) {
        this.commentedOn = commentedOn;
    }

    public String toString() {
        return "Score{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", comment=" + comment +
                ", commentedOn=" + commentedOn +
                '}';
    }
}
