package mk.ukim.finki.wp.jan2023.service.impl;

import mk.ukim.finki.wp.jan2023.model.Candidate;
import mk.ukim.finki.wp.jan2023.model.Gender;
import mk.ukim.finki.wp.jan2023.model.Party;
import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidCandidateIdException;
import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidPartyIdException;
import mk.ukim.finki.wp.jan2023.repository.CandidateRepository;
import mk.ukim.finki.wp.jan2023.repository.PartyRepository;
import mk.ukim.finki.wp.jan2023.service.CandidateService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final PartyRepository partyRepository;

    public CandidateServiceImpl(CandidateRepository candidateRepository, PartyRepository partyRepository) {
        this.candidateRepository = candidateRepository;
        this.partyRepository = partyRepository;
    }

    /**
     * @return List of all candidates in the database
     */

    public List<Candidate> listAllCandidates(){
        return this.candidateRepository.findAll();
    }

    /**
     * returns the candidate with the given id
     *
     * @param id The id of the candidate that we want to obtain
     * @return
     * @throws InvalidCandidateIdException when there is no candidate with the given id
     */
    public Candidate findById(Long id){
        return this.candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException::new);
    }

    /**
     * This method is used to create a new candidate, and save it in the database.
     *
     * @param name
     * @param bio
     * @param dateOfBirth
     * @param gender
     * @param party
     * @return The candidate that is created. The id should be generated when the candidate is created.
     * @throws InvalidPartyIdException when there is no party with the given id
     */
    public Candidate create(String name, String bio, LocalDate dateOfBirth, Gender gender, Long party){
        Party party1=this.partyRepository.findById(party).orElseThrow(InvalidPartyIdException::new);

        Candidate candidate=new Candidate(name,bio,dateOfBirth,gender,party1);
        return this.candidateRepository.save(candidate);
    }

    /**
     * This method is used to update a candidate, and save it in the database.
     *
     * @param id The id of the candidate that is being edited
     * @param name
     * @param bio
     * @param dateOfBirth
     * @param gender
     * @param party
     * @return The candidate that is updated.
     * @throws InvalidCandidateIdException when there is no candidate with the given id
     * @throws InvalidPartyIdException when there is no party with the given id
     */
    public Candidate update(Long id, String name, String bio, LocalDate dateOfBirth, Gender gender, Long party){
        Candidate candidate=this.candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException::new);
        Party party1=this.partyRepository.findById(party).orElseThrow(InvalidPartyIdException::new);

        candidate.setName(name);
        candidate.setBio(bio);
        candidate.setDateOfBirth(dateOfBirth);
        candidate.setGender(gender);
        candidate.setParty(party1);

        return this.candidateRepository.save(candidate);
    }

    /**
     * Method that should delete a candidate. If the id is invalid, it should throw InvalidCandidateIdException.
     *
     * @param id
     * @return The candidate that is deleted.
     * @throws InvalidCandidateIdException when there is no candidate with the given id
     */
    public Candidate delete(Long id){
        Candidate candidate=this.candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException::new);
         this.candidateRepository.delete(candidate);
         return candidate;
    }

    /**
     * Method that should vote for a candidate. If the id is invalid, it should throw InvalidCandidateIdException.
     *
     * @param id
     * @return The candidate that is voted for.
     * @throws InvalidCandidateIdException when there is no candidate with the given id
     */
    public Candidate vote(Long id){
        Candidate candidate=this.candidateRepository.findById(id).orElseThrow(InvalidCandidateIdException::new);

        Integer votes=candidate.getVotes()+1;
        candidate.setVotes(votes);
        return this.candidateRepository.save(candidate);
    }

    /**
     * The implementation of this method should use repository implementation for the filtering.
     *
     * @param yearsMoreThan that is used to filter the candidates who are older than this value.
     *                        This param can be null, and is not used for filtering in this case.
     * @param gender        Used for filtering the candidates gender.
     *                        This param can be null, and is not used for filtering in this case.
     * @return The candidates that meet the filtering criteria
     */
    public List<Candidate> listCandidatesYearsMoreThanAndGender(Integer yearsMoreThan, Gender gender){
        // 24 godini - 21.01.2000 togas 21.01.2024 - 24 = before
        if(yearsMoreThan==null && gender==null){
            return this.candidateRepository.findAll();
        }else if(yearsMoreThan!=null && gender!=null){
            LocalDate birthday=LocalDate.now().minusYears(yearsMoreThan);
            return this.candidateRepository.findAllByDateOfBirthBeforeAndGender(birthday,gender);
        }else if(yearsMoreThan!=null)
        {
            LocalDate birthday=LocalDate.now().minusYears(yearsMoreThan);
            return this.candidateRepository.findAllByDateOfBirthBefore(birthday);
        }else if(gender!=null){
            return this.candidateRepository.findAllByGender(gender);
        }
        return null;
    }
}
