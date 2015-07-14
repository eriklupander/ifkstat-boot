package se.ifkgoteborg.stat.model.sec;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 create table users (    username varchar(50) not null primary key,    password varchar(255) not null,    enabled boolean not null) engine = InnoDb;
 */
@Entity
@Table(name = "users")
public class JdbcUser {

    @Id
    @Column(name="username", length = 50, nullable = false)
    private String username;

    @Column(name="password", length = 255, nullable = false)
    private String password;

    @Column(name="enabled", nullable = false)
    private boolean enabled = true;

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
