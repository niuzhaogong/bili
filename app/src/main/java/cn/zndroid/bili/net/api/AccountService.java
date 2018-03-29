package cn.zndroid.bili.net.api;

import cn.zndroid.bili.entity.user.UserDetailsInfo;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 用户个人账号相关api
 * Created by Administrator on 2018/3/26 0026.
 */

public interface AccountService {
    /**
     * 用户详情数据
     */
    @GET("api/member/getCardByMid")
    Observable<UserDetailsInfo> getUserInfoById(@Query("mid") int mid);
}
