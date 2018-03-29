package cn.zndroid.bili.net;

import cn.zndroid.bili.entity.user.UserDetailsInfo;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class UrlHelper {
    private static final String HDSLB_HOST = "http://i2.hdslb.com";

    public static String getFaceUrl(UserDetailsInfo info) {
        if (info.getCard().getFace().contains(".hdslb.com")) {
            return info.getCard().getFace();
        }
        String face = HDSLB_HOST + info.getCard().getFace();
        if (face.contains("{SIZE}")) {
            face = face.replace("{SIZE}", "");
        }
        return face;
    }


    public static String getFaceUrlByUrl(String url) {
        if (url.contains("/52_52")) {
            return url.replace("/52_52", "");
        }
        return url;
    }


    public static String getClearVideoPreviewUrl(String url) {
        if (!url.contains("/320_180")) {
            return url;
        }
        url = url.replace("/320_180", "");
        return url;
    }
}
