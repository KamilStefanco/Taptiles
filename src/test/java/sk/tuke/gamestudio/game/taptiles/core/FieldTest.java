package sk.tuke.gamestudio.game.taptiles.core;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.taptiles.Core.Field;
import sk.tuke.gamestudio.game.taptiles.Core.FieldState;
import sk.tuke.gamestudio.game.taptiles.Core.TileState;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Random;

public class FieldTest {

    private Field field;
    private int rowsCount;
    private int colsCount;

    private Random randomGenerator = new Random();

    public FieldTest() {
        rowsCount = (randomGenerator.nextInt(3)+2)*2;
        colsCount = (randomGenerator.nextInt(3)+2)*2;
        field = new Field(rowsCount, colsCount);
    }

    @Test
    public void checkFieldForNullTiles() {
        for (int row = 0; row < field.getRows(); row++) {
            for (int col = 0; col < field.getCols(); col++) {
                assertNotNull(field.getTile(row, col));
            }
        }
    }


    @Test
    public void wrongFieldSize() {
        assertThrows(RuntimeException.class, () -> {
            new Field(3, 3);
        });
        assertThrows(RuntimeException.class, () -> {
            new Field(3, 4);
        });
        assertThrows(RuntimeException.class, () -> {
            new Field(6, 3);
        });
        assertThrows(RuntimeException.class, () -> {
            new Field(9, 9);
        });
        assertThrows(RuntimeException.class, () -> {
            new Field(15, 3);
        });
    }

    @Test
    public void checkIsSolvedAndIsSolvable() {
        field.canBeConnected(0,0,1,1);
        assertSame(field.getFieldState(), FieldState.PLAYING);
    }

    @Test
    public void checkSelectTile() {
        field.selectTile(0,0);
        assertSame(field.getTile(0,0).getState(), TileState.SELECTED);

    }

    @Test
    public void canBeConnectedNullTile() {
        assertFalse(field.canBeConnected(-1,-1,-1,-1));
    }
}
