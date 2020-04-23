package de.codecentric.pipeline;

import com.amazonaws.services.codepipeline.model.PipelineSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class PipelineController {
    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    @RequestMapping(value = {"/pipelines", "/pipelines/{group}"})
    public List<PipelineSummary> handleIndex(@PathVariable Optional<String>  group) {
        String filter = group.isPresent() ? group.get() : "";
        return pipelineService.getPipelines(filter);
    }

    @RequestMapping("/pipeline/{name}")
    public PipelineDetailsResult handlePipeline(@PathVariable("name") String name) {
        try {
            return pipelineService.getPipelineDetails(name);
        } catch (PipelineServiceException e) {
            log.warn("Failed to get pipeline details for {}. Will return null response.", name, e);
            return null;
        }
    }
}
