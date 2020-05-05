package de.codecentric.pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.amazonaws.services.codepipeline.model.AWSCodePipelineException;
import com.amazonaws.services.codepipeline.model.GetPipelineStateResult;
import com.amazonaws.services.codepipeline.model.PipelineSummary;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PipelineService {

	private final AwsCodePipelineFacade awsCodePipelineFacade;

	private Map<String, PipelineDetailsResult> latestResult = new HashMap<>();

	public PipelineService(AwsCodePipelineFacade awsCodePipelineFacade) {
		this.awsCodePipelineFacade = awsCodePipelineFacade;
	}

	public List<PipelineSummary> getPipelines(String group) {
		return awsCodePipelineFacade.getPipelineResults(group).getPipelines();
	}

	public PipelineDetailsResult getPipelineDetails(String pipelineName, String pipelineStatus) throws PipelineServiceException {
		try {
			GetPipelineStateResult result = awsCodePipelineFacade.getPipelineStatus(pipelineName, pipelineStatus);
			if (result == null) {
				throw new PipelineServiceException("Failed to get details for " + pipelineName + " with status " + pipelineStatus, null);
			}
			String commitMessage = awsCodePipelineFacade.getLatestCommitMessage(pipelineName);
			PipelineDetailsResult pipelineDetailsResult = new PipelineDetailsResult(result.getStageStates(), commitMessage);
			latestResult.put(pipelineName, pipelineDetailsResult);
			return pipelineDetailsResult;
		} catch (AWSCodePipelineException e) {
			if (latestResult.containsKey(pipelineName)) {
				System.out.println(e.getMessage() + " - Returning cached value for " + pipelineName);
				//log.warn("{} - Returning cached value for {}", e.getMessage(), pipelineName);
				return latestResult.get(pipelineName);
			}
			throw new PipelineServiceException("Failed to get details for " + pipelineName, e);
		}
	}

}
