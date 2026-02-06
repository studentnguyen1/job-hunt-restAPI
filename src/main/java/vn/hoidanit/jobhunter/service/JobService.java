package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    public boolean isJobExist(long id) {
        return this.jobRepository.existsById(id);
    }

    public boolean isSkillandCompanyNotExist(Job newJob) {
        // check skill
        List<Skill> skills = this.skillRepository.findByIdIn(
                newJob.getSkills().stream().map(Skill::getId).toList());
        if (skills == null) {
            return true;
        }
        // check company
        Optional<Company> jobCompany = this.companyRepository.findById(newJob.getCompany().getId());
        if (jobCompany.isEmpty()) {
            return true;
        }
        return false;
    }

    public Job handleCreateJob(Job newJob) {

        // check skills
        if (newJob.getSkills() != null) {
            List<Skill> skills = this.skillRepository.findByIdIn(
                    newJob.getSkills().stream().map(Skill::getId).toList());
            newJob.setSkills(skills);
        }

        // check company
        if (newJob.getCompany() != null) {
            Optional<Company> jobCompany = this.companyRepository.findById(newJob.getCompany().getId());
            if (jobCompany.isPresent()) {
                newJob.setCompany(jobCompany.get());
            }

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
            if (jobInput.getCompany() != null) {
                Optional<Company> jobCompany = this.companyRepository.findById(jobInput.getCompany().getId());
                if (jobCompany.isPresent()) {
                    currentJob.setCompany(jobCompany.get());
                }
            }

            return this.jobRepository.save(currentJob);
        }
        return null;

    }

    public ResultPaginationDTO handleGetAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());
        rs.setMeta(mt);

        rs.setResult(pageJob.getContent());

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
        res.setCreatedBy(job.getCreatedBy());

        if (job.getSkills() != null) {
            List<String> skillUsers = job.getSkills().stream().map(Skill::getName).toList();
            res.setSkill(skillUsers);
        }

        return res;

    }

    public ResUpdateJobDTO convertToResUpdatedDTO(Job job) {
        ResUpdateJobDTO res = new ResUpdateJobDTO();
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
        res.setUpdatedAt(job.getUpdatedAt());
        res.setUpdatedBy(job.getUpdatedBy());

        if (job.getSkills() != null) {
            List<String> skillUsers = job.getSkills().stream().map(Skill::getName).toList();
            res.setSkill(skillUsers);
        }

        return res;

    }
}
