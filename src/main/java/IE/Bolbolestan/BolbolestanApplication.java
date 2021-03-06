package IE.Bolbolestan;

import IE.Bolbolestan.offering.OfferingRepository;
import IE.Bolbolestan.offeringRecord.OfferingRecordRepository;
import IE.Bolbolestan.student.StudentRepository;
import IE.Bolbolestan.waitList.CheckListJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BolbolestanApplication {

	public static void main(String[] args) {
		SpringApplication.run(BolbolestanApplication.class, args);
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		fillStorages();
		scheduler.scheduleAtFixedRate(new CheckListJob(), 0, 15, TimeUnit.MINUTES);
	}

	public static void fillStorages() {
		try {
			OfferingRepository.getDataFromApi();
			StudentRepository.getDataFromApi();
			OfferingRecordRepository.getDataFromApi();
		} catch (SQLException throwables) {
			throwables.printStackTrace();

		}
	}

}
