package net.xgs.query;

import java.util.ArrayList;
import java.util.List;

public class FilterBuilder {

	private List<Object> params = new ArrayList<Object>();
	private String snippets = "";
	
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	public String getSnippets() {
		return snippets;
	}
	public void setSnippets(String snippets) {
		this.snippets += " " +snippets;
	}
	
}
