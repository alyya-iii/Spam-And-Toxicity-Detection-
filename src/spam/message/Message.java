package spam.message;
import java.text.DecimalFormat;

public class Message {

    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String MAGENTA = "\u001B[35m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";

    private final String senderEmail;
    private final String content;
    private final double spam_score;
    private final double toxic_score;
    private final String classification;

    public Message(String s, String c, double spam, double toxic, String cl){
        senderEmail=s;
        content=c;
        spam_score = (double) Math.round(spam*1000)/1000;
        toxic_score = (double) Math.round(toxic * 1000) /1000;
        classification=cl;
    }
    public String getSenderEmail() { return senderEmail; }
    public String getContent() { return content; }
    public double getSpamScore() { return spam_score; }
    public double getToxicityScore() { return toxic_score; }
    public String getClassification() { return classification; }

    public void displayMessageInfo() {
        String classi = getClassification();
        String color = "";

        if (classi.contains("SPAM") || classi.contains("TOXIC")) {
            color=RED;
        } else if (classi.contains("CLEAN")) {
            color=YELLOW;
        }
        DecimalFormat df = new DecimalFormat("#.###");
        System.out.println(MAGENTA + "\n=====================================================");
        System.out.println(GREEN+"Sender Email: " +RESET+ getSenderEmail());
        System.out.println(GREEN+"Message: " + RESET+getContent());
        System.out.println(GREEN+"Spam Score: " +RESET+ df.format(getSpamScore()));
        System.out.println(GREEN+"Toxicity Score: " +RESET+ df.format(getToxicityScore()));

        System.out.println(GREEN+"Classification: " +RESET + color + getClassification());
    }
}
