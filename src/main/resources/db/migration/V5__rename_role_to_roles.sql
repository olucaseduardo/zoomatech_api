ALTER TABLE member DROP CONSTRAINT fk_member_role;
ALTER TABLE "role" RENAME TO roles;
ALTER TABLE member ADD CONSTRAINT fk_member_role FOREIGN KEY (role_id) REFERENCES roles (id);
