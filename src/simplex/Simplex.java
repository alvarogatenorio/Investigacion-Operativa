package simplex;

import java.util.ArrayList;
import java.util.List;

public class Simplex {
	/*
	 * A restriction a_{i1}x_1+\dots+a_{in}x_n=b_i is represented in the restriction
	 * matrix as the i-th row as [a_{i1},\dots,a_{i,n},b_i]
	 */
	private List<List<Fraction>> restrictionsMatrix;
	/* Some auxiliary values */
	private int basicVariables;
	private int variables;
	/*
	 * An objective function c_1x_1+\dots+c_nx_n is represented as [c_1,\dots,c_n]
	 */
	private List<Fraction> objectiveFunction;
	/* Vector of reduced costs */
	private List<Fraction> reducedCosts;
	/* List of the indices of the negative reduced costs */
	private List<Integer> negativeReducedCosts;
	/* List of the indices of the basic variables */
	private List<Integer> basicVariablesIndices;
	/* Simplex table */
	private Table table;
	/* Some control variables */
	private int pivots;
	/* We will apply the Dantzing rule */
	private int minimumReducedCostIndex;

	public Simplex(List<List<Fraction>> restrictionsMatrix, List<Fraction> objectiveFunction) {
		/* Storing parameters */
		this.restrictionsMatrix = restrictionsMatrix;
		this.objectiveFunction = objectiveFunction;
		/* Initializing auxiliary variables */
		this.basicVariables = restrictionsMatrix.size();
		this.variables = restrictionsMatrix.get(0).size() - 1;
		/* Initializing auxiliary lists */
		this.reducedCosts = new ArrayList<Fraction>();
		this.negativeReducedCosts = new ArrayList<Integer>();
		/* Always start supposing the basic variables are the first ones */
		this.basicVariablesIndices = new ArrayList<Integer>();
		for (int i = 0; i < basicVariables; i++) {
			basicVariablesIndices.add(i);
		}
		/* Initializing control variables */
		this.pivots = 0;
		/*
		 * If the minimum reduced cost index is negative, every reduced cost is positive
		 */
		this.minimumReducedCostIndex = -1;
	}

	public void getSolution() {
		if (testOptimality()) {
			for (int i = 0; i < basicVariables; i++) {
				System.out
						.println("x_" + basicVariablesIndices.get(i) + "=" + restrictionsMatrix.get(i).get(variables));
			}
			System.out.println("The remaining variables equals 0");
			System.out.println(
					"Objective function value: " + (Utils.subtract(new Fraction(0), reducedCosts.get(variables))));
		}
	}

	public void execute() {
		/* First of all we have to compute the initial reduced costs */
		computeReducedCosts();
		/* Then compute the objective function value */
		computeObjectiveFunctionValue();
		/* Creating the table */
		List<List<Fraction>> aux = restrictionsMatrix;
		aux.add(reducedCosts);
		table = new Table(aux);
		/* Executing the algorithm itself */
		do {
			if (testOptimality()) {
				System.out.println("Optimal solution!");
			} else if (testUnbounded()) {
				System.out.println("Unbounded problem!");
			} else {
				/* Improving the solution */
				pivots++;
				/* Pivoting the table */
				int pivotRow = selectPivot();
				table.pivot(pivotRow, minimumReducedCostIndex);
				/* Updating information */
				basicVariablesIndices.set(pivotRow, minimumReducedCostIndex);
				minimumReducedCostIndex = table.getMinimumReducedCostIndex();
				restrictionsMatrix = table.getRestrictionsMatrix();
				reducedCosts = table.getReducedCosts();
				negativeReducedCosts = table.getNegativeReducedCosts();
			}
		} while (!testOptimality());
		System.out.println("Optimal solution!");
		System.out.println("Pivots needed: " + pivots);
	}

	/*
	 * Computes the initial reduced costs, we always suppose the initial base is Id
	 * and the basic variables are the first ones. Just follows the definition. Also
	 * updates the list of indices of the negative reduced costs.
	 */
	private void computeReducedCosts() {
		/* The minimum reduced cost will is upper bounded by zero */
		Fraction minimumReducedCost = new Fraction(0);
		/* Each variable has a reduced cost */
		for (int i = 0; i < variables; i++) {
			Fraction aux = new Fraction(0);
			/* Computing the scalar product */
			for (int j = 0; j < basicVariables; j++) {
				aux = Utils.add(aux, Utils.multiply(objectiveFunction.get(j), restrictionsMatrix.get(j).get(i)));
			}
			aux = Utils.subtract(objectiveFunction.get(i), aux);
			reducedCosts.add(aux);
			if (aux.getNumerator() < 0) {
				negativeReducedCosts.add(i);
				minimumReducedCost = Utils.min(minimumReducedCost, reducedCosts.get(i));
				minimumReducedCostIndex = i;
			}
		}
	}

	/*
	 * Computes the opposite of the value of the objective function evaluated in the
	 * first extreme point. Adds it to the end of the reduced costs list.
	 */
	private void computeObjectiveFunctionValue() {
		Fraction aux = new Fraction(0);
		/* Computing the scalar product */
		for (int j = 0; j < basicVariables; j++) {
			aux = Utils.add(aux, Utils.multiply(objectiveFunction.get(j), restrictionsMatrix.get(j).get(variables)));
		}
		aux = Utils.subtract(new Fraction(0), aux);
		reducedCosts.add(aux);
	}

	/* Checks if every reduced cost is positive, i.e if the solution is optimal */
	private boolean testOptimality() {
		return minimumReducedCostIndex == -1;
	}

	/* Checks if the solution is unbounded */
	private boolean testUnbounded() {
		/* Checks for each negative reduced cost */
		for (int j = 0; j < negativeReducedCosts.size(); j++) {
			/* If we find someone positive in the column, this is not our column */
			int i;
			for (i = 0; i < basicVariables; i++) {
				if (restrictionsMatrix.get(i).get(j).getNumerator() > 0) {
					break;
				}
			}
			/* If we didn't find anyone positive, this is our column */
			if (i == basicVariables) {
				return true;
			}
		}
		return false;
	}

	/*
	 * Selects the pivot (or exit variable) following the minimal ratio rule,
	 * returns the row index of the pivot
	 */
	private int selectPivot() {
		int index = -1;
		Fraction minimalRatio = null;
		for (int i = 0; i < basicVariables; i++) {
			Fraction ratio;
			Fraction aux = restrictionsMatrix.get(i).get(minimumReducedCostIndex);
			if (aux.getNumerator() > 0) {
				ratio = Utils.divide(restrictionsMatrix.get(i).get(variables), aux);
				if (minimalRatio == null) {
					minimalRatio = ratio;
					index = i;
				} else {
					if (Utils.subtract(ratio, minimalRatio).getNumerator() < 0) {
						minimalRatio = ratio;
						index = i;
					}
				}
			}
		}
		return index;
	}
}
