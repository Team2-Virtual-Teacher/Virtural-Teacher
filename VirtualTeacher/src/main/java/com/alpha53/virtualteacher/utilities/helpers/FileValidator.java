package com.alpha53.virtualteacher.utilities.helpers;

import com.alpha53.virtualteacher.exceptions.UnsupportedFileTypeException;
import org.springframework.web.multipart.MultipartFile;


public class FileValidator {

    public static boolean fileTypeValidator(MultipartFile file,String fileType) {

        if (file.getContentType() != null && file.getContentType().startsWith(fileType)) {
            return true;
        }
        throw new UnsupportedFileTypeException(fileType);

    }

}
