package org.snippet;


/*
团队反馈主页：
烤盐人反馈主页：https://home.cnblogs.com/u/KaoYanRen/
福大周润发反馈主页：https://home.cnblogs.com/u/deskgame/
逐梦校友圈反馈主页：https://home.cnblogs.com/u/zmxyq/
老九门反馈主页：https://home.cnblogs.com/u/ljm2021/
饱满骑士反馈主页：https://home.cnblogs.com/u/solidknight/
青青草原反馈主页：https://home.cnblogs.com/u/qqcyfzu/
字节乱动反馈主页：https://home.cnblogs.com/u/eightGroup/
评了么反馈主页：https://home.cnblogs.com/u/PingLeMe/
峡谷partners反馈主页：https://home.cnblogs.com/u/partners/

助教&老师点评主页：
单老师：https://home.cnblogs.com/u/fzuedu/
曾助教：https://home.cnblogs.com/u/greyzeng/
张助教：https://home.cnblogs.com/u/zhangadian/
杨助教：https://home.cnblogs.com/u/cykablyat/
孙助教：https://home.cnblogs.com/u/ago8910/
汪老师：https://home.cnblogs.com/u/cocoSE/
徐助教：https://home.cnblogs.com/u/kofyou/
林助教：https://home.cnblogs.com/u/lxy3/
*/

//curl 'https://home.cnblogs.com/ajax/feed/recent?alias=deskgame' \
//        -H 'authority: home.cnblogs.com' \
//        -H 'sec-ch-ua: " Not;A Brand";v="99", "Microsoft Edge";v="91", "Chromium";v="91"' \
//        -H 'accept: text/plain, */*; q=0.01' \
//        -H 'x-requested-with: XMLHttpRequest' \
//        -H 'sec-ch-ua-mobile: ?0' \
//        -H 'user-agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.101 Safari/537.36 Edg/91.0.864.48' \
//        -H 'content-type: application/json; charset=UTF-8' \
//        -H 'origin: https://home.cnblogs.com' \
//        -H 'sec-fetch-site: same-origin' \
//        -H 'sec-fetch-mode: cors' \
//        -H 'sec-fetch-dest: empty' \
//        -H 'referer: https://home.cnblogs.com/u/deskgame/' \
//        -H 'accept-language: zh-CN,zh;q=0.9,en;q=0.8' \
//        -H 'cookie: 自己手动登录以后，复制一下到这里，不要提交!!!' \
//        --data-raw '{"feedListType":"me","appId":"","pageIndex":1,"pageSize":30,"groupId":""}' \
//        --compressed

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 获取助教点评和团队反馈
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
public class App {
    public static void main(String[] args) {
        String user = "greyzeng";
        int pageIndex = 1;
        int pageSize = 30;
        List<String> comments = comment(user,pageIndex,pageSize);
        for (String comment : comments) {
            System.out.println(comment);
        }

    }
    public static List<String> comment(String user, int pageIndex, int pageSize) {
        String cookie = ResourceUtil.getKey("cookie");

        String url = UrlBuilder.of("https://home.cnblogs.com/ajax/feed/recent", UTF_8).addQuery("alias", user).build();
        String referer = "https://home.cnblogs.com/u/" + user + "/";
        String body = HttpRequest.post(url).header("authority", "home.cnblogs.com")
                .header("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Microsoft Edge\";v=\"91\", \"Chromium\";v=\"91\"")
                .header("accept", "text / plain, */*; q=0.01")
                .header("x-requested-with", "XMLHttpRequest")
                .header("sec-ch-ua-mobile", "?0")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.101 Safari/537.36 Edg/91.0.864.48")
                .header("content-type", "application/json; charset=UTF-8")
                .header("origin", "https://home.cnblogs.com")
                .header("sec-fetch-site", "same-origin")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-dest", "empty")
                .header("referer", referer)
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("cookie", cookie)
                .body("{\"feedListType\":\"me\",\"appId\":\"\",\"pageIndex\":" +
                        pageIndex +
                        ",\"pageSize\":" +
                        pageSize +
                        ",\"groupId\":\"\"}")
                .execute().body();
        Document document = Jsoup.parse(body.trim());
        Elements feedItems = document.getElementsByClass("feed_item");
        Elements feedDescs;
        Elements feedDates;
        List<String> comments = new ArrayList<>();
        for (Element feedItem : feedItems) {
            Elements feedTitles = feedItem.getElementsByClass("feed_title");
            if (null != feedTitles && !feedTitles.get(0).text().contains("发表博客：")) {
                feedDescs = feedItem.getElementsByClass("feed_desc");
                feedDates = feedItem.getElementsByClass("feed_date");
                if (feedDescs != null) {
                    comments.add(feedDates.get(0).text() + " " + feedDescs.get(0).text());
                }
            }
        }
        return comments;
    }

}


