package de.codecentric.pipeline;

import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.codepipeline.model.PipelineSummary;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class PipelineController {
	private final PipelineService pipelineService;

	public PipelineController(PipelineService pipelineService) {
		this.pipelineService = pipelineService;
	}

	@RequestMapping(value = {"/pipelines"})
	public List<PipelineSummary> handleIndex(
			@RequestParam("group") Optional<String> group
			) {
		String pipelineGroup = group.isPresent() ? group.get() : "";

		return pipelineService.getPipelines(pipelineGroup);
	}

	@RequestMapping("/pipeline/{name}")
	public PipelineDetailsResult handlePipeline(
			@PathVariable("name") String name,
			@RequestParam("status") Optional<String> status
			) {
		String pipelineStatus = status.isPresent() ? status.get().toLowerCase() : "all";
		try {
			return pipelineService.getPipelineDetails(name, pipelineStatus);
		} catch (PipelineServiceException e) {
			System.out.println("Failed to get pipeline details for " + name + ". Will return null response." + e);
			//log.warn("Failed to get pipeline details for {}. Will return null response.", name, e);
			return null;
		}
	}
}
