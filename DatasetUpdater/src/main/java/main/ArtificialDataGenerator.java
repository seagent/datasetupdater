package main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.util.NodeFactory;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

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
        for (int i = 0; i < 500; i++) {
            Node dbpediaCompanyNode = createCompanyNode(Constants.DBPEDIA_RSC_PREFIX, i + 1);
            Node nytimesCompanyNode = createCompanyNode(Constants.NYTIMES_RSC_PREFIX, i + 1);
            createData(dbpediaCompanyNode, nytimesCompanyNode, i + 1);
            //deleteStockData(i+1);
        }
        logger.debug("Dataset creation has ended.");
    }

    private static void createData(Node dbpediaCompanyNode, Node nytimesCompanyNode, int count) {
        createNytimesData(nytimesCompanyNode, count);
        createDbpediaData(dbpediaCompanyNode, nytimesCompanyNode, count);
        createStockData(nytimesCompanyNode, count);
    }

    private static void createNytimesData(Node nytimesCompanyNode, int count) {
        nytimesGraph.add(new Triple(nytimesCompanyNode, RDF.type.asNode(), Constants.NYTIMES_COMPANY_NODE));
        nytimesGraph.add(new Triple(nytimesCompanyNode, RDFS.label.asNode(),
                Node.createLiteral("Company-" + count, XSDDatatype.XSDstring)));
        nytimesGraph.add(new Triple(nytimesCompanyNode, Constants.ARTICLE_COUNT_NODE, Constants.ZERO_COUNT_NODE));
        nytimesGraph.add(new Triple(nytimesCompanyNode, Constants.NYTIMES_REPUTATION_NODE,
                Node.createLiteral(getReputation(count), XSDDatatype.XSDstring)));
        addSecondaryReputation(nytimesCompanyNode, count);
        Node wallStreetJournalNode = createCompanyNode(Constants.WALL_STREET_JOURNAL_RSC_PREFIX, count);
        nytimesGraph.add(new Triple(nytimesCompanyNode, OWL.sameAs.asNode(), wallStreetJournalNode));
    }

    private static void addSecondaryReputation(Node nytimesCompanyNode, int count) {
        if (count <= 500) {
            nytimesGraph.add(new Triple(nytimesCompanyNode, Constants.NYTIMES_REPUTATION_NODE,
                    Node.createLiteral(Constants.ELITE, XSDDatatype.XSDstring)));
        } else if (count > 4900 && count <= 5000) {
            nytimesGraph.add(new Triple(nytimesCompanyNode, Constants.NYTIMES_REPUTATION_NODE,
                    Node.createLiteral(Constants.INITIAL, XSDDatatype.XSDstring)));
        }
    }

    private static String getReputation(int count) {
        String reputation = "";
        if (count <= 1000) {
            reputation = Constants.VERY_HIGH;
        } else if (count > 1000 && count <= 2000) {
            reputation = Constants.HIGH;
        } else if (count > 2000 && count <= 3000) {
            reputation = Constants.MEDIUM;
        } else if (count > 3000 && count <= 4000) {
            reputation = Constants.LOW;
        } else if (count > 4000 && count <= 5000) {
            reputation = Constants.VERY_LOW;
        } else {
            reputation = Constants.VERY_LOW;
        }
        return reputation;
    }

    private static void createDbpediaData(Node dbpediaCompanyNode, Node nytimesCompanyNode, int count) {
        dbpediaGraph.add(new Triple(dbpediaCompanyNode, RDF.type.asNode(), Constants.DBPEDIA_COMPANY_CLS_NODE));
        dbpediaGraph.add(new Triple(dbpediaCompanyNode, RDFS.label.asNode(),
                Node.createLiteral("Company-" + count, XSDDatatype.XSDstring)));

        dbpediaGraph.add(new Triple(dbpediaCompanyNode, Constants.DBPEDIA_NUMBER_OF_STAFF,
                NodeFactory.intToNode(getStaffCount(count))));
        dbpediaGraph.add(new Triple(dbpediaCompanyNode, OWL.sameAs.asNode(), nytimesCompanyNode));
        if (count <= 100) {
            dbpediaGraph.add(new Triple(dbpediaCompanyNode, Constants.DBPEDIA_INDUSTRY_PRP_NODE, Constants.BANK_RSC_NODE));
        } else if (count>100&&count <= 200) {
            dbpediaGraph.add(new Triple(dbpediaCompanyNode, Constants.DBPEDIA_INDUSTRY_PRP_NODE, Constants.AIRLINE_RSC_NODE));
        } else if (count>200&&count <= 300) {
            dbpediaGraph.add(new Triple(dbpediaCompanyNode, Constants.DBPEDIA_INDUSTRY_PRP_NODE, Constants.SOFTWARE_RSC_NODE));
        } else if (count>300&&count <= 400) {
            dbpediaGraph.add(new Triple(dbpediaCompanyNode, Constants.DBPEDIA_INDUSTRY_PRP_NODE, Constants.ELECTRONICS_RSC_NODE));
        } else if (count>400&&count <= 500) {
            dbpediaGraph.add(new Triple(dbpediaCompanyNode, Constants.DBPEDIA_INDUSTRY_PRP_NODE, Constants.HEALTH_CARE_RSC_NODE));
        } else if (count > 4900 && count <= 5000) {
            dbpediaGraph.add(new Triple(dbpediaCompanyNode, Constants.DBPEDIA_INDUSTRY_PRP_NODE, Constants.RESTAURANT_RSC_NODE));
        }

    }

    private static int getStaffCount(int count) {
        int staffCount = 0;
        if (count <= 500) {
            staffCount = 100 * getRandomNumber(901, 1000);
        } else if (count > 500 && count <= 1000) {
            staffCount = 100 * getRandomNumber(801, 900);
        } else if (count > 1000 && count <= 1500) {
            staffCount = 100 * getRandomNumber(701, 800);
        } else if (count > 1500 && count <= 2000) {
            staffCount = 100 * getRandomNumber(601, 700);
        } else if (count > 2000 && count <= 2500) {
            staffCount = 100 * getRandomNumber(501, 600);
        } else if (count > 2500 && count <= 3000) {
            staffCount = 100 * getRandomNumber(401, 500);
        } else if (count > 3000 && count <= 3500) {
            staffCount = 100 * getRandomNumber(301, 400);
        } else if (count > 3500 && count <= 4000) {
            staffCount = 100 * getRandomNumber(201, 300);
        } else if (count > 4000 && count <= 4500) {
            staffCount = 100 * getRandomNumber(101, 200);
        } else if (count > 4500 && count <= 5000) {
            staffCount = 100 * getRandomNumber(11, 100);
        } else {
            staffCount = 100 * getRandomNumber(1, 10);
        }
        return staffCount;
    }

    private static void createStockData(Node nytimesCompanyNode, int count) {
        stockGraph.add(new Triple(nytimesCompanyNode, RDF.type.asNode(), Constants.STOCK_COMPANY_NODE));
        addStockMarketAndCurrency(nytimesCompanyNode, count);
        int stockPrice = getRandomNumber(1, 500);
        stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_PRICE_NODE, NodeFactory.floatToNode(stockPrice)));
        stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_COMPANYNAME_NODE,
                Node.createLiteral("Company-" + count, XSDDatatype.XSDstring)));
        int trades = getRandomNumber(10, 250) * Math.round(stockPrice);
        int shares = getRandomNumber(10, 200) * trades;
        stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_TRADES_NODE, NodeFactory.intToNode(trades)));
        stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_SHARES_NODE, NodeFactory.intToNode(shares)));
        addValueChangeAndChangePercent(nytimesCompanyNode, count);
    }

    private static void addValueChangeAndChangePercent(Node nytimesCompanyNode, int count) {
        float valueChange = 0, changePercent = 0;
        if (count <= 100) {
            valueChange = getRandomNumber(60000, 69999);
            changePercent = getRandomNumber(18, 20);
        } else if (count>100 && count <= 200) {
            valueChange = getRandomNumber(50000, 59999);
            changePercent = getRandomNumber(15, 17);
        } else if (count >200 && count <= 300) {
            valueChange = getRandomNumber(40000, 49999);
            changePercent = getRandomNumber(12, 14);
        } else if (count >300 && count <= 400) {
            valueChange = getRandomNumber(30000, 39999);
            changePercent = getRandomNumber(9, 11);
        } else if (count >400 && count <= 500) {
            valueChange = getRandomNumber(20000, 29999);
            changePercent = getRandomNumber(6, 8);
        } else if (count > 4900 && count <= 5000) {
            valueChange = getRandomNumber(1000, 5000);
            changePercent = getRandomNumber(1, 3);
        }
        stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_VALUECHANGE_NODE, NodeFactory.floatToNode(valueChange)));
        stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_CHANGEPERCENT_NODE, NodeFactory.floatToNode(changePercent)));
    }

    private static void addStockMarketAndCurrency(Node nytimesCompanyNode, int count) {
        String stockMarket = "";
        String currency = "";
        if (count <= 1000) {
            //New York Stock Exchange
            stockMarket = "NYSE";
            currency = "USD";
        } else if (count > 1000 && count <= 2000) {
            //Tokyo Stock Exchange
            stockMarket = "TSE";
            currency = "JPY";
        } else if (count > 2000 && count <= 3000) {
            //Frankfurter Wertpapierbörse
            stockMarket = "FWB";
            currency = "EUR";
        } else if (count > 3000 && count <= 4000) {
            //London Stock Exchange
            stockMarket = "LSE";
            currency = "GBP";
        } else if (count > 4000 && count <= 5000) {
            //Borsa İstanbul
            stockMarket = "BİST";
            currency = "TRY";
        } else {
            //Borsa İstanbul
            stockMarket = "BİST";
            currency = "TRY";
        }
        stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_MARKET_NODE,
                Node.createLiteral(stockMarket, XSDDatatype.XSDstring)));
        stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_CURRENCY_NODE,
                Node.createLiteral(currency, XSDDatatype.XSDstring)));
    }

    private static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    private static void deleteStockData(int count) {
        Node companyNode = createCompanyNode(Constants.NYTIMES_RSC_PREFIX, count);
        stockGraph.delete(Triple.create(companyNode,Constants.STOCK_VALUECHANGE_NODE,NodeFactory.floatToNode(0)));
        stockGraph.delete(Triple.create(companyNode,Constants.STOCK_CHANGEPERCENT_NODE,NodeFactory.floatToNode(0)));
    }

    private static void deleteData(Node dbpediaCompanyNode, Node nytimesCompanyNode, int count) {
        // deleteNytimesData();

        // dbpediaGraph.delete(new Triple(dbpediaCompanyNode, RDF.type.asNode(),
        // Constants.DBPEDIA_COMPANY_CLS_NODE));
        // dbpediaGraph.delete(new Triple(dbpediaCompanyNode, OWL.sameAs.asNode(),
        // nytimesCompanyNode));
        // nytimesGraph.delete(new Triple(nytimesCompanyNode,
        // Constants.ARTICLE_COUNT_NODE, NodeFactory.intToNode(4)));
        // nytimesGraph.delete(new Triple(nytimesCompanyNode, RDF.type.asNode(),
        // Constants.NYTIMES_COMPANY_NODE));
        // stockGraph.delete(new Triple(nytimesCompanyNode, Constants.STOCK_COUNT_NODE,
        // NodeFactory.intToNode(6)));
    }

    private static void deleteNytimesData() {
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://155.223.25.1:8890/sparql",
                "select * where {?nytCompany <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://data.nytimes.com/elements/Company>. ?nytCompany <http://data.nytimes.com/elements/associated_article_count> ?articleCount.}");
        ResultSet resNyTimes = queryExecution.execSelect();
        while (resNyTimes.hasNext()) {
            QuerySolution querySolution = (QuerySolution) resNyTimes.next();
            Resource nytRsc = querySolution.getResource("nytCompany");
            nytimesGraph.delete(
                    new Triple(nytRsc.asNode(), RDF.type.asNode(), Node.createURI(Constants.NYTIMES_COMPANY_URI)));
            nytimesGraph.delete(new Triple(nytRsc.asNode(), Node.createURI(Constants.ARTICLE_COUNT_URI),
                    querySolution.getLiteral("articleCount").asNode()));
        }
    }

    private static void deleteDbpediaData() {
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("http://155.223.25.4:8890/sparql",
                "select * where {"
                        + "?dbpediaCompany <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/Company>."
                        + "  FILTER (strstarts(str(?dbpediaCompany), 'http://dbpedia.org/resource/company-'))" + "}");
        ResultSet resDbpedia = queryExecution.execSelect();
        while (resDbpedia.hasNext()) {
            QuerySolution querySolution = (QuerySolution) resDbpedia.next();
            Resource nytRsc = querySolution.getResource("dbpediaCompany");
            nytimesGraph.delete(
                    new Triple(nytRsc.asNode(), RDF.type.asNode(), Node.createURI(Constants.NYTIMES_COMPANY_URI)));
            nytimesGraph.delete(new Triple(nytRsc.asNode(), Node.createURI(Constants.ARTICLE_COUNT_URI),
                    querySolution.getLiteral("articleCount").asNode()));
        }
    }

    private static Node createCompanyNode(String prefix, int i) {
        return Node.createURI(prefix + COMPANY_PREFIX + (i));
    }

}
