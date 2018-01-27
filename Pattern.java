/**
 * @author Britton Deets
 * 
 * Pattern class contains a pattern of integers which represent a value of price change from previous time period.
 * 
 */
public class Pattern {
	private int[] pattern;
	private double totalUp;
	private double totalOccurred;
	private double avgChgUp;
	private double avgChgDn;
	private double expReturn;

	
	
	/**
	 * @param pattern
	 * @param totalUp
	 * @param totalOccurred
	 * @param avgPctChange
	 * 
	 */
	public Pattern(int[] pattern, double totalUp, double totalOccurred, double avgChgUp, double avgChgDn) {
		this.pattern = pattern;
		this.totalUp = totalUp;
		this.totalOccurred = totalOccurred;
		this.setAvgChgUp(avgChgUp);
		this.setAvgChgDn(avgChgDn);
		this.setExpReturn();
	}
	
	public double percentUp() {
		return totalUp/totalOccurred;
	}
	
	public String toString() {
		String patternStr = "";
		for(int i: pattern) {
			String s = String.valueOf(i);
			patternStr += s;
		}
		return patternStr + " - %Up: " + percentUp()*100 + " tUp: " + totalUp + " tOcc: " + totalOccurred + " expRet%: " + expReturn*100;
	}
	
	public void setExpReturn() {
		expReturn =  (percentUp()*avgChgUp) + ((1-percentUp()) * avgChgDn);
	}
	
	//Getters and Setters -------------------------------- Begin
	public int[] getPattern() {
		return pattern;
	}

	public double getTotalUp() {
		return totalUp;
	}

	public void setTotalUp(double totalUp) {
		this.totalUp = totalUp;
	}

	public double getTotalOccurred() {
		return totalOccurred;
	}

	public void setTotalOccurred(double totalOccurred) {
		this.totalOccurred = totalOccurred;
	}

	public double getAvgChgUp() {
		return avgChgUp;
	}

	public void setAvgChgUp(double avgChgUp) {
		this.avgChgUp = avgChgUp;
	}

	public double getAvgChgDn() {
		return avgChgDn;
	}

	public void setAvgChgDn(double avgChgDn) {
		this.avgChgDn = avgChgDn;
	}

	public double getExpReturn() {
		return expReturn;
	}

	//Getters and Setters ------------------------------ End
	
}
