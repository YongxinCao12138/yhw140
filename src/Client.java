import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println(
                "Usage: java HelloClient <host name> <port number>");
            System.exit(1);
        }

		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		try (Socket myClientSocket = new Socket(hostName, portNumber)) {
			PrintWriter output = new PrintWriter(myClientSocket.getOutputStream(),true);
			BufferedReader input = new BufferedReader(new InputStreamReader(myClientSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

			while (true) {
				if (isServerClose(myClientSocket)) {
					System.out.println("server is close!");
					break;
				}
				System.out.println(input.readLine()); // reads the first message from the server and prints it
				String userInput = stdIn.readLine(); // reads user's input
				if (userInput.equals("quit") || userInput.equals("q")) {
					break;
				}
				output.println(userInput); // user's input transmitted to server
				// read simulation result
				System.out.println("simulation result: " + input.readLine()); // reads server's ack and prints it
			}
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host " + hostName);
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to " +
					hostName);
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * 判断是否断开连接，断开返回true,没有返回false
	 * @param socket
	 * @return
	 */
	public static Boolean isServerClose(Socket socket){
		try{
			socket.sendUrgentData(0xFF);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
			return false;
		}catch(Exception se){
			return true;
		}
	}
}
