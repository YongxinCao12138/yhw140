import java.net.*;
import java.io.*;
import java.util.List;

public class Server {
	public static void main(String[] args) throws IOException {

		int portNumber;

        if (args.length < 1) {
			System.out.println("Warning: You have provided no arguments\nTrying to connect to the default port 8000...");
			portNumber = 8000;
        } else if (args.length == 1) {
			portNumber = Integer.parseInt(args[0]);
		} else {
			System.out.println("Warning: You have provided > 1 arguments\nTrying with the first argument to connect to a port...");
			portNumber = Integer.parseInt(args[0]);
		}

		while(true){ //in order to serve multiple clients but sequentially, one after the other
			try (
				ServerSocket myServerSocket = new ServerSocket(portNumber);
				Socket aClientSocket = myServerSocket.accept();
				PrintWriter output = new PrintWriter(aClientSocket.getOutputStream(),true);
				BufferedReader input = new BufferedReader(new InputStreamReader(aClientSocket.getInputStream()));
			) {
				System.out.println("Connection established with a new client with IP address: " + aClientSocket.getInetAddress() + "\n");
//				output.println("Server says: Hello Client " + aClientSocket.getInetAddress() + ". This is server " + myServerSocket.getInetAddress() +
//				" speaking. Our connection has been successfully established!");
				output.println("Server says: Hello Client " + aClientSocket.getInetAddress() + ". This is server " + myServerSocket.getInetAddress() +
				" speaking. please input a number for simulation process number");
//
//				String inputLine = input.readLine();
//				System.out.println("Received a new message from client " + aClientSocket.getInetAddress());
//				System.out.println("Client says: " + inputLine);
//				output.println("Server says: Your message has been successfully received! Bye...");
				/**
				 * 集群个数
				 */
				int num_processor = 10;
				try {
					num_processor = Integer.parseInt(input.readLine());
				} catch (NumberFormatException e) {
					output.println("the processor number is illegal. Use the default number 10");
				} finally {
					Process process = LCR.startRandom(num_processor);
					// todo: get result from lrc
					System.out.println();
					System.out.println("Connection with client " + aClientSocket.getInetAddress() + " is now closing...\n");
				}
			} catch (IOException e) {
				System.out.println("Exception caught when trying to listen on port "
				+ portNumber + " or listening for a connection");
				System.out.println(e.getMessage());
			}
		}
	}
}
