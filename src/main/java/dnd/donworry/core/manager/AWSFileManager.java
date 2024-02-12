package dnd.donworry.core.manager;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import dnd.donworry.core.factory.YamlPropertySourceFactory;
import dnd.donworry.domain.constants.ErrorCode;
import dnd.donworry.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@PropertySource(value = {"classpath:application-s3.yml"}, factory = YamlPropertySourceFactory.class)
@Slf4j
@Component
public class AWSFileManager implements FileManager {

	private final AmazonS3 amazonS3;

	@Value(value = "${cloud.aws.s3.bucket}")
	private String bucket;

	@Override
	public String upload(String path, MultipartFile multipartFile) throws Exception {

		ObjectMetadata objectMetadata = metaDataInstance(multipartFile);
		String fileUrl = makeFileUrl(path, multipartFile);

		try {
			amazonS3.putObject(new PutObjectRequest(
				bucket, fileUrl, multipartFile.getInputStream(), objectMetadata));
		} catch (IOException e) {
			throw new CustomException(ErrorCode.IMAGE_UPLOAD_FAIL); // 실제 익셉션으로 변경 필요
		}
		return getFileLocation(multipartFile.getOriginalFilename()) + fileUrl;
	}

	@Override
	public String getFileLocation(String key) {
		return amazonS3.getUrl(bucket, key).toString().split("com")[0] + "com" + File.separator;
	}

	private String makeFileUrl(String path, MultipartFile multipartFile) {
		String extension = getExtension(multipartFile);
		String fileName = UUID.randomUUID() + "." + extension;
		return path + File.separator + fileName;
	}

	private String getExtension(MultipartFile multipartFile) {
		String fileName = multipartFile.getOriginalFilename();
		return Objects.requireNonNull(fileName).substring(fileName.lastIndexOf(".") + 1);
	}

	private ObjectMetadata metaDataInstance(MultipartFile multipartFile) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(multipartFile.getContentType());
		objectMetadata.setContentLength(multipartFile.getSize());

		return objectMetadata;
	}

}
