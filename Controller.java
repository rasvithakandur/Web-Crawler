package crawerapp;
import java.io.FileWriter;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
 public static void main(String[] args) throws Exception {
 String crawlStorageFolder = "/data/crawl";
 int numberOfCrawlers = 20;
 FileWriter fileWriter = null;
 CrawlConfig config = new CrawlConfig();
 config.setCrawlStorageFolder(crawlStorageFolder);
 /*
 * Instantiate the controller for this crawl.
 */
 PageFetcher pageFetcher = new PageFetcher(config);
 RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
 RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
 CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
 /*
 * For each crawl, you need to add some seed urls. These are the first
 * URLs that are fetched and then the crawler starts following links
 * which are found in these pages
 */
 controller.addSeed("http://www.cnn.com/");
 config.setMaxDepthOfCrawling(16);
 config.setMaxPagesToFetch(20000);
 config.setPolitenessDelay(500); 
 config.setUserAgentString("USC viterbi");
 
 //Create files and write headings
 fileWriter = new FileWriter("fetch_CNN.csv",false);
 fileWriter.append("URL,STATUS CODE\n");
 fileWriter.close();
 fileWriter = new FileWriter("visit_CNN.csv",false);
 fileWriter.append("URL,SIZE,No of Outlinks,Content-Type\n");
 fileWriter.close();
 fileWriter = new FileWriter("urls_CNN.csv",false);
 //fileWriter.append("URL,STATUS CODE\n");
 fileWriter.close();

 /*
 * Start the crawl. This is a blocking operation, meaning that your code
 * will reach the line after this only when crawling is finished.
 */
 controller.start(MyCrawler.class, numberOfCrawlers);
 }
}