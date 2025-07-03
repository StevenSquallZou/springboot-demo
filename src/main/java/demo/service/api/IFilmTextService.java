package demo.service.api;


import demo.model.sakila.FilmText;


public interface IFilmTextService {
    FilmText getFilmTextByFilmId(Integer filmId);

    int saveFilmText(FilmText filmText);
}
