package top.openfbi.mdnote;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import com.qiniu.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Component
public class QiniuUploadTest {

    // 上传秘钥
    private String accessKey;
    private String secretKey;
    // 设置上传空间名
    private String bucket;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${qiniu-store.access-key}")
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }
    @Value(value = "${qiniu-store.secret-key}")
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
    @Value(value = "${qiniu-store.bucket}")
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    //测试文件上传
    public void testUpload() {
        //构造一个带指定 Region 对象的配置类，指定存储区域，和存储空间选择的区域一致
        Configuration cfg = new Configuration(Region.beimei());
        //其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //生成上传凭证，然后准备上传
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = UUID.randomUUID() + ".jpg";
        FileInputStream fileInputStream;
        try {
            String filePath = "abc.jpg";
            fileInputStream = new FileInputStream(filePath);
            //得到本地文件的字节数组
            byte[] bytes = IOUtils.toByteArray(fileInputStream);
            //认证
            Auth auth = Auth.create(accessKey, secretKey);
            //认证通过后得到token（令牌）
            String upToken = auth.uploadToken(bucket);
            try {
                //上传文件,参数：字节数组，key，token令牌
                //key: 建议我们自已生成一个不重复的名称
                Response response = uploadManager.put(bytes, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = JSON.parseObject(response.bodyString(),
                        DefaultPutRet.class);
                logger.info("上传成功: {}", JSON.toJSONString(putRet));
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    logger.error("上传失败: {}", ex2.getMessage());
                }
            }
        } catch (IOException ex) {
            logger.error("上传文件失败: {}", ex);
        }
    }
}