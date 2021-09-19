package dev.trangelier.hr;

import dev.trangelier.hr.model.CreatedRecord;
import dev.trangelier.hr.model.Region;
import dev.trangelier.hr.model.dto.RegionDto;
import dev.trangelier.hr.repository.RegionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RegionTest {

    Region region;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegionRepository regionRepository;

    @Test
    public void queryAll() throws Exception {
        Region r1 = new Region();
        r1.setId(nextLong());
        r1.setRegionName("Test 1");
        Region r2 = new Region();
        r2.setId(nextLong());
        r2.setRegionName("Test 2");
        Region r3 = new Region();
        r3.setId(nextLong());
        r3.setRegionName("Test 3");
        List<Region> regions = new ArrayList<>();
        regions.add(r1);
        regions.add(r2);
        regions.add(r3);

        when(regionRepository.queryAll()).thenReturn(regions);

        ResponseEntity<Region[]> responseEntity = restTemplate.exchange(controllerURL("/"), HttpMethod.GET, HttpEntity.EMPTY, Region[].class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(3, responseEntity.getBody().length);
        Assertions.assertEquals(Arrays.toString(regions.toArray()), Arrays.toString(responseEntity.getBody()));
    }

    @Test
    public void findById_Found() {
        Region r1 = new Region();
        r1.setId(nextLong());
        r1.setRegionName("Europe");

        when(regionRepository.findById(r1.getId())).thenReturn(Optional.of(r1));
        System.out.println(controllerURL(r1.getId().toString()));
        ResponseEntity<Region> regionResponseEntity = restTemplate.exchange(controllerURL("/" + r1.getId().toString()), HttpMethod.GET, HttpEntity.EMPTY, Region.class);

        Assertions.assertEquals(regionResponseEntity.getStatusCode(), HttpStatus.OK);
        Assertions.assertNotNull(regionResponseEntity.getBody());
        Region region = regionResponseEntity.getBody();
        Assertions.assertEquals(r1, region);
    }

    @Test
    public void findById_NotFound() {
        Region r1 = new Region();
        r1.setId(nextLong());
        when(regionRepository.findById(r1.getId())).thenReturn(Optional.empty());

        ResponseEntity<Map> responseEntity = restTemplate.exchange(controllerURL("/" + r1.getId().toString()), HttpMethod.GET, HttpEntity.EMPTY, Map.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertNotNull(responseEntity.getBody().get("message"));
    }

    @Test
    public void insert() {
        Region r1 = new Region();
        r1.setId(nextLong());
        r1.setRegionName("Test");
        when(regionRepository.insert(any(Region.class))).thenReturn(r1.getId());

        RegionDto regionDto = new RegionDto();
        regionDto.setRegionName("Test");
        HttpEntity<RegionDto> httpEntity = new HttpEntity<>(regionDto);
        ResponseEntity<CreatedRecord> responseEntity = restTemplate.exchange(controllerURL("/"), HttpMethod.POST, httpEntity, CreatedRecord.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);
        Assertions.assertNotNull(responseEntity.getBody());
        Assertions.assertEquals(r1.getId(), responseEntity.getBody().getId());
    }

    @Test
    public void update() {
        Region r1 = new Region();
        r1.setId(nextLong());
        r1.setRegionName("Test");
        when(regionRepository.update(r1)).thenReturn(1);

        RegionDto regionDto = new RegionDto();
        regionDto.setRegionName("Test");
        HttpEntity<RegionDto> httpEntity = new HttpEntity<>(regionDto);
        ResponseEntity<String> responseEntity = restTemplate.exchange(controllerURL("/" + r1.getId().toString()), HttpMethod.PUT, httpEntity, String.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
        Assertions.assertNull(responseEntity.getBody());
    }

    @Test
    public void delete() {
        Region r1 = new Region();
        r1.setId(nextLong());
        r1.setRegionName("Test");
        when(regionRepository.delete(r1.getId())).thenReturn(1);

        ResponseEntity<String> responseEntity = restTemplate.exchange(controllerURL("/" + r1.getId().toString()), HttpMethod.DELETE, HttpEntity.EMPTY, String.class);

        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.NO_CONTENT);
        Assertions.assertNull(responseEntity.getBody());
    }

    /**
     * Controller URL for this test suite
     *
     * @return http://localhost:port/hr/ControllerRequestMapping
     */
    private String controllerURL(String path) {
        return "http://localhost:" + port + "/hr/regions" + path;
    }

    /**
     * Returns a {@link Long} that is valid for {@link Region}
     *
     * @return threadsafe Long
     */
    private Long nextLong() {
        return ThreadLocalRandom.current().nextLong(0, 9999);
    }

}
