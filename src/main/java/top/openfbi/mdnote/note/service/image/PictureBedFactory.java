package top.openfbi.mdnote.note.service.image;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.openfbi.mdnote.note.service.image.impl.LocalPictureBedServiceImpl;
import top.openfbi.mdnote.note.service.image.impl.QiNiuPictureBedServiceImpl;

import java.util.HashMap;
import java.util.Map;

@Component
public class PictureBedFactory {
    private static int PictureBed;
    private static PictureBedFactory pictureBedFactory;
    @Autowired
    private QiNiuPictureBedServiceImpl qiniuService;
    @Autowired
    private LocalPictureBedServiceImpl localServer;
    private static final Map<Integer, PictureBedInterface> createImgFilePictureBed = new HashMap<>();

    @Value(value = "${fileupload.picturebed}")
    public void setPictureBed(int pictureBed) {
        PictureBedFactory.PictureBed = pictureBed;
    }

    @PostConstruct
    public void init() {
        pictureBedFactory = this;
        pictureBedFactory.createImgFilePictureBed.put(0, this.qiniuService);
        pictureBedFactory.createImgFilePictureBed.put(1, this.localServer);
    }

    @PostConstruct
    public static PictureBedInterface createImgFilePictureBed() {
        return pictureBedFactory.createImgFilePictureBed.get(PictureBedFactory.PictureBed);
    }
}
