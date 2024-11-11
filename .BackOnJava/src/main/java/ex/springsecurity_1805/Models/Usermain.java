package ex.springsecurity_1805.Models;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;


import java.util.ArrayList;

import java.util.Date;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users_In_System")
public class Usermain {


    @Column(unique = true)
    @JsonView(Views.Public.class)
    private String name;
    private String password;
    @JsonView(Views.Public.class)
    private String roles;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long previewImageId;
    @JsonView(Views.Public.class)
    private Date created;
    @JsonView(Views.Public.class)
    private Date updated;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_socials", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "socials")
    private List<String> social = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_playlists", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "idOfPlaylists")
    private List<Long> idOfPlaylists = new ArrayList<>();

    private Long lastTrack;
    public void addImgToProduct(Img img) {img.setUser(this);}



}
