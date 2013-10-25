package com.masaila.v2ex.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.masaila.v2ex.bean.Reply;
import com.masaila.v2ex.bean.Topic;
import com.masaila.v2ex.bean.User;

public class ApiClient {
	public static DefaultHttpClient getHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		// httpClient.getParams().setParameter("http.protocol.content-charset",
		// HTTP.UTF_8);
		// httpClient.getParams().setParameter(HTTP.CONTENT_ENCODING,
		// HTTP.UTF_8);
		// httpClient.getParams().setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
		// httpClient.getParams().setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET,
		// HTTP.UTF_8);
		return httpClient;
	}

	public static HttpGet getHttpGet(String url, Context context) {
		final SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
		httpGet.setHeader("Connection", "keep-alive");
		httpGet.setHeader("Cookie", preferences.getString("cookie", ""));
		httpGet.setHeader("Host", "www.v2ex.com");
		httpGet.setHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36");
		return httpGet;
	}

	public static HttpPost getHttpPost(String url) {
		HttpPost httpPost = new HttpPost(url);

		return httpPost;
	}

	public static User getUser(Context context) {
		User user = new User();
		try {
			DefaultHttpClient httpClient = getHttpClient();
			HttpGet httpGet = getHttpGet("http://www.v2ex.com/settings",
					context);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String HtmlString = EntityUtils.toString(httpResponse.getEntity(),
					"UTF-8");
			Document document = Jsoup.parse(HtmlString);
			Elements elementsSettings = document.getElementById("Main")
					.getElementsByTag("tr");
			user.setUsername(elementsSettings.get(0).getElementsByTag("td")
					.get(1).text());
			user.setAvatar(document.getElementsByClass("avatar").get(0)
					.attr("src"));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return user;
	}

	public static ArrayList<Topic> getTopics(Context context) {
		ArrayList<Topic> arrayListTopics = new ArrayList<Topic>();
		try {
			DefaultHttpClient httpClient = getHttpClient();
			HttpGet httpGet = getHttpGet("http://www.v2ex.com/?tab=all",
					context);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String HtmlString = EntityUtils.toString(httpResponse.getEntity(),
					"UTF-8");
			Document document = Jsoup.parse(HtmlString);
			Elements elements = document.getElementById("Main")
					.getElementsByClass("box").get(0)
					.getElementsByClass("item");
			for (int i = 0; i < elements.size(); i++) {
				Topic topics = new Topic();
				Elements elementsA = elements.get(i).getElementsByTag("a");
				topics.setTitle(elementsA.get(1).text());
				topics.setUrl("http://www.v2ex.com"
						+ elementsA
								.get(1)
								.attr("href")
								.substring(
										0,
										elementsA.get(1).attr("href")
												.indexOf("#")));
				topics.setAvatar(elements.get(i).getElementsByTag("img").get(0)
						.attr("src"));
				topics.setNode(elementsA.get(2).text());
				topics.setUsername(elementsA.get(3).text());
				if (elementsA.size() != 4) {
					topics.setReplyCount(elementsA.get(5).text());
				} else {
					topics.setReplyCount("0");
				}
				// String small =
				// elements.get(i).getElementsByClass("small").get(0).;
				// System.err.println(small.substring(small.indexOf("小时") - 1,
				// small.indexOf("前")));
				arrayListTopics.add(topics);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayListTopics;
	}

	public static ArrayList<Topic> getTopicsAtPage(int page, Context context) {
		ArrayList<Topic> arrayListTopics = new ArrayList<Topic>();
		try {
			DefaultHttpClient httpClient = getHttpClient();
			HttpGet httpGet = getHttpGet("http://www.v2ex.com/recent?p="
					+ String.valueOf(page), context);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String HtmlString = EntityUtils.toString(httpResponse.getEntity(),
					"UTF-8");
			Document document = Jsoup.parse(HtmlString);
			Elements elements = document.getElementById("Main")
					.getElementsByClass("box").get(0)
					.getElementsByClass("item");
			for (int i = 0; i < elements.size(); i++) {
				Topic topics = new Topic();
				Elements elementsA = elements.get(i).getElementsByTag("a");
				topics.setTitle(elementsA.get(1).text());
				topics.setUrl("http://www.v2ex.com"
						+ elementsA
								.get(1)
								.attr("href")
								.substring(
										0,
										elementsA.get(1).attr("href")
												.indexOf("#")));
				topics.setAvatar(elements.get(i).getElementsByTag("img").get(0)
						.attr("src"));
				topics.setNode(elementsA.get(2).text());
				topics.setUsername(elementsA.get(3).text());
				if (elementsA.size() != 4) {
					topics.setReplyCount(elementsA.get(5).text());
				} else {
					topics.setReplyCount("0");
				}
				arrayListTopics.add(topics);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrayListTopics;
	}

	public static Topic getTopic(String url, Topic topic, Context context) {
		try {
			DefaultHttpClient httpClient = getHttpClient();
			System.err.println(url);
			HttpGet httpGet = getHttpGet(url, context);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			String HtmlString = EntityUtils.toString(httpResponse.getEntity(),
					"UTF-8");
			Document document = Jsoup.parse(HtmlString);
			Element element = document.getElementsByClass("gray").get(0);
			topic.setReplyTime(element
					.text()
					.substring(element.text().indexOf("·") + 1,
							element.text().lastIndexOf("·")).replace(" ", ""));
			topic.setClick(element
					.text()
					.substring(element.text().lastIndexOf("·") + 1,
							element.text().length()).replace(" ", ""));
			if (document.getElementsByClass("topic_content").size() > 0) {
				topic.setContent(document.getElementsByClass("topic_content")
						.get(0).html());
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topic;
	}

	public static ArrayList<Reply> getReplys(String url, int replyCount,
			Context context) {
		ArrayList<Reply> replies = new ArrayList<Reply>();
		if (replyCount == 0) {
			return replies;
		}
		try {
			DefaultHttpClient httpClient = getHttpClient();
			int page = (replyCount + 100) / 100;
			for (int j = 1; j <= page; j++) {
				HttpGet httpGet = getHttpGet(url + "?p=" + j, context);
				HttpResponse httpResponse = httpClient.execute(httpGet);
				String HtmlString = EntityUtils.toString(
						httpResponse.getEntity(), "UTF-8");
				Document document = Jsoup.parse(HtmlString);
				Element elementBox = document.getElementById("Main")
						.getElementsByClass("box").get(1);
				Elements elementsReply = elementBox.getElementsByClass("cell");
				Element elementReplyInnner = elementBox.getElementsByClass(
						"inner").get(0);
				for (int i = 1; i < elementsReply.size(); i++) {
					Reply reply = new Reply();
					reply.setAvatar(elementsReply.get(i)
							.getElementsByClass("avatar").get(0).attr("src"));
					reply.setUsername(elementsReply.get(i)
							.getElementsByClass("dark").get(0).text());
					reply.setContent(elementsReply.get(i)
							.getElementsByClass("reply_content").get(0).html());
					reply.setReplyTime(elementsReply.get(i)
							.getElementsByClass("fade").text().replace(" ", ""));
					replies.add(reply);
				}
				if (replyCount > 0) {
					Reply reply = new Reply();
					reply.setAvatar(elementReplyInnner
							.getElementsByClass("avatar").get(0).attr("src"));
					reply.setUsername(elementReplyInnner
							.getElementsByClass("dark").get(0).text());
					reply.setContent(elementReplyInnner
							.getElementsByClass("reply_content").get(0).html());
					reply.setReplyTime(elementReplyInnner.getElementsByClass(
							"fade").text());
					replies.add(reply);
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return replies;
	}
}
