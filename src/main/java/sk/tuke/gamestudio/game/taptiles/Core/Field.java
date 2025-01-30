package sk.tuke.gamestudio.game.taptiles.Core;

import sk.tuke.gamestudio.game.taptiles.entity.GamePlay;
import sk.tuke.gamestudio.game.taptiles.entity.Move;

import java.util.*;

public class Field {
    private final Tile[][] tiles;
    private FieldState state;
    private final int rowCount;
    private final int colsCount;
    boolean found = false;
    private int[][] visited;

    private long startMillis;

    private GamePlay gamePlay;

    private Tile lastTile1 = null;
    private Tile lastTile2 = null;
    

    public Field(int rows, int cols) {
        if((rows * cols) % 4 != 0 || rows < 4 || cols < 4 || (rows >8 && cols >9) || (rows >9 && cols >8)){
            throw new RuntimeException("Invalid field size (field must be atleast 4x4 and rows * cols must be dividable by 4)" +
                    "(maximum is 9x8 or 8x9)");
        }

        this.state = FieldState.PLAYING;
        this.rowCount = rows;
        this.colsCount = cols;
        this.tiles = new Tile[rows][cols];

        gamePlay = new GamePlay();

        this.generateField(rows, cols);

    }

    public Field(GamePlay gamePlay) {
        this.state = FieldState.PLAYING;
        this.rowCount = gamePlay.getRows();
        this.colsCount = gamePlay.getCols();
        this.tiles = new Tile[rowCount][colsCount];
        regeneratefield(gamePlay.getMoves());
    }

    private void regeneratefield(List<Move> moves) {
        for(Move move : moves){
            tiles[move.getxTile1()][move.getyTile1()] = new Tile(move.getCharacter());
            tiles[move.getxTile2()][move.getyTile2()] = new Tile(move.getCharacter());
        }
    }


    public int getScore() {
        return 1000 - (rowCount * colsCount * 3 - (int) (System.currentTimeMillis() - startMillis) / 1000);
    }


    public void selectTile(int row,int col) {
        Tile tile = getTile(row,col);
        if(tile == null){
            return;
        }

        if (tile.getState() == TileState.OPEN) {
            tile.setState(TileState.SELECTED);
        } else if (tile.getState() == TileState.SELECTED) {
            tile.setState(TileState.OPEN);
        }

    }

    private void isSolved() {
        for(int row = 0; row < this.rowCount; ++row) {
            for(int column = 0; column < this.colsCount; ++column) {
                if (this.getTile(row, column).getState() != TileState.CONNECTED) {
                    return;
                }
            }
        }

        setFieldState(FieldState.SOLVED);
    }

