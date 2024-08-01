package tig.server.config;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Profile("!test")
public class S3Uploader {

    @Autowired
    private S3Config s3Config;

    @Value("${AWS_BUCKET}")
    private String bucket;

    @Value("${AWS_CLOUDFRONT_PATH}")
    private String cloudfrontPath;

    private final int PRESIGNED_URL_EXPIRATION = 60 * 1000 * 10; // 10분

    private String getPresignedUrl(String fileName) {
        return generatePresignedUrl(fileName).toString();
    }



    public String getPresignedUrl(Long clubId, String fileName) {
        return generatePresignedUrl(clubId, fileName).toString();
    }

    public List<String> getPresignedUrls(Long clubId, List<String> fileNames) {
        List<String> presignedUrlList = new ArrayList<>();
        for (String fileName : fileNames) {
            presignedUrlList.add(getPresignedUrl(clubId, fileName));
        }
        return presignedUrlList;
    }

    // Upload file to S3 using presigned url
    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(Long clubId, String fileName) {
        return new GeneratePresignedUrlRequest(bucket, clubId.toString() + "/" + fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(setExpiration());
    }

    private URL generatePresignedUrl(Long clubId, String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(clubId, fileName);
        URL url = s3Config.amazonS3().generatePresignedUrl(generatePresignedUrlRequest);
        return url;
    }

    private Date setExpiration(){
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += PRESIGNED_URL_EXPIRATION;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    public String getUniqueFilename(String fileName) {
        return UUID.randomUUID().toString();
    }

    //get file from cloudfront not using presigned url
    private String getCloudfrontFilePath(String fileName) {
        return cloudfrontPath + fileName;
    }

    public void deleteFile(String fileName){
        if (fileName == null) {
            return;
        }
        s3Config.amazonS3().deleteObject(new DeleteObjectRequest(bucket, "test/"+fileName));
        System.out.println(fileName);
    }

    public void deleteFiles(List<String> fileNames) {
        if (fileNames == null || fileNames.isEmpty()) {
            return;
        }

        fileNames.forEach(this::deleteFile);
    }


//    public String uploadFile(String fileName){
//        return getPresignedUrl(fileName);
//    }
//
//    public List<String> uploadFileList(List<String> fileNameList){
//        List<String> presignedUrlList = new ArrayList<>();
//        for (String fileName : fileNameList) {
//            String uniqueFileName = getUniqueFilename(fileName);
//            presignedUrlList.add(getPresignedUrl(uniqueFileName));
//        }
//        return presignedUrlList;
//    }
//
//    public String getImageUrl(String uniqueFileName){
//        return getCloudfrontFilePath(uniqueFileName);
//    }

    public String getImageUrl(String uniqueFileName) {
        if (uniqueFileName.contains("cloudfront.net")) {
            return uniqueFileName; // 이미 CloudFront URL이 포함된 경우 그대로 반환
        }
        return getCloudfrontFilePath(uniqueFileName); // CloudFront URL 생성
    }

    public List<String> getImageUrls(List<String> imageUrls) {
        return imageUrls.stream()
                .map(this::getImageUrl)
                .collect(Collectors.toList());
    }


    // Upload file to S3 using presigned url
    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(String fileName) {
        System.out.println("fileName: " + fileName);
        return new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(setExpiration());
    }

    private URL generatePresignedUrl(String fileName) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratePresignedUrlRequest(fileName);
        URL url = s3Config.amazonS3().generatePresignedUrl(generatePresignedUrlRequest);
        return url;
    }

    public String uploadFile(String fileName){
        return getPresignedUrl(fileName);
    }

    public List<String> uploadFileList(List<String> fileNameList){
        List<String> presignedUrlList = new ArrayList<>();
        for (String fileName : fileNameList) {
            presignedUrlList.add(getPresignedUrl(fileName));
        }
        return presignedUrlList;
    }

    private boolean checkFileExists(String fileName) {
        // cloudfrontUrl 부분을 앞에서부터 제거
        String cleanedFileName = fileName;
        if (fileName.startsWith(cloudfrontPath)) {
            cleanedFileName = fileName.substring(cloudfrontPath.length());
        }
        return s3Config.amazonS3().doesObjectExist(bucket, cleanedFileName);
    }
}
