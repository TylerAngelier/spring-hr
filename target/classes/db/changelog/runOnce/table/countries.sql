--liquibase formatted sql

--changeset trangelier:create-table-countries
CREATE TABLE countries (
        country_id      CHAR(2)  CONSTRAINT country_id_nn NOT NULL,
        country_name    VARCHAR2(40),
        region_id       NUMBER ,
        CONSTRAINT     country_c_id_pk
            PRIMARY KEY (country_id)
    );

ALTER TABLE countries ADD CONSTRAINT countr_reg_fk
    FOREIGN KEY (region_id)
        REFERENCES regions(region_id);
--rollback drop table countries;