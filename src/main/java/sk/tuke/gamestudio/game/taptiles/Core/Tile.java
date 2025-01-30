package sk.tuke.gamestudio.game.taptiles.Core;

public class Tile {
    private TileState state = TileState.OPEN;
    private final char character;

    public Tile( char character) {
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    public TileState getState() {
        return state;
    }

    void setState(TileState state) {
        this.state = state;
    }
}