    private void generateField(int rows, int cols) {
        char[] values = {'1','2','3','4','A','B','C','D','E','F','5','6','$','j','f','g','h','i',
                'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        int tileCount = rows * cols;

        int valuesIdx = 0;
        int characterCount = 0;

        while(tileCount > 0){
            Random random = new Random();
            int randomRow = random.nextInt(rows);
            int randomCol = random.nextInt(cols);

            if(tiles[randomRow][randomCol] == null){
                tiles[randomRow][randomCol] = new Tile(values[valuesIdx]);
                tileCount--;
                characterCount++;

                if(characterCount == 4){
                    valuesIdx++;
                    characterCount = 0;
                }
            }
        }
        startMillis = System.currentTimeMillis();

        if(gamePlay != null){
            gamePlay.setField(tiles,rows,cols);
        }
    }

    public int getRows() {
        return this.rowCount;
    }

    public int getCols() {
        return this.colsCount;
    }

    public Tile getTile(int row, int column) {
        if(column > colsCount || row > rowCount || column < 0 || row < 0){
            return null;
        }

        return this.tiles[row][column];
    }

    public FieldState getFieldState() {
        return this.state;
    }

    private void setFieldState(FieldState state) {
        this.state = state;
    }

    public void connectTiles(int tile1Row, int tile1Col, int tile2Row, int tile2Col) {
        Tile tile1 = getTile(tile1Row,tile1Col);
        Tile tile2 = getTile(tile2Row,tile2Col);

        if (tile1 != null && tile2 != null) {
            if (tile1.getState() == TileState.SELECTED && tile2.getState() == TileState.SELECTED) {
                tile1.setState(TileState.CONNECTED);
                tile2.setState(TileState.CONNECTED);
            }
            isSolvable();
            isSolved();
        }
    }

    public boolean canBeConnected(int tile1Row, int tile1Col, int tile2Row, int tile2Col) {
        Tile tile1 = getTile(tile1Row,tile1Col);
        Tile tile2 = getTile(tile2Row,tile2Col);

        boolean canConnect = false;

        if(tile1Row == tile2Row && tile2Col == tile1Col){
            return false;
        }

        if(tile1 == null || tile2 == null || tile1.getCharacter() != tile2.getCharacter()){
            return false;
        }

        if (tile1.getState() == TileState.CONNECTED || tile2.getState() == TileState.CONNECTED) {
            return false;
        }

        if((tile1Row == tile2Row && tile1Row == 0) || (tile1Row == tile2Row && tile1Row == rowCount-1)){       //horny a dolny okraj
            canConnect = true;
        }

        if((tile1Col == tile2Col && tile1Col == 0) || (tile1Col == tile2Col && tile1Col == colsCount-1)){      //pravy a lavy okraj
            canConnect = true;
        }

        visited = new int[rowCount][colsCount];
        found = false;
        openNeighbour(tile1Row,tile1Col,tile2Row,tile2Col);

        if(canConnect || found){
            if(gamePlay != null){
                gamePlay.addMove(tile1Row,tile1Col,tile2Row,tile2Col,getTile(tile1Row,tile1Col).getCharacter());
            }
            lastTile1 = tile1;
            lastTile2 = tile2;
            return true;
        }


        return false;
    }
    private void openNeighbour(int startRow, int startCol, int endRow, int endCol) {
       
        visited[startRow][startCol] = 1;

        if(openUp(startRow, startCol, endRow, endCol)) return;

        if(openDown(startRow, startCol, endRow, endCol)) return;

        if(openLeft(startRow, startCol, endRow, endCol)) return;

        openRight(startRow, startCol, endRow, endCol);
    }

    private void openRight(int startRow, int startCol, int endRow, int endCol) {
        if(startCol +1 < colsCount){
            if(startRow == endRow && startCol +1 == endCol){
                found = true;
                return;
            }
            if(tiles[startRow][startCol +1].getState() == TileState.CONNECTED && visited[startRow][startCol +1] == 0){
                openNeighbour(startRow, startCol +1, endRow, endCol);
            }
        }
    }

    private boolean openLeft(int startRow, int startCol, int endRow, int endCol) {
        if(startCol -1 >= 0){
            if(startRow == endRow && startCol -1 == endCol){
                found = true;
                return true;
            }
            if(tiles[startRow][startCol -1].getState() == TileState.CONNECTED && visited[startRow][startCol -1] == 0){
                openNeighbour(startRow, startCol -1, endRow, endCol);
            }
        }
        return false;
    }

    private boolean openDown(int startRow, int startCol, int endRow, int endCol) {
        if(startRow +1 < rowCount){
            if(startRow +1 == endRow && startCol == endCol){
                found = true;
                return true;
            }
            if(tiles[startRow +1][startCol].getState() == TileState.CONNECTED && visited[startRow +1][startCol] == 0){
                openNeighbour(startRow +1, startCol, endRow, endCol);
            }
        }
        return false;
    }

    private boolean openUp(int startRow, int startCol, int endRow, int endCol) {
        if(startRow -1 >= 0 ){
            if(startRow -1 == endRow && startCol == endCol){
                found = true;
                return true;
            }
            if(tiles[startRow -1][startCol].getState() == TileState.CONNECTED && visited[startRow -1][startCol] == 0){
                openNeighbour(startRow -1, startCol, endRow, endCol);
            }
        }
        return false;
    }

    public void undo(){
        if(lastTile2 != null && lastTile1 != null && state != FieldState.SOLVED){
            lastTile1.setState(TileState.OPEN);
            lastTile2.setState(TileState.OPEN);

            if(state == FieldState.UNSOLVABLE){
                state = FieldState.PLAYING;
            }
        }
    }

    public void reset(){
        if(state == FieldState.PLAYING){
            for(int i=0;i<rowCount;i++){
                for(int j=0;j<colsCount;j++){
                    tiles[i][j].setState(TileState.OPEN);
                }
            }
        }
    }

    private void isSolvable() {

        int numOfOpenTiles = countOpenTiles();

        if(numOfOpenTiles != 4){
            return;
        }

        checkCharacters();
    }

    private int countOpenTiles() {
        int numOfOpenTiles = 0;
        for (int row = 0; row < rowCount; ++row) {
            for (int column = 0; column < colsCount; ++column) {
                if(tiles[row][column].getState() == TileState.OPEN){
                    numOfOpenTiles++;
                }
            }
        }
        return numOfOpenTiles;
    }

    private void checkCharacters() {
        for (int row = 0; row < rowCount-1; ++row) {
            for (int column = 0; column < colsCount-1; ++column) {
                Tile tileLeftUp = getTile(row,column);
                Tile tileRightDown = getTile(row+1,column+1);

                Tile tileLeftDown = getTile(row,column+1);
                Tile tileRightUp = getTile(row+1,column);

                if(tileLeftUp.getState() != TileState.OPEN || tileRightDown.getState() != TileState.OPEN
                    || tileLeftDown.getState() != TileState.OPEN || tileRightUp.getState() != TileState.OPEN){
                    continue;
                }

                checkMatchingChars(tileLeftUp, tileRightDown, tileLeftDown, tileRightUp);
            }
        }
    }

    private void checkMatchingChars(Tile tileLeftUp, Tile tileRightDown, Tile tileLeftDown, Tile tileRightUp) {
        char leftUp = tileLeftUp.getCharacter();
        char rightDown = tileRightDown.getCharacter();

        char leftDown = tileLeftDown.getCharacter();
        char rightUp = tileRightUp.getCharacter();

        if(leftUp == rightDown && leftDown == rightUp){
            setFieldState(FieldState.UNSOLVABLE);
        }
    }

    public GamePlay getGamePlay() {
        return gamePlay;
    }
}
