package ID2212.Socket.Hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import ID2212.Socket.Hangman.UserInterface;

public class HangmanClient {

	public static void main(String[] args) throws IOException {
		Socket socket;

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter Host Name: ");
		String host = br.readLine();

		System.out.print("Enter Host Port: ");
		int port = Integer.parseInt(br.readLine());

		if (args != null && args.length > 0) {
			host = args[0];
			if (args.length > 1) {
				try {
					port = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
		}
		try {
			socket = new Socket(host, port);
			UserInterface GUI = new UserInterface(socket);
			GUI.setupUserInterface(socket);
			GUI.start();
			System.out.println("start thread");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
