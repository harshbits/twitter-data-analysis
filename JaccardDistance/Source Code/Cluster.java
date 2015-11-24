
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cluster {
	public List<Tweets> tweets;
	public Tweets centroid;
	public int id;

	public Cluster(int id) {
		this.id = id;
		this.tweets = new ArrayList<Tweets>();
		this.centroid = null;
	}

	public void setCentroid(Tweets t) {
		// TODO Auto-generated method stub
		this.centroid = t;
	}

	public Tweets getCentroid() {
		return this.centroid;
	}

	public void addTweettoCluster(Tweets t) {
		this.tweets.add(t);
	}

	public void clear() {
		tweets.clear();
	}

	public void printCluster(BufferedWriter bw) throws IOException {
		bw.write(this.id + "\t\t");
		// bw.write(this.getCentroid().getId() + "\t\n");
		bw.write("[IDs:");
		for (Tweets t : tweets) {
			bw.write(t.getId() + ",");
		}
		bw.write("]");
	}
}
