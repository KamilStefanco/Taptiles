package sk.tuke.gamestudio.game.taptiles.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Move {
    private int xTile1;
    private int yTile1;

    private int xTile2;
    private int yTile2;

    private char character;

    @Id
    @GeneratedValue
    private int ident;


    public Move(int xTile1, int yTile1, int xTile2, int yTile2,char character) {
        this.xTile1 = xTile1;
        this.yTile1 = yTile1;
        this.xTile2 = xTile2;
        this.yTile2 = yTile2;
        this.character = character;
    }

    public Move() {
    }

    public int getxTile1() {
        return xTile1;
    }

    public int getyTile1() {
        return yTile1;
    }

    public int getxTile2() {
        return xTile2;
    }

    public int getyTile2() {
        return yTile2;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        return "Move{" +
                "xTile1=" + xTile1 +
                ", yTile1=" + yTile1 +
                ", xTile2=" + xTile2 +
                ", yTile2=" + yTile2 +
                '}';
    }
}
