package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Comment;

import java.util.Arrays;
import java.util.List;

public class CommentServiceRestClient implements CommentService{

    @Autowired
    private RestTemplate restTemplate;
    private String url = "http://localhost:8080/api/comment";

    @Override
    public void addComment(Comment comment) throws CommentException {
        try{
            restTemplate.postForEntity(url,comment,Comment.class);
        }catch(Exception e){
            throw new CommentException("addComment error",e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try{
            return Arrays.asList(restTemplate.getForEntity(url + "/" + game, Comment[].class).getBody());
        }catch(Exception e){
            throw new CommentException("getComments error",e);
        }
    }

    @Override
    public void reset() throws CommentException {
        throw new UnsupportedOperationException("Reset is not supported on web interface");
    }
}
