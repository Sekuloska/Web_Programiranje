package mk.ukim.finki.wp.sep2022.service.impl;

import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchIdException;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchLocationIdException;
import mk.ukim.finki.wp.sep2022.repository.MatchLocationRepository;
import mk.ukim.finki.wp.sep2022.repository.MatchRepository;
import mk.ukim.finki.wp.sep2022.service.MatchService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchLocationRepository matchLocationRepository;

    public MatchServiceImpl(MatchRepository matchRepository, MatchLocationRepository matchLocationRepository) {
        this.matchRepository = matchRepository;
        this.matchLocationRepository = matchLocationRepository;
    }


    /**
     * @return List of all matches in the database
     */
    public List<Match> listAllMatches(){
        return this.matchRepository.findAll();
    }

    /**
     * returns the entity with the given id
     *
     * @param id The id of the entity that we want to obtain
     * @return
     * @throws InvalidMatchIdException when there is no entity with the given id
     */
    public Match findById(Long id){
        return this.matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
    }

    /**
     * This method is used to create a new entity, and save it in the database.
     *
     * @param name
     * @param description
     * @param price
     * @param type
     * @param location
     * @return The entity that is created. The id should be generated when the entity is created.
     * @throws InvalidMatchLocationIdException when there is no location with the given id
     */
   public Match create(String name, String description, Double price, MatchType type, Long location){
       MatchLocation matchLocation=this.matchLocationRepository.findById(location).orElseThrow(InvalidMatchLocationIdException::new);

       Match match=new Match(name,description,price,type,matchLocation);
       return this.matchRepository.save(match);
   }

    /**
     * This method is used to modify an entity, and save it in the database.
     *
     * @param id The id of the entity that is being edited
     * @param name
     * @param description
     * @param price
     * @param type
     * @param location
     * @return The entity that is updated.
     * @throws InvalidMatchIdException  when there is no entity with the given id
     * @throws InvalidMatchLocationIdException when there is no location with the given id
     */
    public Match update(Long id, String name, String description, Double price, MatchType type, Long location){
        Match match=this.matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
        MatchLocation matchLocation=this.matchLocationRepository.findById(location).orElseThrow(InvalidMatchLocationIdException::new);

        match.setName(name);
        match.setDescription(description);
        match.setPrice(price);
        match.setType(type);
        match.setLocation(matchLocation);

        return this.matchRepository.save(match);

    }

    /**
     * Method that should delete an entity. If the id is invalid, it should throw InvalidMatchIdException.
     *
     * @param id
     * @return The entity that is deleted.
     * @throws InvalidMatchIdException when there is no entity with the given id
     */
    public Match delete(Long id){
        Match match=this.matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);
        this.matchRepository.delete(match);
        return match;
    }

    /**
     * Method for following a match. If the id is invalid, it should throw InvalidMatchIdException.
     *
     * @param id
     * @return The event that is deleted.
     * @throws InvalidMatchIdException when there is no event with the given id
     */
    public Match follow(Long id){
        Match match=this.matchRepository.findById(id).orElseThrow(InvalidMatchIdException::new);

        Integer follow=match.getFollows()+1;
        match.setFollows(follow);
        return this.matchRepository.save(match);
    }

    /**
     * The implementation of this method should use repository implementation for the filtering.
     *
     * @param price       Double that is used to filter the entities that have a price less than it.
     *                    This param can be null, and is not used for filtering in this case.
     * @param type   Used for filtering the entities that belong to that type.
     *                    This param can be null, and is not used for filtering in this case.
     * @return The entities that meet the filtering criteria
     */
    public List<Match> listMatchesWithPriceLessThanAndType(Double price, MatchType type){
        if(price==null && type==null)
        {
            return this.matchRepository.findAll();
        }else if(price!=null && type!=null)
        {
            return this.matchRepository.findAllByPriceLessThanAndType(price,type);
        }else if(price!=null)
        {
            return this.matchRepository.findAllByPriceLessThan(price);
        }else if(type!=null)
        {
            return this.matchRepository.findAllByType(type);
        }
        return null;
    }
}
