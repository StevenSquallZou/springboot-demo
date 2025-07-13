package demo.service.impl;


import demo.mapper.FilmTextMapper;
import demo.model.sakila.FilmText;
import demo.service.api.IFilmTextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("filmTextServiceMyBatisImpl")
public class FilmTextServiceMyBatisImpl implements IFilmTextService {
    private FilmTextMapper filmTextMapper;


    @Autowired
    public FilmTextServiceMyBatisImpl(FilmTextMapper filmTextMapper) {
        this.filmTextMapper = filmTextMapper;
    }


    @Override
    public FilmText getFilmTextByFilmId(Integer filmId) {
        return filmTextMapper.selectByFilmId(filmId);
    }


    @Override
    public int createFilmText(FilmText filmText) {
        return filmTextMapper.insertFilmText(filmText);
    }


    @Override
    public Object getFilmTextAttribute(Integer filmId, String attributeName) {
        throw new UnsupportedOperationException("getFilmTextAttribute NOT implemented yet");
    }

}
