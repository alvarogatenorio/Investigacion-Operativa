package simplex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		List<List<Fraction>> table = new ArrayList<List<Fraction>>();
		List<Fraction> objectiveFunction = new ArrayList<Fraction>();
		/* Reading from the input file */
		Scanner scanner;
		try {
			scanner = new Scanner(new File("input.txt"));
			/*-----File format-----
			 * restrictions
			 * variables
			 * restrictionsMatrix
			 * objectiveFunction*/
			int restrictions = scanner.nextInt();
			int variables = scanner.nextInt();
			for (int i = 0; i < restrictions; i++) {
				table.add(new ArrayList<Fraction>());
				for (int j = 0; j < variables + 1; j++) {
					table.get(i).add(new Fraction(scanner.nextInt(), scanner.nextInt()));
				}
			}
			for (int i = 0; i < variables; i++) {
				objectiveFunction.add(new Fraction(scanner.nextInt(), scanner.nextInt()));
			}
		} catch (FileNotFoundException e) {
			System.err.println("Input file not found!");
		}
		Simplex s = new Simplex(table, objectiveFunction);
		s.execute();
		s.getSolution();
	}
}
