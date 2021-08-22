--liquibase formatted sql

--changeset trangelier:create-table-jobs
CREATE TABLE jobs(
        job_id         VARCHAR2(10),
        job_title      VARCHAR2(35) CONSTRAINT job_title_nn NOT NULL,
        min_salary     NUMBER(6),
        max_salary     NUMBER(6)
    );

ALTER TABLE jobs
ADD ( CONSTRAINT job_id_pk
      		 PRIMARY KEY(job_id)
    );
--rollback drop table jobs;