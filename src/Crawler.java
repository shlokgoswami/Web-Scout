import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.net.URL;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class Crawler{
	//mapping urls to the file
	// Using the hashset to eliminate the duplicate values
	private static Set<String> urlList = new HashSet<>();
    public static final String TEXT_PATH = "Files"+ File.separator+ "TextFiles"; 
    public static final String HTML_PATH = "Files"+ File.separator+ "htmlFiles"; 
	public static Set<String> getUrls(){
		return urlList;
	}
	public static void clear() {
		urlList.clear();
	}
	public static boolean ValidUrl(String url) {
		if (!url.contains("http"))
			url = "https://" + url;
		try {
			URL obj = new URL(url);
			obj.toURI();
			return true;
		}catch (Exception e) {
		}return false;
	}
	public static boolean start(String url, int depth) {
		// here we took max depth as 2
		if(!urlList.contains(url) && depth < 2) {
			System.out.printf("[%d]: %s\n", urlList.size(), url);
			try {
				Document docs = Jsoup.connect(url).get();
				String name = docs.title().toString().replace(" ", "_").replace("|","_");
				toHTML(docs.text(), name);
				toText(url + "::" + docs.text(), name);
				urlList.add(url);
				for (Element page: docs.select("a[href]")) {
					String link = page.attr("abs:href");
					start(link, depth+1);
				}
			}catch(java.net.UnknownHostException| org.jsoup.HttpStatusException e) {
			}catch (Exception e) {
				System.err.printf("[%s] %s\n", url, e);
				e.printStackTrace();
			}
		}
		return urlList.size() > 0;
	}
	public static void toText(String docs, String name) {
		try(
				FileWriter fw = new FileWriter(TEXT_PATH + File.separator + name + ".txt");
				PrintWriter pw = new PrintWriter(fw);
				){
				String data = docs.toLowerCase();
				pw.write(data);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void toHTML(String docs, String name) {
		try(
				FileWriter fw = new FileWriter(HTML_PATH + File.separator + name + ".html");
				PrintWriter pw = new PrintWriter(fw);
				){
			pw.write(docs);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}