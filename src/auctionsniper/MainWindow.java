package auctionsniper;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

public class MainWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String SNIPER_STATUS_NAME = "sniper status";
	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	private  SnipersTableModel snipers;
	
	public MainWindow(SnipersTableModel snipers) {
		super("Auction Sniper");
		this.snipers =snipers;
		setName(MAIN_WINDOW_NAME);
		fillContentPane(makeSnipersTable());
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void fillContentPane(JTable snipersTable) {
			final Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());
			contentPane.add(new JScrollPane(snipersTable), BorderLayout.CENTER);
		}
		private JTable makeSnipersTable() {
			final JTable snipersTable = new JTable(snipers);
			snipersTable.setName(SNIPER_STATUS_NAME);
			snipersTable.setBorder(new LineBorder(Color.BLACK));
			return snipersTable;
		}
		
		public void sniperStateChanged(SniperSnapShot snapshot) {
			snipers.sniperStateChanged(snapshot);
		}
}
