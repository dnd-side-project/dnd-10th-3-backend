package dnd.donworry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.donworry.domain.entity.Selection;
import dnd.donworry.repository.custom.SelectionRepositoryCustom;

public interface SelectionRepository extends JpaRepository<Selection, Long>, SelectionRepositoryCustom {
}
