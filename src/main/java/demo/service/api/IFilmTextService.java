package demo.service.api;


import demo.model.sakila.FilmText;


public interface IFilmTextService {
    FilmText getFilmTextByFilmId(Integer filmId);

    int createFilmText(FilmText filmText);

    Object getFilmTextAttribute(Integer filmId, String attributeName);
}
