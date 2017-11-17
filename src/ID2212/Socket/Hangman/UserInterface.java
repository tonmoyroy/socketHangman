package ID2212.Socket.Hangman;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class UserInterface {
	public JLabel score;
	public JLabel attempts;
	public JTextField answer;
	public JButton send;
	public JLabel wordDisp;;
	public JLabel message;
	public JButton newgame;
	private Socket socket;
	public JPanel toppanel, middlepanel, bottompanel, panel;
	PrintWriter pw;

	public UserInterface(Socket socket) {
		super();
		this.socket = socket;
	}

	public void sendMessage(String msg) {
		PrintWriter pw;
		try {
			pw = new PrintWriter(socket.getOutputStream());
			pw.println(msg);
			System.out.println("message is" + " " + msg);
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void setUIFont(FontUIResource f) {
		Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				FontUIResource orig = (FontUIResource) value;
				Font font = new Font(f.getFontName(), orig.getStyle(), f.getSize());
				UIManager.put(key, new FontUIResource(font));
			}
		}
	}

	public void setupUserInterface(Socket soc) {
		setUIFont(new FontUIResource(new Font("Arial", 0, 12)));
		score = new JLabel("SCORE:");
		attempts = new JLabel("REMAINING:");
		answer = new JTextField();
		answer.setColumns(100);
		send = new JButton("SEND");
		send.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String answerval = answer.getText();
				if (answerval.length() > 0) {
					answer.setText("");
					sendMessage(answerval);
				}
				answer.requestFocus();
			}
		});
		wordDisp = new JLabel();
		message = new JLabel();
		newgame = new JButton("NEW GAME");
		newgame.setPreferredSize(new Dimension(200, 50));
		newgame.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				sendMessage("start");
				answer.requestFocus();
			}
		});

		toppanel = new JPanel();
		middlepanel = new JPanel();
		bottompanel = new JPanel();
		toppanel.setLayout(new BoxLayout(toppanel, BoxLayout.Y_AXIS));
		toppanel.setPreferredSize(new Dimension(200, 50));
		toppanel.setBackground(Color.blue);
		toppanel.setForeground(Color.WHITE);
		toppanel.add(score).setForeground(Color.white);
		toppanel.add(attempts).setForeground(Color.white);

		middlepanel.setPreferredSize(new Dimension(200, 100));
		middlepanel.setBackground(Color.pink);
		middlepanel.setLayout(new BoxLayout(middlepanel, BoxLayout.LINE_AXIS));
		middlepanel.add(message).setForeground(Color.blue);

		middlepanel.add(wordDisp);
		middlepanel.add(Box.createHorizontalGlue());
		middlepanel.add(newgame);

		bottompanel.add(answer);
		bottompanel.add(send);
		panel = new JPanel(new BorderLayout());
		panel.add(toppanel, BorderLayout.PAGE_START);
		panel.add(middlepanel, BorderLayout.CENTER);
		panel.add(bottompanel, BorderLayout.PAGE_END);

		JFrame frame = new JFrame("Hangman");
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}

	public void start() {
		String str;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while ((str = reader.readLine()) != null) {
				String[] msg = str.split(",");
				System.out.println("The message contents:" + str);
				wordDisp.setText(msg[0]);
				System.out.println("Message[0]" + msg[0]);
				message.setText(msg[1].toUpperCase());
				System.out.println("Message[1]" + msg[1]);
				attempts.setText("REMAINING: " + msg[2]);
				System.out.println("Message[2]" + msg[2]);
				score.setText("SCORE: " + msg[3]);
				System.out.println("Message[3]" + msg[3]);
			}
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
