package main;

import static main.LogFieldFormatter.format;
import static main.LogFieldFormatter.pair;

import java.io.BufferedReader;
import java.io.FileReader;
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

	private static final String ARTICLE_COUNT_URI = "http://data.nytimes.com/elements/associated_article_count";
	ArrayList<String> dbpediaCompanyList = new ArrayList<String>();
	ArrayList<String> nytimesCompanyList = new ArrayList<String>();

	VirtGraph store = new VirtGraph("http://nytimes.com", "jdbc:virtuoso://155.223.25.1:1111", "dba", "dba123");

	private Logger logger = LoggerFactory.getLogger(NyTimesUpdater.class);

	private void init() throws IOException {

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

			while (queryCounter < 550)// this.nytimesCompanyList.size())
			{
				logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "nytimes")), "Dataset updated");

				queryCounter++;

				Node firstPredicate = Node.createURI(ARTICLE_COUNT_URI);
				int articleCount = 0;
				for (int i = 0; i < this.nytimesCompanyList.size(); i++) {

					Node subject = Node.createURI(this.nytimesCompanyList.get(i));

					String query = "SELECT ?sameCompany ?count WHERE {<"
							+ dbpediaCompanyList.get(queryCounter).toString()
							+ "> <http://www.w3.org/2002/07/owl#sameAs> ?sameCompany. ?sameCompany <"
							+ ARTICLE_COUNT_URI + "> ?count.}";

					VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create(query, store);
					ResultSet results = vqe.execSelect();
					while (results.hasNext()) {
						QuerySolution result = results.nextSolution();
						articleCount = result.get("count").asLiteral().getInt();

						store.delete(new Triple(subject, firstPredicate, NodeFactory.intToNode(articleCount)));

					}
					articleCount++;
					store.add(new Triple(subject, firstPredicate, NodeFactory.intToNode(articleCount)));
					logger.debug(format(pair("time", LocalDateTime.now()), pair("company", subject.getURI()),
							pair("dataset", "nytimes")), "Company data has been updated");
				}

				Thread.sleep(120000);

				logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "nytimes")), "Dataset updated");
			}

			logger.debug(format(pair("time", LocalDateTime.now()), pair("dataset", "nytimes")),
					"All datasets has been updated");

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

	}

}
