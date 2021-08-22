--liquibase formatted sql

--changeset trangelier:create-table-locations

CREATE TABLE locations(
        location_id    NUMBER(4),
        street_address VARCHAR2(40),
        postal_code    VARCHAR2(12),
        city       VARCHAR2(30) CONSTRAINT loc_city_nn  NOT NULL,
        state_province VARCHAR2(25),
        country_id     CHAR(2)
    );

ALTER TABLE locations
ADD ( CONSTRAINT loc_id_pk
       		 PRIMARY KEY (location_id),
      CONSTRAINT loc_c_id_fk
       		 FOREIGN KEY (country_id)
        	  REFERENCES countries(country_id)
    );
--rollback drop table locations;