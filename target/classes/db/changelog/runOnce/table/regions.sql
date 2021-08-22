--liquibase formatted sql

--changeset trangelier:create-table-regions
CREATE TABLE regions(
        region_id      NUMBER NOT NULL,
        region_name    VARCHAR2(25)
    );

ALTER TABLE regions
ADD ( CONSTRAINT reg_id_pk
       		 PRIMARY KEY (region_id)
    );
--rollback drop table regions;