package mk.ukim.finki.wp.sep2022.service.impl;

import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.sep2022.repository.MatchLocationRepository;
import mk.ukim.finki.wp.sep2022.service.MatchLocationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchLocationServiceImpl implements MatchLocationService {

    private final MatchLocationRepository matchLocationRepository;

    public MatchLocationServiceImpl(MatchLocationRepository matchLocationRepository) {
        this.matchLocationRepository = matchLocationRepository;
    }


    /**
     * returns the location with the given id
     *
     * @param id The id of the location that we want to obtain
     * @return
     * @throws InvalidMatchLocationIdException when there is no  location with the given id
     */
    public MatchLocation findById(Long id){
        return this.matchLocationRepository.findById(id).orElseThrow(InvalidMatchLocationIdException::new);
    }

    /**
     * @return List of all  locations in the database
     */
    public List<MatchLocation> listAll(){
        return this.matchLocationRepository.findAll();
    }

    /**
     * This method is used to create a new  location, and save it in the database.
     *
     * @param name
     * @return The location that is created. The id should be generated when the match location is created.
     */
   public MatchLocation create(String name){
       MatchLocation matchLocation=new MatchLocation(name);
       return this.matchLocationRepository.save(matchLocation);
   }
}
