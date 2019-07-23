package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.util.NodeFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;

import virtuoso.jena.driver.VirtGraph;

public class ArtificialDataGenerator {

	private static final String COMPANY_PREFIX = "company-";
	private static final int COMPANY_SIZE = 3500;
	private static Logger logger = LoggerFactory.getLogger(ArtificialDataGenerator.class);
	private static VirtGraph dbpediaGraph = new VirtGraph("http://dbpedia.org", "jdbc:virtuoso://155.223.25.1:1111",
			"dba", "dba123");
	private static VirtGraph nytimesGraph = new VirtGraph("http://nytimes.com", "jdbc:virtuoso://155.223.25.1:1111",
			"dba", "dba123");
	private static VirtGraph stockGraph = new VirtGraph("http://stockmarket.com", "jdbc:virtuoso://155.223.25.2:1111",
			"dba", "dba123");

	public static void main(String[] args) {
		logger.debug("Dataset creation has started.");
		for (int i = 3000; i < COMPANY_SIZE; i++) {
			Node dbpediaCompanyNode = createCompanyNode(Constants.DBPEDIA_RSC_PREFIX, i);
			Node nytimesCompanyNode = createCompanyNode(Constants.NYTIME_RSC_PREFIX, i);
			//createData(dbpediaCompanyNode, nytimesCompanyNode);
			deleteData(dbpediaCompanyNode, nytimesCompanyNode);
		}
		logger.debug("Dataset creation has ended.");
	}

	private static void createData(Node dbpediaCompanyNode, Node nytimesCompanyNode) {
		dbpediaGraph.add(new Triple(dbpediaCompanyNode, RDF.type.asNode(), Constants.DBPEDIA_COMPANY_CLS_NODE));
		dbpediaGraph.add(new Triple(dbpediaCompanyNode, OWL.sameAs.asNode(), nytimesCompanyNode));
		nytimesGraph.add(new Triple(nytimesCompanyNode, Constants.ARTICLE_COUNT_NODE, Constants.ZERO_COUNT_NODE));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_COUNT_NODE, Constants.ZERO_COUNT_NODE));
	}
	
	private static void deleteData(Node dbpediaCompanyNode, Node nytimesCompanyNode) {
		dbpediaGraph.delete(new Triple(dbpediaCompanyNode, RDF.type.asNode(), Constants.DBPEDIA_COMPANY_CLS_NODE));
		dbpediaGraph.delete(new Triple(dbpediaCompanyNode, OWL.sameAs.asNode(), nytimesCompanyNode));
		nytimesGraph.delete(new Triple(nytimesCompanyNode, Constants.ARTICLE_COUNT_NODE, NodeFactory.intToNode(1)));
		stockGraph.delete(new Triple(nytimesCompanyNode, Constants.STOCK_COUNT_NODE, NodeFactory.intToNode(1)));
	}

	private static Node createCompanyNode(String prefix, int i) {
		return Node.createURI(prefix + COMPANY_PREFIX + (i + 1));
	}

}
