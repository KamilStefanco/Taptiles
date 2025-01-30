package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.*;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService{
    @Id
    @GeneratedValue
    private int ident;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        if(getRating(rating.getGame(),rating.getPlayer())== 0) {
            entityManager.persist(rating);
        }
        else{
            Query updateQuery = entityManager.createNamedQuery("Rating.updateRating");
            updateQuery.setParameter("rating",rating.getRating());
            updateQuery.setParameter("game",rating.getGame());
            updateQuery.setParameter("player",rating.getPlayer());
            updateQuery.setParameter("rated_on",rating.getRatedOn());
            updateQuery.executeUpdate();
        }
    }


    @Override
    public int getAverageRating(String game) throws RatingException {
        try{
            Query query =  entityManager.createQuery("select avg(r.rating) from Rating r where r.game = :game")
                    .setParameter("game",game);
            return ((Number) query.getSingleResult()).intValue();
        }catch(Exception e){
            return 0;
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try {
            Query query = entityManager.createQuery("select r.rating from Rating r where r.game = :game AND r.player = :player")
                    .setParameter("game", game).setParameter("player", player);
            return ((Number) query.getSingleResult()).intValue();
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public void reset() throws RatingException {
        try{
            entityManager.createNativeQuery("DELETE FROM rating").executeUpdate();
        }catch (Exception e){
            throw new RatingException("reset rating error",e);
        }

    }
}
