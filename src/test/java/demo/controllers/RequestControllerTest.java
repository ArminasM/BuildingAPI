package demo.controllers;

import demo.models.Building;
import demo.models.TaxRates;

import java.util.Optional;
import java.util.ArrayList;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import demo.services.BuildingService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(RequestController.class)
public class RequestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BuildingService buildingService;

    @Test
    public void getAll() throws Exception {

        Building building = new Building();
        building.setId(1);
        building.setAddress("Vilniaus g. 57");
        building.setOwner("Liudas");
        building.setPropertyType("house");
        building.setPropSize(50);
        building.setMarketValue(30000);

        Building building1 = new Building();
        building1.setId(2);
        building1.setAddress("Kauno g. 57");
        building1.setOwner("Vasaris");
        building1.setPropertyType("apartment");
        building1.setPropSize(50);
        building1.setMarketValue(40000);

        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(building);
        buildings.add(building1);

        //Check empty
        this.mvc.perform(get("/buildings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        when(buildingService.getAllBuildings()).thenReturn(buildings);

        //Check with 2 items
        mvc.perform(get("/buildings"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].owner", is("Liudas")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].owner", is("Vasaris")))
                .andExpect(status().isOk());
    }

    @Test
    public void addBuilding() throws Exception {

        //The body inside POST request
        Building building = new Building();
        building.setAddress("Kauno g. 57");
        building.setOwner("Liudas");
        building.setPropertyType("house");
        building.setPropSize(50);
        building.setMarketValue(40000);

        //Assume service creates the record with id 1
        when(buildingService.addBuilding(any(Building.class))).thenReturn(1);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(building);

        MvcResult result = mvc.perform(post("/buildings/")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        //We assume the input had an id = 1 parameter, but the id was already occupied in the database
        building.setId(1);
        json = mapper.writeValueAsString(building);
        assertThat(result.getResponse().getContentAsString()).isEqualTo(json);

        //Assume the input included an id which is already in the database
        when(buildingService.addBuilding(any(Building.class))).thenReturn(-1);

        mvc.perform(post("/buildings/")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void updateBuilding() throws Exception {

        //The body inside POST request
        Building building = new Building();
        building.setId(1);
        building.setAddress("Kauno g. 57");
        building.setOwner("Liudas");
        building.setPropertyType("house");
        building.setPropSize(50);
        building.setMarketValue(40000);

        //Assume service updates the record with the given info and return positive response
        when(buildingService.updateBuilding(any(Building.class))).thenReturn(true);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(building);

        //Request with id = 1
        MvcResult result = mvc.perform(put("/buildings")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo(json);

        //Assume the service didn't find a building with a given id
        when(buildingService.updateBuilding(any(Building.class))).thenReturn(false);

        mvc.perform(put("/buildings")
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBuildingById() throws Exception {

        Building building1 = new Building();
        building1.setId(2);
        building1.setAddress("Kauno g. 57");
        building1.setOwner("Liudas");
        building1.setPropertyType("house");
        building1.setPropSize(50);
        building1.setMarketValue(40000);

        Optional<Building> opBuilding = Optional.of(building1);

        when(buildingService.getBuilding(2)).thenReturn(opBuilding);

        mvc.perform(get("/buildings/" + building1.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(2)));
    }

    @Test
    public void deleteBuilding() throws Exception {
        when(buildingService.deleteBuilding(1)).thenReturn(true);

        mvc.perform(delete("/buildings/" + 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

        //Assume service returns false (Didn't find the record with ID 1)
        when(buildingService.deleteBuilding(1)).thenReturn(false);

        mvc.perform(delete("/buildings/" + 1)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getBuildingByOwner() throws Exception {
        String owner = "Subject";

        Building building = new Building();
        building.setId(1);
        building.setAddress("Vilniaus g. 57");
        building.setOwner("Viktoras");
        building.setPropertyType("apartment");
        building.setPropSize(40);
        building.setMarketValue(30000);

        Building building1 = new Building();
        building1.setId(2);
        building1.setAddress("Kauno g. 57");
        building1.setOwner(owner);
        building1.setPropertyType("house");
        building1.setPropSize(50);
        building1.setMarketValue(40000);

        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(building1);

        when(buildingService.getBuildingsOwner(owner)).thenReturn(buildings);

        mvc.perform(get("/buildings/owner/" + building1.getOwner())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$..owner",Matchers.everyItem(is(owner))) );

        buildings.remove(building1);
        mvc.perform(get("/buildings/owner/" + building1.getOwner())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    public void getOwnerTaxes() throws Exception {
        String owner = "Subject";

        //Create dummy tax list
        ArrayList<TaxRates> rates = new ArrayList<>();
        TaxRates apartmentRates = new TaxRates();
        apartmentRates.setTaxRate(1.0f);
        apartmentRates.setType("apartment");

        TaxRates houseRates = new TaxRates();
        houseRates.setTaxRate(0.6f);
        houseRates.setType("house");

        //Fill rates list
        rates.add(apartmentRates);
        rates.add(houseRates);
        //Convert the list to json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(rates);


        //With the given input our service should return 14000.0 (10000*0.6 + 8000*1) (e.g. house marketValue is 10000 and apartment is 8000)
        //Cant use any(ArrayList.class or List.class), because it is a raw type and may produce undefined behavior
        when(buildingService.getTaxes(any(String.class), any())).thenReturn((float)14000);

        MvcResult result = mvc.perform(post("/buildings/taxes/" + owner)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("14000.0");

        //Assume the owner has no buildings in the database, the service returns error code -1
        when(buildingService.getTaxes(any(String.class), any())).thenReturn((float)-1);

        mvc.perform(post("/buildings/taxes/" + owner)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());

        //Assume the input was wrong and the service threw an exception
        when(buildingService.getTaxes(any(String.class), any())).thenThrow(new Exception());

        mvc.perform(post("/buildings/taxes/" + owner)
                .contentType(APPLICATION_JSON)
                .content(json))
                .andExpect(status().is4xxClientError());
    }

}