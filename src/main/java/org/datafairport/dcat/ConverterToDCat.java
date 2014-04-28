package org.datafairport.dcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.datafairport.dcat.jena.Catalog;
import org.datafairport.dcat.jena.Dataset;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.rdf.model.Statement;

public class ConverterToDCat
{
	private final static String DC_PREFIX = "http://purl.org/dc/terms/";
	private final static String DCAT_PREFIX = "http://www.w3.org/ns/dcat#";
	private final static String FOAF_PREFIX = "http://xmlns.com/foaf/0.1/";
	private final static String RDF_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	public final static Map<String, String> prefixMaps = new HashMap<String, String>();
	static
	{
		prefixMaps.put("dcat", DCAT_PREFIX);
		prefixMaps.put("dc", DC_PREFIX);
		prefixMaps.put("foaf", FOAF_PREFIX);
	}

	public static void convert(List<Catalog> catalogs, Model model)
	{
		List<Statement> statements = new ArrayList<Statement>();

		for (Catalog catalog : catalogs)
		{
			// Add triples for catalog
			Resource catalogResource = ResourceFactory.createResource(catalog.getSubject().getURI());
			if (catalog.getTitle() != null)
			{
				statements.add(ResourceFactory.createStatement(catalogResource,
						ResourceFactory.createProperty(DC_PREFIX, Catalog.TITLE), convertNode(catalog.getTitle())));
			}
			if (catalog.getHomePage() != null)
			{
				statements.add(ResourceFactory.createStatement(catalogResource,
						ResourceFactory.createProperty(FOAF_PREFIX, Catalog.HOMEPAGE),
						convertNode(catalog.getHomePage())));
			}
			statements.add(ResourceFactory.createStatement(catalogResource,
					ResourceFactory.createProperty(RDF_PREFIX, "type"),
					ResourceFactory.createResource(DCAT_PREFIX + Catalog.class.getSimpleName())));

			// Iterate through all datasets
			for (Dataset dataSet : catalog.getDataSet())
			{
				// Add dataset reference to catalog
				statements.add(ResourceFactory.createStatement(catalogResource,
						ResourceFactory.createProperty(DCAT_PREFIX, Dataset.class.getSimpleName().toLowerCase()),
						convertNode(dataSet.getSubject())));

				// Add triples for dataset
				addDataSetTriples(statements, dataSet);
			}
		}
		model.add(statements);
	}

	private static void addDataSetTriples(List<Statement> statements, Dataset dataSet)
	{
		Resource dataSetResource = ResourceFactory.createResource(dataSet.getSubject().getURI());

		if (dataSet.getTitle() != null)
		{
			statements.add(ResourceFactory.createStatement(dataSetResource,
					ResourceFactory.createProperty(DC_PREFIX, Dataset.TITLE), convertNode(dataSet.getTitle())));
		}
		if (dataSet.getDescription() != null)
		{
			statements.add(ResourceFactory.createStatement(dataSetResource,
					ResourceFactory.createProperty(DC_PREFIX, Dataset.DESCRIPTION),
					convertNode(dataSet.getDescription())));
		}
		if (dataSet.getIssued() != null)
		{
			statements.add(ResourceFactory.createStatement(dataSetResource,
					ResourceFactory.createProperty(DC_PREFIX, Dataset.ISSUED), convertNode(dataSet.getIssued())));
		}
		if (dataSet.getModified() != null)
		{
			statements.add(ResourceFactory.createStatement(dataSetResource,
					ResourceFactory.createProperty(DC_PREFIX, Dataset.MODIFIED), convertNode(dataSet.getModified())));
		}
		if (dataSet.getRights() != null)
		{
			statements.add(ResourceFactory.createStatement(dataSetResource,
					ResourceFactory.createProperty(DC_PREFIX, Dataset.RIGHTS), convertNode(dataSet.getRights())));
		}
		statements.add(ResourceFactory.createStatement(dataSetResource,
				ResourceFactory.createProperty(RDF_PREFIX, "type"),
				ResourceFactory.createResource(DCAT_PREFIX + Dataset.class.getSimpleName())));
	}

	private static RDFNode convertNode(Node node)
	{
		if (node.isURI()) return ResourceFactory.createResource(node.getURI());
		if (node.isLiteral()) return ResourceFactory.createPlainLiteral(node.getLiteralValue().toString());
		return ResourceFactory.createResource();
	}
}
