package com.github.philippecade.biketoworkstats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

/**
 * Parses the online calendars
 * 
 * @author xphc
 */
class CalendarParser {
	private String url;
	private Consumer<MemberActivity> memberConsumer;
	
	class MemberActivity {
		private int memberId;
		private double kmPerDay;
		private List<Date> bikeDays;

		MemberActivity(int memberId, double kmPerDay, List<Date> bikeDays) {
			this.memberId = memberId;
			this.kmPerDay = kmPerDay;
			this.bikeDays = bikeDays;
		}

		int getMemberId() {
			return this.memberId;
		}

		double getKmPerDay() {
			return this.kmPerDay;
		}

		List<Date> getBikeDays() {
			return this.bikeDays;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("MemberEvent [memberId=");
			builder.append(this.memberId);
			builder.append(", kmPerDay=");
			builder.append(this.kmPerDay);
			builder.append(", bikeDays=");
			builder.append(this.bikeDays);
			builder.append("]");
			return builder.toString();
		}
		
	}

	@FunctionalInterface
	private interface IMemberFoundListener {
		void memberFound(MemberActivity event);
	}
	
	CalendarParser(String url) {
		this.url = url;
	}
	
	void setMemberConsumer(Consumer<MemberActivity> consumer) {
		this.memberConsumer = consumer;
	}
	
	private void propagateMemberActivity(MemberActivity event) {
		if (this.memberConsumer != null) {
			this.memberConsumer.accept(event);
		}
	}
	
	void parse() throws IOException {
		String calendarPage = getCalendarPage(this.url);
		// Excel files list a http page that redirects to a https page
		int idx = calendarPage.indexOf("a href=\"");
		if (idx == -1) {
			return;
		}
		idx += "a href=\"".length();
		String redirectUrl = calendarPage.substring(idx, calendarPage.indexOf("\"", idx));
		calendarPage = getCalendarPage(redirectUrl);
		
		int startIdx = 0;
		while (true) {
			startIdx = calendarPage.indexOf("btwTeamData[", startIdx);
			if (startIdx == -1) {
				break;
			}
			int endIdx = calendarPage.indexOf(";", startIdx);
			String jsonString = calendarPage.substring(startIdx, endIdx);
			processJsonData(jsonString);
			startIdx = endIdx;
		}
	}
	
	private void processJsonData(String jsonString) {
		JsonReader reader = Json.createReader(new StringReader(jsonString.substring(jsonString.indexOf('=')+1)));
		JsonObject jsonObject = reader.readObject();
		int memberId = Integer.parseInt(jsonObject.getString("memberID"));
		double kmPerDay = Double.parseDouble(jsonObject.getString("kmProTag"));
		String year = jsonObject.getString("activityYear");

		JsonObject daysObject = jsonObject.getJsonObject("days");

		List<Date> bikeDays = new ArrayList<>();
		if (daysObject != null) {
			daysObject.forEach((key, value) -> {
				boolean byBike = 1 == Integer.parseInt(((JsonObject)value).getString("status"));
				if (byBike) {
					try {
						Date parse = new SimpleDateFormat("yyyyMMdd").parse(year+key);
						bikeDays.add(parse);
					} catch (Exception e) {
						// ignore if it can't be parsed
					}
				}
			});
		}
		propagateMemberActivity(new MemberActivity(memberId, kmPerDay, bikeDays));
	}
	
	private String getCalendarPage(String url) throws IOException {
		InputStream in = new URL(url).openStream();
		return readFromStream(in);
	}

	private String readFromStream(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}
	
}