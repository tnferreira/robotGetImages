package br.com.robot.downloader;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

//http://gis.stackexchange.com/questions/2008/coordinates-of-upper-left-corner-in-the-world-file/2010#2010
public class WordFile {

	/**
	 * The transformation parameters are stored in the world file in this order:
		20.17541308822119 (A)
		0.00000000000000  (D)
		0.00000000000000  (B)
		-20.17541308822119 (E)
		424178.11472601280548 (C)
		4313415.90726399607956 (F)
		The equation:
		
		x1 = Ax + By + C
		y1 = Dx + Ey + F
		
		where:
		x1 = calculated x-coordinate of the pixel on the map
		y1 =  calculated y-coordinate of the pixel on the map
		x = column number of a pixel in the image
		y = row number of a pixel in the image
		A = x-scale; dimension of a pixel in map units in x direction
		B, D = rotation angle
		C, F = translation parameters; x,y map coordinates of the center of the upper left pixel
		E = negative of y-scale; dimension of a pixel in map units in y direction
	 */
	public static void fromWorldFile() {
		double C = 669658.8924637756;
		double F = 7462036.946918188;
		
		double A = 0.13229193125048183;
		@SuppressWarnings("unused")
		double E = -0.13229193125048183;
		double diff = A / 2;
		
		System.out.println("top: " + (F + diff));
		System.out.println("bottom: " +  (F -(A*951) + diff));
		
		System.out.println("left: " + (C - diff));
		System.out.println("right: " +  (C + (A*1562) - diff));
	}
	
	public static void write(String name, double left, double top, double pxsize) throws IOException {
		String filename = name+".jgw";
		File wf = new File(Downloader.OUTPUT_DIR, filename);
		PrintWriter writer = new PrintWriter(wf, "UTF-8");
		writer.println(pxsize);
		writer.println(0);
		writer.println(0);
		writer.println(-pxsize);
		double diff = pxsize / 2;
		writer.println(left + diff);
		writer.print(top - diff);
		writer.flush();
		writer.close();
	}
	
	public static void print(String name, double left, double top, double pxsize) throws IOException {
		String filename = name+".jgw";
		System.out.println(filename);
		System.out.println(pxsize);
		System.out.println(0);
		System.out.println(0);
		System.out.println(-pxsize);
		double diff = pxsize / 2;
		System.out.println(left + diff);
		System.out.println(top - diff);
		System.out.println("---------------");
	}
}
