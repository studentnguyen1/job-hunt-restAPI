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
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@RestController
@RequestMapping("/api/v1")

public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("create new permission")
    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission newPermission)
            throws IdInvalidException {
        // check exist
        boolean isPermisionExist = this.permissionService.isPermisionExist(newPermission);
        if (isPermisionExist) {
            throw new IdInvalidException("Permission đã tồn tại, vui lòng thêm permission khác");
        }
        // create
        Permission createdPermission = this.permissionService.handleCreatPermission(newPermission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @PutMapping("/permissions")
    @ApiMessage("update an permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permissionInput)
            throws IdInvalidException {

        // check by id
        Permission currentPermission = this.permissionService.handleGetPermissionById(permissionInput.getId());
        if (currentPermission == null) {
            throw new IdInvalidException("Permission không tồn tại.");
        }
        // check api, module ...
        boolean isExist = this.permissionService.isPermisionExist(permissionInput);
        if (isExist) {
            // check name
            if (this.permissionService.isSameName(permissionInput))
                throw new IdInvalidException("Thông tin trong Permission đã tồn tại, nhập thông tin khác.");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.permissionService.handleUpdatePermission(permissionInput));
    }

    @GetMapping("/permissions")
    @ApiMessage("fetch all permission")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(@Filter Specification<Permission> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.permissionService.handleGetAllPermissions(spec, pageable));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission by id")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) throws IdInvalidException {
        Permission currentPermission = this.permissionService.handleGetPermissionById(id);
        if (currentPermission == null) {
            throw new IdInvalidException("Permission với Id = " + id + " không tồn tại");
        }
        this.permissionService.handleDeletePermission(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
