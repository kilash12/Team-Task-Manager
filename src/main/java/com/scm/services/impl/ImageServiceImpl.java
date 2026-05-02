//package com.scm.services.impl;
//
//import java.io.IOException;
//
//import java.util.*;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import com.cloudinary.Cloudinary;
//import com.cloudinary.Transformation;
//import com.cloudinary.utils.ObjectUtils;
//import com.scm.helpers.AppConstants;
//import com.scm.services.ImageService;
//
//@Service
//public class ImageServiceImpl implements ImageService {
//
////    private Cloudinary cloudinary;
//@Autowired(required = false)
//private Cloudinary cloudinary;
//
//    public ImageServiceImpl(Cloudinary cloudinary) {
//        this.cloudinary = cloudinary;
//    }
//
//    @Override
//    public String uploadImage(MultipartFile contactImage, String filename) {
//
//        // code likhnaa hia jo image ko upload kar rha ho
//
//        try {
//            byte[] data = contactImage.getBytes();
//            cloudinary.uploader().upload(data, ObjectUtils.asMap(
//                    "public_id", filename));
//
//            return this.getUrlFromPublicId(filename);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        // and return raha hoga : url
//
//    }
//
//    @Override
//    public String getUrlFromPublicId(String publicId) {
//
//        return cloudinary
//                .url()
//                .transformation(
//                        new Transformation<>()
//                                .width(AppConstants.CONTACT_IMAGE_WIDTH)
//                                .height(AppConstants.CONTACT_IMAGE_HEIGHT)
//                                .crop(AppConstants.CONTACT_IMAGE_CROP))
//                .secure(true)
//                .generate(publicId);
//
//    }
//
//    @Override
//    public void deleteImage(String publicId) {
//        try {
//            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
package com.scm.services.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.scm.helpers.AppConstants;
import com.scm.services.ImageService;

@Service
public class ImageServiceImpl implements ImageService {

    private final Cloudinary cloudinary;

    // Constructor with optional Cloudinary bean
    @Autowired(required = false)
    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile contactImage, String filename) {
        // Check if Cloudinary is available
        if (cloudinary == null) {
            System.out.println("⚠️ Cloudinary not configured – returning default image URL");
            return "/images/logo.png";
        }

        try {
            byte[] data = contactImage.getBytes();
            cloudinary.uploader().upload(data, ObjectUtils.asMap("public_id", filename));
            return getUrlFromPublicId(filename);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getUrlFromPublicId(String publicId) {
        if (cloudinary == null) {
            return "/images/logo.png";
        }
        return cloudinary
                .url()
                .transformation(
                        new Transformation<>()
                                .width(AppConstants.CONTACT_IMAGE_WIDTH)
                                .height(AppConstants.CONTACT_IMAGE_HEIGHT)
                                .crop(AppConstants.CONTACT_IMAGE_CROP))
                .secure(true)
                .generate(publicId);
    }

    @Override
    public void deleteImage(String publicId) {
        if (cloudinary == null) return;
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}