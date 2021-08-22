package dev.trangelier.hr.model;

/**
 * Model for returning newly created records
 */
public class CreatedRecord {
    private Long id;

    public CreatedRecord() {
    }

    public CreatedRecord(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "CreatedRecord{" +
                "id=" + id +
                '}';
    }
}
