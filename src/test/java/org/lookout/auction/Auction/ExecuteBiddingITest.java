package org.lookout.auction.Auction;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lookout.auction.AuctionApplication;
import org.lookout.auction.exceptions.NoWinnerException;
import org.lookout.auction.models.Auction;
import org.lookout.auction.models.Auction.AuctionStatus;
import org.lookout.auction.models.responses.AuctionResponse;
import org.lookout.auction.models.responses.BidderResponse;
import org.lookout.auction.models.responses.WinnerResponse;
import org.lookout.auction.services.AuctionService;
import org.lookout.auction.services.BidService;
import org.lookout.auction.services.BidderService;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuctionApplication.class)
public class ExecuteBiddingITest {
	
	private AuctionService auctionService;
	private BidderService bidderService;
	private BidService bidService;
	
	@BeforeEach
	public void setUp() {
		auctionService = new AuctionService();
		bidderService = new BidderService();
		bidService = new BidService();
		
	}
	
	/* Integeration Tests for sample auctions */
	
	@Test
	public void testBicycleAuction() throws NoWinnerException {
		
		//create an auction
		AuctionResponse auction = auctionService.createAuction("Bicycle");
		
		//add bidders and bids
		BidderResponse bidder1 = bidderService.createBidder("Alice");
		bidService.createBid(bidder1.getId(), auction.getId(), 50.00d, 80.00d, 3.00d);
		
		BidderResponse bidder2 = bidderService.createBidder("Aaron");
		bidService.createBid(bidder2.getId(), auction.getId(), 60.00d, 82.00d, 2.00d);
		
		BidderResponse bidder3 = bidderService.createBidder("Amanda");
		bidService.createBid(bidder3.getId(), auction.getId(), 55.00d, 85.00d, 5.00d);
		
		//find winner
		WinnerResponse winner = bidService.executeBidding(auction.getId());
		
		//assert winner
		Assert.assertEquals(bidder3.getId(), winner.getWinner().getId());
		Assert.assertEquals(85.00d, winner.getWinning_amount(), 0d);
		
		//assert auction status
		Assert.assertEquals(AuctionStatus.COMPLETED, auctionService.fetchAuctionById(auction.getId()).getStatus());
	}
	
	@Test
	public void testScooterAuction() throws NoWinnerException {
		
		//create an auction
		AuctionResponse auction = auctionService.createAuction("Scooter");
		
		//add bidders and bids
		BidderResponse bidder1 = bidderService.createBidder("Alice");
		bidService.createBid(bidder1.getId(), auction.getId(), 700.00d, 725.00d, 2.00d);
		
		BidderResponse bidder2 = bidderService.createBidder("Aaron");
		bidService.createBid(bidder2.getId(), auction.getId(), 599.00d, 725.00d, 15.00d);
		
		BidderResponse bidder3 = bidderService.createBidder("Amanda");
		bidService.createBid(bidder3.getId(), auction.getId(), 625.00d, 725.00d, 8.00d);
		
		//find winner
		WinnerResponse winner = bidService.executeBidding(auction.getId());
		
		//assert winner
		Assert.assertEquals(bidder1.getId(), winner.getWinner().getId());
		Assert.assertEquals(722.00d, winner.getWinning_amount(), 0d);
		
		//assert auction status
		Assert.assertEquals(AuctionStatus.COMPLETED, auctionService.fetchAuctionById(auction.getId()).getStatus());
	}
	
	@Test
	public void testBoatAuction() throws NoWinnerException {
		
		//create an auction
		AuctionResponse auction = auctionService.createAuction("Boat");
		
		//add bidders and bids
		BidderResponse bidder1 = bidderService.createBidder("Alice");
		bidService.createBid(bidder1.getId(), auction.getId(), 2500.00d, 3000.00d, 500.00d);
		
		BidderResponse bidder2 = bidderService.createBidder("Aaron");
		bidService.createBid(bidder2.getId(), auction.getId(), 2800.00d, 3100.00d, 201.00d);
		
		BidderResponse bidder3 = bidderService.createBidder("Amanda");
		bidService.createBid(bidder3.getId(), auction.getId(), 2501.00d, 3200.00d, 247.00d);
		
		//find winner
		WinnerResponse winner = bidService.executeBidding(auction.getId());
		
		//assert winner
		Assert.assertEquals(bidder2.getId(), winner.getWinner().getId());
		Assert.assertEquals(3001.00d, winner.getWinning_amount(), 0d);
		
		//assert auction status
		Assert.assertEquals(AuctionStatus.COMPLETED, auctionService.fetchAuctionById(auction.getId()).getStatus());
	}
	
	@Test
	public void testInvalidAuction() throws NoWinnerException {
		Assert.assertEquals(null, bidService.executeBidding(1));
	}
}
