package dnd.donworry.core.manager;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {
    String upload(String url, MultipartFile multipartFile) throws Exception;

    String getFileLocation(String avatar);
}
