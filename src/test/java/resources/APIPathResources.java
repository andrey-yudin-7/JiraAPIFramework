package resources;

public class APIPathResources {
	
	public String getAddCommentAPI(String issueId)
	{
		String addCommentAPI = "rest/api/2/issue/"+issueId+"/comment";
		return addCommentAPI;
	}
	
	public String getUpdateCommentAPI(String issueId, String commentId)
	{
		String updateCommentAPI = "rest/api/2/issue/"+issueId+"/comment/"+commentId+"";
		return updateCommentAPI;
	}
	
	public String getAddAttachmentAPI(String issueId)
	{
		String addAttachmentAPI = "rest/api/2/issue/"+issueId+"/attachments";
		return addAttachmentAPI;
	}
	
	public String getDeleteIssueAPI(String issueId)
	{
		String deleteIssueAPI = "rest/api/2/issue/"+issueId+"";
		return deleteIssueAPI;
	}
}
