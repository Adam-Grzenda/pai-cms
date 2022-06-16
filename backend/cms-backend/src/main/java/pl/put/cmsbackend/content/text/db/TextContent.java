package pl.put.cmsbackend.content.text.db;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;
import pl.put.cmsbackend.auth.user.app.AppUser;

import javax.persistence.*;
import java.util.Set;

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

    private String imageHref;


    @ElementCollection(targetClass = ContentTag.class)
    @CollectionTable
    @Enumerated(EnumType.STRING)
    @ContainedIn
    private Set<ContentTag> contentTags;

    public TextContent(AppUser owner, String title, String subtitle, String content, Set<ContentTag> contentTags, String imageHref) {
        this.owner = owner;
        this.title = title;
        this.subtitle = subtitle;
        this.content = content;
        this.contentTags = contentTags;
        this.imageHref = imageHref;
    }
}
