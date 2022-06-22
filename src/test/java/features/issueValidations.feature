Feature: Validating Issue API's

@CreateIssue @Smoke
Scenario Outline: Verify if issue is being Succesfully created using createIssueAPI
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"

Examples:
	|summary 	 		|description 			|
	|summary_test |description_test |

@AddComment @Regression
Scenario Outline: Verify add comment functionality is working
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"
	Then user calls "addCommentAPI" with payload as "<comment>" and "POST" http request
	Then the API call got success with status code "201"
	
Examples:
	|summary 	 		|description 			|comment 	 		|
	|summary_test |description_test |comment_test |
	
@AddAttachment @Regression
Scenario Outline: Verify add attachment functionality is working
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"
	Then user calls "addAttachmentAPI" with adding file and "POST" http request
	Then the API call got success with status code "200"
	
Examples:
	|summary 	 		|description 			|
	|summary_test |description_test |
	
@DeleteIssue @Smoke
Scenario Outline: Verify delete issue functionality is working
	Given Created session with issue Payload as "<summary>" "<description>"
	When user calls "createIssueAPI" with "POST" http request
	Then the API call got success with status code "201"
	Then user delete the issue by calling "deleteIssueAPI" with "DELETE" http request 
	Then the API call got success with status code "201"

Examples:
	|summary 	 		|description 			|
	|summary_test |description_test |

#@UpdateComment @Regression
#Scenario Outline: Verify update comment functionality is working
#	Given Created session with issue Payload as "<summary>" "<description>"
#	When user calls "createIssueAPI" with "POST" http request
#	Then user calls "addCommentAPI" with new payload as "<comment>" and "POST" http request
#	Then the API call got success with status code "201"
#	Then user updating comment calling "updateCommentAPI" with payload as "<updateComment>" and "POST" http request
#	Then the API call got success with status code "200"
#	
#Examples:
#	|summary 	 		|description 			|comment 	 						|updateComment 									|
#	|summary_test |description_test |comment_test444444444|Updating existing comment auto |
	
