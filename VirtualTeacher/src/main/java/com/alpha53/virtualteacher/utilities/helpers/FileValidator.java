package com.alpha53.virtualteacher.utilities.helpers;

import com.alpha53.virtualteacher.exceptions.UnsupportedFileTypeException;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;


public class FileValidator {

    private static final Set<String> acceptableSolutionFormats = new HashSet<>(Set.of("application/vnd.openxmlformats-officedocument.wordprocessingml.document", "text/plain"));

    private static final Set<String> acceptablePhotoFormats = new HashSet<>(Set.of("image/png","image/jpg","image/jpeg"));

    public static boolean fileTypeValidator(MultipartFile file, String fileType) {
        if (file==null){
            throw new UnsupportedFileTypeException();
        }
        if (fileType.equalsIgnoreCase("text") && file.getContentType() != null && acceptableSolutionFormats.contains(file.getContentType())) {
            return true;
        }
        if (fileType.equalsIgnoreCase("picture") && file.getContentType() != null && acceptablePhotoFormats.contains(file.getContentType())) {
            return true;
        }
        throw new UnsupportedFileTypeException(file.getContentType());

    }

}
