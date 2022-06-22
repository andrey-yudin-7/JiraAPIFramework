package resources;

//enum is a special class in java which has collection of constants and methods
public enum APIResources {
	createSessionAPI("/rest/auth/1/session"),
	createIssueAPI("/rest/api/2/issue");
	private String resource;
	
	APIResources(String resource)
	{
		this.resource=resource;
	}
	
	public String getResource()
	{
		return resource;
	}

}
