package endtoend;

import auctionsniper.Main;
import auctionsniper.SniperState;
import static auctionsniper.SniperState.JOINING;
import static auctionsniper.SnipersTableModel.textFor;

public class ApplicationRunner {

	public static final String SNIPER_ID = "sniper";
	public static final String SNIPER_PASSWORD = "sniper";
	public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";
	private AuctionSniperDriver driver;
	Thread thread;
	private String itemId;
	
	public ApplicationRunner()
	{
		
	}
	
	public void startBiddingIn(final FakeAuctionServer auction){
		itemId = auction.getItemId();
		
			thread = new Thread("Test Application"){
			@Override
			public void run() {
				try {
					Main.main(FakeAuctionServer.XMPP_HOSTNAME, SNIPER_ID,
							SNIPER_PASSWORD, itemId);
					}
				 catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		
		thread.setDaemon(true);
		thread.start();
		
		driver = new AuctionSniperDriver(1000);
		driver.hasTitle("Auction Sniper");
		driver.hasColumnTitles();
		driver.showsSniperStatus(itemId, 0, 0, textFor(SniperState.JOINING));
	}

	public void stop() throws InterruptedException {
		if (driver != null) {
			driver.dispose();
		}

	}

	public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid, Main.STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning(int winningBid) {
		driver.showsSniperStatus(itemId, winningBid, winningBid, Main.STATUS_WINNING);
		
	}

	public void showsSniperHasWonAuction(int lastPrice) {
		driver.showsSniperStatus(itemId, lastPrice, lastPrice, Main.STATUS_WON);
		
	}
	
	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(itemId, 0, 0, Main.STATUS_LOST);
	}

	public void hasShownSniperIsBiddingAndLost(int lastPrice, int lastBid) {
		driver.showsSniperStatus(itemId, lastPrice, lastBid, Main.STATUS_LOST);
	}
}
