/**
 * @author darkcells
 */
package com.darkcells.webserversimples;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Provider.Service;

public class WebServerSimles {

    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(10000);
            while (true) {
                Socket s = ss.accept();
                Service servico = new Service(s);
                servico.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class Service extends Thread {

        Socket socket = null;

        Service(Socket s) {
            socket = s;
        }//construtor

        public void run() {
            try {
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                String str = "";
                int recEndline = 0;

                String status = "HTTP/1.1 200 OK\rnn";
                String at1 = "Content-Type: text/html\rn\n";
                String at2 = "Server: java\r\n";
                String blank = "\r\n";
                String html = "<htmk><body>Web Server vivo</body></html>";
                String response = status + at1 + at2 + blank + html + blank + blank;

                while (true) {
                    int ch = bis.read();
                    str += (char) ch;
                    if (ch == 10) {
                        System.out.println(str);
                        str = "";
                    }
                    if ((ch == 10) || (ch == 13)) {
                        recEndline++;
                        if (recEndline == 3) {
                            bos.write(response.getBytes());
                            bos.flush();
                        }
                    } else {
                        recEndline = 0;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }//run
        }
    }
}
