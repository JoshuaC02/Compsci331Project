package proj.concert.service.domain;

import proj.concert.common.types.Genre;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "PERFORMERS")
public class Performer {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENRE")
    private Genre genre;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "BLURB", columnDefinition = "TEXT")
    private String blurb;

    @ManyToMany(mappedBy = "performers")
    private Set<Concert> concerts = new HashSet<>();

    public Performer() {}

    public Performer(Long id, String name, Genre genre, String imageName, String blurb) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.imageName = imageName;
        this.blurb = blurb;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getBlurb() {
        return blurb;
    }

    public void setBlurb(String blurb) {
        this.blurb = blurb;
    }
}
