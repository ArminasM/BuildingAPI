package demo.controllers;

import demo.services.BuildingService;
import demo.models.Building;
import demo.models.TaxRates;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/buildings")
public class RequestController {

    @Autowired
    BuildingService buildingService;

    //Listing all buildings
    @GetMapping()
    public ResponseEntity<List<Building>> buildings() {
        return ResponseEntity.ok().body(buildingService.getAllBuildings());
       //return buildingService.getAllBuildings();
    }

    //Find building by id
    @GetMapping("/{id}")
    public ResponseEntity<Building> building(@PathVariable("id") int id){
        return ResponseEntity.of(buildingService.getBuilding(id));
    }

    //Find buildings by owner
    @GetMapping("/owner/{owner}")
    public ResponseEntity<List<Building>> buildingOwner(@PathVariable("owner") String owner){
        List<Building> bList = buildingService.getBuildingsOwner(owner);
        if(bList.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        else{
            return ResponseEntity.ok().body(bList);
        }
    }

    //creating a delete mapping that deletes a specific building
    @DeleteMapping("/{id}")
    public ResponseEntity<Building> deleteBuilding(@PathVariable("id") int id)
    {
        if(buildingService.deleteBuilding(id)){
            return ResponseEntity.ok().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    //creating a put mapping that saves a specific building
    @PostMapping()
    public ResponseEntity<Building> addBuilding(@Validated @RequestBody Building building)
    {
        //If we return -1, the given id already exists in the database, so we do not create it
        int ans = buildingService.addBuilding(building);
        if(ans != -1){
            //If we successfully created the building, we assign the id to the input and display it back with the id assigned inside the repository
            building.setId(ans);
            return ResponseEntity.ok().body(building);
        }
        //Already exists
        return ResponseEntity.status(409).build();
    }

    //creating a put mapping that updates a specific building
    @PutMapping()
    public ResponseEntity<Building> updateBuilding(@RequestBody Building building)
    {
        if(buildingService.updateBuilding(building)){
            return ResponseEntity.ok().body(building);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    //creating a get mapping to get owners year taxes for all his properties
    @PostMapping ("/taxes/{owner}")
    public ResponseEntity<Float> getTaxes(@PathVariable("owner") String owner,@Validated @RequestBody List<TaxRates> taxes) throws Exception
    {
        float taxesTotal = 0;
        try{
            taxesTotal = buildingService.getTaxes(owner,taxes);
        }catch(Exception e){
            return ResponseEntity.status(422).build();
        }

        if(taxesTotal != -1) {
            return ResponseEntity.ok().body(taxesTotal);
        }
        else{
            return ResponseEntity.notFound().build();
        }

    }

}