package ex.springsecurity_1805.Models;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Data
@Entity
@RedisHash("Audio")
@Table(name = "audio")
public class Audio implements Serializable {
    @Id
    @JsonView(Views.Public.class)
    @GeneratedValue(generator = "custom-generator")
    @GenericGenerator(name = "custom-generator", type = ex.springsecurity_1805.Models.CustomIdGenerator.class)
    private Long id;
    @Lob
    @Basic(fetch=FetchType.LAZY)
    private byte[] buffer;
    @JsonView(Views.Public.class)
    private String name;
    private String ContentType;
    private Long Size;
    @JsonView(Views.Public.class)
    private String Author;
    @JsonView({Views.AudioImage.class})
    private byte[] image;
    @JsonView({Views.Public.class})
    private byte[] imagesc;

    public Audio() {

    }
}
