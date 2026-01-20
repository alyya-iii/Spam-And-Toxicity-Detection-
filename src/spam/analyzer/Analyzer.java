package spam.analyzer;
import java.sql.*;
public class Analyzer {

    private static final String url = "jdbc:mysql://localhost:3306/spam_db";
    private static final String user = "root";
    private static final String password = "";

    public static int keywordSpam(String content) {
        String query = "SELECT word, spam_weight FROM keywordweight";
        int spamScore = 0;

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            content = content.toLowerCase();

            while (rs.next()) {
                String key = rs.getString("word");
                int sW = rs.getInt("spam_weight");

                if (content.contains(key)) {
                    spamScore += sW;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return spamScore;
    }

  public static int keywordToxic(String content) {
        String query = "SELECT word, toxic_weight FROM keywordweight";
        int toxicScore = 0;
        try(Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {

            content = content.toLowerCase();
            while(rs.next()){
                String key = rs.getString("word");
                int tW = rs.getInt("toxic_weight");

                if(content.contains(key)){
                    toxicScore += tW;
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return toxicScore;
  }

   public static double calculateSpamScore(String content) {
        String lower = content.toLowerCase();
        String[] spamWords = {"won", "prize", "offer", "money", "congratulations", "grab it", "lottery", "reward", "win",
                               "congrats", "limited", "act now", "cash", "bonus", "free", "guranteed", "risk-free","earn",
                                "save", "100%", "income", "winner", "exclusive", "deal", "chosen", "claim"};
        int count = 0;

        for (String word : spamWords) {
            if (lower.contains(word)) {
                count++;
            }
        }
        int weight = keywordSpam(lower);
        return ((count / (double) content.length()) * 100) + weight;
    }

    public static double calculateToxicScore(String content)  {
        String lower = content.toLowerCase();
        String[] toxicWords = {"stupid", "idiot", "hate", "kill", "dumb", "useless", "worst", "lazy",
                "nonsense", "disgusting", "trash", "annoying", "worthless",
                "pathetic", "terrible", "horrible", "ugly", "failure", "garbage"};
        int count = 0;

        for (String word : toxicWords) {
            if (lower.contains(word)) {
                count++;
            }
        }
        int weight = keywordToxic(lower);
        return ((count / (double) content.length()) * 100) + weight;
    }

    public static String classify(double spamScore, double toxicScore) {

        if (spamScore > 10 && toxicScore>10) {
            return "SPAM & TOXIC";
        } else if (toxicScore > 10) {
            return "TOXIC";
        } else if(spamScore>10) {
            return "SPAM";
        }
        else {
            return "CLEAN";
        }
    }
}