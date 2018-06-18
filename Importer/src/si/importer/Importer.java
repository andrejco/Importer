package si.importer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import si.importer.model.FileData;
import si.importer.utils.TimeWatch;

public class Importer {

	public static void main(String[] args) {

		TimeWatch timeWatch = TimeWatch.start();
		try {
			// First prepare database stuff

			InsertDB insertDB = new InsertDB();
			// Try to clean database before new import
			try {
				insertDB.dropTabel();
			} catch (SQLException e) {
				// Table does not exists yet.
			}
			insertDB.createTable();

			// Read and import to DB
			ReadFile readFile;
			if (args.length < 1) {
				readFile = new ReadFile("fo_random.txt");
			} else {
				readFile = new ReadFile(args[0]);
			}

			System.out.println("Reading file time(ms): " + timeWatch.time(TimeUnit.MILLISECONDS));

			timeWatch = TimeWatch.start();
			ArrayList<FileData> dataList = readFile.getData();

			Collections.sort(dataList);
			System.out.println("Sorting array time(ms): " + timeWatch.time(TimeUnit.MILLISECONDS));

			timeWatch = TimeWatch.start();
			insertDB.batch(dataList);
			System.out.println("Inserting array to DB time(ms): " + timeWatch.time(TimeUnit.MILLISECONDS));
			
			Date minDate = insertDB.getMinDateInsert();
			System.out.println("Min date insert: " + minDate);
			
			Date maxDate = insertDB.getMaxDateInsert();
			System.out.println("Max date insert: " + maxDate);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
