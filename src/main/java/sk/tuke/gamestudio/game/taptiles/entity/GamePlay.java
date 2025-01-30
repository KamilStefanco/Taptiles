package sk.tuke.gamestudio.game.taptiles.entity;

import sk.tuke.gamestudio.game.taptiles.Core.Tile;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class GamePlay {
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "gameplay_id")
    private List<Move> moves;

    @Transient
    private Tile[][] field;

    private int rows;
    private int cols;

    @Id
    @GeneratedValue
    private int ident;

    public void addMove(int row1, int col1, int row2, int col2, char character){
        if(moves == null){
            moves = new ArrayList<>();
        }
        moves.add(new Move(row1,col1,row2,col2,character));
    }

    public Tile[][] getField() {
        return field;
    }

    public void setField(Tile[][] field,int rows,int cols) {
        this.field = field;
        this.rows = rows;
        this.cols = cols;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    @Override
    public String toString() {
        return "GamePlay{" +
                "moves=" + moves +
                ", field=" + Arrays.toString(field) +
                '}';
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
