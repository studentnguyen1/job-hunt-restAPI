package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role handleCreateRole(Role r) {
        // check permision
        if (r.getPermissions() != null) {
            List<Permission> permissions = this.permissionRepository.findByIdIn(
                    r.getPermissions().stream().map(Permission::getId).toList());
            r.setPermissions(permissions);
        }
        return this.roleRepository.save(r);
    }

    public Role handleGetRoleById(long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    public Role handleUpdateRole(Role r) {
        Role currentRole = this.handleGetRoleById(r.getId());
        // update
        currentRole.setName(currentRole.getName());
        currentRole.setDescription(currentRole.getDescription());
        currentRole.setActive(currentRole.isActive());
        currentRole.setCreatedAt(currentRole.getCreatedAt());
        currentRole.setCreatedBy(currentRole.getCreatedBy());
        currentRole.setUpdatedAt(currentRole.getUpdatedAt());
        currentRole.setUpdatedBy(currentRole.getUpdatedBy());

        if (r.getPermissions() != null) {
            List<Permission> permissions = this.permissionRepository.findByIdIn(
                    r.getPermissions().stream().map(Permission::getId).toList());
            currentRole.setPermissions(permissions);
        }

        return this.roleRepository.save(currentRole);

    }

    public ResultPaginationDTO handleGetAllRoles(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageJob = this.roleRepository.findAll(spec, pageable);
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

    public void handleDeleteRole(long id) {
        this.roleRepository.deleteById(id);
    }

    public boolean existByName(String name) {
        return this.roleRepository.existsByName(name);

    }
}
