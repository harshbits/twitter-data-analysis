import java.util.List;

public class Point {
	private int id;
	private double x = 0;
	private double y = 0;
	private int cluster_id = 0;

	public List<Point> points;

	public Point(double x, double y) {
		this.setX(x);
		this.setY(y);
	}

	public Point(int id, double x, double y) {
		this.setId(id);
		this.setX(x);
		this.setY(y);
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getX() {
		return this.x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getY() {
		return this.y;
	}

	protected static double Euclideandistance(Point p, Point c) {
		return Math.sqrt(Math.pow((c.getY() - p.getY()), 2)
				+ Math.pow((c.getX() - p.getX()), 2));
	}

	public int getclusterid() {
		return this.cluster_id;
	}

	public void setclusterid(int cid) {
		this.cluster_id = cid;
	}

}
