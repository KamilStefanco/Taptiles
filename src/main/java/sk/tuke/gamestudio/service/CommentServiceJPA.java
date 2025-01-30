package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class CommentServiceJPA implements CommentService{

    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void addComment(Comment comment) throws CommentException {
        try{
            entityManager.persist(comment);
        }catch(Exception e){
            throw new CommentException("addComment error",e);
        }
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        try{
            return entityManager.createQuery("select s from Comment s where s.game = :game")
                    .setParameter("game",game)
                    .setMaxResults(10)
                    .getResultList();
        }catch(Exception e){
            throw new CommentException("getComments error",e);
        }
    }

    @Override
    public void reset() throws CommentException {
        try{
            entityManager.createNativeQuery("DELETE FROM comment").executeUpdate();
        }catch(Exception e){
            throw new CommentException("reset Comment error",e);
        }
    }
}
