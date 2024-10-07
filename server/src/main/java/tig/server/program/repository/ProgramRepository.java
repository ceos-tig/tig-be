package tig.server.program.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.club.domain.Club;
import tig.server.program.domain.Program;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Program,Long> {
    List<Program> findByClub_Id(Long clubId);
}
