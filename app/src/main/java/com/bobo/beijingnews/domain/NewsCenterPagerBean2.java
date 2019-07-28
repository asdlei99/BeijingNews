package com.bobo.beijingnews.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by 求知自学网 on 2019/7/28. Copyright © Leon. All rights reserved.
 * Functions: 手动解析json练习 本类中的内容全部是手写的
 * 有挑战性的练习地址： https://api.bilibili.com/online_list?_device=android&platform=android&typeid=13&sign=a520d8d8f7a
 * 7240013006e466c8044f7
 */
public class NewsCenterPagerBean2 {

    List<DetailPagerData> data;

    private List<Integer> extend;

    //"retcode":200,
    private int retcode;

    public List<DetailPagerData> getData() {
        return data;
    }

    public void setData(List<DetailPagerData> data) {
        this.data = data;
    }

    public List<Integer> getExtend() {
        return extend;
    }

    public void setExtend(List<Integer> extend) {
        this.extend = extend;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public static class DetailPagerData{

        //"id":10000,
        private int id;

        //"title":"专题",
        private String title;

        //"type":10,
        private int type;

        //"url":"/static/api/news/10002/list_1.json",
        private String url;

        // "url1":"/static/api/news/10002/list1_1.json"
        private String url1;

        // "excurl":"/static/api/news/comment/exc_1.json",
        private String excurl;

        //"dayurl":"/static/api/news/comment/day_1.json",
        private String dayurl;

        //"weekurl":"/static/api/news/comment/week_1.json"
        private String weekurl;

        private List<ChildrenData> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl1() {
            return url1;
        }

        public void setUrl1(String url1) {
            this.url1 = url1;
        }

        public String getExcurl() {
            return excurl;
        }

        public void setExcurl(String excurl) {
            this.excurl = excurl;
        }

        public String getDayurl() {
            return dayurl;
        }

        public void setDayurl(String dayurl) {
            this.dayurl = dayurl;
        }

        public String getWeekurl() {
            return weekurl;
        }

        public void setWeekurl(String weekurl) {
            this.weekurl = weekurl;
        }

        public List<ChildrenData> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenData> children) {
            this.children = children;
        }

        public static class  ChildrenData{

            //"id":10192,
            private int id;

            // "title":"倍儿逗",
            private String title;

            //"type":1,
            private int type;

            //"url":"/static/api/news/10192/list_1.json"
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
