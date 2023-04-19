import util.SocketUtil;

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

			// multiple input message
			while (true) {
				if (SocketUtil.isConnectClose(myClientSocket)) {
					System.out.println("server is close!");
					break;
				}
				// read server message
				System.out.println(input.readLine());
				// reads user's input
				String userInput = stdIn.readLine();
				if (userInput.equals("quit") || userInput.equals("q")) {
					output.println(userInput);
					break;
				}
				// user's input processor number to server
				output.println(userInput);
				// read simulation result
				System.out.println(input.readLine());
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
}
