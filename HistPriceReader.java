import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class HistPriceReader {

	public HistPriceReader() {
	
	}

	public ArrayList<Candle> reader(String csvFile) {

		ArrayList<Candle> candleList = new ArrayList<Candle>();

		String line;
		File file = new File(csvFile);

		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;

		// Skip first line
		line = input.nextLine();

		// Skip second Line
		line = input.nextLine();

		while ((input.hasNext())) {
			line = input.nextLine();
			i += 1;

			// use comma as separator
			String[] candle = line.split(",");

			String date = candle[0];
			Double close = Double.parseDouble(candle[1]);
			int volume = Integer.parseInt(candle[2]);
			Double open = Double.parseDouble(candle[3]);
			Double high = Double.parseDouble(candle[4]);
			Double low = Double.parseDouble(candle[5]);

			Candle c = new Candle(open, high, low, close, volume, date);

			candleList.add(i - 1, c);
		}

		return candleList;

	}
}
