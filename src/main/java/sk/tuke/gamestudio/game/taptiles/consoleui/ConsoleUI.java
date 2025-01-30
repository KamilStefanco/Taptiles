package sk.tuke.gamestudio.game.taptiles.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import sk.tuke.gamestudio.game.taptiles.Core.Field;
import sk.tuke.gamestudio.game.taptiles.Core.FieldState;
import sk.tuke.gamestudio.game.taptiles.Core.Tile;
import sk.tuke.gamestudio.game.taptiles.Core.TileState;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.service.*;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private Field field;
    private boolean again = false;
    private final Scanner scanner = new Scanner(System.in);
    private int xTile1;
    private int yTile1;
    private int xTile2;
    private int yTile2;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String SET_BOLD_TEXT = "\033[0;1m";

    private final Pattern pattern = Pattern.compile("([A-Za-z])([0-9])");
    private final Pattern endOfGame = Pattern.compile("([Y|N|y|n])");

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    private int level = 3;

    public ConsoleUI(Field field) {
        this.field = field;
    }

    public void play(){
        //scoreService.addScore(new Score("Taptiles","Kami",788,new Date()));
        //commentService.addComment(new Comment("Kamil","Taptiles","2odovzdavka",new Date()));

        //ratingService.reset();
        //System.out.println(ratingService.getRating("Taptiles","Kamil"));
        //ratingService.setRating(new Rating("Jozo","Taptiles",2,new Date()));

        playAgain();

        printStart();
        do{
            gameLoop();
            updateLevel();
        }while(!again);

    }

    private void updateLevel() {
        level++;
        if(level == 2){
            field = new Field(4,5);
        }
        if(level ==3){
            field = new Field(4,6);
        }

        if(level == 4){
            System.out.println(SET_BOLD_TEXT + ANSI_YELLOW+"       YOU WON!" + ANSI_RESET);
            scoreService.addScore(new Score("Taptiles", System.getProperty("user.name"), field.getScore(), new Date()));
            handleEndOfGame();
        }
    }

    private void printStart() {
        System.out.println(SET_BOLD_TEXT + ANSI_GREEN+"\n---------> WELCOME TO TAPTILES! <---------\n" + ANSI_RESET );
        System.out.println(SET_BOLD_TEXT + "The goal of the game is to connect all pairs.");
        System.out.println("  Pairs with a path can be connected");
        System.out.println("         Example input: A4");
        System.out.println("    You can exit anytime by typing X");
        System.out.println("            Good luck :)" + ANSI_RESET);
        System.out.println(SET_BOLD_TEXT + ANSI_GREEN+"------------------------------------------" + ANSI_RESET );
    }

    private void gameLoop() {
        System.out.println(SET_BOLD_TEXT + ANSI_PURPLE+"           ---------" + ANSI_RESET );
        System.out.println(SET_BOLD_TEXT + ANSI_CYAN+"            LEVEL: "+level+"");
        System.out.println(SET_BOLD_TEXT + ANSI_PURPLE+"           ---------\n" + ANSI_RESET );
        do{
            printField();

            handleInput(1);
            field.selectTile(xTile1,yTile1);

            handleInput(2);
            field.selectTile(xTile2,yTile2);

            if(field.canBeConnected(xTile1,yTile1,xTile2,yTile2)){
                field.connectTiles(xTile1,yTile1,xTile2,yTile2);
            }
            else{
                System.out.println(SET_BOLD_TEXT + ANSI_RED + "Tiles cant be connected\n");
                field.selectTile(xTile1,yTile1);
                field.selectTile(xTile2,yTile2);
            }

        } while(field.getFieldState() == FieldState.PLAYING);

        printField();

        if(field.getFieldState() == FieldState.UNSOLVABLE){
            System.out.println(SET_BOLD_TEXT + ANSI_RED + "You lost! The field is unsolvable" + ANSI_RESET);
            handleEndOfGame();
        }

    }

    private void handleEndOfGame() {

        System.out.println(SET_BOLD_TEXT + "    Your score: " + field.getScore());
        System.out.println("");

        playAgain();

    }

    private void playAgain() {
        Matcher matcher; String line;
        do {
            System.out.println(SET_BOLD_TEXT + ANSI_PURPLE + "-> "+ANSI_RESET+ SET_BOLD_TEXT +"Want to play again? (Y/N)" + ANSI_RESET);
            System.out.println(SET_BOLD_TEXT + "Press H for " + ANSI_YELLOW +"Hall of Fame"+SET_BOLD_TEXT +
                    " ,press C to add "+ ANSI_CYAN +"Comment"+ ANSI_RESET + SET_BOLD_TEXT + ",press V to add rating: ");

            line = scanner.nextLine();
            matcher = endOfGame.matcher(line);

            if(line.length() == 0){
                continue;
            }

            if(line.charAt(0) == 'H' || line.charAt(0) == 'h'){
                printHallOfFame();
            }

            if(line.charAt(0) == 'C' || line.charAt(0) == 'c'){
                addComment();
            }
            if(line.charAt(0) == 'V' || line.charAt(0) == 'v'){
                addRating();
            }
        } while(!matcher.matches());

        if(line.charAt(0) == 'Y' || line.charAt(0) == 'y'){
            level = 1;
            field = new Field(4,4);
        }
        if(line.charAt(0) == 'N' || line.charAt(0) == 'n'){
            System.out.println(SET_BOLD_TEXT + "Goodbye! Thanks for playing.");
            again = true;
            System.exit(0);
        }
    }

    private void addRating() {
        int line = 0;
        System.out.println(SET_BOLD_TEXT +"Type your rating(from 1 to 5): ");
        do{
            line = scanner.nextInt();
            if(line<1 || line> 5){
                System.out.println("Rating needs to be from 1 to 5");
            }

        }while(line<1 || line> 5);


        ratingService.setRating(new Rating(System.getProperty("user.name"),"Taptiles",line,new Date()));
        System.out.println(SET_BOLD_TEXT+ "Rating saved!");
        System.out.println("Average rating of this game is: " + ratingService.getAverageRating("Taptiles"));
    }

    private void addComment() {
        System.out.println(SET_BOLD_TEXT +"Type your comment(max 64 characters): ");
        String line = scanner.nextLine();
        commentService.addComment(new Comment(System.getProperty("user.name"),"Taptiles",line,new Date()));
        System.out.println(SET_BOLD_TEXT+ "Comment saved!\nOther comments: \n");

        List<Comment> comments = commentService.getComments("Taptiles");

        for(int idx = 0;idx < comments.size();idx++){
            Comment comment = comments.get(idx);
            System.out.printf("%s %s  %s\n", comment.getComment(), comment.getPlayer(),comment.getCommentedOn());
        }

    }

    private void handleInput(int inputNumber) {

        Matcher matcher;
        do {
            System.out.print(SET_BOLD_TEXT + ANSI_PURPLE + "-> " +ANSI_RESET+SET_BOLD_TEXT
                    + "Enter coordinates of the "+ inputNumber +". tile: ");
            matcher = checkInput();

        } while(!matcher.matches());

        if(inputNumber == 1){
            xTile1 = Character.toUpperCase(matcher.group(1).charAt(0))- 'A';
            yTile1 = Character.toUpperCase(matcher.group(2).charAt(0))- '1';
        }
        else {
            xTile2 = Character.toUpperCase(matcher.group(1).charAt(0))- 'A';
            yTile2 = Character.toUpperCase(matcher.group(2).charAt(0))- '1';
        }

    }

    private Matcher checkInput() {
        Matcher matcher;
        String line = scanner.nextLine();

        if(line.charAt(0) == 'X' || line.charAt(0) == 'x' ){
            System.out.println(SET_BOLD_TEXT +"Goodbye! Thanks for playing.");
            System.exit(0);
        }

        matcher = pattern.matcher(line);

        if(!matcher.matches()){
            System.out.println(SET_BOLD_TEXT + ANSI_RED +"Wrong input. (Example: A1)" + ANSI_RESET);
        }
        return matcher;
    }

    private void printField() {
        printHeader();

        for (int row = 0; row < field.getRows(); ++row) {
            System.out.print(SET_BOLD_TEXT +ANSI_GREEN +"  "+(char) (row + 'A') + SET_BOLD_TEXT+ANSI_PURPLE + " | "+ANSI_RESET);
            printTile(row);

            System.out.println(" ");
        }
        System.out.println();
    }

    private void printTile(int row) {
        for (int column = 0; column < field.getCols(); ++column) {
            Tile tile = field.getTile(row, column);
            if( tile.getState() == TileState.CONNECTED){
                System.out.print("  ");
            }
            else{
                System.out.print(SET_BOLD_TEXT + tile.getCharacter() + " ");
            }
        }
    }

    private void printHeader() {
        System.out.print("      ");
        for (int column = 0; column < field.getCols(); ++column) {
            System.out.print( SET_BOLD_TEXT + ANSI_GREEN + (column + 1) + " " + ANSI_RESET);
        }
        System.out.println();
        System.out.print("     ");

        for (int column = 0; column < field.getCols(); ++column) {
            System.out.print(SET_BOLD_TEXT + ANSI_PURPLE +"--" + ANSI_RESET);
        }
        System.out.println();
    }

    private void printHallOfFame(){
        System.out.println(SET_BOLD_TEXT + ANSI_YELLOW+  "-------- Hall Of Fame --------"+ ANSI_RESET + SET_BOLD_TEXT);
        List<Score> scores = scoreService.getTopScores("Taptiles");

        for(int idx = 0;idx < scores.size();idx++){
            Score score = scores.get(idx);
            System.out.printf("        %d. %s %d\n", idx + 1, score.getPlayer(), score.getPoints());
        }
        System.out.println(SET_BOLD_TEXT + ANSI_YELLOW+"------------------------------"+ ANSI_RESET);

    }
}
