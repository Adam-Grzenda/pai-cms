package pl.put.cmsbackend.content.text.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;
import pl.put.cmsbackend.auth.user.app.AppUser;

import javax.persistence.*;

@Entity
@Indexed
@NoArgsConstructor
@Getter
@Setter
public class TextContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private AppUser owner;

    private boolean shared = false;

    @Field(termVector = TermVector.YES, name = "title")
    private String title;

    @Field(termVector = TermVector.YES, name = "subtitle")
    private String subtitle;

    @Column(columnDefinition = "text")
    @Field(termVector = TermVector.YES, name = "content")
    private String content;

    public TextContent(AppUser owner, String title, String subtitle, String content) {
        this.owner = owner;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
    }
}
