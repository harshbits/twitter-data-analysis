
import java.io.*;
import java.util.*;

public class KMeansAnalysis {
	private static List<Tweets> tweets;
	private static List<Cluster> clusters;
	private static int NUM_CLUSTERS;
	private static double SSE;

	public static void main(String[] args) throws IOException {
		NUM_CLUSTERS = Integer.parseInt(args[0]);
		String initialSeedsFile = args[1];
		String TweetsDataFile = args[2];
		String outputFile = args[3];

		// Read Tweets from the file : "Tweets.json"
		readFile(TweetsDataFile);

		// Read initial centroids from the given file: "InitialSeeds.txt"
		ArrayList<Long> initialSeeds = readInitialSeeds(initialSeedsFile);

		// create clusters using the initial file.
		createclusters(initialSeeds);

		List<Tweets> lastCentroids;
		List<Tweets> currentCentroids;
		double change;
		do {
			change = 0;

			// clear clusters
			clearClusters();

			lastCentroids = getCentroids();

			// Assign clusters to the tweets
			assignclusters();

			// Update centroids from the new assigned tweets
			updatecentroids();

			currentCentroids = getCentroids();

			// Calculate change in the centroids
			for (int j = 0; j < lastCentroids.size(); j++) {
				change += Tweets.calcJacardDistance(lastCentroids.get(j),
						currentCentroids.get(j));
			}
		} while (change > 0);

		// output data
		printclusters(outputFile);
	}

	// Read Tweets from file
	public static void readFile(String path) throws IOException {
		tweets = new ArrayList<Tweets>();
		FileInputStream fin = new FileInputStream(path);
		BufferedReader input = new BufferedReader(new InputStreamReader(fin));
		String line;
		ArrayList<String> s = new ArrayList<String>();
		while ((line = input.readLine()) != null) {
			s.add(line);
		}
		for (String item : s) {
			Tweets t = new Tweets();
			String[] l = item.split("\"text\":");
			String[] m = item.split("\"id\":");
			t.setText(l[1].substring(1, l[1].indexOf(',')).replace("\"", ""));
			t.setId(Long.parseLong(m[1].substring(1, m[1].indexOf(','))
					.replace("\"", "")));
			tweets.add(t);
		}
		input.close();

	}

	// create clusters
	public static void createclusters(ArrayList<Long> centroids)
			throws IOException {
		clusters = new ArrayList<Cluster>();
		for (int i = 0; i < NUM_CLUSTERS; i++) {
			Cluster c = new Cluster(i);
			c.setCentroid((Tweets) (getTweetbyId(centroids.get(i))));
			clusters.add(c);
		}
	}

	// Read initial centroids
	public static ArrayList<Long> readInitialSeeds(String path)
			throws IOException {
		ArrayList<Long> list = new ArrayList<>();
		FileInputStream fin = new FileInputStream(path);
		BufferedReader input = new BufferedReader(new InputStreamReader(fin));
		String line;
		while ((line = input.readLine()) != null) {
			String[] sa = line.split(",");
			for (String s1 : sa) {
				list.add(Long.parseLong(s1));
			}
		}
		input.close();

		return list;
	}

	// given a tweet id, return the object of tweets
	public static Tweets getTweetbyId(Long id) {
		for (Tweets tweet : tweets) {
			if (tweet.getId().equals(id)) {
				return tweet;
			}

		}
		return null;
	}

	// Assign clusters to the tweets
	public static void assignclusters() {
		for (Tweets t : tweets) {
			double mindist = Double.MAX_VALUE;
			int cluster = 0;
			for (int i = 0; i < clusters.size(); i++) {
				Cluster c = clusters.get(i);
				double dist = Tweets.calcJacardDistance(t, c.getCentroid());
				if (mindist > dist) {
					mindist = dist;
					cluster = i;
				}
			}
			t.setClusterId(cluster);
			clusters.get(cluster).addTweettoCluster(t);
		}

	}

	// Update centroids from the new assigned tweets
	public static void updatecentroids() {
		for (Cluster c : clusters) {
			double mindist = Double.MAX_VALUE;
			Tweets newcentroid = new Tweets();
			for (Tweets t : c.tweets) {
				double totaldist = 0;
				for (Tweets t1 : c.tweets) {
					totaldist = totaldist + Tweets.calcJacardDistance(t, t1);
				}
				if (mindist > totaldist) {
					mindist = totaldist;
					newcentroid = t;
				}
			}
			c.setCentroid(newcentroid);
		}
	}

	// returns a list of centroids of all clusters
	private static List<Tweets> getCentroids() {
		List<Tweets> centroids = new ArrayList<Tweets>(clusters.size());
		for (Cluster cluster : clusters) {
			Tweets centroid = cluster.getCentroid();
			Tweets t = new Tweets(centroid.getId(), centroid.getText());
			centroids.add(t);
		}
		return centroids;
	}

	// clears all the tweets from the clusters
	private static void clearClusters() {
		for (Cluster cluster : clusters) {
			cluster.clear();
		}
	}

	// output data-- write to some file as per given path
	private static void printclusters(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("Cluster ID: \t\t" + "Tweet IDs");
		bw.newLine();

		for (Cluster c : clusters) {
			c.printCluster(bw);
			bw.newLine();
		}
		calcSSE();
		bw.write("SSE for k = " + NUM_CLUSTERS + " is " + SSE);
		bw.close();

	}

	// Calculate SSE.
	private static void calcSSE() {
		double dist = 0;
		for (Cluster c : clusters) {
			for (Tweets t : c.tweets) {
				double d = Tweets.calcJacardDistance(t, c.getCentroid());
				dist = dist + Math.pow(d, 2);
			}
		}
		SSE = dist;
	}
}