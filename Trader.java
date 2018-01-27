
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Trader is a class that is a trading strategy. It executes trades based on
 * historical probabilities of the next candle being up compared to a pattern of
 * candles of length n directly preceding the resulting candle.
 * 
 * @author Britton Deets
 *
 */
public class Trader {

	/**
	 * Properties of the trader class
	 */
	private ArrayList<Candle> candleList = new ArrayList<Candle>();

	private ArrayList<Pattern> patternList = new ArrayList<Pattern>();

	// private ArrayList<int[]> tables = new ArrayList<int[]>();

	/**
	 * Main function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Trader trainer = new Trader();

		String trainerFile = "Resources/AAPL5Yr.csv";
		trainer.candleList = trainer.reader(trainerFile);

		int[] variables = { 1, 0 };
		System.out.println("{1,0}, 2 candles");
		trainer.runAll(2, variables);

		for (Pattern p : trainer.patternList) {
			System.out.println(p);
		}
		
		
		
		Trader backtester = new Trader();
		
		String backtestFile = "Resources/AAPl1Yr.csv";
		backtester.candleList = backtester.reader(backtestFile);
		
		

	}

	/**
	 * Constructor does nothing
	 */
	public Trader() {

	}

	/**
	 * Reads in csv of stock candles
	 * 
	 * @return candleList
	 */
	public ArrayList<Candle> reader(String csvFile) {

		String line = "";
		File file = new File(csvFile);

		Scanner input = null;
		try {
			input = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
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

	public void analyzePattern(int[] pattern, int number) {

		double total = 0.0;
		double totalUp = 0.0;
		double avgChgUp = 0.0;
		double avgChgDn = 0.0;
		
		for (int k = 0; k < candleList.size(); k++) {

			int toGet = k + number + 1;
			if (toGet  >= candleList.size()) {
				break;
			} else {
				Candle resulting = candleList.get(toGet);
				List<Candle> cTable = candleList.subList(k, toGet-1);

				int i = 0;
				Candle[] cTableA = new Candle[number];
				for (Candle c : cTable) {
					cTableA[i] = c;
					i++;
				}
				if (tableChecker(cTableA, pattern)) {
					total += 1.0;

					if (resulting.upDown() == 1) {
						totalUp += 1.0;
						double pctChgUp = (resulting.getClose() - resulting.getOpen()) / resulting.getOpen();
						if (avgChgUp == 0) {
							avgChgUp = pctChgUp;
						} else {
							avgChgDn = (avgChgUp + pctChgUp) / 2;
						}

					} else {
						double pctChgDn = (resulting.getClose() - resulting.getOpen()) / resulting.getOpen();
						if (avgChgUp == 0) {
							avgChgUp = pctChgDn;
						} else {
							avgChgDn = (avgChgUp + pctChgDn) / 2;
						}
					}
				}
			}
		}

		Pattern p = new Pattern(pattern, totalUp, total, avgChgUp, avgChgDn);
		patternList.add(p);
	}

	/**
	 * Analyzes a certain pattern in structuredList accepting a pattern of 1 or 0
	 * for either i or j
	 * 
	 * @param number
	 * @param variables
	 */
	public void runAll(int number, int[] variables) {
		TTable perms = new TTable(number, variables);
		ArrayList<int[]> tables = perms.getTables();
		for (int j = 0; j < tables.size(); j++) {
			int[] datums = tables.get(j);
			analyzePattern(datums, number);

		}
	}

	public boolean tableChecker(Candle[] testCandles, int[] basePerm) {

		for (int i = 0; i < testCandles.length; i++) {
			if (testCandles[i].upDown() != basePerm[i]) {
				return false;
			}
		}
		return true;
	}

}

