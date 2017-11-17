package ID2212.Socket.Hangman;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/*Created to compute the Hangman Code*/

public class HangmanRequestHandler {
	private ArrayList<String> wordlist;
	private Socket socket;

	private String currWord;
	private String guessedLetters;
	private int Remaining;
	private int score;

	public HangmanRequestHandler(ArrayList<String> wordlist, Socket socket) {
		this.wordlist = wordlist;
		this.socket = socket;
		this.score = 0;
	}

	// Gets a random word from the dictionary-"words.txt"
	private String getWordFromDictionary() {
		Random rand = new Random();
		return wordlist.get(rand.nextInt(wordlist.size()));
	}

	// Returns a dashed-word based on the guess the user made
	private String ReceiveWordFromServer() {
		if (guessedLetters.length() > 0) {
			return currWord.replaceAll("[^" + guessedLetters + "]", "-");
		} else {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < currWord.length(); i++) {
				sb.append("-");
			}
			return sb.toString();
		}
	}

	public void start() {
		try {
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			String str;
			while ((str = reader.readLine()) != null) {
				// When you press the new game key
				if (str.equals("start")) {
					currWord = getWordFromDictionary();
					Remaining = currWord.length();
					guessedLetters = "";
					StringBuilder sb = new StringBuilder();
					sb.append(ReceiveWordFromServer()).append(',').append("Guess the word!").append(',')
							.append(Remaining).append(',').append(score);
					writer.println(sb);
					writer.flush();
				}
				// When the correct word is found
				else if (str.equals(currWord)) {
					score++;
					StringBuilder sb = new StringBuilder();
					sb.append(currWord).append(',').append("Congratulations! You won!").append(',').append(Remaining)
							.append(',').append(score);
					writer.println(sb);
					writer.flush();
				}

				else if (str.length() == 1) {
					// When 1character is guessed by the user
					String receivedWord = ReceiveWordFromServer();
					guessedLetters += str;
					String updatedWord = ReceiveWordFromServer();

					// When the complete word is found
					if (updatedWord.equals(currWord)) {
						score++;
						StringBuilder sb = new StringBuilder();
						sb.append(currWord).append(',').append("Congratulations! You won!").append(',')
								.append(Remaining).append(',').append(score);
						writer.println(sb);
						writer.flush();
					} else if (receivedWord.equals(updatedWord)) {
						// If your Guess is wrong
						Remaining--;

						// If no of attempts remaining=0,Sorry, game done!
						if (Remaining == 0) {
							score--;
							StringBuilder sb = new StringBuilder();
							sb.append(currWord).append(',').append("Game Over!").append(',').append(Remaining)
									.append(',').append(score);
							writer.println(sb);
							writer.flush();
						} else {
							// If your guess was wrong!!
							StringBuilder sb = new StringBuilder();
							sb.append(updatedWord).append(',').append("Guess was wrong!!").append(',').append(Remaining)
									.append(',').append(score);
							writer.println(sb);
							writer.flush();
						}
					} else {
						// Guess is right!!
						StringBuilder sb = new StringBuilder();
						sb.append(updatedWord).append(',').append("Guess was right!!").append(',').append(Remaining)
								.append(',').append(score);
						writer.println(sb);
						writer.flush();
					}
				} else {
					Remaining--;

					if (Remaining == 0) {
						score--;
						StringBuilder sb = new StringBuilder();
						sb.append(currWord).append(',').append("Game Over!").append(',').append(Remaining).append(',')
								.append(score);
						writer.println(sb);
						writer.flush();
					} else {
						StringBuilder sb = new StringBuilder();
						sb.append(ReceiveWordFromServer()).append(',').append("Guess was wrong!!").append(',')
								.append(Remaining).append(',').append(score);
						writer.println(sb);
						writer.flush();
					}
				}
			}

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
