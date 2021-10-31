package dev.trangelier.hr.controller;

import dev.trangelier.hr.exception.NotFoundException;
import dev.trangelier.hr.model.CreatedRecord;
import dev.trangelier.hr.model.Region;
import dev.trangelier.hr.model.dto.RegionDto;
import dev.trangelier.hr.repository.RegionRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/regions")
@Slf4j
public class RegionController {

    private final RegionRepository regionRepository;
    private final ModelMapper modelMapper;

    @Autowired
    RegionController(RegionRepository regionRepository, ModelMapper modelMapper) {
        this.regionRepository = regionRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<Region> findAll() {
        return regionRepository.queryAll();
    }

    @GetMapping("/{id}")
    public Region findById(@PathVariable("id") Long id) {
        Optional<Region> optionalRegion = regionRepository.findById(id);
        if (optionalRegion.isEmpty()) {
            throw new NotFoundException("Region with id " + id + " was not found");
        }
        log.info(optionalRegion.toString());
        return optionalRegion.get();
    }

    @PostMapping(path = "/", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedRecord create(@RequestBody RegionDto regionDto) {
        Region region = convertToEntity(regionDto);
        region.setId(ThreadLocalRandom.current().nextLong(0, 9999));
        Long id = regionRepository.insert(region);
        log.info("Created " + id);
        return new CreatedRecord(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Long id, @RequestBody RegionDto regionDto) {
        Region region = convertToEntity(regionDto);
        region.setId(id);
        int rowsUpdated = regionRepository.update(region);
        if (rowsUpdated == 0) {
            log.error("Region with id " + id + " was not found");
            throw new NotFoundException("Region with id " + id + " was not found");
        }
        log.info("Updated " + id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        int rowsDeleted = regionRepository.delete(id);
        log.info("Deleted " + rowsDeleted);
        log.info("Deleted rows " + rowsDeleted);
    }


    /**
     * Convert a {@link Region} to a {@link RegionDto}
     *
     * @param region
     * @return
     */
    private RegionDto convertToDto(Region region) {
        return modelMapper.map(region, RegionDto.class);
    }

    /**
     * Convert a {@link RegionDto} to a {@link Region}
     *
     * @param regionDto
     * @return
     */
    private Region convertToEntity(RegionDto regionDto) {
        return modelMapper.map(regionDto, Region.class);
    }
}
