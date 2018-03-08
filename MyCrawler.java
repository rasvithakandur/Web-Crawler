package crawerapp;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.*;

import org.apache.http.Header;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler{
	
	
	static int total_count = 0;
	static int a_count = 0;
	static int b_count = 0;
	static int c_count = 0;
	static Map<String,Integer> unique_url = new HashMap<String,Integer>();
	static Map<String,Integer> in_url = new HashMap<String,Integer>();
	static Map<String,Integer> out_url = new HashMap<String,Integer>();
	
	private	final static Pattern FILTERS=Pattern.compile(".*(\\.(css|js|bmp|gif|jpg|jpe?g|png|tiff?|mid|mp2|mp3|mp4|wav|avi|mov|mpeg|ram|m4v"+"|rm|smil|wmv|swf|wma|zip|rar|gz|php|iso|ico))$");

	public boolean shouldVisit(Page page, WebURL url) {
		String	href=url.getURL().toLowerCase();
		
		String url_file = "urls_CNN.csv";
		
		List<String> url_data = new ArrayList<String>();
		url_data.add(href.replaceAll(",", "-"));
		if(unique_url.containsKey(href.replaceAll(",", "-")))
			unique_url.put(href.replaceAll(",", "-"), unique_url.get(href.replaceAll(",", "-"))+1);
		else 
			unique_url.put(href.replaceAll(",", "-"), 1);
		
		
		if(href.startsWith("http://www.cnn.com/")){
			
			url_data.add("OK");
			if(in_url.containsKey(href.replaceAll(",", "-")))
				in_url.put(href.replaceAll(",", "-"), in_url.get(href.replaceAll(",", "-"))+1);
			else 
				in_url.put(href.replaceAll(",", "-"), 1);
			
			
		}else{
			url_data.add("N_OK");
			if(out_url.containsKey(href.replaceAll(",", "-")))
				out_url.put(href.replaceAll(",", "-"), out_url.get(href.replaceAll(",", "-"))+1);
			else 
				out_url.put(href.replaceAll(",", "-"), 1);
			
		}
		
		writeCSV(url_file, url_data);
		 System.out.println("unique count:: "+ unique_url.size());
		 System.out.println("in count:: "+ in_url.size());
		 System.out.println("out count:: "+ out_url.size());
		//System.out.println(page.getStatusCode());
		return !FILTERS.matcher(href).matches()&&href.startsWith("http://www.cnn.com/");
	}

	@Override
	protected WebURL handleUrlBeforeProcess(WebURL curURL) {
		// TODO Auto-generated method stub
		String	href=curURL.getURL().toLowerCase();
		System.out.println("BEFORE PROCESS----> "+href);
		return super.handleUrlBeforeProcess(curURL);
	}

	
	

	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
		// TODO Auto-generated method stub
		String	href=webUrl.getURL().toLowerCase();
		String fetch_file = "fetch_CNN.csv";
		total_count++;
		System.out.println("Page Status Error PROCESS----> "+href+"::"+statusCode+ " :: "+total_count);
		List<String> fetchData = new ArrayList<String>();
		fetchData.add(href.replaceAll(",", "-"));
		 fetchData.add(String.valueOf(statusCode));
		 writeCSV(fetch_file,fetchData);
		super.handlePageStatusCode(webUrl, statusCode, statusDescription);
	}

	@Override
	protected void onUnexpectedStatusCode(String urlStr, int statusCode, String contentType, String description) {
		// TODO Auto-generated method stub
		System.out.println("Unexpected PROCESS----> "+urlStr+" :: "+statusCode+ " :: "+total_count);
		System.out.println("a count:: "+ ++a_count);
		List<String> fetchData = new ArrayList<String>();
		String fetch_file = "fetch_CNN.csv";
		fetchData.add(urlStr.replaceAll(",", "-"));
		 fetchData.add(String.valueOf(statusCode));
		 writeCSV(fetch_file,fetchData);
		super.onUnexpectedStatusCode(urlStr, statusCode, contentType, description);
	}

	@Override
	protected void onPageBiggerThanMaxSize(String urlStr, long pageSize) {
		// TODO Auto-generated method stub
		List<String> fetchData = new ArrayList<String>();
		System.out.println("b count:: "+ ++b_count);
		String fetch_file = "fetch_CNN.csv";
		//fetchData.add(urlStr.replaceAll(",", "-"));
		 //fetchData.add(String.valueOf(0));
		 //writeCSV(fetch_file,fetchData);
		
		System.out.println("BIGG PROCESS----> "+urlStr);
		super.onPageBiggerThanMaxSize(urlStr, pageSize);
	}

	@Override
	protected void onContentFetchError(WebURL webUrl) {
		// TODO Auto-generated method stub
		System.out.println("c count:: "+ ++c_count);
		List<String> fetchData = new ArrayList<String>();
		String	href=webUrl.getURL().toLowerCase();
		
		String fetch_file = "fetch_CNN.csv";
		fetchData.add(href.replaceAll(",", "-"));
		 fetchData.add(String.valueOf(0));
		 writeCSV(fetch_file,fetchData);
		System.out.println("Fetch Error PROCESS----> "+href);
		super.onContentFetchError(webUrl);
		
	}

	@Override
	protected void onParseError(WebURL webUrl) {
		// TODO Auto-generated method stub
		String	href=webUrl.getURL().toLowerCase();
		System.out.println("Parse Error PROCESS----> "+href);
		super.onParseError(webUrl);
	}

	@Override
	public void visit(Page page) {
		
		
		List<String> visitData = new ArrayList<String>();
		
		String visit_file = "visit_CNN.csv";
		String url  = page . getWebURL() . getURL();
		 System . out . println( "URL: " + url );
		 
		
		 if (page . getParseData()  instanceof HtmlParseData) { HtmlParseData htmlParseData  = (HtmlParseData) page . getParseData();
		// String text  = htmlParseData . getText();
		 //String html  = htmlParseData . getHtml();
		 Set< WebURL >links  = htmlParseData . getOutgoingUrls();
		// System.out.println( "Text length: " + text . length());
		 //System.out.println( "Html length: " + html . length());
		 //System.out.println( "Number of outgoing links : " + links . size());
		 //System.out.println("content-length: "+page.getContentData().length);
		// System.out.println("HTML: "+html);
		 visitData.add(url);
		 visitData.add(String.valueOf(page.getContentData().length));
		 visitData.add(String.valueOf(links . size()));
		 visitData.add(page.getContentType());
		 /*Header[] header = page.getFetchResponseHeaders();
		 
		 for(int i=0; i<header.length;i++)
			 System.out.println(header[i].getName()+" :: "+header[i].getValue());
		 */
		 writeCSV(visit_file,visitData);
		 System.out.println("a count:: "+a_count);
		 System.out.println("b count:: "+ b_count);
		 System.out.println("c count:: "+ c_count);
		 System.out.println("total count:: "+ total_count);
		

		 }
		
		
		
	}
	
	
	public void writeCSV(String fileName,List<String> data){
		FileWriter fileWriter = null;
		 
		  final String COMMA_DELIMITER = ",";
		  final String NEW_LINE_SEPARATOR = "\n";


		 
        try {
 
            fileWriter = new FileWriter(fileName,true);

            //Write a new student object list to the CSV file
            for(int i=0;i<data.size();i++){
 
                fileWriter.append(data.get(i));
                
                if(i!=data.size()-1)
                 fileWriter.append(COMMA_DELIMITER);
                else 
                fileWriter.append(NEW_LINE_SEPARATOR);
 
            }

                        
 
        } catch (Exception e) {
 
            System.out.println("Error in CsvFileWriter !!!");
 
            e.printStackTrace();
 
        } finally {
 
             
 
            try {
 
                fileWriter.flush();
 
                fileWriter.close();
 
            } catch (IOException e) {
 
                System.out.println("Error while flushing/closing fileWriter !!!");
 
                e.printStackTrace();
 
            }
 
             
        }
        
 
    

	}
	

}
