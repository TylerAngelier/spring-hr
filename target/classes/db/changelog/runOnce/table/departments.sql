--liquibase formatted sql

--changeset trangelier:create-table-departments
CREATE TABLE departments(
        department_id    NUMBER(4),
        department_name  VARCHAR2(30) CONSTRAINT dept_name_nn  NOT NULL,
        manager_id       NUMBER(6),
        location_id      NUMBER(4)
    );

ALTER TABLE departments
ADD ( CONSTRAINT dept_id_pk
       		 PRIMARY KEY (department_id),
      CONSTRAINT dept_loc_fk
       		 FOREIGN KEY (location_id)
        	  REFERENCES locations (location_id)
     );
--rollback drop table departments;