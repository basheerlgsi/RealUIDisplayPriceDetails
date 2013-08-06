package auctionsniper;

public enum Column {
	ITEM_IDENTIFIER("Item"){
	@Override 
	public Object valueIn(SniperSnapShot snapshot) {
			return snapshot.itemId;
		}
	},
	LAST_PRICE("Last Price"){
	@Override 
	public Object valueIn(SniperSnapShot snapshot) {
			return snapshot.lastPrice;
		}
	},
	LAST_BID ("Last Bid"){
	@Override 
	public Object valueIn(SniperSnapShot snapshot) {
			return snapshot.lastBid;
		}
	},
	SNIPER_STATE("State"){
	@Override 
	public Object valueIn(SniperSnapShot snapshot) {
			return SnipersTableModel.textFor(snapshot.state);
		}
	};
	
	public final String name;

	abstract public Object valueIn(SniperSnapShot snapshot);

	private Column(String name) {
		this.name = name;
		}
	public static Column at(int columnIndex) {
		if(0==columnIndex) return ITEM_IDENTIFIER;
		if(1==columnIndex) return LAST_PRICE;
		if(2==columnIndex) return LAST_BID;
		else return SNIPER_STATE;
	}
}