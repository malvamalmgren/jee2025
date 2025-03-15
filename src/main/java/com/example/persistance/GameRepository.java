package com.example.persistance;

import com.example.entity.Game;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.*;

import java.awt.font.OpenType;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    //Grundläggande CRUD-operationer finns redan via CrudRepository

    @Query("select g from Game g where title = :title")
    Optional<Game> findByTitle(@Param("title") String title);

    @Query("select g from Game g " +
            "where publisher = :publisher order by g.publisher asc")
    Stream<Game> findByPublisher(String publisher);

    @Query("select g from Game g where title = :title and publisher = :publisher and release = :release")
    Optional<Game> findByTitlePublisherRelease(@Param("title") String title,
                                               @Param("publisher") String publisher,
                                               @Param("release") Integer release);

    @Query("select g from Game g order by g.release asc")
    Stream<Game> findAllByRelease();
}
