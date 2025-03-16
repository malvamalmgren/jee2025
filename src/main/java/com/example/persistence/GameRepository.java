package com.example.persistence;

import com.example.entity.Game;
import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.*;

import java.util.Optional;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {

    @Query("select g from Game g where title = :title")
    Optional<Game> findByTitle(@Param("title") String title);

    @Query("SELECT g FROM Game g WHERE g.publisher = :publisher ORDER BY g.publisher ASC")
    Page<Game> findByPublisher(@Param("publisher") String publisher, PageRequest pageable);

    @Query("select g from Game g where title = :title and publisher = :publisher and release = :release")
    Optional<Game> findByTitlePublisherRelease(@Param("title") String title,
                                               @Param("publisher") String publisher,
                                               @Param("release") Integer release);

    @Query("select g from Game g order by g.release asc")
    Page<Game> findAllByRelease(PageRequest pageable);

}
