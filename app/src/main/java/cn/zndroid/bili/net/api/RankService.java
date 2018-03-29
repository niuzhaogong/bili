package cn.zndroid.bili.net.api;

import cn.zndroid.bili.entity.discover.AllareasRankInfo;
import cn.zndroid.bili.entity.discover.OriginalRankInfo;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public interface RankService {
    /**
     * 原创排行榜请求
     */
    @GET("index/rank/{type}")
    Observable<OriginalRankInfo> getOriginalRanks(@Path("type") String type);

    /**
     * 全区排行榜数据请求
     */
    @GET("index/rank/{type}")
    Observable<AllareasRankInfo> getAllareasRanks(@Path("type") String type);
}
