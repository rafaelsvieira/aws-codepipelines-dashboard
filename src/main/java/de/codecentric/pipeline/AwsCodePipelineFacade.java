package de.codecentric.pipeline;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.amazonaws.services.codepipeline.AWSCodePipeline;
import com.amazonaws.services.codepipeline.model.GetPipelineExecutionRequest;
import com.amazonaws.services.codepipeline.model.GetPipelineExecutionResult;
import com.amazonaws.services.codepipeline.model.GetPipelineRequest;
import com.amazonaws.services.codepipeline.model.GetPipelineStateRequest;
import com.amazonaws.services.codepipeline.model.GetPipelineStateResult;
import com.amazonaws.services.codepipeline.model.ListPipelineExecutionsRequest;
import com.amazonaws.services.codepipeline.model.ListPipelineExecutionsResult;
import com.amazonaws.services.codepipeline.model.ListPipelinesRequest;
import com.amazonaws.services.codepipeline.model.ListPipelinesResult;
import com.amazonaws.services.codepipeline.model.ListTagsForResourceRequest;
import com.amazonaws.services.codepipeline.model.PipelineSummary;
import com.amazonaws.services.codepipeline.model.Tag;

@Component
public class AwsCodePipelineFacade {
	private final AWSCodePipeline client;
	private Tag tagFilter = null;

	public AwsCodePipelineFacade (AWSCodePipeline awsCodePipeline) {
		this.client = awsCodePipeline;
		this.tagFilter = new Tag();
		String key =  System.getenv("AWS_TAG_FILTER") != null ? System.getenv("AWS_TAG_FILTER") : "canais";
		tagFilter.setKey(key);
	}

	public ListPipelinesResult getPipelineResults(String group) {
		ListPipelinesResult result = client.listPipelines(new ListPipelinesRequest());

		if(!group.isEmpty()) {
			tagFilter.setValue(group);
			Predicate<PipelineSummary> filter = pipeline ->
			this.client.listTagsForResource(new ListTagsForResourceRequest().withResourceArn(
					this.client.getPipeline(new GetPipelineRequest().withName(pipeline.getName()))
					.getMetadata().getPipelineArn())).getTags().contains(tagFilter);

			result.setPipelines(result.getPipelines().stream().filter(filter)
					.collect(Collectors.toList()));
		}

		return result;
	}

	public GetPipelineStateResult getPipelineStatus(String pipelineName) {
		return client.getPipelineState(new GetPipelineStateRequest().withName(pipelineName));
	}

	public String getLatestCommitMessage(String pipelineName) {
		String latestPipelineExecutionId = getLatestPipelineExecutionId(pipelineName);
		GetPipelineExecutionResult pipelineExecution = getPipelineExecutionSummary(pipelineName, latestPipelineExecutionId);
		return getLatestRevisionSummary(pipelineExecution);
	}

	private String getLatestPipelineExecutionId(String name) {
		ListPipelineExecutionsResult pipelineExecutionsResult = getLatestPipelineExecutionResult(name);
		return pipelineExecutionsResult.getPipelineExecutionSummaries().get(0).getPipelineExecutionId();
	}

	private String getLatestRevisionSummary(GetPipelineExecutionResult pipelineExecution) {
		return pipelineExecution.getPipelineExecution()
				.getArtifactRevisions()
				.get(0)
				.getRevisionSummary();
	}

	private GetPipelineExecutionResult getPipelineExecutionSummary(String name, String latestPipelineExecutionId) {
		return client.getPipelineExecution(
				new GetPipelineExecutionRequest()
				.withPipelineExecutionId(latestPipelineExecutionId).withPipelineName(name));
	}

	private ListPipelineExecutionsResult getLatestPipelineExecutionResult(String name) {
		return client.listPipelineExecutions(
				new ListPipelineExecutionsRequest().withPipelineName(name).withMaxResults(1));
	}

}
