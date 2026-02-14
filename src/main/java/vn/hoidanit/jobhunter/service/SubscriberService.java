package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;



@Service
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
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

}
