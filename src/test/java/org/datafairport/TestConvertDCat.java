package org.datafairport;

import java.io.IOException;
import java.util.Arrays;

import org.datafairport.dcat.ConverterToDCat;
import org.datafairport.dcat.jena.Catalog;
import org.datafairport.dcat.jena.Dataset;
import org.testng.annotations.Test;

import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

/**
 * Created by mswertz on 27/04/14.
 */
public class TestConvertDCat
{
	@Test
	public void test1() throws IOException
	{
		String outputDir = "src/main/resources/";

		String PROJECT_PREDIX = "http://www.example.org/";

		ModelMaker modelMaker = ModelFactory.createFileModelMaker(outputDir);
		Model model = modelMaker.createModel("example-test.ttl");
		model.setNsPrefixes(ConverterToDCat.prefixMaps);

		Dataset dataset1 = new Dataset(NodeFactory.createURI(PROJECT_PREDIX + "dataset-1"));
		dataset1.setTitle(NodeFactory.createLiteral("dataset-1 title"));
		dataset1.setIdentifier(NodeFactory.createLiteral("dataset-1 identifier"));
		dataset1.setDescription(NodeFactory.createLiteral("dataset-1 description"));

		Dataset dataset2 = new Dataset(NodeFactory.createURI(PROJECT_PREDIX + "dataset-2"));
		dataset2.setTitle(NodeFactory.createLiteral("dataset-2 title"));
		dataset2.setIdentifier(NodeFactory.createLiteral("dataset-2 identifier"));
		dataset2.setDescription(NodeFactory.createLiteral("dataset-2 description"));
		dataset2.setIssued(NodeFactory.createLiteral(System.currentTimeMillis() / 100 + ""));
		dataset2.setModified(NodeFactory.createLiteral(System.currentTimeMillis() * 2 + ""));

		Catalog catalog = new Catalog(NodeFactory.createURI(PROJECT_PREDIX + "catalog"));
		catalog.setTitle(NodeFactory.createLiteral("catalog title"));
		catalog.setHomePage(NodeFactory.createLiteral("http://www.molgenis.org"));
		catalog.setModified(NodeFactory.createLiteral(System.currentTimeMillis() + ""));
		catalog.getDataSet().add(dataset1);
		catalog.getDataSet().add(dataset2);

		ConverterToDCat.convert(Arrays.asList(catalog), model);

		model.close();
		modelMaker.close();
	}
}
