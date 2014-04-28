package org.datafairport.dcat.jena;

import com.hp.hpl.jena.graph.Node;

/**
 * Created by mswertz on 27/04/14.
 */
public class Dataset
{
	private final Node subject;
	private Node identifier;
	private Node source;
	private Node title;
	private Node description;
	private Node issued;
	private Node modified;
	private Node rights;

	public Dataset(Node subject)
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

	public Node getDescription()
	{
		return description;
	}

	public String getDescriptionValue()
	{
		return convertToValue(description);
	}

	public void setDescription(Node description)
	{
		this.description = description;
	}

	public Node getIssued()
	{
		return issued;
	}

	public String getIssuedValue()
	{
		return convertToValue(issued);
	}

	public void setIssued(Node issued)
	{
		this.issued = issued;
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

	public Node getRights()
	{
		return rights;
	}

	public String getRightsValue()
	{
		return convertToValue(rights);
	}

	public void setRights(Node rights)
	{
		this.rights = rights;
	}

	public Node getSubject()
	{
		return subject;
	}

	public Node getSource()
	{
		return source;
	}

	public String getSourceValue()
	{
		return convertToValue(source);
	}

	public void setSource(Node source)
	{
		this.source = source;
	}

	public Node getIdentifier()
	{
		return identifier;
	}

	public String getIdentifierValue()
	{
		return convertToValue(identifier);
	}

	public void setIdentifier(Node identifier)
	{
		this.identifier = identifier;
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
