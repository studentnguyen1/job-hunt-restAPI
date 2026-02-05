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
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.JobService;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")

public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;
    private final JobService jobService;

    public ResumeController(ResumeService resumeService, UserService userService, JobService jobService) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.jobService = jobService;
    }

    @PostMapping("/resumes")
    @ApiMessage("create new resume")
    public ResponseEntity<ResCreateResumeDTO> createNewResume(@Valid @RequestBody Resume newResume)
            throws IdInvalidException {
        // check user-id exist
        boolean isUserExist = this.userService.isIdExist(newResume.getUser().getId());
        if (!isUserExist) {
            throw new IdInvalidException("User Id : " + newResume.getUser().getId() + " không tồn tại, nhập lại id");
        }

        // check job-id exist
        boolean isJobExist = this.jobService.isJobExist(newResume.getJob().getId());
        if (!isJobExist) {
            throw new IdInvalidException("Job Id : " + newResume.getJob().getId() + " không tồn tại, nhập lại id");
        }

        // create resume
        Resume createdResume = this.resumeService.handleCreateResume(newResume);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.resumeService.convertToCreateResumeDTO(createdResume));
    }

    @PutMapping("/resumes")
    @ApiMessage("update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resumeInput)
            throws IdInvalidException {
        Resume currentResume = this.resumeService.handleUpdateResume(resumeInput);
        if (currentResume == null) {
            throw new IdInvalidException("Resume: " + resumeInput.getId() + " không tồn tại, nhập lại id");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.resumeService.convertToUpdateResumeDTO(currentResume));
    }

    @DeleteMapping("/resumes/{resume-id}")
    @ApiMessage("delete a resume by id")
    public ResponseEntity<Void> deleteResume(@PathVariable("resume-id") long id) throws IdInvalidException {
        Resume currentResume = this.resumeService.handleGetResumeById(id);
        if (currentResume == null) {
            throw new IdInvalidException("Resume với Id =  " + id + " không tồn tại");
        }
        this.resumeService.handleDeleteResume(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/resumes/{resume-id}")
    @ApiMessage("get resume by id")
    public ResponseEntity<ResResumeDTO> getResumeById(@PathVariable("resume-id") long id) throws IdInvalidException {
        Resume fetchResume = this.resumeService.handleGetResumeById(id);
        if (fetchResume == null) {
            throw new IdInvalidException("Job với Id =  " + id + " không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.convertToResResumeDTO(fetchResume));
    }

    @GetMapping("/resumes")
    @ApiMessage("fetch all resume")
    public ResponseEntity<ResultPaginationDTO> getAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.resumeService.handleGetAllResumes(spec, pageable));
    }

}
