package auctionsniper;

//import javax.swing.SwingUtilities;

public class SwingThreadSniperListener {

	@SuppressWarnings("unused")
	private SnipersTableModel snipers;
	
	public SwingThreadSniperListener(SnipersTableModel snipers) {
				this.snipers = snipers;
	}

//	@Override
//	public void sniperStateChanged(final SniperSnapShot snapshot) {
//		SwingUtilities.invokeLater(new Runnable() {
//			public void run() {
//				snipers.sniperStatusChanged(snapshot);
//			}
//		});
//	}
}
