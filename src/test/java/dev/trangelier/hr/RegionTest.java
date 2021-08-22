package dev.trangelier.hr;

import dev.trangelier.hr.model.CreatedRecord;
import dev.trangelier.hr.model.Region;
import dev.trangelier.hr.model.dto.RegionDto;
import dev.trangelier.hr.repository.RegionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RegionTest {

    Region region;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private RegionRepository regionRepository;

//    @BeforeEach
//    public void createRecord() {
//        region = new Region();
//        region.setId(999L);
//        region.setRegionName("Test");
//        Long id = regionRepository.insert(region);
//        if (id == null) {
//            Assertions.fail("Failed to insert test record for tests");
//        }
//        System.out.println("Created Record ID: " + id);
//    }
//
//    @AfterEach
//    public void deleteTestRecord() {
//        if (region.getId() == null) {
//            Assertions.fail("Test Region ID is null");
//        }
//        int rowsAffected = regionRepository.delete(region.getId());
//        if (rowsAffected != 1) {
//            Assertions.fail("rowsAffected was not 1: " + rowsAffected);
//        }
//    }

    @Test
    public void queryAll() {
        ResponseEntity<Region[]> regions = restTemplate.exchange(controllerURL() + "/", HttpMethod.GET, HttpEntity.EMPTY, Region[].class);

        Assertions.assertEquals(regions.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(regions.getBody());
        Assertions.assertTrue(regions.getBody().length >= 4);
    }

    @Test
    public void findById_Found() {
        ResponseEntity<Region> regionResponseEntity = restTemplate.exchange(controllerURL() + "/1", HttpMethod.GET, HttpEntity.EMPTY, Region.class);

        Assertions.assertEquals(regionResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(regionResponseEntity.getBody());
        Region region = regionResponseEntity.getBody();
        Assertions.assertEquals(region.getRegionName(), "Europe");
        Assertions.assertEquals(region.getId(), 1);
    }

    @Test
    public void findById_NotFound() {
        ResponseEntity<Map> responseEntity = restTemplate.exchange(controllerURL() + "/9999", HttpMethod.GET, HttpEntity.EMPTY, Map.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(responseEntity.getBody().get("message"), "Region with id 9999 was not found");
    }

    @Test
    public void insert() {
        RegionDto regionDto = new RegionDto();
        regionDto.setRegionName("Test");
        HttpEntity<RegionDto> httpEntity = new HttpEntity<>(regionDto);
        ResponseEntity<CreatedRecord> responseEntity = restTemplate.exchange(controllerURL() + "/", HttpMethod.POST, httpEntity, CreatedRecord.class);

        System.out.println(responseEntity.getBody());

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertNotNull(responseEntity.getBody().getId());

        // cleanup
        regionRepository.delete(responseEntity.getBody().getId());
    }

    @Test
    public void update() {
        RegionDto regionDto = new RegionDto();
        regionDto.setRegionName("Test Upd");
        HttpEntity<RegionDto> httpEntity = new HttpEntity<>(regionDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(controllerURL() + "/1", HttpMethod.PUT, httpEntity, String.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
        Assertions.assertNull(responseEntity.getBody());

        Optional<Region> updatedRegion = regionRepository.findById(1L);
        Assertions.assertTrue(updatedRegion.isPresent());
        Assertions.assertEquals(updatedRegion.get().getRegionName(), "Test Upd");

        // cleanup
        Region cleanupRegion = new Region();
        cleanupRegion.setId(1L);
        cleanupRegion.setRegionName("Europe");
        regionRepository.update(cleanupRegion);
    }

    /**
     * Controller URL for this test suite
     *
     * @return http://localhost:port/hr/ControllerRequestMapping
     */
    private String controllerURL() {
        return "http://localhost:" + port + "/hr/regions";
    }

}
