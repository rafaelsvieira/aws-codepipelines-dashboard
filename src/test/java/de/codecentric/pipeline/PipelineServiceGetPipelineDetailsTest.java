package de.codecentric.pipeline;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.amazonaws.services.codepipeline.model.GetPipelineStateResult;
import com.amazonaws.services.codepipeline.model.StageState;

@RunWith(MockitoJUnitRunner.class)
public class PipelineServiceGetPipelineDetailsTest {
	private static final String ANY_PIPELINE_NAME = "pipeline name";
	private static final String ANY_COMMIT_MESSAGE = "any commit message";
	private static final StageState ANY_STAGE_STATE = new StageState();

	@Mock
	private AwsCodePipelineFacade awsCodePipelineFacade;

	@Mock
	private GetPipelineStateResult getPipelineStateResult;

	@InjectMocks
	private PipelineService pipelineService;

	@Ignore("not yet ready , Please ignore.")
	@Test
	public void getPipelineDetails_uses_getPipelineStatus_with_given_name() throws PipelineServiceException {
		when(awsCodePipelineFacade.getPipelineStatus(anyString(), "all")).thenReturn(getPipelineStateResult);
		when(getPipelineStateResult.getStageStates()).thenReturn(Arrays.asList(ANY_STAGE_STATE));
		pipelineService.getPipelineDetails(ANY_PIPELINE_NAME, "all");
		verify(awsCodePipelineFacade).getPipelineStatus(ANY_PIPELINE_NAME, "all");
	}
	@Ignore("not yet ready , Please ignore.")
	@Test
	public void getPipelineDetails_uses_getLatestCommitMessage_with_given_name() throws PipelineServiceException {
		when(awsCodePipelineFacade.getPipelineStatus(anyString(), "all")).thenReturn(getPipelineStateResult);
		when(getPipelineStateResult.getStageStates()).thenReturn(Arrays.asList(ANY_STAGE_STATE));
		pipelineService.getPipelineDetails(ANY_PIPELINE_NAME, "all");

		verify(awsCodePipelineFacade).getLatestCommitMessage(ANY_PIPELINE_NAME);
	}
	@Ignore("not yet ready , Please ignore.")
	@Test
	public void getPipelineDetails_uses_getLatestCommitMessage() throws PipelineServiceException {
		when(awsCodePipelineFacade.getPipelineStatus(anyString(), "all")).thenReturn(getPipelineStateResult);
		when(getPipelineStateResult.getStageStates()).thenReturn(Arrays.asList(ANY_STAGE_STATE));
		when(awsCodePipelineFacade.getLatestCommitMessage(anyString())).thenReturn(ANY_COMMIT_MESSAGE);

		PipelineDetailsResult pipelineDetails = pipelineService.getPipelineDetails(ANY_PIPELINE_NAME, "all");
		//assertEquals(ANY_COMMIT_MESSAGE, pipelineDetails.getCommitMessage());
	}

}