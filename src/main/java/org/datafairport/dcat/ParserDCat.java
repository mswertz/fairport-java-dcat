package org.datafairport.dcat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.datafairport.dcat.jena.Catalog;
import org.datafairport.dcat.jena.Dataset;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class ParserDCat
{
	private final static String DC_PREFIX = "http://purl.org/dc/terms/";
	private final static String DCAT_PREFIX = "http://www.w3.org/ns/dcat#";
	private final static String FOAF_PREFIX = "http://xmlns.com/foaf/0.1/";
	private final static String RDF_PREFIX = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";

	public static List<Catalog> parse(Model model)
	{
		StmtIterator listStatements = model.listStatements();
		Map<Node, List<Triple>> dataSetTripleStores = new HashMap<Node, List<Triple>>();
		Map<Node, List<Triple>> catalogTripleStores = new HashMap<Node, List<Triple>>();

		// Collect all triples from RDF
		while (listStatements.hasNext())
		{
			Statement st = listStatements.nextStatement();
			Triple triple = st.asTriple();
			Node subject = triple.getSubject();

			Resource r = model.getResource(subject.getURI());
			Statement dataTypeStatement = r.getProperty(model.createProperty(RDF_PREFIX, "type"));

			if (dataTypeStatement != null)
			{
				if (dataTypeStatement.asTriple().getObject().getLocalName()
						.equalsIgnoreCase(Dataset.class.getSimpleName()))
				{
					if (!dataSetTripleStores.containsKey(subject))
					{
						dataSetTripleStores.put(subject, new ArrayList<Triple>());
					}
					dataSetTripleStores.get(subject).add(triple);
				}
				else
				{
					if (!catalogTripleStores.containsKey(subject))
					{
						catalogTripleStores.put(subject, new ArrayList<Triple>());
					}
					catalogTripleStores.get(subject).add(triple);
				}
			}
		}

		// Collect all info for dataset
		Map<String, Dataset> dataSetMap = new HashMap<String, Dataset>();
		for (Entry<Node, List<Triple>> entry : dataSetTripleStores.entrySet())
		{
			Dataset dataSet = new Dataset(entry.getKey());
			dataSetMap.put(dataSet.getSubject().getURI(), dataSet);
			for (Triple triple : entry.getValue())
			{
				Node predicate = triple.getPredicate();
				Node object = triple.getObject();
				if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Dataset.IDENTIFIER).toString()))
				{
					dataSet.setIdentifier(object);
				}
				else if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Dataset.TITLE).toString()))
				{
					dataSet.setTitle(object);
				}
				else if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Dataset.DESCRIPTION).toString()))
				{
					dataSet.setDescription(object);
				}
				else if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Dataset.ISSUED).toString()))
				{
					dataSet.setIssued(object);
				}
				else if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Dataset.MODIFIED).toString()))
				{
					dataSet.setModified(object);
				}
				else if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Dataset.RIGHTS).toString()))
				{
					dataSet.setRights(object);
				}
				else if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Dataset.SOURCE).toString()))
				{
					dataSet.setSource(object);
				}
			}
		}

		// Collect all info for catalogs
		List<Catalog> catalogs = new ArrayList<Catalog>();
		for (Entry<Node, List<Triple>> entry : catalogTripleStores.entrySet())
		{
			Catalog catalog = new Catalog(entry.getKey());
			for (Triple triple : entry.getValue())
			{
				Node predicate = triple.getPredicate();
				Node object = triple.getObject();

				if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Catalog.TITLE).toString()))
				{
					catalog.setTitle(object);
				}
				else if (predicate.getURI().equals(model.createProperty(DC_PREFIX, Catalog.MODIFIED).toString()))
				{
					catalog.setModified(object);
				}
				else if (predicate.getURI().equals(model.createProperty(FOAF_PREFIX, Catalog.HOMEPAGE).toString()))
				{
					catalog.setHomePage(object);
				}
				else if (predicate.getURI().equals(
						model.createProperty(DCAT_PREFIX, Dataset.class.getSimpleName().toLowerCase()).toString()))
				{
					catalog.getDataSet().add(dataSetMap.get(object.getURI()));
				}
			}
			catalogs.add(catalog);
		}
		return catalogs;
	}
}
