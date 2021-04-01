package main;

import com.hp.hpl.jena.datatypes.xsd.impl.XSDDateType;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.graph.GraphFactory;
import com.hp.hpl.jena.sparql.util.NodeFactory;
import com.hp.hpl.jena.vocabulary.RDF;

public class MainTestGraph {

	public static void main(String[] args) {
		Model model = ModelFactory.createDefaultModel();
		Graph stockGraph = model.getGraph();
		Node nytimesCompanyNode = Node.createURI(Constants.NYTIMES_RSC_PREFIX + "Netflix");
		;
		stockGraph.add(new Triple(nytimesCompanyNode, RDF.type.asNode(), Constants.STOCK_COMPANY_NODE));
		stockGraph.add(
				new Triple(nytimesCompanyNode, Constants.STOCK_COMPANYNAME_NODE, Node.createLiteral("Netflix Inc.")));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_MARKET_NODE,
				Node.createLiteral("New Yowk Stock Exchange")));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_TRADES_NODE, Constants.ZERO_COUNT_NODE));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_SHARES_NODE, Constants.ZERO_COUNT_NODE));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_CURRENCY_NODE, Node.createLiteral("USD")));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_VALUECHANGE_NODE, NodeFactory.floatToNode(0)));
		stockGraph.add(new Triple(nytimesCompanyNode, Constants.STOCK_CHANGEPERCENT_NODE, NodeFactory.floatToNode(0)));
		model.write(System.out);
		System.out.println(new Triple(Node.createURI("http://nytimes.com/company-1"),
				Node.createURI("http://www.w3.org/2004/02/skos/core#prefLabel"),
				Node.createLiteral("Company-1", "en", null)));
	}

}
