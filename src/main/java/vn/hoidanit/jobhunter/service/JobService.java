package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResJobDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isJobExist(long id) {
        return this.jobRepository.existsById(id);
    }

    public Job handleCreateJob(Job newJob) {
        if (newJob.getSkills() != null) {
            List<Skill> skills = this.skillRepository.findByIdIn(
                    newJob.getSkills().stream().map(Skill::getId).toList());
            newJob.setSkills(skills);
        }
        return this.jobRepository.save(newJob);
    }

    public Job handleUpdateJob(Job jobInput) {
        Job currentJob = this.jobRepository.findById(jobInput.getId()).orElse(null);

        if (currentJob != null) {
            currentJob.setName(jobInput.getName());
            currentJob.setLocation(jobInput.getLocation());
            currentJob.setSalary(jobInput.getSalary());
            currentJob.setQuantity(jobInput.getQuantity());
            currentJob.setLevel(jobInput.getLevel());
            currentJob.setDescription(jobInput.getDescription());
            currentJob.setStartDate(jobInput.getStartDate());
            currentJob.setEndDate(jobInput.getEndDate());
            currentJob.setActive(jobInput.isActive());
            if (jobInput.getSkills() != null) {
                List<Skill> skills = this.skillRepository.findByIdIn(
                        jobInput.getSkills().stream().map(Skill::getId).toList());
                currentJob.setSkills(skills);
            }

            return this.jobRepository.save(currentJob);
        }
        return null;

    }

    public ResultPaginationDTO handleGetAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        List<ResJobDTO> resJobs = pageJob.getContent().stream().map(this::convertToResDTO).toList();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());
        rs.setMeta(mt);

        rs.setResult(resJobs);

        return rs;
    }

    public Job handleGetJobById(long id) {
        return this.jobRepository.findById(id).orElse(null);
    }

    public void handleDeleteJob(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResJobDTO convertToResDTO(Job job) {
        ResJobDTO res = new ResJobDTO();
        // update fields
        res.setId(job.getId());
        res.setName(job.getName());
        res.setLocation(job.getLocation());
        res.setSalary(job.getSalary());
        res.setQuantity(job.getQuantity());
        res.setLevel(job.getLevel());
        res.setDescription(job.getDescription());
        res.setStartDate(job.getStartDate());
        res.setEndDate(job.getEndDate());
        res.setActive(job.isActive());
        res.setCreatedAt(job.getCreatedAt());
        res.setUpdatedAt(job.getUpdatedAt());
        res.setCreatedBy(job.getCreatedBy());
        res.setUpdatedBy(job.getUpdatedBy());

        if (job.getSkills() != null) {
            List<String> skillUsers = job.getSkills().stream().map(Skill::getName).toList();
            res.setSkill(skillUsers);
        }

        return res;

    }
}
