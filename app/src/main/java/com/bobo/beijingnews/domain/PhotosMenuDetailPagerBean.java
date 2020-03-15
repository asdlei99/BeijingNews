package com.bobo.beijingnews.domain;

import java.util.List;

/**
 * Created by 微信公众号IT波 on 2020/3/15. Copyright © Leon. All rights reserved.
 * Functions: 组图页 数据模型
 */
public class PhotosMenuDetailPagerBean {


    private int retcode;
    private DataBean data;

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        /**
         * title : 组图
         * topic : []
         * news : [{"id":147265,"title":"广州93岁肌肉\u201c型爷\u201d走红","url":"/static/html/2015/
         * countcommenturl : /client/content/countComment/
         * more : /static/api/news/10003/list_2.json
         */
        private String title;
        private String countcommenturl;
        private String more;
        private List<?> topic;
        private List<NewsBean> news;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCountcommenturl() {
            return countcommenturl;
        }

        public void setCountcommenturl(String countcommenturl) {
            this.countcommenturl = countcommenturl;
        }

        public String getMore() {
            return more;
        }

        public void setMore(String more) {
            this.more = more;
        }

        public List<?> getTopic() {
            return topic;
        }

        public void setTopic(List<?> topic) {
            this.topic = topic;
        }

        public List<NewsBean> getNews() {
            return news;
        }

        public void setNews(List<NewsBean> news) {
            this.news = news;
        }

        public static class NewsBean {
            /**
             * id : 147265
             * title : 广州93岁肌肉“型爷”走红
             * url : /static/html/2015/10/19/724F6A544164197A6B267F47.html
             * listimage : /static/images/2015/10/19/35/1987564164OEUZ.jpg
             * smallimage : /static/images/2015/10/19/74/5619484425YVE.jpg
             * largeimage : /static/images/2015/10/19/62/865381517OOAW.jpg
             * pubdate : 2015-10-19 08:18
             * comment : true
             * commenturl : /client/user/newComment/147265
             * type : news
             * commentlist : /static/api/news/10003/65/147265/comment_1.json
             */

            private int id;
            private String title;
            private String url;
            private String listimage;
            private String smallimage;
            private String largeimage;
            private String pubdate;
            private boolean comment;
            private String commenturl;
            private String type;
            private String commentlist;

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

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getListimage() {
                return listimage;
            }

            public void setListimage(String listimage) {
                this.listimage = listimage;
            }

            public String getSmallimage() {
                return smallimage;
            }

            public void setSmallimage(String smallimage) {
                this.smallimage = smallimage;
            }

            public String getLargeimage() {
                return largeimage;
            }

            public void setLargeimage(String largeimage) {
                this.largeimage = largeimage;
            }

            public String getPubdate() {
                return pubdate;
            }

            public void setPubdate(String pubdate) {
                this.pubdate = pubdate;
            }

            public boolean isComment() {
                return comment;
            }

            public void setComment(boolean comment) {
                this.comment = comment;
            }

            public String getCommenturl() {
                return commenturl;
            }

            public void setCommenturl(String commenturl) {
                this.commenturl = commenturl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getCommentlist() {
                return commentlist;
            }

            public void setCommentlist(String commentlist) {
                this.commentlist = commentlist;
            }
        }
    }
}
