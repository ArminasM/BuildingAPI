package demo.services;

import demo.models.Building;
import demo.repositories.BuildingRepository;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;

import demo.models.TaxRates;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuildingServiceTest {

    @Autowired
    private BuildingService buildingService;

    @MockBean
    private BuildingRepository buildingRepository;

    @Test
    public void getTaxes() throws Exception {

        String owner = "Subject";

        Building house = new Building();
        house.setId(1);
        house.setAddress("Vilniaus g. 57");
        house.setOwner(owner);
        house.setPropertyType("house");
        house.setPropSize(40);
        house.setMarketValue(30000);

        Building apartment = new Building();
        apartment.setId(2);
        apartment.setAddress("Kauno g. 57");
        apartment.setOwner(owner);
        apartment.setPropertyType("apartment");
        apartment.setPropSize(25);
        apartment.setMarketValue(40000);

        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(house);
        buildings.add(apartment);

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

        float expected = house.getMarketValue() * houseRates.getTaxRate();
        expected += apartment.getMarketValue() * apartmentRates.getTaxRate();

        //Assume our database contains 2 buildings from our specified owner
        when(buildingRepository.findByOwner(any(String.class))).thenReturn(buildings);
        //Assert that our answer is the asserted value
        assertThat(buildingService.getTaxes(owner, rates)).isEqualTo(expected);

        buildings.clear();
        //Assume our database doesnt contain and our method return error code -1
        when(buildingRepository.findByOwner(any(String.class))).thenReturn(buildings);
        assertThat(buildingService.getTaxes(owner, rates)).isEqualTo(-1);
    }

    @Test
    public void findAll() throws Exception {
        Building building = new Building();
        building.setId(1);
        building.setAddress("Vilniaus g. 57");
        building.setOwner("Subject");
        building.setPropertyType("building");
        building.setPropSize(40);
        building.setMarketValue(30000);

        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(building);

        //Our database returns the list
        when(buildingRepository.findAll()).thenReturn(buildings);
        //Assert that it returns it
        assertThat(buildingService.getAllBuildings()).isEqualTo(buildings);
    }

    @Test
    public void findById() throws Exception {
        Building building = new Building();
        building.setId(1);
        building.setAddress("Vilniaus g. 57");
        building.setOwner("Subject");
        building.setPropertyType("building");
        building.setPropSize(40);
        building.setMarketValue(30000);

        Optional<Building> opBuilding = Optional.of(building);


        //Our database returns optional of found building
        when(buildingRepository.findById(any(Integer.class))).thenReturn(opBuilding);
        //Assert that it returns it
        assertThat(buildingService.getBuilding(1)).isEqualTo(opBuilding);
    }

    @Test
    public void addBuilding() throws Exception {
        Building building = new Building();

        building.setAddress("Vilniaus g. 57");
        building.setOwner("Subject");
        building.setPropertyType("building");
        building.setPropSize(40);
        building.setMarketValue(30000);

        //Lets simulate incrementation of the id in the database
        for(int i = 1; i <= 5; i++){
            building.setId(i);
            when(buildingRepository.save(any(Building.class))).thenReturn(building);
            //Assert that the creation is successful and our id is correct
            assertThat(buildingService.addBuilding(building)).isEqualTo(i);
        }

        //Assume the given building by the requester had an ID which is already occupied in the database
        when(buildingRepository.findById(any(Integer.class))).thenReturn(Optional.of(building));
        //Assert that the error code is sent
        assertThat(buildingService.addBuilding(building)).isEqualTo(-1);
    }

    @Test
    public void deleteBuilding() throws Exception {
        //If it does exist in the database
        when(buildingRepository.existsById(any(Integer.class))).thenReturn(true);
        //Assert that if has been deleted and we return a positive response
        assertThat(buildingService.deleteBuilding(1)).isEqualTo(true);

        //If it does NOT exist in the database
        when(buildingRepository.existsById(any(Integer.class))).thenReturn(false);
        //Assert that we return a negative response
        assertThat(buildingService.deleteBuilding(1)).isEqualTo(false);
    }

    @Test
    public void getBuildingByOwner() throws Exception {
        String owner = "Subject";
        Building building = new Building();
        building.setId(1);
        building.setAddress("Vilniaus g. 57");
        building.setOwner(owner);
        building.setPropertyType("building");
        building.setPropSize(40);
        building.setMarketValue(30000);

        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(building);

        //If 1 building exists which is owner by the given owner
        when(buildingRepository.findByOwner(any(String.class))).thenReturn(buildings);
        //Assert that we return it
        assertThat(buildingService.getBuildingsOwner(owner)).isEqualTo(buildings);

    }

    @Test
    public void updateBuilding() throws Exception {
        Building building = new Building();
        building.setId(1);
        building.setAddress("Vilniaus g. 57");
        building.setOwner("Subject");
        building.setPropertyType("building");
        building.setPropSize(40);
        building.setMarketValue(30000);


        //If teh building with the given id exists
        when(buildingRepository.existsById(any(Integer.class))).thenReturn(true);
        //Assert that we update it and return a positive response
        assertThat(buildingService.updateBuilding(building)).isEqualTo(true);

        //If teh building with the given id does NOT exist
        when(buildingRepository.existsById(any(Integer.class))).thenReturn(false);
        //Assert that we return a negative response
        assertThat(buildingService.updateBuilding(building)).isEqualTo(false);

    }

}