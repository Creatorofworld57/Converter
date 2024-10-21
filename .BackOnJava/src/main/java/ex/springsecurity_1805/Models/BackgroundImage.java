package ex.springsecurity_1805.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class BackgroundImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String ContentType;

    Long size;

    byte[] image;

}
