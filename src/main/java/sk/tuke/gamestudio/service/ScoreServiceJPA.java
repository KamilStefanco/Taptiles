package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class ScoreServiceJPA implements ScoreService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addScore(Score score) throws ScoreException {
        try{
            entityManager.persist(score);
        }catch (Exception e){
            throw new ScoreException("addScore error",e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) throws ScoreException {
        try{
            return entityManager.createQuery("select s from Score s where s.game = :game order by s.points DESC")
                    .setParameter("game",game)
                    .setMaxResults(10)
                    .getResultList();
        }catch(Exception e){
            throw new ScoreException("getTopScores error",e);
        }
    }

    @Override
    public void reset() throws ScoreException {
        entityManager.createNativeQuery("DELETE FROM score").executeUpdate();
    }
}
