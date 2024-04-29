package mk.ukim.finki.wp.jan2023.repository;

import mk.ukim.finki.wp.jan2023.model.Candidate;
import mk.ukim.finki.wp.jan2023.model.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate,Long> {

    List<Candidate> findAllByDateOfBirthBeforeAndGender(LocalDate birthday, Gender gender);
    List<Candidate> findAllByDateOfBirthBefore(LocalDate birthday);

    List<Candidate> findAllByGender(Gender gender);
}
