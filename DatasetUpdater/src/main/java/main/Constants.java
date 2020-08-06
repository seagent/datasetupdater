package main;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.util.NodeFactory;

public class Constants {

	public static final String DBPEDIA_RSC_PREFIX = "http://dbpedia.org/resource/";
	public static final String NYTIME_RSC_PREFIX = "http://data.nytimes.com/";
	public static final String ARTICLE_COUNT_URI = "http://data.nytimes.com/elements/associated_article_count";
	public static final String STOCK_VALUE_URI = "http://stockmarket.com/elements/stockValue";
	public static final String DBPEDIA_ONTOLOGY_URI = "http://dbpedia.org/ontology/";
	public static final String DBPEDIA_COMPANY_CLS_URI = DBPEDIA_ONTOLOGY_URI + "Company";
	static final Node DBPEDIA_COMPANY_CLS_NODE = Node.createURI(DBPEDIA_COMPANY_CLS_URI);
	static final Node ARTICLE_COUNT_NODE = Node.createURI(ARTICLE_COUNT_URI);
	static final Node STOCK_COUNT_NODE = Node.createURI(STOCK_VALUE_URI);
	static final Node ZERO_COUNT_NODE = NodeFactory.intToNode(0);
	static final int NYTIMES_UPDATE_IN_MILLIS = 1200000;
	static final int STOCK_UPDATE_IN_MILLIS = 600000;
	static int COMPANY_SIZE = 3500;

}
