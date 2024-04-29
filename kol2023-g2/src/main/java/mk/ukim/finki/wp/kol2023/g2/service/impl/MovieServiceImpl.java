package mk.ukim.finki.wp.kol2023.g2.service.impl;

import mk.ukim.finki.wp.kol2023.g2.model.Director;
import mk.ukim.finki.wp.kol2023.g2.model.Genre;
import mk.ukim.finki.wp.kol2023.g2.model.Movie;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidDirectorIdException;
import mk.ukim.finki.wp.kol2023.g2.model.exceptions.InvalidMovieIdException;
import mk.ukim.finki.wp.kol2023.g2.repository.DirectorRepository;
import mk.ukim.finki.wp.kol2023.g2.repository.MovieRepository;
import mk.ukim.finki.wp.kol2023.g2.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieServiceImpl implements MovieService {

    public final MovieRepository movieRepository;
    public final DirectorRepository directorRepository;

    public MovieServiceImpl(MovieRepository movieRepository, DirectorRepository directorRepository) {
        this.movieRepository = movieRepository;
        this.directorRepository = directorRepository;
    }

    public List<Movie> listAllMovies(){
        return this.movieRepository.findAll();
    }

    public Movie findById(Long id){
       return this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
   }


    public Movie create(String name, String description, Double rating, Genre genre, Long director){
        Director director1=this.directorRepository.findById(director).orElseThrow(InvalidDirectorIdException::new);

        return this.movieRepository.save(new Movie(
                name,
                description,
                rating,
                genre,
                director1
        ));
    }

    /**
     * This method is used to update a movie, and save it in the database.
     *
     * @param id The id of the movie that is being edited
     * @param name
     * @param description
     * @param rating
     * @param genre
     * @param director
     * @return The movie that is updated.
     * @throws InvalidMovieIdException when there is no movie with the given id
     * @throws InvalidDirectorIdException when there is no director with the given id
     */
    public Movie update(Long id, String name, String description, Double rating, Genre genre, Long director){
        Movie movie=this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);
        Director director1=this.directorRepository.findById(director).orElseThrow(InvalidDirectorIdException::new);

        movie.setName(name);
        movie.setDescription(description);
        movie.setRating(rating);
        movie.setGenre(genre);
        movie.setDirector(director1);
        this.movieRepository.save(movie);
        return movie;
    }

    /**
     * Method that should delete a movie. If the id is invalid, it should throw InvalidMovieIdException.
     *
     * @param id
     * @return The movie that is deleted.
     * @throws InvalidMovieIdException when there is no movie with the given id
     */
    public Movie delete(Long id){
        Movie movie=this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);

        this.movieRepository.delete(movie);
        return movie;
    }

    /**
     * Method that should vote for a movie. If the id is invalid, it should throw InvalidMovieIdException.
     *
     * @param id
     * @return The movie that is voted for.
     * @throws InvalidMovieIdException when there is no movie with the given id
     */
   public Movie vote(Long id){
       Movie movie=this.movieRepository.findById(id).orElseThrow(InvalidMovieIdException::new);

       Integer votes=movie.getVotes()+1;
       movie.setVotes(votes);
       this.movieRepository.save(movie);
       return movie;
   }

    /**
     * The implementation of this method should use repository implementation for the filtering.
     *
     * @param rating          Double that is used to filter the movies that have less rating than this value.
     *                        This param can be null, and is not used for filtering in this case.
     * @param genre           Used for filtering the movies that are from this genre.
     *                        This param can be null, and is not used for filtering in this case.
     * @return The movies that meet the filtering criteria
     */
    public List<Movie> listMoviesWithRatingLessThenAndGenre(Double rating, Genre genre){

        if(rating==null && genre==null){
            return this.movieRepository.findAll();
        }else if(rating!=null && genre!=null){
            return this.movieRepository.findAllByRatingIsLessThanAndGenre(rating,genre);
        }else if(rating!=null){
            return this.movieRepository.findAllByRatingIsLessThan(rating);
        }else {
            return this.movieRepository.findAllByGenre(genre);
        }
    }
}
