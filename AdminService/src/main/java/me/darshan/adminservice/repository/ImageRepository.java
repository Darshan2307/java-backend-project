package me.darshan.adminservice.repository;
import me.darshan.adminservice.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
