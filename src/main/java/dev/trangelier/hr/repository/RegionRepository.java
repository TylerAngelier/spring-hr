package dev.trangelier.hr.repository;

import dev.trangelier.hr.model.Region;
import org.jdbi.v3.sqlobject.SqlObject;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@RegisterBeanMapper(Region.class)
public interface RegionRepository extends SqlObject {
    @SqlQuery("select * from regions")
    List<Region> queryAll();

    @SqlQuery("select * from regions where region_id = :id")
    Optional<Region> findById(Long id);

    @SqlUpdate("insert into regions (region_id, region_name) values (:id, :regionName)")
    @GetGeneratedKeys("region_id")
    @Transactional
    Long insert(@BindBean Region region);

    @SqlUpdate("update regions set region_name = :regionName where region_id = :id")
    @Transactional
    int update(@BindBean Region Region);

    @SqlUpdate("delete regions where region_id = :id")
    @Transactional
    int delete(Long id);
}
