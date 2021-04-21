package IE.Bolbolestan;

import IE.Bolbolestan.offering.OfferingEntity;
import IE.Bolbolestan.offering.OfferingStorage;
import IE.Bolbolestan.offeringRecord.OfferingRecordStorage;
import IE.Bolbolestan.student.StudentStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class BolbolestanApplication {

	public static void main(String[] args) {
		SpringApplication.run(BolbolestanApplication.class, args);
		fillStorages();

		for (OfferingEntity offeringEntity: OfferingStorage.getAllOfferings()) {
			offeringEntity.setPrerequisites(new ArrayList<>());
			offeringEntity.setCapacity(2);
		}
	}

	public static void fillStorages() {
		OfferingStorage.getDataFromApi();
		StudentStorage.getDataFromApi();
		OfferingRecordStorage.getDataFromApi();
	}

}
