import lcr.LCR;
import lcr.Process;
import util.SocketUtil;
import util.StringUtil;

import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {

		int portNumber;

		if (args.length < 1) {
			System.out.println("Warning: You have provided no arguments\nTrying to connect to the default port 8000...");
			portNumber = 8001;
		} else if (args.length == 1) {
			portNumber = Integer.parseInt(args[0]);
		} else {
			System.out.println("Warning: You have provided > 1 arguments\nTrying with the first argument to connect to a port...");
			portNumber = Integer.parseInt(args[0]);
		}

		ServerSocket myServerSocket = null;
		try {
			myServerSocket = new ServerSocket(portNumber);

			//in order to serve multiple clients but sequentially, one after the other
			while (true) {
				// get client
				Socket aClientSocket = myServerSocket.accept();
				PrintWriter output = new PrintWriter(aClientSocket.getOutputStream(), true);
				BufferedReader input = new BufferedReader(new InputStreamReader(aClientSocket.getInputStream()));

				System.out.println("Connection established with a new client with IP address: " + aClientSocket.getInetAddress());

				while (true) {
					if (SocketUtil.isConnectClose(aClientSocket)) {
						System.out.println("Connection with client " + aClientSocket.getInetAddress() + " is now closing...");
						break;
					}

					output.println("please input a number for simulation process number");
					String message = null;
					try {
						message = input.readLine();
					} catch (IOException e) {
						// Prevent abnormal exit of the client, even if the server cannot handle it
						System.out.println("Connection with client " + aClientSocket.getInetAddress() + " is now closing...");
					}

					// check client message
					if (!StringUtil.isEmpty(message)) {
						System.out.println("Client says: " + message);
						if (message.equals("quit") || message.equals("q")) {
							System.out.println("Connection with client " + aClientSocket.getInetAddress() + " is now closing...");
							break;
						}

						// default processor number
						int defaultNumProcessor = 10;
						try {
							defaultNumProcessor = Integer.parseInt(message);
						} catch (NumberFormatException e) {
							System.out.println("the processor number is illegal. Use the default number 10");
						} finally {
							Process process = LCR.startRandom(defaultNumProcessor);
							output.println("lcr result: " + process.getLeaderId() + " is the leader!");
						}
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
			e.printStackTrace();
		} finally {
			if (myServerSocket != null) {
				myServerSocket.close();
			}
		}
	}
}
