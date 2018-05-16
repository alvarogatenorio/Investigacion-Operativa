package simplex;

/* Class for representing fractions, we guarantee that gcd(numerator,denominator)=1 */
public class Fraction {
	private int numerator;
	private int denominator;

	/*
	 * Builds a fraction from two numbers, throws an exception if the denominator
	 * equals zero
	 */
	public Fraction(int numerator, int denominator) throws IllegalArgumentException {
		if (denominator == 0) {
			throw new IllegalArgumentException("You can not divide by zero!");
		} else {
			int factor = Utils.gcd(numerator, denominator);
			this.numerator = numerator / factor;
			this.denominator = denominator / factor;
			if (this.denominator < 0) {
				this.numerator = -this.numerator;
				this.denominator = -this.denominator;
			}
		}
	}

	/* Builds a fraction from an integer */
	public Fraction(int numerator) {
		this.numerator = numerator;
		this.denominator = 1;
	}

	/* Checks if the fraction is an integer */
	private boolean isInteger() {
		return denominator == 1;
	}

	public int getNumerator() {
		return numerator;
	}

	public int getDenominator() {
		return denominator;
	}

	/* Returns "a/b" if a/b is not an integer and "a" if b=1 */
	public String toString() {
		return isInteger() ? String.valueOf(numerator) : numerator + "/" + denominator;
	}
}
