package top.openfbi.mdnote.note.service.image.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.openfbi.mdnote.common.ResultStatus;
import top.openfbi.mdnote.common.exception.ResultException;
import top.openfbi.mdnote.note.model.Image;
import top.openfbi.mdnote.note.service.image.PictureBedInterface;
import top.openfbi.mdnote.utils.Hash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

@Service
public class LocalPictureBedServiceImpl implements PictureBedInterface {

    private String fileUrlPrefix;
    private String path;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value = "${local-store.url-prefix}")
    public void setFileUrlPrefix(String fileUrlPrefix) {
        this.fileUrlPrefix = fileUrlPrefix;
    }

    @Value(value = "${local-store.path}")
    public void setPath(String path) {
        this.path = path;
    }

    public String generateImageFileName(String fileMd5, int suffix, String extension) {
        // 返回md5+文件后缀名
        StringBuilder sb = new StringBuilder();
        sb.append(fileMd5);
        if (suffix != 0) {
            sb.append('-');
            sb.append(suffix);
        }
        sb.append(extension);

        return sb.toString();
    }

    public void uploadFile(MultipartFile uploadFile, String imgNewName) throws ResultException {
        //判断该路径是否存在
        File path = new File(this.path);
        if (!path.exists()) {
            //如果这个文件夹不存在的话,就创建这个文件
            path.mkdirs();
        }
        //完成文件上传
        File file = new File(this.path, imgNewName);
        try {
            uploadFile.transferTo(file);
        } catch (IOException e) {
            logger.warn("文件保存错误，上传失败");
            throw new ResultException(ResultStatus.FILE_SAVE_FALL);
        }
    }

    @Override
    public Image save(MultipartFile uploadFile) throws ResultException {
        //判断文件是否为空
        if (uploadFile.isEmpty()) {
            logger.warn("文件内容为空，上传失败");
            throw new ResultException(ResultStatus.FILE_CONTENT_EMPTY);
        }
        // 获取文件名
        String uploadFileName = uploadFile.getOriginalFilename();
        if (uploadFileName == null || uploadFileName.isEmpty()) {
            logger.warn("文件名为空，上传失败");
            throw new ResultException(ResultStatus.FILE_NAME_FALL);
        }
        //获取文件后缀
        if (uploadFileName.lastIndexOf(".") == -1) {
            logger.warn("文件不存在后缀名，上传失败");
            throw new ResultException(ResultStatus.FILE_EXTENSION_EMPTY);
        }
        String extension = uploadFileName.substring(uploadFileName.lastIndexOf("."));
        if (extension.equals(".")) {
            logger.warn("文件后缀名格式不正确，上传失败");
            throw new ResultException(ResultStatus.FILE_EXTENSION_FORMAT_INACCURACY);
        }

        // 用文件内容计算md5
        byte[] fileBytes;
        try {
            fileBytes = uploadFile.getBytes();
        } catch (IOException e) {
            logger.error("上传的文件读取错误，上传失败");
            throw new ResultException(ResultStatus.FILE_READ_FALL);
        }

        String fileMd5 = Hash.md5(fileBytes);
        // 如果文件存在，则在md5后添加后缀（-1/-2/-3），循环读取，直到无法找到增加后缀文件
        // 默认最多10次
        Image image = new Image();
        image.setMd5(fileMd5);
        image.setExtension(extension);
        for (int suffix = 0; suffix < 10; suffix++) {
            String fileName = generateImageFileName(fileMd5, suffix, extension);
            // 用md5+文件后缀名 去文件系统上进行读取
            File filePath = new File(path + "/" + fileName);
            String fileUrl = fileUrlPrefix + fileName;
            image.setSuffix(suffix);
            image.setFileUrl(fileUrl);
            try {
                FileInputStream fileInputStream = new FileInputStream(filePath);
                if (Arrays.equals(fileBytes, fileInputStream.readAllBytes())) {
                    //文件已存在
                    return image;
                }
            } catch (FileNotFoundException e) {
                // 如果文件不存在，则写入文件，
                uploadFile(uploadFile, fileName);
                return image;
            } catch (IOException e) {
                logger.error("服务器本地文件读取错误，上传失败");
                throw new ResultException(ResultStatus.FILE_READ_FALL);
            }
        }

        throw new ResultException(ResultStatus.FILE_HASH_COLLISION_TOO_MUCH);
    }
}
