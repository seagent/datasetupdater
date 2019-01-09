package main;

import static main.LogFieldFormatter.format;
import static main.LogFieldFormatter.pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.util.NodeFactory;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class NyTimesUpdater extends Thread {

	ArrayList<String> dbpediaCompanyList = new ArrayList<String>();
	ArrayList<String> nytimesCompanyList = new ArrayList<String>();

	VirtGraph nytimesStore;
	private static int COMPANY_SIZE = 10000;

	private Logger logger = LoggerFactory.getLogger(NyTimesUpdater.class);

	private void init() throws IOException {
		nytimesStore = new VirtGraph("http://nytimes.com", "jdbc:virtuoso://155.223.25.1:1111", "dba", "dba123");
		//readOrganizationData();
		//COMPANY_SIZE = this.nytimesCompanyList.size();
	}

	private void readOrganizationData() throws IOException {
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

		}
	}

	public void run() {

		try {
			this.init();

			int queryCounter = 0;

			logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "nytimes")),
					"All datasets are being updated");

			while (queryCounter < COMPANY_SIZE)// this.nytimesCompanyList.size())
			{
				logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "nytimes")), "Dataset updated");

				queryCounter++;

				int articleCount = 0;
				for (int i = 0; i < COMPANY_SIZE; i++) {

					// String nytimesCompanyURI = nytimesCompanyList.get(i);
					String nytimesCompanyURI = Constants.NYTIME_RSC_PREFIX + "company-" + (i + 1);
					Node subject = Node.createURI(nytimesCompanyURI);

					articleCount = getArticleCount(nytimesCompanyURI);
					nytimesStore.delete(
							new Triple(subject, Constants.ARTICLE_COUNT_NODE, NodeFactory.intToNode(articleCount)));
					articleCount++;
					nytimesStore.add(
							new Triple(subject, Constants.ARTICLE_COUNT_NODE, NodeFactory.intToNode(articleCount)));
					logger.debug(
							format(pair("time", LocalDateTime.now()), pair("company", subject.getURI()),
									pair("dataset", "nytimes"), pair("article-count", articleCount)),
							"Company data has been updated");
				}

				Thread.sleep(240000);

				logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "nytimes")), "Dataset updated");
			}

			logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "nytimes")),
					"All datasets has been updated");

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

	}

	private int getArticleCount(String nytimesCompanyURI) {
		String query = "SELECT ?count WHERE {<" + nytimesCompanyURI + "> <" + Constants.ARTICLE_COUNT_URI
				+ "> ?count.}";

		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, nytimesStore);
		int articleCount = 0;
		ResultSet results = vqe.execSelect();
		if (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			articleCount = result.get("count").asLiteral().getInt();

		}
		vqe.close();
		return articleCount;
	}

}
