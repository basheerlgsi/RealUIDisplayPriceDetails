package auctionsniper;

import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.States;
import org.junit.Test;

import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.SniperState;

public class AuctionSniperTest {
	private String itemId= "item-54321";
	private final Mockery context = new Mockery();
	private final SniperListener sniperListener = context.mock(SniperListener.class);
	private final Auction auction = context.mock(Auction.class);
	private final AuctionSniper sniper = new AuctionSniper(itemId,auction,sniperListener);

	private final States sniperState = context.states("sniper"); //1
	
	@Test
	public void reportsLostWhenAuctionClosesImmediately() { //2
		context.checking(new Expectations() {
			{
				one(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.LOST)));
			}
		});
		sniper.auctionClosed();
	}

	@Test
	public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
		final int price = 1001;
		final int increment = 25;
		final int bid = price + increment;
		context.checking(new Expectations() {
			{
				one(auction).bid(bid);
				atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapShot(itemId, price, bid,SniperState.BIDDING));
																	when(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
			}
		});
		sniper.currentPrice(price, increment,PriceSource.FromOtherBidder);
	}
	
	@Test
	public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
																	then(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged( new SniperSnapShot(itemId, 135, 135, SniperState.WINNING));
																	when(sniperState.is("bidding"));
				atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.WINNING)));
			}
		});
		
		sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
		sniper.currentPrice(135, 45, PriceSource.FromSniper);
	}

	@Test 
	public void reportsLostIfAuctionClosesWhenBidding() {
		context.checking(new Expectations() {
			{
				ignoring(auction); //3
				allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
												then(sniperState.is("bidding")); //4
				atLeast(1).of(sniperListener).sniperStateChanged(new SniperSnapShot(itemId, 123, 123, SniperState.LOST));
												when(sniperState.is("bidding")); //5
				atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.LOST)));
			}
		});
		
		sniper.currentPrice(123, 45, PriceSource.FromOtherBidder); //6
		sniper.auctionClosed();
	}
	
	
	@Test 
	public void reportsWonIfAuctionClosesWhenWinning() {
		context.checking(new Expectations() {
			{
				ignoring(auction);
				allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.WINNING)));
												then(sniperState.is("won"));
				atLeast(1).of(sniperListener).sniperStateChanged( new SniperSnapShot(itemId, 135, 135, SniperState.WON));
												when(sniperState.is("winning"));
				atLeast(1).of(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.WON)));								
			}
		});
		
		sniper.currentPrice(135, 45, PriceSource.FromSniper);
		sniper.auctionClosed();
	}
	
	private Matcher<SniperSnapShot> aSniperThatIs(final SniperState state) {
			return new FeatureMatcher<SniperSnapShot, SniperState>(
					equalTo(state), "sniper that is ", "was"){
			@Override
			protected SniperState featureValueOf(SniperSnapShot actual) {
				return actual.state;
			}
		};
	}
}
