package cn.zndroid.bili.net.api;

import cn.zndroid.bili.entity.discover.HotSearchTag;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public interface SearchService {
    /**
     * 首页发现热搜词
     */
    @GET("main/hotword?access_key=ec0f54fc369d8c104ee1068672975d6a&actionKey=appkey&appkey=27eb53fc9058f8c3")
    Observable<HotSearchTag> getHotSearchTags();
}
