import entity.Game;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, Long> {
    //Grundläggande CRUD-operationer finns redan via CrudRepository
    //Lägg till egna specifika om du vill
    Game findByReleaseAndPublisher(Integer release, String publisher);
}
