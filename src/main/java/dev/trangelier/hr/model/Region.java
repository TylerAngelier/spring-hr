package dev.trangelier.hr.model;

import lombok.Data;
import lombok.Getter;
import org.jdbi.v3.core.mapper.reflect.ColumnName;

@Data
public class Region {
    @Getter(onMethod = @__(@ColumnName("REGION_ID")))
    private Long id;
    private String regionName;
}
