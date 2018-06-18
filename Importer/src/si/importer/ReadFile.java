package si.importer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import si.importer.model.FileData;
import si.importer.utils.Utils;

/**
 * Reading file content to java objects.
 * 
 * @author Andrej Kogovsek
 *
 */
public class ReadFile {
	
	private ArrayList<FileData> data = new ArrayList<>();

	/**
	 * Constructor
	 * 
	 * @param path
	 * @throws FileNotFoundException
	 */
	public ReadFile(String path) throws FileNotFoundException {

		File file = new File(path);

		Scanner scanner = new Scanner(file);
		
		int lineCounter = 0;

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			
			lineCounter++;
			// Skip first line, because it contains header.
			if(lineCounter == 1) {
				continue;
			}
			
			
			
			String[] splitedLine = line.split("\\|");
			
			FileData fileData = new FileData();
			
			long matchId = Long.parseLong(Utils.extractDigits(splitedLine[0]));
			fileData.setMatchId(matchId);
			
			long marketId = Long.parseLong(splitedLine[1]);
			fileData.setMarketId(marketId);
			
			long outcomeId = Long.parseLong(Utils.extractDigits(splitedLine[2]));
			fileData.setOutcomeId(outcomeId);
			
			data.add(fileData);
		}
		
		
		scanner.close();

	}

	/**
	 * 
	 * @return ArrayList with FileData objects.
	 */
	public ArrayList<FileData> getData() {
		return data;
	}

}
