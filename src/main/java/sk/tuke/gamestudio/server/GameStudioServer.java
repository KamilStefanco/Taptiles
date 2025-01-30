package sk.tuke.gamestudio.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import sk.tuke.gamestudio.game.taptiles.service.GamePlayServiceJPA;
import sk.tuke.gamestudio.service.AuthenticationServiceJPA;
import sk.tuke.gamestudio.service.*;

@SpringBootApplication
@Configuration
@EntityScan(basePackages = {"sk.tuke.gamestudio.entity","sk.tuke.gamestudio.game.taptiles.entity"})
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class);
    }

    @Bean
    public ScoreService scoreService(){
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService(){
        return new RatingServiceJPA();
    }

    @Bean
    public GamePlayServiceJPA taptilesGamePlayService(){
        return new GamePlayServiceJPA();
    }

    @Bean
    public AuthenticationServiceJPA authenticationServiceJPA(){
        return new AuthenticationServiceJPA();
    }
}
