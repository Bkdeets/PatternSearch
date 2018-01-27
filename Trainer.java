
import java.util.ArrayList;
import java.util.List;


/**
 * Trader is a class that is a trading strategy. It executes trades based on
 * historical probabilities of the next candle being up compared to a pattern of
 * candles of length n directly preceding the resulting candle.
 * 
 * @author Britton Deets 
 *
 */
public class Trainer {

	/**
	 * Properties of the trainer class
	 */
	private ArrayList<Candle> candleList = new ArrayList<Candle>();
	private ArrayList<Pattern> patternList = new ArrayList<Pattern>();


	/**
	 * Main function
	 * 
	 * @param args
	 */
//	public static void main(String[] args) {
//		
//		String trainerFile = "Resources/AAPL5Yr.csv";
//		Trainer trainer = new Trainer(trainerFile);
//		HistPriceReader hpr = new HistPriceReader();
//		
//		
//		trainer.setCandleList(hpr.reader(trainerFile));
//	
//
//		int[] variables = { 1, 0 };
//		System.out.println("{1,0}, 2 candles");
//		trainer.runAll(5, variables);
//
//		for (Pattern p : trainer.patternList) {
//			System.out.println(p);
//		}
//		
//		
//	}

	
	
	/**
	 * Constructor does nothing
	 */
	public Trainer(String trainerFile, int number, int[] variables) {
		HistPriceReader hpr = new HistPriceReader();
		setCandleList(hpr.reader(trainerFile));
		runAll(number, variables);
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
	
	
	public void analyzePattern(int[] pattern, int number) {

		double total = 0.0;
		double totalUp = 0.0;
		double avgChgUp = 0.0;
		double avgChgDn = 0.0;
		
		for (int k = 0; k < getCandleList().size(); k++) {

			int toGet = k + number + 1;
			if (toGet  >= getCandleList().size()) {
				break;
			} else {
				Candle resulting = getCandleList().get(toGet);
				List<Candle> cTable = getCandleList().subList(k, toGet-1);

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
	
	public ArrayList<Pattern> getPatternList(){
		return patternList;
	}



	public ArrayList<Candle> getCandleList() {
		return candleList;
	}



	public void setCandleList(ArrayList<Candle> candleList) {
		this.candleList = candleList;
	}
	


}
