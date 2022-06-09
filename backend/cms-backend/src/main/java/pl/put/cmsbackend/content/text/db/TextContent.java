package pl.put.cmsbackend.content.text.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.put.cmsbackend.auth.user.app.AppUser;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TextContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    private boolean shared = false;

    private String title;

    private String subtitle;

    @Column(columnDefinition = "text")
    private String content;

    public TextContent(AppUser owner, String title, String subtitle, String content) {
        this.owner = owner;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
    }
}