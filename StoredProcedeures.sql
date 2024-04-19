CREATE OR REPLACE PROCEDURE DESA_JAYON.find_all_roles(p_roles  OUT SYS_REFCURSOR)
IS
BEGIN
    OPEN p_roles FOR
        SELECT id, name, description, created_at, updated_at
        FROM roles;
END find_all_roles;
/

VARIABLE p_roles REFCURSOR;
BEGIN
  DESA_JAYON.find_all_roles(:p_roles);
END;
/

PRINT p_roles

CREATE OR REPLACE PROCEDURE save_role(
    p_name IN roles.name%TYPE,
    p_description IN roles.description%TYPE
)
IS
BEGIN
    INSERT INTO roles (name, description, created_at, updated_at, status)
    VALUES (p_name, p_description, SYSDATE, SYSDATE, 1);
    COMMIT;
END save_role;
/