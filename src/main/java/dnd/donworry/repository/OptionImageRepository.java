package dnd.donworry.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dnd.donworry.domain.entity.OptionImage;

public interface OptionImageRepository extends JpaRepository<OptionImage, Long> {
}
