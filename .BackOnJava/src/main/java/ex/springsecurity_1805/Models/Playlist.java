package ex.springsecurity_1805.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="playlist")
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "playlist_tracks", joinColumns = @JoinColumn(name = "playlist_id"))
    @Column(name = "tracks")
    private List<Long> tracks = new ArrayList<>();

    @OneToOne(cascade = CascadeType.REFRESH,fetch= FetchType.EAGER)
    private Usermain user;
}
