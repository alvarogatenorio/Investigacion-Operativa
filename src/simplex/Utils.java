package simplex;

/* Class used for representing math operations over fractions */
public class Utils {
	/* Euclides' algorithm for calculating gcd(a,b) */
	public static int gcd(int a, int b) {
		return (b == 0) ? a : gcd(b, a % b);
	}

	public static int lcm(int a, int b) {
		return (a * b) / gcd(a, b);
	}

	/* Divides two fractions */
	public static Fraction divide(Fraction f1, Fraction f2) {
		return new Fraction(f1.getNumerator() * f2.getDenominator(), f1.getDenominator() * f2.getNumerator());
	}

	/* Multiplies tow fractions */
	public static Fraction multiply(Fraction f1, Fraction f2) {
		return new Fraction(f1.getNumerator() * f2.getNumerator(), f1.getDenominator() * f2.getDenominator());
	}

	/* Subtracts two fractions */
	public static Fraction subtract(Fraction f1, Fraction f2) {
		int factor = lcm(f1.getDenominator(), f2.getDenominator());
		return new Fraction((f1.getNumerator() * (factor / f1.getDenominator()))
				- (f2.getNumerator() * (factor / f2.getDenominator())), factor);
	}

	/* Adds two fractions */
	public static Fraction add(Fraction f1, Fraction f2) {
		int factor = lcm(f1.getDenominator(), f2.getDenominator());
		return new Fraction((f1.getNumerator() * (factor / f1.getDenominator()))
				+ (f2.getNumerator() * (factor / f2.getDenominator())), factor);
	}

	/* Computes the minimum of two fractions */
	public static Fraction min(Fraction f1, Fraction f2) {
		return subtract(f1, f2).getNumerator() < 0 ? f1 : f2;
	}
}
