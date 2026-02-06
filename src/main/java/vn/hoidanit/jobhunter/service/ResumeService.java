package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO.JobResume;
import vn.hoidanit.jobhunter.domain.response.resume.ResResumeDTO.UserResume;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.ResumeRepository;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public Resume handleCreateResume(Resume newResume) {
        return this.resumeRepository.save(newResume);

    }

    public Resume handleUpdateResume(Resume resume) {
        Resume currentResume = this.handleGetResumeById(resume.getId());
        if (currentResume == null) {
            return null;
        }
        currentResume.setStatus(resume.getStatus());

        return this.resumeRepository.save(currentResume);
    }

    public Resume handleGetResumeById(long id) {
        Optional<Resume> currentResume = this.resumeRepository.findById(id);
        if (currentResume.isPresent()) {
            Resume resume = currentResume.get();
            return resume;
        }
        return null;
    }

    public void handleDeleteResume(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResultPaginationDTO handleGetAllResumes(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        List<ResResumeDTO> listResumes = pageResume.getContent().stream().map(this::convertToResResumeDTO).toList();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(listResumes);

        return rs;

    }

    public ResCreateResumeDTO convertToCreateResumeDTO(Resume newResume) {
        ResCreateResumeDTO res = new ResCreateResumeDTO(newResume.getId(), newResume.getCreatedAt(),
                newResume.getCreatedBy());
        return res;
    }

    public ResUpdateResumeDTO convertToUpdateResumeDTO(Resume resume) {
        ResUpdateResumeDTO res = new ResUpdateResumeDTO(resume.getUpdatedAt(), resume.getUpdatedBy());
        return res;
    }

    public ResResumeDTO convertToResResumeDTO(Resume resume) {
        ResResumeDTO resResume = new ResResumeDTO();
        UserResume userResume = new UserResume(resume.getUser().getId(), resume.getUser().getName());
        JobResume userJob = new JobResume(resume.getJob().getId(), resume.getJob().getName());

        resResume.setId(resume.getId());
        resResume.setEmail(resume.getEmail());
        resResume.setUrl(resume.getUrl());
        resResume.setStatus(resume.getStatus());
        resResume.setCreatedAt(resume.getCreatedAt());
        resResume.setCreatedBy(resume.getCreatedBy());
        resResume.setUpdatedAt(resume.getUpdatedAt());
        resResume.setUpdatedBy(resume.getUpdatedBy());
        if (resume.getJob() != null) {
            resResume.setCompanyName(resume.getJob().getCompany().getName());
        }
        resResume.setUser(userResume);
        resResume.setJob(userJob);

        return resResume;

    }

}
