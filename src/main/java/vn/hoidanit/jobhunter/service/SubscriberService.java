package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.response.email.ResEmailJob;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository,
            JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public boolean isEmailExist(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public boolean isIdExist(long id) {
        return this.subscriberRepository.existsById(id);
    }

    public Subscriber handleCreatSubscriber(Subscriber s) {
        // check skill
        if (s.getSkills() != null) {
            List<Skill> subSkill = this.skillRepository.findByIdIn(s.getSkills().stream().map(Skill::getId).toList());
            s.setSkills(subSkill);
        }

        return this.subscriberRepository.save(s);
    }

    public Subscriber handleUpdateSubscriber(Subscriber inputSub) {
        Subscriber currentSub = handleGetSubById(inputSub.getId());

        // check skill
        if (inputSub.getSkills() != null) {

            List<Skill> subSkill = this.skillRepository
                    .findByIdIn(inputSub.getSkills().stream().map(Skill::getId).toList());
            currentSub.setSkills(subSkill);
        }
        return this.subscriberRepository.save(currentSub);

    }

    public Subscriber handleGetSubById(long id) {
        return this.subscriberRepository.findById(id).orElse(null);
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        List<ResEmailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .toList();
        res.setSkills(s);
        return res;
    }

}
