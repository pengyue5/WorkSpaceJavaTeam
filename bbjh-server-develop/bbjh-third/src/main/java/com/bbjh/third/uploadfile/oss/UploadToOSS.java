package com.bbjh.third.uploadfile.oss;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Configuration
@PropertySource(value = "classpath:/third.properties")
public class UploadToOSS {
    // OSS
    private static String accessId;

    private static String accessKey;

    private static String ossEndpoint;
    //图片域名
    private static String returnImgUrl;
    //文件域名
    private static String returnFileUrl;
    //OSS图片上传根目录
    private static String imagePath;
    //OSS文件上传根目录
    private static String filePath;
    // OSS连接超时时间
    private static int socketTimeout;
    // OSS最大连接次数
    private static int maxConnections;

    //OSS图片允许格式
    private static String imgAllowType;
    //OSS文件允许格式
    private static String fileAllowType;
    //OSS imageBucket
    private static String imageBucketName;
    //OSS fileBucket
    private static String fileBucketName;

    /**
     * 文件上传 已封装文件重命名逻辑
     *
     * @param upload   要上传的文件
     * @param fileType 文件类型，如 .txt .jpg
     * @return 上传成功则返回文件保存的相对路径，否则为null
     * @throws FileNotFoundException
     * @throws ClientException
     * @throws OSSException
     * @throws Exception
     */
    public static Map<String,String> uploadFileToOss(MultipartFile upload, String fileType)
            throws OSSException, ClientException, IOException {
        // --------------OSS操作
        OSSObjectUpload oSSObjectUpload = new OSSObjectUpload();
        String bucketName = "";
        String MEDIA_PATH = "";//上传根目录
        /* 验证是否支持该格式 */
        List<String> imageAllowList = Arrays.asList(imgAllowType.split(","));
        List<String> fileAllowList = Arrays.asList(fileAllowType.split(","));

        if (imageAllowList.contains(fileType)) {
            bucketName = imageBucketName;
            MEDIA_PATH = imagePath;
        } else if (fileAllowList.contains(fileType)) {
            bucketName = fileBucketName;
            MEDIA_PATH = filePath;
        } else {
            return null;
        }



        String yyyyMMdd = DateUtil.date().toString(DatePattern.PURE_DATE_PATTERN);
        //当前时间戳
        long time = new Date().getTime();
        //重新定义文件名（文件名后加时间戳）
        String newFileName = time + "." + fileType;
        String key = MEDIA_PATH + "/" + yyyyMMdd + "/"
                + newFileName;// OCS访问目录
        // 可以使用ClientConfiguration对象设置代理服务器、最大重试次数等参数。
        ClientConfiguration config = new ClientConfiguration();
        config.setSocketTimeout(socketTimeout);
        config.setMaxConnections(maxConnections);
        OSSClient client = new OSSClient(ossEndpoint, accessId,
                accessKey, config);
        // 如果Bucket不存在，则创建它。
        oSSObjectUpload.ensureBucket(client, bucketName);
        // 把Bucket设置为所有人可读
        //oSSObjectUpload.setBucketPublicReadable(client , bucketName);
        // 上传文件
        String contentType = upload.getContentType();
        oSSObjectUpload.uploadFile(client, bucketName, key,
                upload.getOriginalFilename(),
                contentType, upload.getInputStream());
        //根据文件类型返回文件类型的域名
        Map<String,String> result = new HashMap<>();
        if (imageAllowList.contains(fileType)) {
            result.put("fileUrl",returnImgUrl + key);
        } else if (fileAllowList.contains(fileType)) {
            result.put("fileUrl",returnFileUrl + key);
        }
        result.put("filePath",key);
        return result;
    }

    @Value("${OSS_ACCESS_ID}")
    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    @Value("${OSS_ACCESS_KEY}")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    @Value("${OSS_OSS_ENDPOINT}")
    public void setOssEndpoint(String ossEndpoint) {
        this.ossEndpoint = ossEndpoint;
    }

    @Value("${OSS_RETURN_IMG_URL}")
    public void setReturnImgUrl(String returnImgUrl) {
        this.returnImgUrl = returnImgUrl;
    }

    @Value("${OSS_RETURN_FILE_URL}")
    public void setReturnFileUrl(String returnFileUrl) {
        this.returnFileUrl = returnFileUrl;
    }

    @Value("${OSS_IMAGE_PATH}")
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Value("${OSS_FILE_PATH}")
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Value("${OSS_SOCKET_TIMEOUT}")
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    @Value("${OSS_MAX_CONNECTIONS}")
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    @Value("${OSS_IMG_ALLOW_TYPE}")
    public void setImgAllowType(String imgAllowType) {
        this.imgAllowType = imgAllowType;
    }

    @Value("${OSS_FILE_ALLOW_TYPE}")
    public void setFileAllowType(String fileAllowType) {
        this.fileAllowType = fileAllowType;
    }

    @Value("${OSS_IMAGE_BUCKET}")
    public void setImageBucketName(String imageBucketName) {
        this.imageBucketName = imageBucketName;
    }

    @Value("${OSS_FILE_BUCKET}")
    public void setFileBucketName(String fileBucketName) {
        this.fileBucketName = fileBucketName;
    }
}
