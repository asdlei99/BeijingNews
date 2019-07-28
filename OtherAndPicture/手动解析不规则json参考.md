
#手动解析不规则的json可以参考以前写过的这段代码
Github地址：https://github.com/leonInShanghai/NewsReader/blob/7a500cb8eb503565773211cc353b477d1adca181/app/src/main/java/com/bobo520/newsreader/news/controller/activity/KeyboardManActivity.java

               //准备一个集合出来
                ArrayList<NewsCommentBean> newsCommentList = new ArrayList<>();

                //解析起来首先需要手动解析id
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //1.分为两块，第一块先解析commentIds,评论的id数组，第二块comments这些评论jsonObject
                    JSONArray commentIds = jsonObject.getJSONArray("commentIds");
                    JSONObject comments = jsonObject.getJSONObject("comments");

                    //2解析commentIds,把这些id都读取出来
                    for (int i = 0;i < commentIds.length();i++){
                        //这里直接getString(i)就不需要再将对象转string了
                        String commentId = commentIds.getString(i);

                        //3.根据这些id来解析第二块comments中的各个评论 getJSONObject
                        //id可能两个并排在一起"56415914,57116175"，此时取出最后面的id进行使用
                        if (commentId.contains(",")){
                            //将56415914,57116175"从“，”剪切成N段
                            String[] split = commentId.split(",");
                            //只要最后一段
                            commentId = split[split.length - 1];
                        }
                        JSONObject comment = comments.optJSONObject(commentId);

                        //4.拿到各个匹配的string数据，通过三方库gson来解析成为JavaBean
                        String s = comment.toString();
                        NewsCommentBean newsCommentBean = JsonUtil.parseJson(s, NewsCommentBean.class);
                        newsCommentList.add(newsCommentBean);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //设置list view数据
                setListViewData(newsCommentList);
            }
        });