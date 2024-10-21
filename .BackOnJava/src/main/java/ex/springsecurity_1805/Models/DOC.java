package ex.springsecurity_1805.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="docs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DOC {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String originalFileName;
    private Long size;
    private String ContentType;
    @Lob
    private byte[] bytes;

    @ManyToOne(cascade = CascadeType.REFRESH,fetch=FetchType.EAGER)
    private Usermain user;

}