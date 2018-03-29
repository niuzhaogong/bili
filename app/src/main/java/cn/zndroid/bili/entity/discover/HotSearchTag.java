package cn.zndroid.bili.entity.discover;

import java.util.List;

/**
 * 热搜标签模型类
 * Created by Administrator on 2018/3/26 0026.
 */

public class HotSearchTag {
    private int code;
    private String seid;
    private String message;
    private int timestamp;
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String keyword;
        private String status;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
