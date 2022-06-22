package resources;

public class payload {
	
	public static String CreateSession(String username, String password)
	{
		String payload = "{\r\n"
				+ "  \"username\": \""+username+"\",\r\n"
				+ "  \"password\": \""+password+"\"\r\n"
				+ "}";
		return payload;
	}
	
	public static String CreateIssue(String summary, String description, String projectKey)
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
	
	public static String AddComment(String comment)
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
	
	public static String UpdateComment(String updateComment)
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
