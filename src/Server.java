import lcr.LCR;
import lcr.Process;
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

			while (true) { //in order to serve multiple clients but sequentially, one after the other
				// get client
				Socket aClientSocket = myServerSocket.accept();
				PrintWriter output = new PrintWriter(aClientSocket.getOutputStream(), true);
				BufferedReader input = new BufferedReader(new InputStreamReader(aClientSocket.getInputStream()));

				System.out.println("Connection established with a new client with IP address: " + aClientSocket.getInetAddress());

				while (true) {
					if (isServerClose(aClientSocket)) {
						System.out.println("Connection with client " + aClientSocket.getInetAddress() + " is now closing...");
						break;
					}

					output.println("please input a number for simulation process number");
					String message = null;
					try {
						message = input.readLine();
					} catch (IOException e) {
						// 防止客户端异常退出，服务器端无法处理也异常退出
						System.out.println("Connection with client " + aClientSocket.getInetAddress() + " is now closing...");
					}

					// check client message
					if (!StringUtil.isEmpty(message)) {
						System.out.println("Client says: " + message);
						if (message.equals("quit") || message.equals("q")) {
							System.out.println("Connection with client " + aClientSocket.getInetAddress() + " is now closing...");
							break;
						}

						// 默认集群数量
						int defaultNumProcessor = 10;
						try {
							defaultNumProcessor = Integer.parseInt(message);
						} catch (NumberFormatException e) {
							System.out.println("the processor number is illegal. Use the default number 10");
						} finally {
							Process process = LCR.startRandom(defaultNumProcessor);
							output.println("lcr result!" + process.getLeaderId());
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
