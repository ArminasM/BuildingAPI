package demo.services;

import java.util.*;

import demo.models.Building;
import demo.models.TaxRates;

import demo.repositories.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuildingService
{
    @Autowired
    BuildingRepository buildingRepository;
    //getting all building records
    public List<Building> getAllBuildings()
    {
        return new ArrayList<>(buildingRepository.findAll());
    }

    //Get a single record
    public Optional<Building> getBuilding(int id){
        return buildingRepository.findById(id);
    }

    //Add a building to the record list
    public int addBuilding(Building building) {
        if(buildingRepository.findById(building.getId()).isPresent()){
            return -1;
        }
        else{
            return buildingRepository.save(building).getId();
        }

    }

    //Deleting a specific record
    public boolean deleteBuilding(int id) {
        if(buildingRepository.existsById(id)){
            buildingRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //Updating a single record
    public boolean updateBuilding(Building building) {
        if(buildingRepository.existsById(building.getId())){
            buildingRepository.save(building);
            return true;
        }
        return false;
    }

    //Getting all buildings that are owned by a given owner
    public List<Building> getBuildingsOwner(String owner) {
        return buildingRepository.findByOwner(owner);
    }

    //Calculate taxes for the given owner's properties
    public float getTaxes(String owner, List<TaxRates> taxRates) throws Exception{
        List<Building> buildings = buildingRepository.findByOwner(owner);
        if(buildings.isEmpty()) return -1;
        float taxesTotal = 0.0f;
        float taxRate = 0;
        for (Building b: buildings) {
            //Take type of building
            String type = b.getPropertyType();
            //Has the taxRate changed?
            boolean isChanged=false;

            //look trough the given tax rate list
            for (TaxRates tax: taxRates) {
                //If property type matches the given taxRate list, assign the new taxRate
                if(tax.getType().equals(type)){
                    taxRate = tax.getTaxRate();
                    isChanged = true;
                }
                else if(!isChanged){
                    //If tax rate for a specific building type is not given, the tax is not included
                    taxRate = 0;
                }
            }

            taxesTotal += b.getMarketValue()*taxRate;
        }
        return taxesTotal;
    }
}