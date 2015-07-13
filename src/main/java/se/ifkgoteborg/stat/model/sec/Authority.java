package se.ifkgoteborg.stat.model.sec;

import javax.persistence.*;

/**
 create table authorities (    username varchar(50) not null,    authority varchar(50) not null,    foreign key (username) references users (username),    unique index authorities_idx_1 (username, authority))

 */
@Entity
@Table(name = "authorities", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "authority"}))
public class Authority {

    @Id
    @Column(name="username", length = 50, nullable = false)
    private String username;

    @Column(name="authority", length = 50, nullable = false)
    private String authority;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
