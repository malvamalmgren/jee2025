package dto;

import java.math.BigDecimal;
import java.util.List;

public record UpdateGame(String title, Integer release, String publisher, String description, List<String> genres, BigDecimal price) {
}
