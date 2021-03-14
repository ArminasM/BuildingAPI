package demo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import demo.models.Building;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import demo.models.TaxRates;
import demo.repositories.BuildingRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BuildingServiceTest {

    @Mock
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingService buildingService;


   @Test
   public void getAllBuildings(){
   }

    @Test
    public void getTaxes() throws Exception {
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
        building1.setOwner("Subject");
        building1.setPropertyType("house");
        building1.setPropSize(50);
        building1.setMarketValue(40000);

        ArrayList<Building> buildings = new ArrayList<>();
        buildings.add(building);
        buildings.add(building1);

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

        //when(buildingRepository.findByOwner(any(String.class))).thenReturn(buildings);

        //when(buildingRepository.findByOwner(owner)).thenReturn(buildings);

        //assertThat(buildingService.getTaxes(owner, rates)).isEqualTo(14000f);


    }

}