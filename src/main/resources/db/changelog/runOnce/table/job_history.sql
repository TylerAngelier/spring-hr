--liquibase formatted sql

--changeset trangelier:create-table-job_history
CREATE TABLE job_history (
    employee_id   NUMBER(6) CONSTRAINT jhist_employee_nn NOT NULL,
	start_date    DATE CONSTRAINT jhist_start_date_nn NOT NULL,
	end_date      DATE CONSTRAINT jhist_end_date_nn NOT NULL,
	job_id        VARCHAR2(10) CONSTRAINT jhist_job_nn NOT NULL,
    department_id NUMBER(4),
    CONSTRAINT    jhist_date_interval
                    CHECK (end_date > start_date)
    ) ;

CREATE UNIQUE INDEX jhist_emp_id_st_date_pk
ON job_history (employee_id, start_date) ;

ALTER TABLE job_history
ADD ( CONSTRAINT jhist_emp_id_st_date_pk
      PRIMARY KEY (employee_id, start_date)
    , CONSTRAINT     jhist_job_fk
                     FOREIGN KEY (job_id)
                     REFERENCES jobs
    , CONSTRAINT     jhist_emp_fk
                     FOREIGN KEY (employee_id)
                     REFERENCES employees
    , CONSTRAINT     jhist_dept_fk
                     FOREIGN KEY (department_id)
                     REFERENCES departments
    ) ;
--rollback drop table job_history;