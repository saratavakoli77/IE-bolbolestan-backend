package IE.Bolbolestan;

import IE.Bolbolestan.offering.OfferingStorage;
import IE.Bolbolestan.offeringRecord.OfferingRecordStorage;
import IE.Bolbolestan.student.StudentStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BolbolestanApplication {

	public static void main(String[] args) {
		SpringApplication.run(BolbolestanApplication.class, args);
		fillStorages();
	}

	public static void fillStorages() {
		OfferingStorage.getDataFromApi();
		StudentStorage.getDataFromApi();
		OfferingRecordStorage.getDataFromApi();
	}

}
