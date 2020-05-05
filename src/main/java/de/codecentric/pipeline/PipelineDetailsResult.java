package de.codecentric.pipeline;

import java.util.List;

import com.amazonaws.services.codepipeline.model.StageState;

import lombok.Getter;

@Getter
public class PipelineDetailsResult {

	private final List<StageState> stageStates;
	private final String commitMessage;
	private final String pipelineName;

	PipelineDetailsResult(String pipelineName, List<StageState> stageStates, String commitMessage) {
		this.stageStates = stageStates;
		this.commitMessage = commitMessage;
		this.pipelineName = pipelineName;
	}

}
