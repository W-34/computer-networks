import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
public class EmailClient {
    public static void main(String[] args) {
        // 加载配置文件
        Properties properties = new Properties();
        InputStream inputStream = EmailClient.class.getClassLoader().getResourceAsStream("../mail.properties");
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 创建会话对象，用于和SMTP服务器通信
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.user"), properties.getProperty("mail.password"));
            }
        });

        try {
            // 创建邮件消息对象
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.user")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("收件人的邮箱地址"));
            message.setSubject("邮件主题");
            message.setText("邮件正文");

            // 发送邮件消息
            Transport.send(message);
            System.out.println("邮件发送成功！");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
