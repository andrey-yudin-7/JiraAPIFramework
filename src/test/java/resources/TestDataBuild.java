package resources;

public class TestDataBuild {
	
	
	public String CreateSessionPayload(String username, String password)
	{
		String payload = "{\r\n"
				+ "  \"username\": \""+username+"\",\r\n"
				+ "  \"password\": \""+password+"\"\r\n"
				+ "}";
		return payload;
	}
	
	public String CreateIssuePayload(String summary, String description, String projectKey)
	{
		
		String payload = "{\r\n"
				+ "    \"fields\": {\r\n"
				+ "        \"project\": {\r\n"
				+ "            \"key\": \""+projectKey+"\"\r\n"
				+ "        },\r\n"
				+ "        \"summary\": \""+summary+"\",\r\n"
				+ "		\"description\": \""+description+"\",\r\n"
				+ "        \"issuetype\": {\r\n"
				+ "            \"name\": \"Bug\"\r\n"
				+ "        }\r\n"
				+ "	}\r\n"
				+ "}";
		return payload;
	}
	
	public String AddCommentPayload(String comment)
	{
		String payload = "{\r\n"
				+ "    \"body\": \""+comment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}";
		return payload;
	}
	
	public String UpdateCommentPayload(String updateComment)
	{
		String payload = "{\r\n"
				+ "    \"body\": \""+updateComment+"\",\r\n"
				+ "    \"visibility\": {\r\n"
				+ "        \"type\": \"role\",\r\n"
				+ "        \"value\": \"Administrators\"\r\n"
				+ "    }\r\n"
				+ "}";
		return payload;
	}
	
}
