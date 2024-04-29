package mk.ukim.finki.wp.jan2023.service.impl;

import mk.ukim.finki.wp.jan2023.model.Party;
import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidPartyIdException;
import mk.ukim.finki.wp.jan2023.repository.PartyRepository;
import mk.ukim.finki.wp.jan2023.service.PartyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartyServiceImpl implements PartyService {

    private final PartyRepository partyRepository;

    public PartyServiceImpl(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    /**
     * returns the party with the given id
     *
     * @param id The id of the party that we want to obtain
     * @return
     * @throws InvalidPartyIdException when there is no party with the given id
     */
    public Party findById(Long id){
        return this.partyRepository.findById(id).orElseThrow(InvalidPartyIdException::new);
    }

    /**
     * @return List of all parties in the database
     */
    public List<Party> listAll(){
        return this.partyRepository.findAll();
    }

    /**
     * This method is used to create a new party, and save it in the database.
     *
     * @param name
     * @return The party that is created. The id should be generated when the party is created.
     */
    public Party create(String name){
        Party party=new Party(name);
        return this.partyRepository.save(party);
    }
}
