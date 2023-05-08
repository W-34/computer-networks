import java.io.*;
import java.net.*;

public class MultiWebServer {
    public static void main(String[] args) throws IOException {
        // 监听8080端口
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Listening on port 8080...");
        while (true) {
            // 等待客户端连接
            Socket clientSocket = serverSocket.accept();
            // 读取客户端发送的HTTP请求
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream())
            );
            String line, method = null, url = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.startsWith("GET")) {
                    String[] tokens = line.split(" ");
                    method = tokens[0];
                    url = tokens[1];
                }
                if (line.isEmpty()) {
                    break;
                }
            }

            // 发送HTTP响应
            PrintWriter writer = new PrintWriter(
                clientSocket.getOutputStream(), true
            );
            if (method == null || url == null) {
                // 如果请求不合法，返回400错误
                writer.println("HTTP/1.1 400 Bad Request");
                writer.println();
            } else if (!method.equals("GET")) {
                // 如果不是GET请求，返回501错误
                writer.println("HTTP/1.1 501 Not Implemented");
                writer.println();
            } else {
                // 根据URL返回相应的页面
                String fileName = "./project1/html/"+url.substring(1);
                File file = new File(fileName);
                if (!file.exists()) {
                    // 如果文件不存在，返回404错误
                    writer.println("HTTP/1.1 404 Not Found");
                    writer.println();
                } else {
                    // 发送文件内容作为HTTP响应
                    writer.println("HTTP/1.1 200 OK");
                    writer.println("Content-Type: text/html");
                    writer.println();
                    BufferedReader fileReader = new BufferedReader(
                        new FileReader(file)
                    );
                    String fileLine;
                    while ((fileLine = fileReader.readLine()) != null) {
                        writer.println(fileLine);
                    }
                    fileReader.close();
                }
            }
            writer.flush();
            // 关闭连接
            clientSocket.close();
        }
    }
}
