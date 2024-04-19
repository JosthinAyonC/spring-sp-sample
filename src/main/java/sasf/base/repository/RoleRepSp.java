package sasf.base.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import sasf.base.entity.Role;

@Repository
public class RoleRepSp {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RoleRepo _roleRepo;

    public Page<Role> findAllSp(Pageable pageable) {
        try {
            StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("find_all_roles")
                    .registerStoredProcedureParameter(1, void.class, ParameterMode.REF_CURSOR);

            sp.execute();

            List<Object[]> resultList = sp.getResultList();
            List<Role> roles = new ArrayList<>();

            for (Object[] row : resultList) {
                Role role = new Role();

                role.setId(((Number) row[0]).longValue());
                role.setName((String) row[1]);
                role.setDescription((String) row[2]);
                role.setCreatedAt((Date) row[3]);
                role.setUpdatedAt((Date) row[4]);

                roles.add(role);
            }

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), roles.size());
            Page<Role> page = new PageImpl<>(roles.subList(start, end), pageable, roles.size());

            return page;

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error al recuperar la lista de roles desde el procedimiento almacenado: " + e.getMessage());
        }
    }

    public void saveRole(Role role) {
        try {
            if (_roleRepo.existsByName(role.getName())) {
                throw new DataException("Ya existe un rol con ese nombre", null);
            }
            StoredProcedureQuery sp = entityManager.createStoredProcedureQuery("save_role");

            sp.registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN);
            sp.registerStoredProcedureParameter("p_description", String.class, ParameterMode.IN);

            sp.setParameter("p_name", role.getName());
            sp.setParameter("p_description", role.getDescription());

            sp.execute();
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el rol: " + e.getMessage());
        }
    }

}
