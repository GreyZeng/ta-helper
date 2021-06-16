package org.snippet;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.http.HttpRequest;

public class ReportRecord {
    public String name, id, link;
    public int total, dailyNew;
    List<String> comments;

    public ReportRecord(String name, String id, String link, int total, int dailyNew) {
        this.name = name;
        this.id = id;
        this.link = link;
        this.total = total;
        this.dailyNew = dailyNew;
        comments = new ArrayList<String>();
    }

    public void prepare() {
        total += dailyNew;
        dailyNew = 0;
        comments = new ArrayList<String>();
    }

    public void setComments(LocalDate today) {
        int pageIndex = 1;
        int pageSize = 30;
        boolean nextPage;
        do {
            nextPage = false;
            comments.addAll(comments(id, pageIndex, pageSize));
            if (sameDay(comments.get(comments.size() - 1), today.minusDays(1))) {
                nextPage = true;
                pageIndex++;
            }
        } while (nextPage);
    }

    public void setDailyNew() {
        dailyNew = comments.size();
    }

    private boolean sameDay(String comment, LocalDate someday) {
        String commentDateString = comment.split(" ")[0];
        LocalDate commentDate = LocalDate.parse(commentDateString, DateTimeFormatter.ISO_LOCAL_DATE);

        if (commentDate.equals(someday)) {
            return true;
        } else {
            return false;
        }
    }

    public void filterComments(LocalDate today) {
        ListIterator<String> iter = comments.listIterator();
        while (iter.hasNext()) {
            if (!sameDay(iter.next(), today.minusDays(1))) {
                iter.remove();
            }
        }
    }

    private static List<String> comments(String user, int pageIndex, int pageSize) {
        String cookie = ResourceUtil.getKey("cookie");

        String url = UrlBuilder.of("https://home.cnblogs.com/ajax/feed/recent", UTF_8).addQuery("alias", user).build();
        String referer = "https://home.cnblogs.com/u/" + user + "/";
        String body = HttpRequest.post(url).header("authority", "home.cnblogs.com")
                .header("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Microsoft Edge\";v=\"91\", \"Chromium\";v=\"91\"")
                .header("accept", "text / plain, */*; q=0.01")
                .header("x-requested-with", "XMLHttpRequest")
                .header("sec-ch-ua-mobile", "?0")
                .header("user-agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.101 Safari/537.36 Edg/91.0.864.48")
                .header("content-type", "application/json; charset=UTF-8")
                .header("origin", "https://home.cnblogs.com")
                .header("sec-fetch-site", "same-origin")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-dest", "empty")
                .header("referer", referer)
                .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                .header("cookie", cookie)
                .body("{\"feedListType\":\"me\",\"appId\":\"\",\"pageIndex\":" + pageIndex + ",\"pageSize\":" + pageSize
                        + ",\"groupId\":\"\"}")
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
