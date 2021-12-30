package main;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.util.NodeFactory;

public class Constants {

	private static final String STOCKMARKET_PREFIX = "http://stockmarket.com/elements/";
	public static final String DBPEDIA_RSC_PREFIX = "http://dbpedia.org/resource/";
	public static final String NYTIMES_RSC_PREFIX = "http://data.nytimes.com/";
	public static final String WALL_STREET_JOURNAL_RSC_PREFIX = "https://www.wsj.com/market-data/quotes/";
	public static final String ARTICLE_COUNT_URI = NYTIMES_RSC_PREFIX + "elements/associated_article_count";
	public static final String NYTIMES_REPUTATION_URI = NYTIMES_RSC_PREFIX + "elements/reputation";
	public static final String NYTIMES_COMPANY_URI = NYTIMES_RSC_PREFIX + "elements/Company";
	public static final String STOCK_PRICE_URI = STOCKMARKET_PREFIX + "stockPrice";
	public static final String STOCK_COMPANY_URI = STOCKMARKET_PREFIX + "Company";
	public static final String STOCK_COMPANYNAME_URI = STOCKMARKET_PREFIX + "companyName";
	public static final String STOCK_MARKET_URI = STOCKMARKET_PREFIX + "market";
	public static final String STOCK_TRADES_URI = STOCKMARKET_PREFIX + "trades";
	public static final String STOCK_SHARES_URI = STOCKMARKET_PREFIX + "shares";
	public static final String STOCK_CURRENCY_URI = STOCKMARKET_PREFIX + "currency";
	public static final String STOCK_VALUECHANGE_URI = STOCKMARKET_PREFIX + "valueChange";
	public static final String STOCK_CHANGEPERCENT_URI = STOCKMARKET_PREFIX + "changePercent";
	public static final String DBPEDIA_ONTOLOGY_URI = "http://dbpedia.org/ontology/";
	public static final String DBPEDIA_COMPANY_CLS_URI = DBPEDIA_ONTOLOGY_URI + "Company";
	public static final String DBPEDIA_INDUSTRY_PRP_URI = DBPEDIA_ONTOLOGY_URI + "industry";
	static final Node DBPEDIA_INDUSTRY_PRP_NODE = Node.createURI(DBPEDIA_INDUSTRY_PRP_URI);
	static final Node DBPEDIA_COMPANY_CLS_NODE = Node.createURI(DBPEDIA_COMPANY_CLS_URI);
	public static final Node DBPEDIA_NUMBER_OF_STAFF = Node.createURI("http://dbpedia.org/ontology/numberOfStaff");
	static final Node ARTICLE_COUNT_NODE = Node.createURI(ARTICLE_COUNT_URI);
	static final Node NYTIMES_REPUTATION_NODE = Node.createURI(NYTIMES_REPUTATION_URI);
	static final Node NYTIMES_COMPANY_NODE = Node.createURI(NYTIMES_COMPANY_URI);
	static final Node STOCK_PRICE_NODE = Node.createURI(STOCK_PRICE_URI);
	static final Node ZERO_COUNT_NODE = NodeFactory.intToNode(0);
	static final Node STOCK_COMPANY_NODE = Node.createURI(STOCK_COMPANY_URI);
	static final int NYTIMES_UPDATE_IN_MILLIS = 1200000;
	static final int STOCK_UPDATE_IN_MILLIS = 600000;
	public static final Node STOCK_COMPANYNAME_NODE = Node.createURI(STOCK_COMPANYNAME_URI);
	public static final Node STOCK_MARKET_NODE = Node.createURI(STOCK_MARKET_URI);
	public static final Node STOCK_TRADES_NODE = Node.createURI(STOCK_TRADES_URI);
	public static final Node STOCK_SHARES_NODE = Node.createURI(STOCK_SHARES_URI);
	public static final Node STOCK_CURRENCY_NODE = Node.createURI(STOCK_CURRENCY_URI);
	public static final Node STOCK_VALUECHANGE_NODE = Node.createURI(STOCK_VALUECHANGE_URI);
	public static final Node STOCK_CHANGEPERCENT_NODE = Node.createURI(STOCK_CHANGEPERCENT_URI);
	public static final String ELITE = "Elite";
	public static final String INITIAL = "Initial";
	public static final String VERY_HIGH = "Very High";
	public static final String HIGH = "High";
	public static final String MEDIUM = "Medium";
	public static final String LOW = "Low";
	public static final String VERY_LOW = "Very Low";
	static int COMPANY_SIZE = 5000;

	public static final Node AIRLINE_RSC_NODE = Node.createURI(Constants.DBPEDIA_RSC_PREFIX+"Airline");
	public static final Node BANK_RSC_NODE = Node.createURI(Constants.DBPEDIA_RSC_PREFIX+"Bank");
	public static final Node SOFTWARE_RSC_NODE = Node.createURI(Constants.DBPEDIA_RSC_PREFIX+"Software");
	public static final Node ELECTRONICS_RSC_NODE = Node.createURI(Constants.DBPEDIA_RSC_PREFIX+"Electronics");
	public static final Node HEALTH_CARE_RSC_NODE = Node.createURI(Constants.DBPEDIA_RSC_PREFIX+"Health_care");
	public static final Node RESTAURANT_RSC_NODE = Node.createURI(Constants.DBPEDIA_RSC_PREFIX+"Restaurant");

}
