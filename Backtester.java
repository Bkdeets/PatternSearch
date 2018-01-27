import java.util.ArrayList;
import java.util.List;

public class Backtester {

	private ArrayList<Pattern> patternList = new ArrayList<Pattern>();
	private ArrayList<Candle> candleList = new ArrayList<Candle>();
	
	private double acctAmt = 100000;
	
	public static void main(String[] args) {
		
		String backtestFile = "Resources/Stocks/ODP1.csv";
		String trainerFile = "Resources/Stocks/ODP5.csv";
		int number = 5;
		int[] variables = {1,0};
	
		Backtester backtester = new Backtester(backtestFile, trainerFile, number, variables);
		
//		for(Pattern p: backtester.patternList) {
//			System.out.println(p);
//		}
		System.out.println(backtester.acctAmt);
		backtester.tradePatterns(number);
		
	}

	

	public Backtester(String backtestFile, String trainerFile, int number, int[] variables) {
		
		Trainer trainer = new Trainer(trainerFile, number, variables);

		//initializing backtest candles
		//set candleList from backtest file and patterns from trainer 
		HistPriceReader hpr = new HistPriceReader();
		this.candleList = hpr.reader(backtestFile);
		this.patternList = trainer.getPatternList();
				
	}
	
	
	public double longPosition(Candle c) {

		double commission = 5;
		
		double entryPrice = c.getOpen();	
		int shares = (int) (acctAmt/entryPrice);
		double entryValue = shares*entryPrice;
		
		double fivePctLowPrice = entryPrice*.90;
		
		double exitPrice = c.getClose();
		double exitValue = shares * exitPrice;
		
		double profitPct = ((exitPrice-entryPrice)/entryPrice) *100;
		
		if(c.getLow()<fivePctLowPrice) {
			exitValue = fivePctLowPrice*shares;
			profitPct = -10;
		}
		
		
		
		acctAmt = acctAmt + (exitValue - entryValue);
		acctAmt -= commission*2;
		
		
		//System.out.println("Long Trade - Account Amount: " + acctAmt + " Percent Profit: " + profitPct);
		System.out.println(acctAmt);
		return profitPct;
		
		
	}
	
	public double shortPosition(Candle c) {
		
		double commission = 5;
		
		double entryPrice = c.getOpen();	
		int shares = (int) (acctAmt/entryPrice);
		double entryValue = shares*entryPrice;
		
		double fivePctHighPrice = entryPrice*1.10;
		
		double exitPrice = c.getClose();
		double exitValue = shares * exitPrice;
		
		
		double profitPct = (entryPrice-exitPrice)/exitPrice;
		
		if(c.getHigh()>fivePctHighPrice) {
			exitValue = fivePctHighPrice*shares;
			profitPct = -10;
		}
		
		
		acctAmt+= entryValue - exitValue;
		acctAmt -= commission*2;
		//System.out.println("Short Trade - Account Amount: " + acctAmt + "Percent Profit: " + profitPct);
		System.out.println(acctAmt);
		
		return profitPct;
	}
	
	
	public void tradePatterns(int number) {

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
				
				for(Pattern p: patternList) {
					if (tableChecker(cTableA, p.getPattern())) {
	
						if (p.getExpReturn() > 0) {
							longPosition(resulting);
							updatePattern(cTableA,resulting);
	
						} else {
							shortPosition(resulting);
							updatePattern(cTableA,resulting);
						}
					}
					else {
						continue;
					}
				}
			}
			}
		}


	private boolean tableChecker(Candle[] testCandles, int[] basePerm) {
		
		for (int i = 0; i < testCandles.length; i++) {
			if (testCandles[i].upDown() != basePerm[i]) {
				return false;
			}
		}
		return true;
	}


	private ArrayList<Candle> getCandleList() {
		// TODO Auto-generated method stub
		return candleList;
	}

}