--liquibase formatted sql

--changeset trangelier:schema-init-indexes
CREATE INDEX emp_department_ix
       ON employees (department_id);

CREATE INDEX emp_job_ix
       ON employees (job_id);

CREATE INDEX emp_manager_ix
       ON employees (manager_id);

CREATE INDEX emp_name_ix
       ON employees (last_name, first_name);

CREATE INDEX dept_location_ix
       ON departments (location_id);

CREATE INDEX jhist_job_ix
       ON job_history (job_id);

CREATE INDEX jhist_employee_ix
       ON job_history (employee_id);

CREATE INDEX jhist_department_ix
       ON job_history (department_id);

CREATE INDEX loc_city_ix
       ON locations (city);

CREATE INDEX loc_state_province_ix
       ON locations (state_province);

CREATE INDEX loc_country_ix
       ON locations (country_id);

COMMIT;
--rollback drop index emp_department_ix;
--rollback drop index emp_job_ix;
--rollback drop index emp_manager_ix;
--rollback drop index emp_name_ix;
--rollback drop index dept_location_ix;
--rollback drop index jhist_job_ix;
--rollback drop index jhist_employee_ix;
--rollback drop index jhist_department_ix;
--rollback drop index loc_city_ix;
--rollback drop index loc_state_province_ix;
--rollback drop index loc_country_ix;