package sk.tuke.gamestudio.entity;


import javax.persistence.*;

@Entity
@Table(name = "auth")
public class User {

    @Id
    @GeneratedValue
    private int ident;
    @Column(unique = true)
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Authentication{" +
                "ident=" + ident +
                ", name='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}

