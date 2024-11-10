package com.sarthak.mycart.services;

import com.sarthak.mycart.dto.ImageDto;
import com.sarthak.mycart.entities.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image getImageById(Long id);

    List<ImageDto> saveImage(List<MultipartFile> files, Long productId);

    void deleteImageById(Long id);

    void updateImage(MultipartFile file, Long id);
}
