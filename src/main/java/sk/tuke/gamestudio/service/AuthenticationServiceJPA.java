package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class AuthenticationServiceJPA {

    @PersistenceContext
    private EntityManager entityManager;

    public boolean register(User user){
        String select = "SELECT u FROM User u WHERE u.username = :username";
        try {
            User matchingUser = entityManager.createQuery(select, User.class)
                    .setParameter("username", user.getUsername()).getSingleResult();
            return false;
        } catch (NoResultException ex) {

            entityManager.persist(user);
            return true;
        }
    }

    public boolean isRegistered(User user) {
        String select = "SELECT u FROM User u WHERE u.username = :username AND u.password = :password";
        try{
            User matchingUser = entityManager.createQuery(select, User.class)
                    .setParameter("username", user.getUsername())
                    .setParameter("password", user.getPassword())
                    .getSingleResult();
            return matchingUser != null;
        }catch (NoResultException e){
            return false;
        }
    }

}
