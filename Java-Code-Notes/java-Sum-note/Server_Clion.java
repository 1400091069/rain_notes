```java
package Day01;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class dome04 {
    static HashSet<OutputStream> outputStreams = new HashSet<>();

public static void main(String[] args) throws IOException {

//        服务端
        ServerSocket serverSocket = new ServerSocket(4545, 10, InetAddress.getLocalHost());

    while (true) {
        Socket accept = serverSocket.accept();
        InputStream inputStream = accept.getInputStream();
        OutputStream outputStream = accept.getOutputStream();

        File file = new File("C:\\Login");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File("C:\\Login\\Login_logs.txt");
        if (!file1.exists()) {
            file1.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(file1,true);
        outputStreams.add(outputStream);
        InetAddress inetAddress = accept.getInetAddress();
        String hostAddress = "IP: "+inetAddress.getHostAddress() + "    HostName: " + inetAddress.getHostName() + "    Port: " + accept.getPort();
        String targtPrint = hostAddress + "    在线状态:上线了！( ‘-ωก̀ )    日期: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(new Date());

//            打印状态信息到文件上
            fileWriter.append("\r\n"+targtPrint);
            fileWriter.flush();
//              输出状态信息
            System.out.println(targtPrint);
            Server01 server01 = new Server01(inputStream, outputStream, outputStreams, hostAddress,fileWriter);
            server01.start();

    }
}


}

class Server01 extends Thread {
    InputStream inputStream;
    OutputStream outputStream;
    HashSet<OutputStream> outputStreams;
    String hostAddress;
    FileWriter fileWriter;

public Server01(InputStream inputStream, OutputStream outputStream, HashSet<OutputStream> outputStreams, String hostAddress, FileWriter fileWriter) {
    this.inputStream = inputStream;
    this.outputStream = outputStream;
    this.outputStreams = outputStreams;
    this.hostAddress = hostAddress;
    this.fileWriter = fileWriter;
}

public Server01(InputStream inputStream, OutputStream outputStream, HashSet<OutputStream> outputStreams, String hostAddress) {
    this.inputStream = inputStream;
    this.outputStream = outputStream;
    this.outputStreams = outputStreams;
    this.hostAddress = hostAddress;
}

@Override
public void run() {
    while (true) {
        byte[] bytes = new byte[1024];
        try {
            int read = inputStream.read(bytes);
            String s = new String(bytes, 0, read);
            for (OutputStream outputStreams : outputStreams) {
                if (outputStreams == outputStream) {
                    outputStreams.write("消息发送成功！（づ￣3￣）づ╭❤～ ".getBytes());
                    outputStreams.flush();
                    continue;
                } else {
                    outputStreams.write(s.getBytes());
                    outputStreams.flush();
                }
            }
            File file6 = new File("C:\\Message");
            if (!file6.exists()) {
                file6.mkdirs();
            }
            File files = new File("C:\\Message\\record.txt");
            if (!files.exists()) {
                files.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(files,true);
            fileWriter.append("\r\n"+hostAddress+":   "+s);
            fileWriter.flush();
            System.out.println(hostAddress + ":    " + s);
        } catch (IOException e) {
            String df = hostAddress + " + 已经下线！( ˘•ω•˘ ) + " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E").format(new Date());
            System.err.println(df);
            try {
                fileWriter.append("\r\n"+df);
                fileWriter.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return;
        }
        super.run();
    }
}

}
//======================================================================================================================
//客户端
class dome05 {
    public static void main(String[] args) throws IOException {
        Socket socket = null;
        try {
            while (true) {
                int i = new Random().nextInt(9999);
                if (i > 999) {
                    socket = new Socket("192.168.217.11", 4545, InetAddress.getLocalHost(), new Random().nextInt(9999));
                    break;
                }else {
                    continue;
                }
            }
        } catch (IOException e) {
            System.err.println("服务器连接失败！");
            return;
        }
        OutputStream outputStream = socket.getOutputStream();
        InputStream inputStream = socket.getInputStream();
        Scanner scanner = new Scanner(System.in);
        new Shou(inputStream).start();
        while (true) {
//            System.out.println("请输入：");
            outputStream.write(scanner.next().getBytes());
        }
    }
}

class Shou extends Thread {
    InputStream inputStream;

public Shou(InputStream inputStream) {
    this.inputStream = inputStream;
}

@Override
public void run() {
    while (true) {
        byte[] bytes = new byte[1024];
        try {
            int read = inputStream.read(bytes);
            System.out.println(new String(bytes, 0, read));
        } catch (IOException e) {
            System.err.println("已经与服务器断开连接。。。");
            System.out.println("友情提示：请重新启动程序并连接服务器！");
            return;
        }
        super.run();
    }
}

}
```

