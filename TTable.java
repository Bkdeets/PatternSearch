import java.util.ArrayList;

class TTable {
	private ArrayList<int[]> tables;
	private int[] table;

	/**
	 * Constructor calls createTables which updates the value for tables
	 * 
	 * @param number
	 * @param variables
	 */
	public TTable(int number, int[] variables) {
		table = new int[number];
		tables = new ArrayList<int[]>();
		createTables(number, variables);
	}

	/**
	 * Creates an ArrayList of int[] based on variables passed and number of
	 * candles(length of pattern)
	 * 
	 * @param number
	 * @param variables
	 */
	public void createTables(int number, int[] variables) {

		ArrayList<int[]> tbl = new ArrayList<int[]>();

		// initialize t
		int[] t = new int[number];
		for (int i = 0; i < t.length; i++) {
			t[i] = variables[0];
		}
		tbl.add(t);

		int lenPossSet = (int) Math.pow((double) variables.length, (double) number);
		int n = number - 1;
		int[] tb = new int[number];
		while (tbl.size() < lenPossSet) {

			// reset tb if n == number-1
			if (n == number - 1) {
				tb = new int[number];

				// tb = last table in tbl
				for (int i = 0; i < tbl.get(tbl.size() - 1).length; i++) {
					tb[i] = tbl.get(tbl.size() - 1)[i];
				}
			}

			if (isIn(tb, tbl)) {

				int searchVariable = tb[n];
				int varIndex = getIndex(variables, searchVariable);

				if (varIndex == variables.length - 1) {

					tb[n] = variables[varIndex - 1];
				} else {

					tb[n] = variables[varIndex + 1];
				}
				n--;
			}

			if (isIn(tb, tbl) == false) {

				tbl.add(tb);
				n = number - 1;
			}

		}
		tables = tbl;

	}

	public ArrayList<int[]> getTables() {
		return tables;
	}// getTables

	public boolean tableChecker(int[] t1, int[] t2) {

		for (int i = 0; i < t1.length; i++) {
			if (t1[i] != t2[i]) {
				return false;
			}
		}
		return true;
	}

	public boolean isIn(int[] t, ArrayList<int[]> tbl) {

		for (int i = 0; i < tbl.size(); i++) {
			if (tableChecker(tbl.get(i), t)) {
				return true;
			}
		}

		return false;

	}

	public int getIndex(int[] variables, int value) {

		for (int i = 0; i < variables.length; i++) {
			if (variables[i] == value) {
				return i;
			}
		}
		return 0;
	}

}// class



