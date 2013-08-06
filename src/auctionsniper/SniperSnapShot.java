package auctionsniper;

public class SniperSnapShot {

		public final String itemId;
		public final int lastPrice;
		public final int lastBid;
		public final SniperState state;
		
	public SniperSnapShot(String itemId, int lastPrice, int lastBid, SniperState State) {
		this.itemId = itemId;
		this.lastPrice = lastPrice;
		this.lastBid = lastBid;
		this.state = State;
	}
	
	public SniperSnapShot bidding(int newLastPrice, int newLastBid) {
		return new SniperSnapShot(itemId, newLastPrice, newLastBid, SniperState.BIDDING);
	}
	
	public SniperSnapShot winning(int newLastPrice) {
		return new SniperSnapShot(itemId, newLastPrice, lastBid, SniperState.WINNING);
	}
	
	public static SniperSnapShot joining(String itemId) {
		return new SniperSnapShot(itemId, 0, 0, SniperState.JOINING);
	}

	public SniperSnapShot closed() {
		return new SniperSnapShot(itemId, lastPrice, lastBid, state.whenAuctionClosed());
	}
}

