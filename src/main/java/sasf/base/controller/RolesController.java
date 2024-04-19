package sasf.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import sasf.base.entity.Role;
import sasf.base.repository.RoleRepSp;
import sasf.base.repository.RoleRepo;
import sasf.base.utils.Globales;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("role")
public class RolesController {
    @Autowired
    private RoleRepSp _roleRepSp;

    @Autowired
    private RoleRepo _roleRep;

    @GetMapping
    public ResponseEntity<Page<Role>> getAllRoles(
            @PageableDefault(page = Globales.PAGEABLE_PAGE_DEFAULT, size = Globales.PAGEABLE_SIZE_DEFAULT) Pageable pageable) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(_roleRepSp.findAllSp(pageable));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> saveRole(@RequestBody Role role) {
        try {
            _roleRepSp.saveRole(role);
            return ResponseEntity.status(HttpStatus.CREATED).body("{\"msg\":\"Role creado exitosamente\"}");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<Role> getById(@PathVariable Long id) {
    //     try {
    //         return ResponseEntity.status(HttpStatus.OK).body();
    //     } catch (Exception e) {
    //         throw new RuntimeException("No se pudo listar por id el role:");
    //     }
    // }

}
