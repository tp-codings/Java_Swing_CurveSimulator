/**
 * 
 */
package utils;

/**
 * Einf�hrung in die Computergrafik.
 * 
 * Matrixoperationen
 * @author F.N. Rudolph
 * (c) 2012
 *
 */
public class Matrix {
	
	public static double dot(double[] a, double[] b) {
		double sum = 0.0;
		for(int i = 0; i < a.length; ++i) {
			sum += a[i] * b[i];
		}
		return sum;
	}

	/**
	 * Drucke Matrix
	 * 
	 * @param c Name der Matrix
	 * @param m Vektor
	 */
	public static void print(String c, float[][] m) {
		for (int i = 0; i < m.length; i++) {
			System.out.print(c + "[ " + i + "][*]: ");
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j] + ", ");
			}
			System.out.println(" ");
		}
	}
	public static void printF(String c, float [][] m) {
		for (int i = 0; i < m.length; i++) {
			System.out.print(c + "[ " + i + "][*]: ");
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j] + ", ");
			}
			System.out.println(" ");
		}
	}

	/**
	 * Drucke Vektor
	 * 
	 * @param c Name des Vektors
	 * @param m Vektor
	 */
	public static void print(String c, double[] m) {
		for (int i = 0; i < m.length; i++) {
			System.out.println(c + "[ " + i + "]: " + m[i] + ", ");
		}
	}

	/**
	 * Drucke Matrix
	 * 
	 * @param c Name der Matrix
	 * @param m Vektor
	 */
	public static void print(String c, double[][] m) {
		for (int i = 0; i < m.length; i++) {
			System.out.print(c + "[ " + i + "][*]: ");
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j] + ", ");
			}
			System.out.println(" ");
		}
	}

	/**
	 * Drucke Vektor
	 * 
	 * @param c Name des Vektors
	 * @param m Vektor
	 */
	public static void print(String c, float[] m) {
		for (int i = 0; i < m.length; i++) {
			System.out.println(c + "[ " + i + "]: " + m[i] + ", ");
		}
	}

	/**
	 * Legt die Kopie einer Matrix an
	 * 
	 * @param matrix 
	 * @return clone neue Matrix
	 */
	public static float[][] cloneMatrix(float[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;
		float[][] clone = new float[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				clone[i][j] = matrix[i][j];
			}
		}
		return clone;
	}
	public static double[][] cloneMatrix(double[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;
		double[][] clone = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				clone[i][j] = matrix[i][j];
			}
		}
		return clone;
	}
	
	/**
	 * Berechnet das Produkt eines Vektors mit einem Skalar.
	 * Vektor wird nicht ver�ndert
	 * @param s Faktor
	 * @param vector Vektor
	 * @return s*vector
	 */
	public static float [] produkt(float s, float [] vector){
		int n = vector.length;
		float [] newVec = new float [n];
		for (int i = 0; i < n; ++i){
			newVec [i] = s * vector[i];
		}
		return newVec;
	}
	
	public static double [] produkt(double s, double [] vector){
		int n = vector.length;
		double [] newVec = new double [n];
		for (int i = 0; i < n; ++i){
			newVec [i] = s * vector[i];
		}
		return newVec;
	}

	/**
	 * Berechnet das Produkt einer Matrix mit einem Skalar.
	 * Matrix wird nicht ver�ndert
	 * @param s Faktor
	 * @param matrix Matrix
	 * @return newMatrix s*Matrix
	 */
	public static float [] [] produkt(float s, float [] [] matrix){
		int n = matrix.length;
		int m = matrix[0].length;
		float [] [] newMatrix = new float [n][m];
		for (int i = 0; i < n; ++i){
			for (int j = 0; j < m; ++j){
			newMatrix [i][j] = s * matrix[i][j];
			}
		}
		return newMatrix;
	}

	/**
	 * Matrixmultiplikation matrix3 = matrix1 * matrix2
	 * 
	 * Matrix2 muss so viele Zeilen haben wie Matrix1 Spalten hat.
	 * 
	 * @param matrix1 linke Matrix
	 * @param matrix2 rechte Matrix
	 * @return Matrix3 Ergebnismatrix
	 */
	public static float[][] matMult(float[][] matrix1, float[][] matrix2) {
		// Dimensionen und Schleifengrenzen
		int l = matrix1.length; // Anzahl Zeilen Matrix1
		int m = matrix2.length; // Anzahl Zeilen Matris2
		int n = matrix2[0].length; // Anzahl Spalten Matrix2
		// System.out.println(l + " " + m + " "+ n);

		if (m != matrix1[0].length)
			// throw new RuntimeException();
			throw new IndexOutOfBoundsException("Array bounds incompatible: matrix1 [" + l + "][" + matrix1[0].length
				+ "] * matrix2[" + m + "][" + n + "]");
		else {
			float[][] matrix3 = new float[l][n];
			for (int i = 0; i < n; i++) { // f�r alle Spalten von matrix2 und matrix3
				for (int j = 0; j < l; j++) { // f�r alle Zeilen von matrix1 und matrix3
					matrix3[j][i] = (float) 0.0;
					for (int k = 0; k < m; k++) { // summiere f�r alle Spalten von matrix1 (Zeilen von Matrix2)
						matrix3[j][i] += +matrix1[j][k] * matrix2[k][i];
					}
				}
			}
			return matrix3;
		}
	}// MatMult
	public static double[][] matMult(double[][] matrix1, double[][] matrix2) {
		// Dimensionen und Schleifengrenzen
		int l = matrix1.length; // Anzahl Zeilen Matrix1
		int m = matrix2.length; // Anzahl Zeilen Matris2
		int n = matrix2[0].length; // Anzahl Spalten Matrix2
		// System.out.println(l + " " + m + " "+ n);

		if (m != matrix1[0].length)
			// throw new RuntimeException();
			throw new IndexOutOfBoundsException("Array bounds incompatible: matrix1 [" + l + "][" + matrix1[0].length
				+ "] * matrix2[" + m + "][" + n + "]");
		else {
			double[][] matrix3 = new double[l][n];
			for (int i = 0; i < n; i++) { // f�r alle Spalten von matrix2 und matrix3
				for (int j = 0; j < l; j++) { // f�r alle Zeilen von matrix1 und matrix3
					matrix3[j][i] = (float) 0.0;
					for (int k = 0; k < m; k++) { // summiere f�r alle Spalten von matrix1 (Zeilen von Matrix2)
						matrix3[j][i] += +matrix1[j][k] * matrix2[k][i];
					}
				}
			}
			return matrix3;
		}
	}// MatMult

	/**
	 * Matrixmultiplikation vektor2 = matrix1 * vektor
	 * 
	 * Vektor muss so viele Zeilen haben wie Matrix1 Spalten hat.
	 * 
	 * @param matrix1 linkes Element
	 * @param vektor rechtes Element
	 * @return vektor2 Ergebnisvektor
	 */
	public static float[] matMult(float[][] matrix1, float[] vektor) {
		// Dimensionen und Schleifengrenzen
		int l = matrix1.length; // Anzahl Zeilen Matrix1
		int m = vektor.length; // Anzahl Zeilen Matris2
		// System.out.println(l + " " + m + " "+ n);

		if (m != matrix1[0].length)
			// throw new RuntimeException();
			throw new IndexOutOfBoundsException("Array bounds incompatible: matrix1 [" + l + "][" + matrix1[0].length
				+ "] * matrix2[" + m + "]");
		else {
			float[] matrix3 = new float[l];
				for (int j = 0; j < l; j++) { // f�r alle Zeilen von matrix1 und matrix3
					matrix3[j] = (float) 0.0;
					for (int k = 0; k < m; k++) { // summiere f�r alle Spalten von matrix1 (Zeilen von Matrix2)
						matrix3[j] += +matrix1[j][k] * vektor[k];
					}
				}
			return matrix3;
		}
	}// matMult
	public static double[] matMult(double[][] matrix1, double[] vektor) {
		// Dimensionen und Schleifengrenzen
		int l = matrix1.length; // Anzahl Zeilen Matrix1
		int m = vektor.length; // Anzahl Zeilen Matris2
		// System.out.println(l + " " + m + " "+ n);

		if (m != matrix1[0].length)
			// throw new RuntimeException();
			throw new IndexOutOfBoundsException("Array bounds incompatible: matrix1 [" + l + "][" + matrix1[0].length
				+ "] * matrix2[" + m + "]");
		else {
			double[] matrix3 = new double[l];
				for (int j = 0; j < l; j++) { // f�r alle Zeilen von matrix1 und matrix3
					matrix3[j] = 0.0;
					for (int k = 0; k < m; k++) { // summiere f�r alle Spalten von matrix1 (Zeilen von Matrix2)
						matrix3[j] += +matrix1[j][k] * vektor[k];
					}
				}
			return matrix3;
		}
	}

	/**
	 * Invertiere die quadratische Matrix m
	 * 
	 * @param m zu invertierende Matrix
	 * @return r inverse Matrix
	 */
	public static float[][] invertiereMatrix(float[][] m) {
		int n = m.length;
		float[][] tmp = new float[n][2 * n];
		// print("Parameter", m);
		// --- Kopie der Matrix mit Einheitsmatrix rechts
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				tmp[i][j] = m[i][j];
			}
			tmp[i][i + n] = 1f;
		}
		// --- Dreieckselimination
 		float[][] tmp1 = Gauss.dreiecksElimination(tmp);
		// --- Rueckwaertssubstitution
		return Gauss.rueckwaertsSubstitutionMehrereGLS(tmp1);
	}
		public static double[][] invertiereMatrix(double[][] m) {
			int n = m.length;
			double[][] tmp = new double[n][2 * n];
			// print("Parameter", m);
			// --- Kopie der Matrix mit Einheitsmatrix rechts
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					tmp[i][j] = m[i][j];
				}
				tmp[i][i + n] = 1d;
			}
		// --- Dreieckselimination
 		double[][] tmp1 = Gauss.dreiecksElimination(tmp);
		// --- Rueckwaertssubstitution
		return Gauss.rueckwaertsSubstitutionMehrereGLS(tmp1);
	}

}
