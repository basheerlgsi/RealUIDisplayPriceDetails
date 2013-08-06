package auctionsniper;

import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import auctionsniper.Column;

@SuppressWarnings("serial")
public class SnipersTableModel extends AbstractTableModel implements SniperListener{		
	private static String[] STATUS_TEXT = { Main.STATUS_JOINING, Main.STATUS_BIDDING, Main.STATUS_WINNING, Main.STATUS_LOST, Main.STATUS_WON};
	
	private final static SniperSnapShot STARTING_UP = new SniperSnapShot("item-54321", 0, 0,SniperState.JOINING);
	private SniperSnapShot snapshot = STARTING_UP;
	
	public int getColumnCount() { 
		return Column.values().length; 
		}
	
	public int getRowCount() { 
		return 1; 
	}


	public void sniperStatusChanged(SniperSnapShot newSnapshot) {
		this.snapshot = newSnapshot;	
		fireTableRowsUpdated(0, 0);
		
	}
	public static String textFor(SniperState state) {
		return STATUS_TEXT[state.ordinal()];
		}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return Column.at(columnIndex).valueIn(snapshot);
	}
	
	@Override public String getColumnName(int column) {
		return Column.at(column).name;
		}

	@Override
	public void sniperStateChanged(final SniperSnapShot snapshot) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				sniperStatusChanged(snapshot); 
				}
			});
	}
}