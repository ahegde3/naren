import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class PolynomialDegreeThree {
	static int Answer;

	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);

		int T = sc.nextInt();
		for (int test_case = 0; test_case < T; test_case++) {
			int nrOfPolynomials = sc.nextInt();
			PolynamialIntersection pi = new PolynamialIntersection(
					nrOfPolynomials);
			for (int i = 0; i < nrOfPolynomials; ++i) {
				pi.addPolynomial(sc.nextInt(), sc.nextInt(), sc.nextInt(),
						sc.nextInt());
			}

			// Print the answer to standard output(screen).
			System.out.println("Case #" + (test_case + 1));
			System.out.println(pi.computeResult());
		}
	}

	private static class Polynomial {
		public int A;
		public int B;
		public int C;
		public int D;
		public int nrOfIntersectionPoints = 1;
	}

	private static class PolynamialIntersection {
		private final int nrOfPolynomials;
		private final List<Polynomial> polynomials;
		private int totalNrOfPoints = 1;

		public PolynamialIntersection(int nrOfPolynomials) {
			this.nrOfPolynomials = nrOfPolynomials;
			polynomials = new ArrayList<Polynomial>(nrOfPolynomials);
		}

		public void addPolynomial(int a, int b, int c, int d) {
			Polynomial p = new Polynomial();
			p.A = a;
			p.B = b;
			p.C = c;
			p.D = d;
			polynomials.add(p);
		}

		public String computeResult() {
			int pieces = 0;
			for (int i = 0; i < nrOfPolynomials; ++i) {
				Polynomial p = polynomials.get(i);
				for (int j = i + 1; j < nrOfPolynomials; ++j) {
					computeIntersections(p, polynomials.get(j));
				}
				pieces += (p.nrOfIntersectionPoints + 1);
			}
			return String.format("%d %d", totalNrOfPoints, pieces);
		}

		private void computeIntersections(Polynomial p1, Polynomial p2) {
			double a = p1.A - p2.A;
			double b = p1.B - p2.B;
			double c = p1.C - p2.C;
			if (a == 0) {
				if (b != 0) {
					double x = -c / b;
					addIntersectionPoint(x, p1, p2);
				}
			} else {
				double delta = b * b - 4 * a * c;
				if (delta > 0.0) {
					double sqrtdelta = Math.sqrt(delta);
					double x1 = (-b + sqrtdelta) / (2 * a);
					addIntersectionPoint(x1, p1, p2);
					double x2 = (-b - sqrtdelta) / (2 * a);
					addIntersectionPoint(x2, p1, p2);
				} else if (delta == 0.0) {
					double x = (-b) / (2 * a);
					addIntersectionPoint(x, p1, p2);
				}
			}
		}

		private void addIntersectionPoint(double x, Polynomial p1, Polynomial p2) {
			if (x == 0.0) {
				return;
			}
			++p1.nrOfIntersectionPoints;
			++p2.nrOfIntersectionPoints;
			++totalNrOfPoints;
		}
	}
}