package top.openfbi.mdnote.note.service.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.openfbi.mdnote.common.exception.ResultException;
import top.openfbi.mdnote.note.model.Image;

/**
 * 图片上传接口类
 */
@Service
public interface PictureBedInterface {

    /**
     * 图片上传
     * @return
     */
    Image save(MultipartFile uploadFile) throws ResultException;
}
