package demo.repositories;

import demo.models.Building;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BuildingRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private BuildingRepository buildingRepository;

	@Test
	public void whenFindAll() {

		Building firstBuilding = new Building();
		firstBuilding.setAddress("Vilniaus g. 57");
		firstBuilding.setOwner("Liudas");
		firstBuilding.setPropertyType("house");
		firstBuilding.setPropSize(50);
		firstBuilding.setMarketValue(30000);

		//Save the entity
		entityManager.persist(firstBuilding);

		Building secondBuilding = new Building();
		secondBuilding.setAddress("Kauno g. 12");
		secondBuilding.setOwner("Vasaris");
		secondBuilding.setPropertyType("apartment");
		secondBuilding.setPropSize(30);
		secondBuilding.setMarketValue(7000);

		entityManager.persist(secondBuilding);
		//Run the query on the database
		entityManager.flush();

		List<Building> buildings = buildingRepository.findAll();

		assertThat(buildings.size()).isEqualTo(2);
		assertThat(buildings.get(0)).isEqualTo(firstBuilding);
		assertThat(buildings.get(1)).isEqualTo(secondBuilding);
	}

	@Test
	public void whenFindByOwner() {

		Building firstBuilding = new Building();
		firstBuilding.setAddress("Vilniaus g. 57");
		firstBuilding.setOwner("Liudas");
		firstBuilding.setPropertyType("house");
		firstBuilding.setPropSize(50);
		firstBuilding.setMarketValue(30000);
		//Save the entity
		entityManager.persist(firstBuilding);
		entityManager.flush();


		Building secondBuilding = new Building();
		secondBuilding.setAddress("Kauno g. 12");
		secondBuilding.setOwner("Vasaris");
		secondBuilding.setPropertyType("apartment");
		secondBuilding.setPropSize(30);
		secondBuilding.setMarketValue(7000);

		entityManager.persist(secondBuilding);
		//Run the query on the database
		entityManager.flush();

		Building thirdBuilding = new Building();
		thirdBuilding.setAddress("Jurbarko g. 57");
		thirdBuilding.setOwner("Liudas");
		thirdBuilding.setPropertyType("house");
		thirdBuilding.setPropSize(40);
		thirdBuilding.setMarketValue(40000);
		//Save the entity
		entityManager.persist(thirdBuilding);
		entityManager.flush();

		List<Building> buildings = buildingRepository.findByOwner(firstBuilding.getOwner());

		assertThat(buildings.size()).isEqualTo(2);
		assertThat(buildings.get(0)).isEqualTo(firstBuilding);
		assertThat(buildings.get(1)).isEqualTo(thirdBuilding);
	}

}
