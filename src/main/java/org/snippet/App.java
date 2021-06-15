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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 获取助教点评和团队反馈
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
public class App {
	private static final Boolean FIRST = false;
	
	private static final String OUTPUT_JSONS = "output\\jsons\\";
	private static final String OUTPUT_COMMENTS = "output\\comments\\";
	private static final String OUTPUT_REPORTS = "output\\reports\\";
	
	static List<ReportRecord> report = new ArrayList<ReportRecord>(); 
	
	static LocalDate today = LocalDate.now();
	
    public static void main(String[] args) {
    	if(FIRST) {
    		initReportByNew();
    	} else {
    		initReportByJson();
    	}

        int pageIndex = 1;
        int pageSize = 30;
        for (ReportRecord record : report) {
        	String user = record.id;
            List<String> comments = comment(user, pageIndex, pageSize);
            record.dailyNew = getDailyNew(comments);
            System.out.println(comments.size() + " " + record.dailyNew);
        }
        
        writeJson();
        writeReport();
    }

	private static void readJson() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(OUTPUT_JSONS + today.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE))) {
        	Type listType = new TypeToken<ArrayList<ReportRecord>>(){}.getType();
            report = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private static void writeJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(OUTPUT_JSONS + today.format(DateTimeFormatter.ISO_LOCAL_DATE))) {
            gson.toJson(report, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private static void initReportByJson() {
		readJson();
		for(ReportRecord record : report ) {
			record.total += record.dailyNew;
			record.dailyNew = 0;
		}
	}

	private static void initReportByNew() {
        report.add(new ReportRecord("单老师", "fzuedu", "", 562, 15));
        report.add(new ReportRecord("曾助教", "greyzeng", "", 380, 15));
        report.add(new ReportRecord("张助教", "zhangadian", "", 255, 18));
        report.add(new ReportRecord("杨助教", "cykablyat", "", 242, 0));
        report.add(new ReportRecord("孙助教", "ago8910", "", 198, 3));
        report.add(new ReportRecord("汪老师", "cocoSE", "", 191, 0));
        report.add(new ReportRecord("徐助教", "kofyou", "", 189, 2));
        report.add(new ReportRecord("林助教", "lxy3", "", 155, 0));
	}

	private static int getDailyNew(List<String> comments) {
    	int dailyNew = 0;
		for(String comment : comments) {
			String commentDateString = comment.split(" ")[0];
			LocalDate commentDate = LocalDate.parse(commentDateString, DateTimeFormatter.ISO_LOCAL_DATE);
			
			LocalDate yesterday = today.minusDays(1);
			if(commentDate.equals(yesterday)) {
				dailyNew++;
				System.out.println(comment);
			}
		}
		return dailyNew;
	}
    
    private static void writeReport() {
		
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


