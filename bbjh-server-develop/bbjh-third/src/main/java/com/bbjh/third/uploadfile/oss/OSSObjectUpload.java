package com.bbjh.third.uploadfile.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.*;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;

/**
 * 该示例代码展示了如果在OSS中创建和删除一个Bucket，以及如何上传和下载一个文件。
 * <p>
 * 该示例代码的执行过程是： 1. 检查指定的Bucket是否存在，如果不存在则创建它； 2. 上传一个文件到OSS； 3. 下载这个文件到本地； 4.
 * 清理测试资源：删除Bucket及其中的所有Objects。
 * <p>
 * 尝试运行这段示例代码时需要注意： 1. 为了展示在删除Bucket时除了需要删除其中的Objects,
 * 示例代码最后为删除掉指定的Bucket，因为不要使用您的已经有资源的Bucket进行测试！ 2.
 * 请使用您的API授权密钥填充ACCESS_ID和ACCESS_KEY常量； 3.
 * 需要准确上传用的测试文件，并修改常量uploadFilePath为测试文件的路径； 修改常量downloadFilePath为下载文件的路径。 4.
 * 该程序仅为示例代码，仅供参考，并不能保证足够健壮。
 */
public class OSSObjectUpload {

    // 如果Bucket不存在，则创建它。
    public void ensureBucket(OSSClient client, String bucketName) throws OSSException,
            ClientException {

        if (client.isBucketExist(bucketName)) {
            return;
        }

        // 创建bucket
        client.createBucket(bucketName);
    }

    // 删除一个Bucket和其中的Objects
    public void deleteBucket(OSSClient client, String bucketName) throws OSSException,
            ClientException {

        ObjectListing ObjectListing = client.listObjects(bucketName);
        List<OSSObjectSummary> listDeletes = ObjectListing.getObjectSummaries();
        for (int i = 0; i < listDeletes.size(); i++) {
            String objectName = listDeletes.get(i).getKey();
            // 如果不为空，先删除bucket下的文件
            client.deleteObject(bucketName, objectName);
        }
        client.deleteBucket(bucketName);
    }

    // 把Bucket设置为所有人可读
    public void setBucketPublicReadable(OSSClient client, String bucketName) throws OSSException,
            ClientException {
        // 创建bucket
        client.createBucket(bucketName);

        // 设置bucket的访问权限，public-read-write权限
        client.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
    }

    // 上传文件
    public void uploadFile(OSSClient client, String bucketName, String key, String filename,
                           String contentType) throws OSSException, ClientException, FileNotFoundException {
        File file = new File(filename);
        ObjectMetadata objectMeta = new ObjectMetadata();
        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType(contentType);
        objectMeta.setContentDisposition("attachment;filename=\"" + file.getName() + "\"");
        InputStream input = new FileInputStream(file);
        client.putObject(bucketName, key, input, objectMeta);
    }

    // 上传文件
    public void uploadFile(OSSClient client, String bucketName, String key, String filename,
                           String contentType, InputStream inputStream) throws OSSException, ClientException, FileNotFoundException {
//        File file = new File(filename);
        ObjectMetadata objectMeta = new ObjectMetadata();
//        objectMeta.setContentLength(file.length());
        // 可以在metadata中标记文件类型
        objectMeta.setContentType(contentType);
        objectMeta.setContentDisposition("attachment;filename=\"" + filename + "\"");
        client.putObject(bucketName, key, inputStream, objectMeta);
    }

    // 下载文件
    public void downloadFile(OSSClient client, String bucketName, String key, String filename)
            throws OSSException, ClientException, UnsupportedEncodingException {
        client.getObject(new GetObjectRequest(bucketName, key), new File(URLEncoder.encode(filename, "utf-8")));
    }
}
