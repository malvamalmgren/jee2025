package entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "game")
public class Game {
    //Modellen som kopplas mot databasen
    //Entitetsklass i JPA
    //Villkor för entity: defaultkonstruktor, id, fält, inte final - extendas i runtime
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false)
    @NotBlank(message = "Release is required")
    private Integer release;

    @Size(max = 1000)
    private String description;

    @Column(nullable = false)
    @NotBlank(message = "Publisher is required")
    private String publisher;

    @Column(nullable = false)
    @NotBlank(message = "At least one genre is required")
    @ElementCollection
    private List<String> genres;

    @Column(nullable = false)
    @NotNull(message = "Price is required")
    private BigDecimal price;

    //Constructors
//    public Game() {
//        this.price = BigDecimal.ZERO;
//    }
//    public Game(String title, Integer release, String description, String publisher, List<String> genres, BigDecimal price) {
//        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title is required");
//        if (release == null) throw new IllegalArgumentException("Release year is required");
//        if (publisher == null || publisher.isBlank()) throw new IllegalArgumentException("Publisher is required");
//        if (genres == null || genres.isEmpty()) throw new IllegalArgumentException("At least one genre is required");
//        if (price == null) price = BigDecimal.ZERO; // Default if missing
//
//        this.title = title;
//        this.release = release;
//        this.description = description;
//        this.publisher = publisher;
//        this.genres = genres;
//        this.price = price;
//    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return getId() != null && Objects.equals(getId(), game.getId());
    }

    @Override
    public final int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", release=" + release +
                ", publisher='" + publisher + '\'' +
                ", description='" + description + '\'' +
                ", genres=" + genres +
                ", price=" + price +
                '}';
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getRelease() {
        return release;
    }
    public void setRelease(int release) {
        this.release = release;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    public List<String> getGenres() {
        return genres;
    }
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
