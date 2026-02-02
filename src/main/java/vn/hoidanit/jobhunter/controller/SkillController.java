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
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("create new skill")
    public ResponseEntity<Skill> createNewSkill(@Valid @RequestBody Skill newSkill) throws IdInvalidException {
        boolean isSkillExist = this.skillService.isSkillExist(newSkill.getName());
        if (isSkillExist) {
            throw new IdInvalidException("Skill " + newSkill.getName() + " đã tồn tại, vui lòng thêm skill khác");
        }
        Skill createdSkill = this.skillService.handleCreateSkill(newSkill);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill);
    }

    @PutMapping("/skills")
    @ApiMessage("update an skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skillInput) throws IdInvalidException {
        Skill currentSkill = this.skillService.handleGetSkillById(skillInput.getId());
        if (currentSkill == null) {
            throw new IdInvalidException("Skill id: " + skillInput.getId() + " không tồn tại, nhập lại id");
        }
        if (currentSkill.getName() != null && this.skillService.isSkillExist(skillInput.getName())) {
            throw new IdInvalidException("Skill name: " + skillInput.getName() + " đã tồn tại, vui lòng nhập tên khác");
        }
        currentSkill.setName(skillInput.getName());

        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleUpdateSkill(currentSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(@Filter Specification<Skill> spec, Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleGetAllSkills(spec, pageable));
    }

    @GetMapping("/skills/{skill-id}")
    @ApiMessage("get job by id")
    public ResponseEntity<Skill> getJobById(@PathVariable("skill-id") long id) throws IdInvalidException {
        Skill fetchSkill = this.skillService.handleGetSkillById(id);
        if (fetchSkill == null) {
            throw new IdInvalidException("Skill với Id =  " + id + " không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK).body(fetchSkill);
    }

    @DeleteMapping("/skills/{skill-id}")
    @ApiMessage("delete an skill by id")
    public ResponseEntity<Void> deleteSkill(@PathVariable("skill-id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.handleGetSkillById(id);
        if (currentSkill == null) {
            throw new IdInvalidException("Skill với Id =  " + id + " không tồn tại");
        }
        this.skillService.handleDeleteSkill(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
