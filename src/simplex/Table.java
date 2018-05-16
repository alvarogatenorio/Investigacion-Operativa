package simplex;

import java.util.ArrayList;
import java.util.List;

public class Table {
	private int variables;
	private int restrictions;
	private List<List<Fraction>> table;

	private List<Integer> negativeReducedCosts;
	private int minimumReducedCostIndex;

	/* Constructs a table from the matrix */
	public Table(List<List<Fraction>> table) {
		this.table = table;
		/* Excluding the last column (independent vector) */
		this.variables = table.get(0).size() - 1;
		/* Excluding the last row (reduced costs vector) */
		this.restrictions = table.size() - 1;
	}

	/* Pivot the table taking as pivot the specified row and column */
	public void pivot(int row, int column) throws IllegalArgumentException {
		/* Re-initializing stuff */
		this.minimumReducedCostIndex = -1;
		this.negativeReducedCosts = new ArrayList<Integer>();
		/* If the pivot is in an invalid position, throws an exception */
		if ((row < 0) || (row > restrictions - 1) || (column < 0) || (column > variables - 1)) {
			throw new IllegalArgumentException("You can't take (" + row + "," + column + ") as a pivot!");
		} else {
			/* Saving the pivoting element */
			Fraction pivotElement = table.get(row).get(column);
			/* First we normalize the pivot row */
			for (int j = 0; j < variables + 1; j++) {
				Fraction newValue = Utils.divide(table.get(row).get(j), pivotElement);
				table.get(row).set(j, newValue);
				table.set(row, table.get(row));
			}
			/* Finally we pivot the rest of the table */
			for (int i = 0; i < row; i++) {
				pivotRow(i, row, column);
			}
			for (int i = row + 1; i < restrictions + 1; i++) {
				pivotRow(i, row, column);
			}
		}
	}

	/* Pivots a row which doesn't equals row */
	private void pivotRow(int i, int row, int column) {
		Fraction newValue;
		Fraction factor = table.get(i).get(column);
		for (int j = 0; j < variables + 1; j++) {
			newValue = Utils.subtract(table.get(i).get(j), Utils.multiply(table.get(row).get(j), factor));
			table.get(i).set(j, newValue);
			table.set(i, table.get(i));
			Fraction aux = table.get(i).get(j);
			/* Updating the reduced costs index list and the minimum reduced cost index */
			if (i == restrictions && (aux.getNumerator() < 0)) {
				negativeReducedCosts.add(j);
				if ((minimumReducedCostIndex == -1) || Utils
						.subtract(aux, table.get(restrictions).get(minimumReducedCostIndex)).getNumerator() < 0) {
					minimumReducedCostIndex = j;
				}
			}
		}
	}

	public List<Integer> getNegativeReducedCosts() {
		return negativeReducedCosts;
	}

	public int getMinimumReducedCostIndex() {
		return minimumReducedCostIndex;
	}

	public List<List<Fraction>> getRestrictionsMatrix() {
		List<List<Fraction>> restrictionsMatrix = new ArrayList<List<Fraction>>(table);
		restrictionsMatrix.remove(restrictions);
		return restrictionsMatrix;
	}

	public List<Fraction> getReducedCosts() {
		return this.table.get(restrictions);
	}

	public String toString() {
		String result = "";
		for (int i = 0; i < restrictions + 1; i++) {
			for (int j = 0; j < variables + 1; j++) {
				result += table.get(i).get(j) + " ";
			}
			result += "\n";
		}
		return result;
	}
}
