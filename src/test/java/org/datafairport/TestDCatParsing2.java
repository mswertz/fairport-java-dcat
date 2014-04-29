package org.datafairport;

import java.io.IOException;
import java.util.List;

import org.datafairport.dcat.ParserDCat;
import org.datafairport.dcat.jena.Catalog;
import org.datafairport.dcat.jena.Dataset;
import org.testng.annotations.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

/**
 * Created by mswertz on 27/04/14.
 */
public class TestDCatParsing2
{
	@Test
	public void test1() throws IOException
	{
		String inputFileName = "src/main/resources/example2.ttl";

		Model model = FileManager.get().loadModel(inputFileName);

		List<Catalog> catalogs = ParserDCat.parse(model);

		for (Catalog catalog : catalogs)
		{
			System.out.println(catalog.getTitle());
			for (Dataset dataSet : catalog.getDataSet())
			{
				System.out.println(dataSet.getSubject());
			}
		}
	}
}
