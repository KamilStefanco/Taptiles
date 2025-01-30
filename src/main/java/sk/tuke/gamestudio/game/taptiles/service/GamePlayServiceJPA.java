package sk.tuke.gamestudio.game.taptiles.service;

import sk.tuke.gamestudio.game.taptiles.entity.GamePlay;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class GamePlayServiceJPA {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(GamePlay gamePlay){
        entityManager.persist(gamePlay);
    }

    public GamePlay loadLast() {
        try{
            return (GamePlay) entityManager.createQuery
                    ("SELECT g FROM GamePlay g WHERE g.ident = (SELECT MAX(g2.ident) FROM GamePlay g2)")
                    .getSingleResult();
        }catch(NoResultException e){
            return null;
        }
    }
}
