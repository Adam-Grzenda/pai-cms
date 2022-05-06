package pl.put.cmsbackend.content.text;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class TextContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private Long ownerId;
    private boolean shared;

    private String title;
    private String subtitle;

    private String content;
}
