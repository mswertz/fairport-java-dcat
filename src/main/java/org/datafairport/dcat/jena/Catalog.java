package org.datafairport.dcat.jena;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.graph.Node;

/**
 * Created by mswertz on 27/04/14.
 */
public class Catalog
{
	public final static String TITLE = "title";
	public final static String ISSUED = "issued";
	public final static String MODIFIED = "modified";
	public final static String HOMEPAGE = "homepage";

	private final Node subject;
	private Node title;
	private Node homePage;
	private Node modified;
	private final List<Dataset> dataSet = new ArrayList<Dataset>();

	public Catalog(Node subject)
	{
		this.subject = subject;
	}

	public Node getTitle()
	{
		return title;
	}

	public String getTitleValue()
	{
		return convertToValue(title);
	}

	public void setTitle(Node title)
	{
		this.title = title;
	}

	public Node getHomePage()
	{
		return homePage;
	}

	public String getHomePageValue()
	{
		return convertToValue(homePage);
	}

	public void setHomePage(Node homePage)
	{
		this.homePage = homePage;
	}

	public Node getModified()
	{
		return modified;
	}

	public String getModifiedValue()
	{
		return convertToValue(modified);
	}

	public void setModified(Node modified)
	{
		this.modified = modified;
	}

	public Node getSubject()
	{
		return subject;
	}

	public List<Dataset> getDataSet()
	{
		return dataSet;
	}

	private String convertToValue(Node node)
	{
		String value = null;
		if (node.isLiteral())
		{
			value = node.isLiteral() ? node.getLiteralValue().toString() : node.getLocalName();
		}
		else
		{
			value = node.getLocalName().isEmpty() ? node.getNameSpace() : node.getLocalName();
		}
		return value;
	}
}
