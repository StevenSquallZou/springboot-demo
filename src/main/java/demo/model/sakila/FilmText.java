package demo.model.sakila;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Schema(
        name = "filmText",
        description = "the filmText JSON/XML to create",
        example = """
                {
                    "filmId": 1001,
                    "title": "Test FilmText",
                    "description": "This is a test FilmText"
                }
                """,
        requiredMode = Schema.RequiredMode.REQUIRED,
        requiredProperties = {"filmId", "title", "description"}
)
public class FilmText {
    @Schema(name = "film id", description = "the unique film id", example = "1")
    @NotNull
    private Integer filmId;

    @Schema(name = "title", description = "the film title", example = "Titanic")
    @NotBlank
    private String title;

    @Schema(name = "description", description = "the film description", example = "the story of Titanic")
    @NotBlank
    private String description;


    public Integer getFilmId() {
        return filmId;
    }


    public void setFilmId(Integer filmId) {
        this.filmId = filmId;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "FilmText{" +
                "filmId=" + filmId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
