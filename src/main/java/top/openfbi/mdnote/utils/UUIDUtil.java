package top.openfbi.mdnote.utils;

import java.util.UUID;

/**
 * UUID生成类
 */
public class UUIDUtil {

    /**
     * 生成UUID
     */
    public static Integer random() {
        Integer uuid = UUID.randomUUID().toString().replaceAll("-", "").hashCode();
        uuid = uuid < 0 ? -uuid : uuid;//String.hashCode() 值会为空
        return uuid;
    }
}