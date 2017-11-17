package ID2212.Socket.Hangman;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class HangmanServer {

	private static ArrayList<String> words;

	public static void main(String[] args) {
		int port = 7788;
		String data;
		words = new ArrayList<>();
		if (args != null && args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
			while ((data = reader.readLine()) != null)
				words.add(data);
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {

			@SuppressWarnings("resource")
			ServerSocket socket = new ServerSocket(port);
			while (true) {
				Socket cSocket = socket.accept();
				HangmanRequestHandler ServerThread = new HangmanRequestHandler(words, cSocket);
				ServerThread.start();
				System.out.println("Client Connection Established");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
