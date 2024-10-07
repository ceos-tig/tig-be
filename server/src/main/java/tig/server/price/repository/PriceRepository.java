package tig.server.price.repository;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import tig.server.price.domain.Price;
import tig.server.program.domain.Program;

import java.util.List;
import java.util.Optional;

public interface PriceRepository extends JpaRepository<Price, Long> {

    List<Price> findByProgram(Program program);
}
