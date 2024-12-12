import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.util.Properties;

public class EmailChecker {
    public static void main(String[] args) {

        String host = "imap.gmail.com";
        String username = ""; // пошта
        String password = ""; // пароль від пошти

        int checkIntervalSeconds = 60;
        while (true) {
            checkEmails(host, username, password);
            try {
                Thread.sleep(checkIntervalSeconds * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void checkEmails(String host, String username, String password) {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imap");
        properties.put("mail.imap.host", host);
        properties.put("mail.imap.port", "993");
        properties.put("mail.imap.ssl.enable", "true");

        try {
            Session session = Session.getDefaultInstance(properties);
            Store store = session.getStore("imap");
            store.connect(username, password);

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            for (Message message : messages) {
                String from = InternetAddress.toString(message.getFrom());
                String subject = message.getSubject();
                System.out.println("From: " + from);
                System.out.println("Subject: " + subject);
                System.out.println("---------------------------------");
            }

            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}