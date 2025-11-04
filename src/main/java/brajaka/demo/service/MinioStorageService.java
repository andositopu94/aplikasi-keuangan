//package brajaka.demo.service;
//
//import io.minio.GetPresignedObjectUrlArgs;
//import io.minio.MinioClient;
//import io.minio.PutObjectArgs;
//import io.minio.RemoveObjectArgs;
//import io.minio.http.Method;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//
//@Service
//public class MinioStorageService {
//    @Autowired
//    private MinioClient minioClient;
//
//    @Value("${minio.bucket.name}")
//    private String bucketName;
//
//    @Value("${minio.url.expiry:60}")
//    private int expiryMinutes;
//
//    //upload file dan return
//    public String storeFile(MultipartFile file) throws Exception {
//        String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();
//        minioClient.putObject(
//                PutObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(objectName)
//                        .stream(file.getInputStream(), file.getSize(), -1)
//                        .contentType(file.getContentType())
//                        .build()
//        );
//        return objectName;
//    }
//
//    public String getFileUrl(String objectName) throws Exception {
//        return minioClient.getPresignedObjectUrl(
//                GetPresignedObjectUrlArgs.builder()
//                        .method(Method.GET)
//                        .bucket(bucketName)
//                        .object(objectName)
//                        .expiry(expiryMinutes, TimeUnit.MINUTES)
//                        .build()
//        );
//    }
//
//    public void deleteFile(String objectName) throws Exception {
//        minioClient.removeObject(
//                RemoveObjectArgs.builder()
//                        .bucket(bucketName)
//                        .object(objectName)
//                        .build()
//        );
//    }
//}
