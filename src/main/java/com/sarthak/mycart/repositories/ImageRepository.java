package com.sarthak.mycart.repositories;

import com.sarthak.mycart.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
