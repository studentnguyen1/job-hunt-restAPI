package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")

public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("create new job")
    public ResponseEntity<ResJobDTO> createNewJob(@Valid @RequestBody Job newJob) throws IdInvalidException {
        Job createdJob = this.jobService.handleCreateJob(newJob);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.convertToResDTO(createdJob));
    }

    @PutMapping("/jobs")
    @ApiMessage("update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job jobInput) throws IdInvalidException {
        Job currentJob = this.jobService.handleUpdateJob(jobInput);
        if (currentJob == null) {
            throw new IdInvalidException("Job: " + jobInput.getName() + " không tồn tại, nhập lại id");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.convertToResUpdatedDTO(currentJob));
    }

    @GetMapping("/jobs")
    @ApiMessage("fetch all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Job> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.jobService.handleGetAllJobs(spec, pageable));
    }

    @GetMapping("/jobs/{job-id}")
    @ApiMessage("get job by id")
    public ResponseEntity<Job> getJobById(@PathVariable("job-id") long id) throws IdInvalidException {
        Job fetchJob = this.jobService.handleGetJobById(id);
        if (fetchJob == null) {
            throw new IdInvalidException("Job với Id =  " + id + " không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchJob);
    }

    @DeleteMapping("/jobs/{job-id}")
    @ApiMessage("delete a job by id")
    public ResponseEntity<Void> deleteJob(@PathVariable("job-id") long id) throws IdInvalidException {
        // Job currentJob = this.jobService.handleGetJobById(id);
        // if (currentJob == null) {
        // throw new IdInvalidException("Job với Id = " + id + " không tồn tại");
        // }
        this.jobService.handleDeleteJob(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
