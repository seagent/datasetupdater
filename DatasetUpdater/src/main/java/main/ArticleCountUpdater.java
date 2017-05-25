package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class ArticleCountUpdater extends Thread {

	ArrayList<String> dbpediaCompanyList = new ArrayList<String>();
	ArrayList<String> nytimesCompanyList = new ArrayList<String>();
	ArrayList<Integer> articleCount = new ArrayList<Integer>();

	FileOutputStream out;
	PrintStream pSOut;

	DateTime jodaTime = new DateTime();

	DateTimeFormatter formatter = DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss.SSS");

	private void init() throws IOException {

		BufferedReader br = new BufferedReader(
				new FileReader("/home/oylum/Desktop/workspace/SemanticCartago2/organisation_data.txt"));

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

			while (queryCounter < 550)// this.nytimesCompanyList.size())
			{

				try {
					out = new FileOutputStream(
							"/home/oylum/Desktop/workspace/SemanticCartago2/Journal1/output" + queryCounter + ".txt",
							true);
					pSOut = new PrintStream(out);

				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.setOut(pSOut);
				System.setErr(pSOut);

				System.out.println("\n-----------------------------------------new update started: "
						+ formatter.print(jodaTime.now()) + "---------------------------------\n");

				queryCounter++;
				String query = "SELECT ?sameCompany ?count ?stock WHERE {<"
						+ dbpediaCompanyList.get(queryCounter).toString()
						+ "> <http://www.w3.org/2002/07/owl#sameAs> ?sameCompany. ?sameCompany <http://data.nytimes.com/elements/associated_article_count> ?count. ?sameCompany <http://stockmarket.com/elements/stockValue> ?value}";
//				TODO: Count sorgusu burada sisteme atılacak

				System.out.println("\n-----------------------------------------update finished: "
						+ formatter.print(jodaTime.now()) + "---------------------------------");

				Thread.sleep(120000);

			}

		} catch (Exception ex) {
			System.out.println("Exception in article count updater...");
			ex.printStackTrace();
		}

	}

}
