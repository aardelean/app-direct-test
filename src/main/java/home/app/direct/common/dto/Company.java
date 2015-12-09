package home.app.direct.common.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Company {
    @Id
    @GeneratedValue
    private Long id;
    protected String country;
    protected String email;
    protected String name;
    protected String phoneNumber;
    protected String uuid;
    protected String website;
}
