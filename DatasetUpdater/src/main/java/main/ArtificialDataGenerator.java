package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.impl.XSDDateType;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.sparql.util.NodeFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.XSD;

import virtuoso.jena.driver.VirtGraph;

public class ArtificialDataGenerator {

	private static final String COMPANY_PREFIX = "company-";
	private static Logger logger = LoggerFactory.getLogger(ArtificialDataGenerator.class);
	private static VirtGraph dbpediaGraph = new VirtGraph("http://dbpedia.org", "jdbc:virtuoso://155.223.25.4:1111",
			"dba", "dba123");
	private static VirtGraph nytimesGraph = new VirtGraph("http://data.nytimes.com",
			"jdbc:virtuoso://155.223.25.1:1111", "dba", "dba123");
	private static VirtGraph stockGraph = new VirtGraph("http://stockmarket.com", "jdbc:virtuoso://155.223.25.2:1111",
			"dba", "dba123");

	public static void main(String[] args) {
		logger.debug("Dataset creation has started.");
		for (int i = 0; i < Constants.COMPANY_SIZE; i++) {
			Node dbpediaCompanyNode = createCompanyNode(Constants.DBPEDIA_RSC_PREFIX, i);
			Node nytimesCompanyNode = createCompanyNode(Constants.NYTIMES_RSC_PREFIX, i);
			createData(dbpediaCompanyNode, nytimesCompanyNode, i + 1);
			// deleteData(dbpediaCompanyNode, nytimesCompanyNode,i+1);
		}
		logger.debug("Dataset creation has ended.");
	}

	private static void createData(Node dbpediaCompanyNode, Node nytimesCompanyNode, int count) {
		// dbpediaGraph.add(new Triple(dbpediaCompanyNode, RDF.type.asNode(),
		// Constants.DBPEDIA_COMPANY_CLS_NODE));
		// dbpediaGraph.add(new Triple(dbpediaCompanyNode, OWL.sameAs.asNode(),
		// nytimesCompanyNode));
		// nytimesGraph.add(new Triple(nytimesCompanyNode, Constants.ARTICLE_COUNT_NODE,
		// Constants.ZERO_COUNT_NODE));
		// nytimesGraph.add(new Triple(nytimesCompanyNode, RDF.type.asNode(),
		// Constants.NYTIMES_COMPANY_NODE));
		// stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_COUNT_NODE,
		// Constants.ZERO_COUNT_NODE));

//		nytimesGraph.add(new Triple(nytimesCompanyNode, Node.createURI("http://www.w3.org/2004/02/skos/core#prefLabel"),
//				Node.createLiteral("Company-" + count, "en", null)));

//		createStockData(nytimesCompanyNode, count);
	}

	private static void createStockData(Node nytimesCompanyNode, int count) {
		stockGraph.add(new Triple(nytimesCompanyNode, RDF.type.asNode(), Constants.STOCK_COMPANY_NODE));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_COMPANYNAME_NODE,
				Node.createLiteral("Company-" + count)));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_MARKET_NODE,
				Node.createLiteral("New Yowk Stock Exchange")));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_TRADES_NODE, Constants.ZERO_COUNT_NODE));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_SHARES_NODE, Constants.ZERO_COUNT_NODE));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_CURRENCY_NODE, Node.createLiteral("USD")));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_VALUECHANGE_NODE, NodeFactory.floatToNode(0)));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_CHANGEPERCENT_NODE, NodeFactory.floatToNode(0)));
	}

	private static void deleteData(Node dbpediaCompanyNode, Node nytimesCompanyNode, int count) {
		//deleteNytimesData();
		
		//dbpediaGraph.delete(new Triple(dbpediaCompanyNode, RDF.type.asNode(), Constants.DBPEDIA_COMPANY_CLS_NODE));
		//dbpediaGraph.delete(new Triple(dbpediaCompanyNode, OWL.sameAs.asNode(), nytimesCompanyNode));
		//nytimesGraph.delete(new Triple(nytimesCompanyNode, Constants.ARTICLE_COUNT_NODE, NodeFactory.intToNode(4)));
		//nytimesGraph.delete(new Triple(nytimesCompanyNode, RDF.type.asNode(), Constants.NYTIMES_COMPANY_NODE));
		//stockGraph.delete(new Triple(nytimesCompanyNode, Constants.STOCK_COUNT_NODE, NodeFactory.intToNode(6)));
	}

	private static void deleteNytimesData() {
		QueryExecution queryExecution = QueryExecutionFactory.sparqlService( "http://155.223.25.1:8890/sparql","select * where {?nytCompany <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://data.nytimes.com/elements/Company>. ?nytCompany <http://data.nytimes.com/elements/associated_article_count> ?articleCount.}");
		ResultSet resNyTimes = queryExecution.execSelect();
		while (resNyTimes.hasNext()) {
			QuerySolution querySolution = (QuerySolution) resNyTimes.next();
			nytimesGraph.delete(new Triple(querySolution.getResource("nytCompany").asNode(), RDF.type.asNode(), Node.createURI(Constants.NYTIMES_COMPANY_URI)));
			nytimesGraph.delete(new Triple(querySolution.getResource("nytCompany").asNode(), Node.createURI(Constants.ARTICLE_COUNT_URI), querySolution.getLiteral("articleCount").asNode()));
		}
	}

	private static Node createCompanyNode(String prefix, int i) {
		return Node.createURI(prefix + COMPANY_PREFIX + (i + 1));
	}

}
