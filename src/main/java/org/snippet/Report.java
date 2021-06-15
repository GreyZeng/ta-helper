package org.snippet;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Report {
	List<ReportRecord> report;
	LocalDate today;
	String jsonPath, reportPath, commentPath;
	
	public Report(LocalDate today, String path) {
		this.today = today;
		jsonPath = path + "\\jsons\\";
		reportPath = path + "\\reports\\";
		commentPath = path + "\\comments\\";
	}
	
	public void generateReport() {
		for (ReportRecord record : report) {
			record.setComments();
			record.filterComments(today);
			record.setDailyNew();
			record.writeComments(commentPath, today);
		}
		
        writeJson();
        writeReport();
	}
	
	private void readJson() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(jsonPath + today.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE), StandardCharsets.UTF_8)) {
        	Type listType = new TypeToken<ArrayList<ReportRecord>>(){}.getType();
            report = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	private void writeJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(jsonPath + today.format(DateTimeFormatter.ISO_LOCAL_DATE), StandardCharsets.UTF_8)) {
            gson.toJson(report, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	public void initReportByJson() {
		readJson();
		for(ReportRecord record : report ) {
			record.total += record.dailyNew;
			record.dailyNew = 0;
		}
	}

	public void initReportByNew(List<ReportRecord> report) {
        this.report = report;
	}
    
    private void writeReport() {
    	try (FileWriter writer = new FileWriter(reportPath + today.format(DateTimeFormatter.ISO_LOCAL_DATE), StandardCharsets.UTF_8)) {
    	    writer.write(String.valueOf(Character.toChars(0x2666)) + "点评和问题收集打卡\n");
    	    
    	    writer.write("\n" + String.valueOf(Character.toChars(0x1F680)) + today.format(DateTimeFormatter.ISO_LOCAL_DATE) + "当日点评排行榜\n");
    	    writer.write("---\n");
    	    
    	    Collections.sort(report, (ReportRecord r1, ReportRecord r2) -> r2.dailyNew - r1.dailyNew);
    	    for(ReportRecord record : report) {
    	    	if(record.dailyNew != 0) {
    	    		writer.write(record.name + "：" + record.dailyNew + "\n");
    	    	}
    	    }
    	    
    	    writer.write("\n" + String.valueOf(Character.toChars(0x1F31F)) + "点评汇总排行榜\n");
    	    writer.write("---\n");
    	    Collections.sort(report, (ReportRecord r1, ReportRecord r2) -> r2.dailyNew + r2.total - r1.dailyNew - r1.total);
    	    for(ReportRecord record : report) {
    	    	if(record.dailyNew != 0) {
    	    		writer.write(record.name + "：" + record.link + " " + "点评数：" + record.total + "+" +record.dailyNew + String.valueOf(Character.toChars(0x1F195)) + "\n\n");
    	    	} else {
    	    		writer.write(record.name + "：" + record.link + " " + "点评数：" + record.total + "\n\n");
    	    	}
    	    }
    	    
    	    writer.write(String.valueOf(Character.toChars(0x1F914)) + "团队问题列表\n");
    	    writer.write("---\n");
    	    writer.write("https://github.com/FZUSESPR21W/Class_Resources/blob/main/%E5%90%84%E5%9B%A2%E9%98%9F%E9%97%AE%E9%A2%98%E6%94%B6%E9%9B%86.md");
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
