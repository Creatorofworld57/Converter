package ex.springsecurity_1805.services;

import ex.springsecurity_1805.Models.Img;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServiceHelperFunctions {
    public static Img toImgEntity(MultipartFile file) throws IOException {
        Img img = new Img();
        img.setName(file.getName());
        img.setOriginalFileName(file.getOriginalFilename());
        img.setContentType(file.getContentType());
        img.setSize(file.getSize());
        img.setBytes(file.getBytes());
        return img;

    }

}
