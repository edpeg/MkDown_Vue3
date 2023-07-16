package top.openfbi.mdnote.note.service.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.openfbi.mdnote.common.exception.ResultException;
import top.openfbi.mdnote.note.model.Image;

/**
 * 图片上传
 */
@Service
public class ImageService {

    /**
     * 上保存文件信息
     */
    public Image save(MultipartFile file) throws ResultException {
        // 保存图片信息
        PictureBedInterface pictureBed = PictureBedFactory.createImgFilePictureBed();
        return pictureBed.save(file);
    }
}
