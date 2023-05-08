import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.smtp.*;
public class EmailClient {
    public static void main(String[] args) throws IOException{
        // 加载配置文件
        Properties properties = new Properties();
        FileInputStream inputStream = new FileInputStream("D:/.vscode/java2023/mail.properties");
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
            message.setFrom(new InternetAddress(properties.getProperty("mail.user")+"@qq.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("20281022@bjtu.edu.cn"));
            message.setSubject("test");
            message.setText("smtp client test");

            // 发送邮件消息
            SMTPTransport t = (SMTPTransport)session.getTransport("smtp");
            Socket socket = new Socket("smtp.qq.com", 587);
            t.connect(socket);
            t.sendMessage(message, message.getAllRecipients());
            System.out.println("邮件发送成功！");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
