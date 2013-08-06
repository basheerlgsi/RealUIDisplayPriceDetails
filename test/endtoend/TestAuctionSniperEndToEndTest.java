package endtoend;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAuctionSniperEndToEndTest {
	private  FakeAuctionServer auction;
	private  ApplicationRunner application;
	@Before
	public void setUp() throws Exception {
		 auction = new FakeAuctionServer("item-54321");
		 application = new ApplicationRunner();
	}
	
	@After
	public void tearDown() throws Exception {
		auction.stop();
		auction = null;
		application.stop();
		application =null;
	}

	@Test //1st end-end test
	public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
		auction.startSellingItem(); // Step 1
		application.startBiddingIn(auction); // Step 2
		auction.hasReceivedJoinRequestFromSniper(); // Step 3
		auction.announceClosed(); // Step 4
		application.showsSniperHasLostAuction(); // Step 5
	}
	
	//2nd End-End test
	@Test 
	public void sniperMakesAHigherBidButLoses() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(1000,1098);
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.announceClosed();
		application.hasShownSniperIsBiddingAndLost(1000,1098);
	}

	@Test //3rd End-End test
	public void sniperWinsAnAuctionByBiddingHigher() throws Exception {
		auction.startSellingItem();
		application.startBiddingIn(auction);
		auction.hasReceivedJoinRequestFromSniper(ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1000, 98, "other bidder");
		application.hasShownSniperIsBidding(1000, 1098); // last price, last bid-Towards a Real User Interface
		//application.hasShownSniperIsBidding();
		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
		auction.reportPrice(1098, 97, ApplicationRunner.SNIPER_XMPP_ID);//new cases
		//application.hasShownSniperIsWinning();//new cases
		application.hasShownSniperIsWinning(1098); // winning bid-Towards a Real User Interface
		auction.announceClosed();
		application.showsSniperHasWonAuction(1098);//new cases
		//application.hasShownSniperIsWinning(1098); // // last price-Towards a Real User Interface
	}
	
}
