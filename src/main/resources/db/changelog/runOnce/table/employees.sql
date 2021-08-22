--liquibase formatted sql

--changeset trangelier:create-table-employees
CREATE TABLE employees(
        employee_id    NUMBER(6),
        first_name     VARCHAR2(20),
        last_name      VARCHAR2(25) CONSTRAINT emp_last_name_nn NOT NULL,
        email          VARCHAR2(25) CONSTRAINT emp_email_nn NOT NULL,
        phone_number   VARCHAR2(20),
        hire_date      DATE CONSTRAINT emp_hire_date_nn NOT NULL,
        job_id         VARCHAR2(10) CONSTRAINT emp_job_nn NOT NULL,
        salary         NUMBER(8,2),
        commission_pct NUMBER(2,2),
        manager_id     NUMBER(6),
        department_id  NUMBER(4),
        CONSTRAINT     emp_salary_min
            CHECK (salary > 0),
        CONSTRAINT     emp_email_uk
            UNIQUE (email)
    );

ALTER TABLE employees
ADD (
    CONSTRAINT     emp_emp_id_pk
                     PRIMARY KEY (employee_id),
    CONSTRAINT     emp_dept_fk
                     FOREIGN KEY (department_id)
                      REFERENCES departments,
    CONSTRAINT     emp_job_fk
                     FOREIGN KEY (job_id)
                      REFERENCES jobs (job_id),
    CONSTRAINT     emp_manager_fk
                     FOREIGN KEY (manager_id)
                      REFERENCES employees
    ) ;

ALTER TABLE departments
ADD ( CONSTRAINT dept_mgr_fk
      		 FOREIGN KEY (manager_id)
      		  REFERENCES employees (employee_id)
    ) ;
--rollback drop table employees;