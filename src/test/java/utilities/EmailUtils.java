package utilities;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;

/**
 * Emails the ExtentReport HTML file after a suite run, via SMTP.
 * Configured for Mailtrap's sandbox (safe for demos - nothing actually
 * leaves Mailtrap's fake inbox), but works with any standard SMTP host.
 *
 * A failure here (bad credentials, SMTP down) is logged and swallowed
 * rather than thrown - a broken mail step should never fail the test run
 * itself; the ExtentReport on disk is still the source of truth.
 *
 * Required environment variables (no defaults - see DBUtils for why):
 *   SMTP_HOST      e.g. sandbox.smtp.mailtrap.io
 *   SMTP_PORT      e.g. 2525
 *   SMTP_USERNAME  Mailtrap inbox username
 *   SMTP_PASSWORD  Mailtrap inbox password
 *   MAIL_FROM      e.g. qa-automation@zurich-demo.local
 *   MAIL_TO        e.g. you@example.com
 */
public class EmailUtils {

    private EmailUtils() {
        // static-access only
    }

    private static String requiredEnv(String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Required environment variable '" + key + "' is not set for email reporting.");
        }
        return value;
    }

    public static void sendReport(String reportPath, int passed, int failed, int skipped) {
        try {
            String host = requiredEnv("SMTP_HOST");
            String port = requiredEnv("SMTP_PORT");
            String username = requiredEnv("SMTP_USERNAME");
            String password = requiredEnv("SMTP_PASSWORD");
            String from = requiredEnv("MAIL_FROM");
            String to = requiredEnv("MAIL_TO");

            Properties props = new Properties();
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.port", port);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(buildSubject(passed, failed, skipped));

            MimeBodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(buildBody(passed, failed, skipped), "utf-8", "html");

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(bodyPart);

            if (reportPath != null && new File(reportPath).exists()) {
                long fileSizeBytes = new File(reportPath).length();
                // Mailtrap's free sandbox caps total message size at 5MB; base64-encoding an
                // attachment inflates it ~37%, so stay well under that with a safety margin.
                long maxAttachmentBytes = 3_000_000;

                if (fileSizeBytes <= maxAttachmentBytes) {
                    MimeBodyPart attachmentPart = new MimeBodyPart();
                    attachmentPart.attachFile(new File(reportPath));
                    multipart.addBodyPart(attachmentPart);
                } else {
                    System.out.println("[EmailUtils] Report is " + (fileSizeBytes / 1_000_000)
                            + "MB - too large to attach, sending summary only. Full report: " + reportPath);
                    MimeBodyPart noteBodyPart = new MimeBodyPart();
                    noteBodyPart.setText("<p><i>Full report not attached (file too large for this mailbox's limit). "
                            + "Available locally at: " + reportPath + "</i></p>", "utf-8", "html");
                    multipart.addBodyPart(noteBodyPart);
                }
            }

            message.setContent(multipart);
            Transport.send(message);
            System.out.println("[EmailUtils] Report emailed to " + to);

        } catch (IllegalStateException e) {
            System.err.println("[EmailUtils] Skipping report email - " + e.getMessage());
        } catch (MessagingException e) {
            System.err.println("[EmailUtils] Failed to send report email: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("[EmailUtils] Unexpected error while emailing report: " + e.getMessage());
        }
    }

    private static String buildSubject(int passed, int failed, int skipped) {
        String status = failed > 0 ? "FAILED" : "PASSED";
        return "[Zurich QA Demo] Test Run " + status + " - " + passed + " passed, " + failed + " failed, " + skipped + " skipped";
    }

    private static String buildBody(int passed, int failed, int skipped) {
        return "<html><body>"
                + "<h3>Zurich QA Automation Demo - Run Summary</h3>"
                + "<p><b>Passed:</b> " + passed + "<br>"
                + "<b>Failed:</b> " + failed + "<br>"
                + "<b>Skipped:</b> " + skipped + "</p>"
                + "<p>Full ExtentReport is attached.</p>"
                + "</body></html>";
    }
}