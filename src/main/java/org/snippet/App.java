package org.snippet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

/**
 * 获取助教点评和团队反馈
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @since
 */
public class App {
    private static final Boolean FIRST = true;

    private static final String TA_PATH = "output\\ta";
    private static final String STU_PATH = "output\\stu";

    public static void main(String[] args) {
        Report taReport = new Report(LocalDate.now(), TA_PATH);
        if (FIRST) {
            List<ReportRecord> report = new ArrayList<ReportRecord>();
            report.add(new ReportRecord("单老师", "fzuedu", "https://home.cnblogs.com/u/fzuedu/", 577, 16));
            report.add(new ReportRecord("曾助教", "greyzeng", "https://home.cnblogs.com/u/greyzeng/", 395, 7));
            report.add(new ReportRecord("张助教", "zhangadian", "https://home.cnblogs.com/u/zhangadian/", 273, 0));
            report.add(new ReportRecord("杨助教", "cykablyat", "https://home.cnblogs.com/u/cykablyat/", 242, 0));
            report.add(new ReportRecord("孙助教", "ago8910", "https://home.cnblogs.com/u/ago8910/", 201, 0));
            report.add(new ReportRecord("汪老师", "cocoSE", "https://home.cnblogs.com/u/cocoSE/", 191, 4));
            report.add(new ReportRecord("徐助教", "kofyou", "https://home.cnblogs.com/u/kofyou/", 191, 3));
            report.add(new ReportRecord("林助教", "lxy3", "https://home.cnblogs.com/u/lxy3/", 155, 0));
            taReport.initReportByNew(report);
        } else {
            taReport.initReportByJson();
        }
        taReport.generateReport();

//    	Report stuReport = new Report(LocalDate.now(), STU_PATH);
//    	if(FIRST) {
//    		List<ReportRecord> report = new ArrayList<ReportRecord>()
//	        report.add(new ReportRecord("曾助教", "greyzeng", "https://home.cnblogs.com/u/greyzeng/", 395, 7));
//	        report.add(new ReportRecord("张助教", "zhangadian", "https://home.cnblogs.com/u/zhangadian/", 273, 0));
//	        report.add(new ReportRecord("杨助教", "cykablyat", "https://home.cnblogs.com/u/cykablyat/", 242, 0));
//	        report.add(new ReportRecord("孙助教", "ago8910", "https://home.cnblogs.com/u/ago8910/", 201, 0));
//	        report.add(new ReportRecord("汪老师", "cocoSE", "https://home.cnblogs.com/u/cocoSE/", 191, 4));
//	        report.add(new ReportRecord("徐助教", "kofyou", "https://home.cnblogs.com/u/kofyou/", 191, 3));
//	        report.add(new ReportRecord("林助教", "lxy3", "https://home.cnblogs.com/u/lxy3/", 155, 0));
//          stuReport.initReportByNew(report);
//    	} else {
//    		stuReport.initReportByJson();
//    	}
//    	stuReport.generateReport();
    }

}
