package br.com.robot.downloader;

public class MyExtent {

	public double xmin;
	public double ymin;
	public double xmax;
	public double ymax;

	public MyExtent() {
	}

	public MyExtent(double xmin, double ymin, double xmax, double ymax) {
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
	}
}
