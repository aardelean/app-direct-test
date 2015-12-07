package home.app.direct.subscription.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    protected String email;
    protected String firstName;
    protected String language;
    protected String lastName;
    protected String openId;
    protected String uuid;
}
