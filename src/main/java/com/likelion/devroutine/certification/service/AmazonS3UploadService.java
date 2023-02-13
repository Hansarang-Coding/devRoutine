package com.likelion.devroutine.certification.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.likelion.devroutine.certification.dto.amazons3.FileUploadResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AmazonS3UploadService {

    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public FileUploadResponse uploadFiles(Long userId, MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("Error: MultipartFile -> File로 전환이 실패했습니다."));
        return upload(userId, uploadFile, dirName);
    }

    @Transactional
    public FileUploadResponse upload(Long certificationId, File uploadFile, String filePath) {
        log.info("start upload method");
        String fileName = filePath + "/" + certificationId + uploadFile.getName(); // S3에 저장된 파일 이름
        log.info("upload fileName : "+fileName);
        String uploadImageUrl = putS3(uploadFile, fileName); // S3로 업로드
        log.info("uploadImageUrl = " + uploadImageUrl);
        removeNewFile(uploadFile);
        return new FileUploadResponse(fileName, uploadImageUrl);
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        log.info("start puts3 method");
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        log.info("start convert method");
        log.info("convertFile : "+System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        log.info("convertFile : " + convertFile);
        if (convertFile.createNewFile()) {
            log.info("convertFile create if문");
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                log.info("fos try-catch 내부");
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.of(convertFile);
    }
}
