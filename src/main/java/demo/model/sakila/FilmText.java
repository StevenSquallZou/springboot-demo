package demo.model.sakila;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class FilmText {
    @NotNull
    private Integer filmId;
    @NotBlank
    private String title;
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
