package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.game.taptiles.Core.Field;
import sk.tuke.gamestudio.game.taptiles.Core.FieldState;
import sk.tuke.gamestudio.game.taptiles.Core.Tile;
import sk.tuke.gamestudio.game.taptiles.Core.TileState;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.taptiles.entity.GamePlay;
import sk.tuke.gamestudio.game.taptiles.entity.Move;
import sk.tuke.gamestudio.game.taptiles.service.GamePlayServiceJPA;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;

@Controller
@RequestMapping("/Taptiles")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class TaptilesController {

    private Field field = new Field(4,4);
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private UserController userController;
    private GamePlay gamePlay;
    private int replayStep;
    @Autowired
    private GamePlayServiceJPA gamePlayServiceJPA;
    private int lastRow = -1;
    private int lastColumn = -1;

    @RequestMapping
    public String taptiles(@RequestParam(required = false) Integer row,@RequestParam(required = false) Integer column, Model model){
        processCommand(row, column);
        fillModel(model);

        return "taptiles";
    }

    private void processCommand(Integer row, Integer column) {
        if(row != null && column != null){

            if(lastRow != -1 && lastColumn != -1 && field.getFieldState() == FieldState.PLAYING ){
                field.selectTile(row, column);
                field.selectTile(lastRow,lastColumn);
                if(field.canBeConnected(row, column,lastRow,lastColumn)){
                    field.connectTiles(row, column,lastRow,lastColumn);

                    if(field.getFieldState() == FieldState.SOLVED && userController.isLogged()){
                        scoreService.addScore(new Score("Taptiles", userController.getLoggedUser().getUsername(), field.getScore(),new Date()));
                        gamePlayServiceJPA.save(field.getGamePlay());
                    }
                }
                else{
                    field.selectTile(row, column);
                    field.selectTile(lastRow,lastColumn);
                }
                lastColumn = -1;
                lastRow = -1;

            }

            lastRow = row;
            lastColumn = column;
        }
    }


    @RequestMapping("/new")
    public String newGame(@RequestParam int level,Model model){
        switch (level){
            case 1:
                field = new Field(4,4);
                break;
            case 2:
                field = new Field(4,6);
                break;
            case 3:
                field = new Field(6,8);
                break;
            default:
                field = new Field(4,4);
        }

        lastRow = -1;
        lastColumn = -1;

        fillModel(model);

        return "taptiles";
    }


    public String getState(){
        return field.getFieldState().toString();
    }

    @RequestMapping(value = "/field",produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String taptiles(@RequestParam(required = false) Integer row,@RequestParam(required = false) Integer column){
        processCommand(row,column);
        return getHtmlField();
    }

    @RequestMapping(value = "/field-replay",produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String taptiles_replay(){
        if(replayStep < gamePlay.getMoves().size() && replayStep != -1){
            Move move = gamePlay.getMoves().get(replayStep);

            field.selectTile(move.getxTile1(), move.getyTile1());
            field.selectTile(move.getxTile2(), move.getyTile2());

            field.connectTiles(move.getxTile1(), move.getyTile1(), move.getxTile2(), move.getyTile2());
        }
        replayStep++;
        return getHtmlField();
    }

    @RequestMapping("/replay")
    public String replay(Model model){
        replayStep=-1;
        gamePlay = gamePlayServiceJPA.loadLast();
        field = new Field(gamePlay);
        return "taptiles-replay";
    }

    @RequestMapping("/undo")
    public String undoMove(Model model){
        lastRow = -1;
        lastColumn = -1;
        field.undo();
        fillModel(model);

        return "taptiles";
    }


    public String getHtmlField() {
        return getHtmlField("/Taptiles");
    }

        public String getHtmlField(String address) {
            StringBuilder sb = new StringBuilder();
            sb.append("<table class='field'>\n");
            for (int row = 0; row < field.getRows(); row++) {
                sb.append("<tr>\n");
                for (int column = 0; column < field.getCols(); column++) {
                    Tile tile = field.getTile(row, column);
                    sb.append("<td class='tile" + getImageName(tile) + "'>\n");
                    sb.append("<a href='" + address + "?row=" + row + "&column=" + column + "'>\n");
                    sb.append("<span>" + getImageName(tile) + "</span>");
                    sb.append("</a>\n");
                    sb.append("</td>\n");
                }
                sb.append("</tr>\n");
            }

            sb.append("</table>\n");
            return sb.toString();
        }

    @RequestMapping("/addComment")
    public String addComment(@RequestParam String comment,Model model){
        if(userController.isLogged()){
            commentService.addComment(new Comment(userController.getLoggedUser().getUsername(),"Taptiles",comment,new Date() ));
        }
        fillModel(model);
        return "taptiles";
    }

    @RequestMapping("/rating")
    public String addRating(@RequestParam int rating,Model model){
        if(userController.isLogged()){
            ratingService.setRating(new Rating(userController.getLoggedUser().getUsername(),"Taptiles",rating,new Date() ));
        }
        fillModel(model);
        return "redirect:/Taptiles";
    }

    public String retRating(){
        if(userController.isLogged()){
            Integer result = ratingService.getRating("Taptiles",userController.getLoggedUser().getUsername());

            if(result == 0){
                return "No rating";
            }
            return Integer.toString(result);
        }

        return "Login/Register to rate";
    }

    @RequestMapping("/reset")
    public String resetField(Model model){
        field.reset();
        fillModel(model);
        return "redirect:/Taptiles";
    }


    private void fillModel(Model model){
        model.addAttribute("htmlField",getHtmlField());
        model.addAttribute("scores",scoreService.getTopScores("Taptiles"));
        model.addAttribute("comments",commentService.getComments("Taptiles"));
        model.addAttribute("avgrating",ratingService.getAverageRating("Taptiles"));
        model.addAttribute("rating",ratingService.getAverageRating("Taptiles"));
    }

    private String getImageName(Tile tile) {
        if(tile.getState() == TileState.CONNECTED){
            return "";
        }
        switch (tile.getCharacter()){
            case '1':
                return "1";
            case '2':
                return "2";
            case '3':
                return "3";
            case '4':
                return "4";
            case 'A':
                return "A";
            case 'B':
                return "B";
            case 'C':
                return "C";
            case 'D':
                return "D";
            case 'E':
                return "E";
            case 'F':
                return "F";
            case '5':
                return "5";
            case '6':
                return "6";
            case '$':
                return "$";
        }
        return "";
    }

    public int getScore(){
        return field.getScore();
    }

}
