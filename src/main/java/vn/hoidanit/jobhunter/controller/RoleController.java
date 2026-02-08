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
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")

public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("create new role")
    public ResponseEntity<Role> createNewRole(@Valid @RequestBody Role newRole)
            throws IdInvalidException {
        boolean isNameExist = this.roleService.existByName(newRole.getName());
        if (isNameExist) {
            throw new IdInvalidException("Name đã tồn tại, vui lòng thêm name khác");
        }
        Role createdRole = this.roleService.handleCreateRole(newRole);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/roles")
    @ApiMessage("update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role roleInput)
            throws IdInvalidException {
        // check id role
        Role currentRole = this.roleService.handleGetRoleById(roleInput.getId());
        if (currentRole == null) {
            throw new IdInvalidException("Role " + roleInput.getId() + " không tồn tại.");
        }

        // // check name
        // boolean isNameExist = this.roleService.existByName(roleInput.getName());
        // if (isNameExist) {
        // throw new IdInvalidException("Name đã tồn tại, vui lòng thêm name khác");
        // }

        Role roleUpdated = this.roleService.handleUpdateRole(roleInput);

        return ResponseEntity.status(HttpStatus.OK).body(roleUpdated);
    }

    @GetMapping("/roles")
    @ApiMessage("fetch all roles")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(@Filter Specification<Role> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.roleService.handleGetAllRoles(spec, pageable));
    }

    @DeleteMapping("/roles/{role-id}")
    @ApiMessage("delete a role by id")
    public ResponseEntity<Void> deleteRole(@PathVariable("role-id") long id) throws IdInvalidException {
        Role currentRole = this.roleService.handleGetRoleById(id);
        if (currentRole == null) {
            throw new IdInvalidException("Role với Id = " + id + " không tồn tại");
        }
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
