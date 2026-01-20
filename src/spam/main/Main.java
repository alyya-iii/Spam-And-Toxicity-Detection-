package spam.main;
import java.util.Scanner;
import spam.analyzer.Analyzer;
import spam.message.Message;

import static spam.database.Database.*;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";
    public static final String MAGENTA = "\u001B[35m";
    public  static final String YELLOW = "\u001B[93m";


    public static void main(String[] args) throws InterruptedException {
        System.out.println();
        Scanner input = new Scanner(System.in);
        boolean running = true;
        while(running){

            String[] spinner = {"|", "/", "-", "\\"};

            System.out.println(MAGENTA + "\n=====================================================");
            System.out.println(CYAN + "                   Spam Detection                    ");
            System.out.println(GREEN+ "1. " + GREEN +"Analyze A Message");
            System.out.println("2. "  + GREEN + "Previous Messages");
            System.out.println("3. " + GREEN + "Show Statistics");
            System.out.println("4. " + GREEN + "Train The System");
            System.out.println("5. " + GREEN+"Exit");
            System.out.println(MAGENTA+"=====================================================\n");

            System.out.print(GREEN + "Enter A Number: \n" + RESET);

            int option = Integer.parseInt(input.nextLine());
            switch (option){
                case 1:
                    System.out.print(GREEN + "Enter The Sender: \n" + RESET);
                    String sender=input.nextLine();
                    System.out.print(GREEN + "Enter The Message: \n" + RESET);
                    String msg = input.nextLine();

                    for (int i = 0; i < 20; i++) {
                        System.out.print(CYAN+"          \rAnalyzing " + spinner[i % spinner.length]+RESET);
                        Thread.sleep(80);
                    }
                    System.out.print("\r");

                    double spam = Analyzer.calculateSpamScore(msg);
                    double toxic = Analyzer.calculateToxicScore(msg);
                    String result = Analyzer.classify(spam, toxic);
                    Message message2 = new Message(sender,msg,spam,toxic,result);
                    message2.displayMessageInfo();
                    saveMessage(message2);
                    break;

                case 2:
                    System.out.println(GREEN+"1. View All Previous Messages");
                    System.out.println("2. Search By Keyword");
                    System.out.println("3. Delete A Record\n" +RESET);
                    System.out.print(GREEN + "Enter A Number: \n" + RESET);
                    int choice2 = Integer.parseInt(input.nextLine());
                    switch (choice2){
                        case 1:
                            for (int i = 0; i < 20; i++) {
                                System.out.print(CYAN+"          \rGetting All Messages " + spinner[i % spinner.length]+RESET);
                                Thread.sleep(80);
                            }
                            System.out.print("\r");

                            System.out.println(MAGENTA+"====================================================================================================================================================");
                            System.out.println(CYAN+"                                                        All Messages"+RESET);
                            System.out.println();
                            viewAllMessages();
                            System.out.println(MAGENTA+"====================================================================================================================================================\n");
                            break;

                        case 2:
                            System.out.print(GREEN + "Enter Keyword To Search\n" + RESET);
                            String keyword= input.nextLine();

                            for (int i = 0; i < 20; i++) {
                                System.out.print(CYAN+"          \rSearching " + spinner[i % spinner.length]+RESET);
                                Thread.sleep(80);
                            }
                            System.out.print("\r");

                            searchByKeyword(keyword);
                            break;

                        case 3:
                            System.out.print(GREEN + "Enter Message ID To Delete\n" + RESET);
                            int id= Integer.parseInt(input.nextLine());
                            System.out.println(GREEN+"Are You Sure You Want to Delete This Entry? (yes/no) "+RESET);
                            String dele=input.nextLine();
                            boolean del= dele.equalsIgnoreCase("yes");
                            if(del) {
                                for (int i = 0; i < 20; i++) {
                                    System.out.print(CYAN+"          \rDeleting " + spinner[i % spinner.length]+RESET);
                                    Thread.sleep(80);
                                }
                                System.out.print("\r");
                                 deleteMessage(id); }
                            else {
                               System.out.println(GREEN+"Understood!\n");
                            }
                            break;

                        default:
                            System.out.println("Invalid Input.\n");
                    }
                    break;

                case 3:
                    System.out.println(GREEN+"1. View Spam & Toxic Statistics");
                    System.out.println("2. View Top Words\n" +RESET);
                    System.out.print(GREEN + "Enter A Number: \n" + RESET);
                    int choice3 = Integer.parseInt(input.nextLine());
                    switch (choice3) {
                        case 1:
                            /*System.out.println(MAGENTA + "=====================================================");
                            System.out.println(CYAN + "                   Spam & Toxic Statistics ");*/
                            System.out.println();
                            for (int i = 0; i < 20; i++) {
                                System.out.print(CYAN + "          \rCalculating " + spinner[i % spinner.length] + RESET);
                                Thread.sleep(80);
                            }
                            System.out.print("\r");

                            showStatistics();
                            // System.out.println(MAGENTA + "=====================================================\n");
                            break;

                        case 2:
                            System.out.println();
                            for (int i = 0; i < 20; i++) {
                                System.out.print(CYAN + "          \rCalculating " + spinner[i % spinner.length] + RESET);
                                Thread.sleep(80);
                            }
                            System.out.print("\r");
                            topWords();
                    }
                    break;

                case 4:
                    System.out.print(GREEN + "Enter The Keyword: \n" + RESET);
                    String word = input.nextLine();
                    System.out.println(GREEN+"Enter Spam Weight (0-10): "+RESET);
                    int s_weight = Integer.parseInt(input.nextLine());
                    System.out.println(GREEN+"Enter Toxic Weight (0-10): "+RESET);
                    int t_weight = Integer.parseInt(input.nextLine());
                    for (int i = 0; i < 20; i++) {
                        System.out.print(CYAN+"          \rUpdating " + spinner[i % spinner.length]+RESET);
                        Thread.sleep(80);
                    }
                    System.out.print("\r");
                    addKeywordWeight(word,s_weight,t_weight);
                    break;

                case 5:
                    running=false;
                    System.out.println(CYAN+"Goodbye :(");
                    System.exit(0);
                    break;

                default:
                    System.out.println(GREEN+"Invalid Input.");
            }
        }
    }
}
