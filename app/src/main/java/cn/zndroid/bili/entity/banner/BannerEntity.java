package cn.zndroid.bili.entity.banner;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public class BannerEntity {
    public String title;
    public String img;
    public String link;

    public BannerEntity(String link, String title, String img) {
        this.link = link;
        this.title = title;
        this.img = img;
    }
}
