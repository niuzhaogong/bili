package cn.zndroid.bili.net.api;

import cn.zndroid.bili.entity.user.UserCoinsInfo;
import cn.zndroid.bili.entity.user.UserContributeInfo;
import cn.zndroid.bili.entity.user.UserPlayGameInfo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2018/3/26 0026.
 */

public interface UserService {

    /**
     * 用户所玩游戏
     */
    @GET("ajax/game/GetLastPlay")
    Observable<UserPlayGameInfo> getUserPlayGames(@Query("mid") int mid);

    /**
     * 用户投币视频
     */
    @GET("ajax/member/getCoinVideos")
    Observable<UserCoinsInfo> getUserCoinVideos(@Query("mid") int mid);


    /**
     * 用户投稿视频
     */
    @GET("ajax/member/getSubmitVideos")
    Observable<UserContributeInfo> getUserContributeVideos(
            @Query("mid") int mid, @Query("page") int page, @Query("pagesize") int pageSize);
}
