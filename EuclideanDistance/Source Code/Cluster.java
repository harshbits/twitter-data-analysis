import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cluster {
	public List<Point> points;
	public Point centroid;
	public int id;

	public Cluster(int id) {
		this.id = id;
		this.points = new ArrayList<>();
		this.centroid = null;
	}

	public void setCentroid(Point p) {
		this.centroid = p;
	}

	public Point getCentroid() {
		return this.centroid;
	}

	public void addPoint(Point p) {
		this.points.add(p);
	}

	public List<Point> getPoints() {
		return this.points;
	}

	public void clear() {
		points.clear();
	}

	public void printCluster(BufferedWriter bw) throws IOException {
		bw.write(this.id + "\t\t");
		// bw.write(this.getCentroid().getId() + "\t\n");
		bw.write("[Points:");
		for (Point p : points) {
			bw.write(p.getId() + ",");
		}
		bw.write("]");
	}

}
