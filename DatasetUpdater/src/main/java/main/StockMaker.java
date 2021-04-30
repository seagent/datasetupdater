package main;

import static main.LogFieldFormatter.format;
import static main.LogFieldFormatter.pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
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
	VirtGraph stockStore;
	private static int COMPANY_SIZE = 10000;

	private Logger logger = LoggerFactory.getLogger(StockMaker.class);

	public StockMaker() throws NumberFormatException, IOException {
		super();
		stockStore = new VirtGraph("http://stockmarket.com", "jdbc:virtuoso://155.223.25.2:1111", "dba", "dba123");

		readOrganizationData();
		COMPANY_SIZE=this.nytimesCompanyList.size();
		VirtuosoUpdateRequest vur;
		String str;
		str = "CLEAR GRAPH <http://stockmarket.com> ";

		// transactionHandler.commit();

		vur = VirtuosoUpdateFactory.create(str, stockStore);
		vur.exec();

		for (int i = 0; i < COMPANY_SIZE; i++) {
			// transactionHandler.begin();
			 String companyUri = this.nytimesCompanyList.get(i);
			//String companyUri = Constants.NYTIME_RSC_PREFIX + "company-" + (i + 1);
			Node subject = Node.createURI(companyUri);

			str = "INSERT INTO GRAPH <http://stockmarket.com> {<" + subject.getURI() + "> <" + Constants.STOCK_PRICE_URI
					+ "> \"" + 0 + "\"^^xsd:integer}";
			logger.debug(
					format(pair("time", LocalDateTime.now()), pair("company", companyUri), pair("dataset", "stock")),
					"Company data has been inserted");
			// transactionHandler.commit();

			vur = VirtuosoUpdateFactory.create(str, stockStore);
			vur.exec();
		}

	}

	private void readOrganizationData() throws IOException {
		BufferedReader br;
		try {
			br = new BufferedReader(
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream("organization_data.txt")));
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
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new StockMaker();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}