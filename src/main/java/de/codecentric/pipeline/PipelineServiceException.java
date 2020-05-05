package de.codecentric.pipeline;

public class PipelineServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	private String pipelineName;
	public PipelineServiceException(String pipelineName, String message, Throwable cause) {
		super(message, cause);
		this.pipelineName = pipelineName;
	}
}
