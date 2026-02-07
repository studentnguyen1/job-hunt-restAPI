package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.PermissionRepository;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public Permission handleCreatPermission(Permission newPermission) {
        return this.permissionRepository.save(newPermission);
    }

    public boolean isPermisionExist(Permission newPermission) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(newPermission.getModule(),
                newPermission.getApiPath(), newPermission.getMethod());

    }

    public Permission handleGetPermissionById(long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    public Permission handleUpdatePermission(Permission p) {
        Permission currentPer = this.handleGetPermissionById(p.getId());

        currentPer.setName(p.getName());
        currentPer.setApiPath(p.getApiPath());
        currentPer.setMethod(p.getMethod());
        currentPer.setModule(p.getModule());

        return this.permissionRepository.save(currentPer);
    }

    public ResultPaginationDTO handleGetAllPermissions(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pagePermissions.getTotalPages());
        mt.setTotal(pagePermissions.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pagePermissions.getContent());

        return rs;

    }

    public void handleDeletePermission(long id) {
        // delete permission inside permission_roles
        Permission currentPermission = this.handleGetPermissionById(id);
        currentPermission.getRoles().forEach(role -> role.getPermissions().remove(currentPermission));

        // delete skill
        this.permissionRepository.delete(currentPermission);
    }

}
