package project.an.readnewsapp;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


import org.jsoup.select.Elements;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import project.an.readnewsapp.Fragment.Navigation.HomeFragment;
import project.an.readnewsapp.Models.NewsItem;

public class RSSUtils {

    public static String fetchRSS(String newsURL) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(newsURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // Thời gian chờ kết nối
            connection.setReadTimeout(10000); // Thời gian chờ đọc dữ liệu

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Lỗi RSS: ", e.getMessage());
        }
        return result.toString();
    }

    public static List<NewsItem> parseRSS(String rssData) {
        List<NewsItem> newsItems = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new java.io.ByteArrayInputStream(rssData.getBytes()));

            NodeList itemNodes = document.getElementsByTagName("item");
            for (int i = 0; i < itemNodes.getLength(); i++) {
                Node itemNode = itemNodes.item(i);
                if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element itemElement = (Element) itemNode;

                    // Trích xuất các trường dữ liệu
                    String title = getElementValue(itemElement, "title");
                    String link = getElementValue(itemElement, "link");
                    String description = getElementValue(itemElement, "description");
                    String pubDate = getElementValue(itemElement, "pubDate");
                    String content = getElementValue(itemElement, "content:encoded");
                    // Giả sử hình ảnh nằm trong thẻ <media:content> hoặc trong <description>
                    String imageUrl = extractImageFromMediaContent(itemElement, "media:content");
                    if (imageUrl == null || imageUrl.isEmpty()) {
                        imageUrl = extractFirstImage(description);
                    }
                    if (imageUrl == null || imageUrl.isEmpty()){
                        imageUrl = extractFirstImage(content);
                    }
                    if (description == null || description.isEmpty()){
                        description = content;
                    }
                    NewsItem newsItem = new NewsItem(title, imageUrl, pubDate, link, description);
                    newsItem.formatDay();
                    newsItems.add(newsItem);
                    String today = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.ENGLISH).format(new Date());
                    Log.i("Today", today);
                    if(newsItem.getPupDate().equals(today)) Log.i("NewsToday", newsItem.getTitle());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsItems;
    }

    // Trích xuất nội dung từ thẻ XML
    private static String getElementValue(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }


    private static String extractImageFromMediaContent(Element parent, String tagName) {
        NodeList mediaContentList = parent.getElementsByTagName(tagName);
        for (int i = 0; i < mediaContentList.getLength(); i++) {
            Node node = mediaContentList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element mediaElement = (Element) node;
                if (mediaElement.hasAttribute("url")) {
                    return mediaElement.getAttribute("url");
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy thẻ hoặc thuộc tính "url"
    }

    private static String extractFirstImage(String htmlContent) {
        try {
            Document doc = Jsoup.parse(htmlContent);
            // Lấy tất cả các thẻ img
            Elements imgs = doc.select("img");
            for (org.jsoup.nodes.Element img : imgs) {
                String src = img.attr("src");
                if (src != null && !src.isEmpty()) {
                    return src; // Trả về hình ảnh đầu tiên
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Trả về null nếu không tìm thấy hình ảnh
    }

}
