import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class KMeansAnalysis {
	private static int NUM_CLUSTERS;
	private static List<Point> points;
	private static List<Cluster> clusters;
	private static double SSE;

	public static void main(String args[]) throws IOException {
		NUM_CLUSTERS = Integer.parseInt(args[0]);
		// Read the files.
		readfile(args[1]);
		// Create number of clusters depend on the value of k
		createclusters();
		int iter = 0;
		// Limit the number of iterations to 25;
		for (int i = 0; i < 25; i++) {

			clearClusters();
			List<Point> lastCentroids = getCentroids();
			assignclusters();
			// Update the centroid
			updateCentroids();

			List<Point> currentCentroids = getCentroids();
			double change = 0;

			for (int j = 0; j < lastCentroids.size(); j++) {
				change += Point.Euclideandistance(lastCentroids.get(j),
						currentCentroids.get(j));
			}
			if (change == 0)
				break; // This will break the statements.

			// Printing clusters to the output file.
			printclusters(args[2]);
			iter++;
		}

		// This will print the number of iterations.
		System.out.println("Total iterations: " + iter);

	}

	// It will read data from the given file
	public static void readfile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = br.readLine();
		points = new ArrayList<Point>();
		while ((line = br.readLine()) != null) {
			String[] linechars = line.split("\t");
			int id = Integer.parseInt(linechars[0]);
			double x = Double.parseDouble(linechars[1]);
			double y = Double.parseDouble(linechars[2]);
			Point p = new Point(id, x, y);
			points.add(p);
		}
		br.close();
	}

	//It will create The clusters.
	public static void createclusters() {
		clusters = new ArrayList<Cluster>();
		for (int i = 0; i < NUM_CLUSTERS; i++) {
			Cluster c = new Cluster(i);
			c.setCentroid((Point) (points.get(i)));
			clusters.add(c);
		}
	}

	//Assign Clusters
	public static void assignclusters() {
		for (Point p : points) {
			double mindist = Double.MAX_VALUE;
			int cluster = 0;
			for (int i = 0; i < NUM_CLUSTERS; i++) {
				Cluster c = clusters.get(i);
				double dist = Point.Euclideandistance(p, c.getCentroid());
				if (mindist > dist) {
					mindist = dist;
					cluster = i;
				}
			}
			p.setclusterid(cluster);
			clusters.get(cluster).addPoint(p);
		}

	}

	// Update Centroid Method
	private static void updateCentroids() {
		for (Cluster c : clusters) {
			double sumX = 0;
			double sumY = 0;
			List<Point> list = c.getPoints();
			int no_of_points = list.size();

			for (Point point : list) {
				sumX += point.getX();
				sumY += point.getY();
			}

			Point centroid = c.getCentroid();
			if (no_of_points > 0) {
				double newX = sumX / no_of_points;
				double newY = sumY / no_of_points;
				centroid.setX(newX);
				centroid.setY(newY);
			}
		}
	}

	private static void clearClusters() {
		for (Cluster cluster : clusters) {
			cluster.clear();
		}
	}

	private static List<Point> getCentroids() {
		List<Point> centroids = new ArrayList<Point>(NUM_CLUSTERS);
		for (Cluster cluster : clusters) {
			Point centroid = cluster.getCentroid();
			Point point = new Point(centroid.getX(), centroid.getY());
			centroids.add(point);
		}
		return centroids;
	}

	// output data
	private static void printclusters(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("Cluster ID: \t\t" + "Point IDs");
		bw.newLine();

		for (Cluster c : clusters) {
			c.printCluster(bw);
			bw.newLine();
		}
		calcSSE();
		bw.write("SSE for k = " + NUM_CLUSTERS + " is " + SSE);
		bw.close();

	}

	//Calculate the SSE
	private static void calcSSE() {
		double dist = 0;
		for (Cluster c : clusters) {
			for (Point p : c.points) {
				double d = Point.Euclideandistance(p, c.getCentroid());
				dist = dist + Math.pow(d, 2);
			}
		}
		SSE = dist;
	}

}
