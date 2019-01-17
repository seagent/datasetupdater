package main;

import static main.LogFieldFormatter.format;
import static main.LogFieldFormatter.pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.util.NodeFactory;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class StockUpdater extends Thread {

	ArrayList<String> dbpediaCompanyList = new ArrayList<String>();
	ArrayList<String> nytimesCompanyList = new ArrayList<String>();
	ArrayList<Integer> articleCount = new ArrayList<Integer>();
	VirtGraph stockStore;
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
	private Logger logger = LoggerFactory.getLogger(StockUpdater.class);

	private static int COMPANY_SIZE = 10000;

	private void init() throws IOException

	{

		stockStore = new VirtGraph("http://stockmarket.com", "jdbc:virtuoso://155.223.25.2:1111", "dba", "dba123");
		readCompanyData();
		COMPANY_SIZE = this.nytimesCompanyList.size();
	}

	private void readCompanyData() throws IOException {
		StockUpdater.class.getClassLoader();
		BufferedReader br = new BufferedReader(
				new InputStreamReader(ClassLoader.getSystemResourceAsStream("organization_data.txt")));

		String line;
		int blankPosition;
		while ((line = br.readLine()) != null) {
			blankPosition = line.indexOf(" ");
			this.dbpediaCompanyList.add(line.substring(0, blankPosition));
			line = line.substring(blankPosition + 1);
			blankPosition = line.indexOf(" ");
			this.nytimesCompanyList.add(line.substring(0, blankPosition));
			line = line.substring(blankPosition + 1);
			this.articleCount.add(Integer.parseInt(line));

		}

	}

	public void run() {

		try {
			this.init();

			int queryCounter = 0;

			logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "stock")),
					"All datasets are being updated");

			while (queryCounter < COMPANY_SIZE) {

				logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "stock")),
						"Dataset update started");
				queryCounter++;

				for (int i = 0; i < COMPANY_SIZE; i++) {

					String nytimesCompanyUri = this.nytimesCompanyList.get(i);
					//String nytimesCompanyUri = Constants.NYTIME_RSC_PREFIX + "company-" + (i + 1);
					Node subject = Node.createURI(nytimesCompanyUri);

					Query sparql = QueryFactory.create("SELECT ?stock WHERE { <" + subject.getURI() + "> <"
							+ Constants.STOCK_VALUE_URI + "> ?stock }");

					VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(sparql, stockStore);
					int stock = 0;
					ResultSet results = vqe.execSelect();
					while (results.hasNext()) {
						QuerySolution result = results.nextSolution();
						stock = result.get("stock").asLiteral().getInt();

						stockStore
								.delete(new Triple(subject, Constants.STOCK_COUNT_NODE, NodeFactory.intToNode(stock)));

					}
					stock++;
					stockStore.add(new Triple(subject, Constants.STOCK_COUNT_NODE, NodeFactory.intToNode(stock)));
					logger.debug(
							format(pair("time", LocalDateTime.now()), pair("company", subject.getURI()),
									pair("dataset", "stock"), pair("stock-value", stock)),
							"Company data has been updated");
				}

				logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "stock")),
						"Dataset update ended");
				Thread.sleep(120000);

			}

			logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "stock")),
					"All datasets has been updated");

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

	}

}
