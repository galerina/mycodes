import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

// From http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html:
//
//   Given a list of wordnet nouns A1, A2, ..., An, which noun is the least related to 
//   the others? To identify an outcast, compute the sum of the distances between each 
//   noun and every other one:
//
//     di   =   dist(Ai, A1)   +   dist(Ai, A2)   +   ...   +   dist(Ai, An)
//   and return a noun At for which dt is maximum.
//
// This class is initialized by a WordNet and it finds the outcast from a group of nouns.
public class Outcast {
    private WordNet wordnet;

    // constructor takes a WordNet object
    public Outcast(WordNet w) {
        wordnet = w;
    }

   // given an array of WordNet nouns, return an outcast
   public String outcast(String[] nouns) {
       int maxDistanceSum = -1;
       String outcastNoun = "";

       for (int i = 0; i < nouns.length; i++) {
       String currentNoun = nouns[i];
       int distanceSum = 0;
       for (int j = 0; j < nouns.length; j++) {
           if (j != i) {
           distanceSum += wordnet.distance(currentNoun, nouns[j]);
           }
       }

       if (distanceSum > maxDistanceSum) {
           maxDistanceSum = distanceSum;
           outcastNoun = currentNoun;
       }
       }

       return outcastNoun;
   }

   // Sample execution:
   // % more outcast5.txt
   // horse zebra cat bear table
   //
   // % more outcast8.txt
   // water soda bed orange_juice milk apple_juice tea coffee
   //
   // % more outcast11.txt
   // apple pear peach banana lime lemon blueberry strawberry mango watermelon potato
   //
   //
   // % java Outcast synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
   // outcast5.txt: table
   // outcast8.txt: bed
   // outcast11.txt: potato
   public static void main(String[] args) {
       WordNet wordnet = new WordNet(args[0], args[1]);
       Outcast outcast = new Outcast(wordnet);
       for (int t = 2; t < args.length; t++) {
       In in = new In(args[t]);
       String[] nouns = in.readAllStrings();
       StdOut.println(args[t] + ": " + outcast.outcast(nouns));
       }
   }
}
