package spam.database;
import spam.message.Message;
import spam.swing.UI;

import java.sql.*;
import java.util.*;
import java.util.List;

import static spam.analyzer.Analyzer.classify;
import static spam.main.Main.YELLOW;


public class Database {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String MAGENTA = "\u001B[35m";

    private static final String url = "jdbc:mysql://localhost:3306/spam_db";
    private static final String user = "root";
    private static final String password = "";

    public static void saveMessage(Message message) {
        String query = "INSERT INTO messages(sender,content,spam_score,toxic_score,classification) VALUES(?,?,?,?,?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, message.getSenderEmail());
            stmt.setString(2, message.getContent());
            stmt.setDouble(3, message.getSpamScore());
            stmt.setDouble(4, message.getToxicityScore());
            stmt.setString(5, message.getClassification());
            stmt.executeUpdate();
            System.out.println(GREEN+"\nMessage saved to database!");
            System.out.println(MAGENTA + "=====================================================");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void printRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String sender = rs.getString("sender");
        String content = rs.getString("content");
        double spam = rs.getDouble("spam_score");
        double toxic = rs.getDouble("toxic_score");
        String classi = rs.getString("classification");
        Timestamp created_at = rs.getTimestamp("created_at");

        String classification = classify(spam, toxic);
        String classColor = switch(classification) {
            case "SPAM & TOXIC", "TOXIC", "SPAM" -> RED;
            default -> YELLOW;
        };

        System.out.printf( RESET+
                "%-3d | %-15s | %-40s | %-6.2f | %-6.2f   | " +
                        classColor + "%-15s"+RESET+" | %-19s%n" + RESET,
                id, sender,
                content.length() > 40 ? content.substring(0, 37) + "..." : content,
                spam, toxic, classification, created_at
        );
    }

    public static void viewAllMessages() {
        String query = "SELECT id, sender, content, spam_score, toxic_score, classification, created_at FROM messages ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            System.out.printf(
                    MAGENTA+ "%-3s | %-15s | %-40s | %-6s | %-6s | %-20s | %-19s%n",
                    "ID", "Sender", "Content", "Spam", "Toxicity", "Class", "Created At" + RESET
            );
             while (rs.next()) printRow(rs);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void searchByKeyword(String keyword) {
        String query = "SELECT id, sender, content, spam_score, toxic_score, classification, created_at "
                + "FROM messages WHERE content LIKE ? OR sender LIKE ? ORDER BY id DESC";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            String like = "%" + keyword + "%";
            stmt.setString(1, like);
            stmt.setString(2, like);

            ResultSet rs = stmt.executeQuery();
            System.out.println(MAGENTA+"====================================================================================================================================================");
            System.out.println(GREEN+"Search Keyword (keyword: \"" +RESET+ keyword + GREEN+ "\") ");
            boolean found = false;
            System.out.printf(
                    MAGENTA+ "%-3s | %-15s | %-40s | %-6s | %-6s | %-20s | %-19s%n",
                    "ID", "Sender", "Content", "Spam", "Toxicity", "Class", "Created At" + RESET
            );
            while (rs.next()) {
                found = true;
                printRow(rs);
            }
            System.out.println(MAGENTA+"====================================================================================================================================================");
            if (!found) System.out.println("No messages found!");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteMessage(int id) {
        String query = "DELETE FROM messages WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                System.out.println(MAGENTA+"=====================================================");
                System.out.println(CYAN+"                 Message deleted successfully! ");
                System.out.println(MAGENTA+"=====================================================\n");
            } else {
                System.out.println(GREEN+"No message found with that ID :( ");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void showStatistics() {
        String totalQuery = "SELECT COUNT(*) FROM messages";
        String spamQuery = "SELECT COUNT(*) FROM messages WHERE spam_score>10";
        String toxicQuery = "SELECT COUNT(*) FROM messages WHERE toxic_score>10";
        String bothQuery = "SELECT COUNT(*) FROM messages WHERE classification LIKE '%SPAM & TOXIC%'";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(totalQuery);
            rs.next();
            int total = rs.getInt(1);

            ResultSet spamResult = stmt.executeQuery(spamQuery);
            spamResult.next();
            int spam = spamResult.getInt(1);

            ResultSet toxicResult = stmt.executeQuery(toxicQuery);
            toxicResult.next();
            int toxic = toxicResult.getInt(1);

            ResultSet bothResult = stmt.executeQuery(bothQuery);
            bothResult.next();
            int both = bothResult.getInt(1);

            UI.showStatisticsWindow(total, spam, toxic, both);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void topWords() {
        String query = "SELECT content FROM messages";
        Map<String, Integer> frequency = new HashMap<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String content = rs.getString("content");
                content = content.toLowerCase();
                String[] words = content.split("\\s+");

                for (String word : words) {
                    String word2 = word.replaceAll("\\.", "");
                    Integer amount = frequency.get(word2);
                    if (amount == null)
                        frequency.put(word2, 1);
                    else
                        frequency.put(word2, amount + 1);
                }
            }
            List<Map.Entry<String, Integer>> list = new ArrayList<>(frequency.entrySet());
            list.sort((a, b) -> b.getValue() - a.getValue());

            UI.topWordsWindow(list);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addKeywordWeight(String word, int s_weight, int t_weight) {
        String query = "INSERT INTO keywordweight(word, spam_weight, toxic_weight) VALUES (?,?,?)";
        try(Connection conn = DriverManager.getConnection(url,user,password);
        PreparedStatement stmt = conn.prepareStatement(query)) {

        stmt.setString(1, word.toLowerCase());
        stmt.setInt(2, s_weight);
        stmt.setInt(3, t_weight);

        stmt.executeUpdate();
        System.out.println(GREEN+"\nWeights Saved To Database!");
        System.out.println(MAGENTA + "=====================================================");
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}