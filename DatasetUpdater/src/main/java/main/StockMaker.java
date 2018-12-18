package main;

import static main.LogFieldFormatter.format;
import static main.LogFieldFormatter.pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Node;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class StockMaker {

	ArrayList<String> dbpediaCompanyList = new ArrayList<String>();
	ArrayList<String> nytimesCompanyList = new ArrayList<String>();
	ArrayList<Integer> articleCount = new ArrayList<Integer>();
	ArrayList<VirtGraph> storeList = new ArrayList<VirtGraph>();

	FileOutputStream out;
	PrintStream pSOut;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS");
	private Logger logger = LoggerFactory.getLogger(StockMaker.class);

	public StockMaker() throws NumberFormatException, IOException {
		super();
		storeList.add(new VirtGraph("http://stockmarket.com", "jdbc:virtuoso://155.223.25.2:1111", "dba", "dba123"));
		// storeList.add(new VirtGraph("dbpedia","jdbc:virtuoso://localhost:1111",
		// "dba","dba"));

		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("src/main/resources/organization_data.txt"));
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

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		VirtuosoUpdateRequest vur;
		String str;
		str = "CLEAR GRAPH <http://stockmarket.com> ";

		// transactionHandler.commit();

		vur = VirtuosoUpdateFactory.create(str, storeList.get(0));
		vur.exec();

		Node firstPredicate = Node.createURI("http://stockmarket.com/elements/stockValue");

		for (int i = 0; i < this.nytimesCompanyList.size(); i++) {
			// transactionHandler.begin();
			String companyUri = this.nytimesCompanyList.get(i);
			Node subject = Node.createURI(companyUri);

			str = "INSERT INTO GRAPH <http://stockmarket.com> {<" + subject.getURI() + "> <" + firstPredicate.getURI()
					+ "> \"" + 0 + "\"^^xsd:integer}";
			logger.debug(
					format(pair("time", LocalDateTime.now()), pair("company", companyUri), pair("dataset", "stock")),
					"Company data has been inserted");
			// transactionHandler.commit();

			vur = VirtuosoUpdateFactory.create(str, storeList.get(0));
			vur.exec();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			StockMaker maker = new StockMaker();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
