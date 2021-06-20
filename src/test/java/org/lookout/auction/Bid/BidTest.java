package org.lookout.auction.Bid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lookout.auction.AuctionApplication;
import org.lookout.auction.models.responses.AuctionResponse;
import org.lookout.auction.models.responses.BidResponse;
import org.lookout.auction.models.responses.BidderResponse;
import org.lookout.auction.services.AuctionService;
import org.lookout.auction.services.BidService;
import org.lookout.auction.services.BidderService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuctionApplication.class)
public class BidTest {

	private AuctionService auctionService;
	private BidderService bidderService;
	private BidService bidService;
	
	private AuctionResponse auction1;
	private AuctionResponse auction2;
	private BidderResponse bidder1;
	private BidderResponse bidder2;
	private BidderResponse bidder3;
	
	private static double startingBid = 100.00d;
	private static double maxBid = 250.00d;
	private static double incrementAmount = 10.00d;
	
	@BeforeEach
	public void setUp() {
		auctionService = new AuctionService();
		bidderService = new BidderService();
		bidService = new BidService();
		auction1 = auctionService.createAuction("Bicycle");
		auction2 = auctionService.createAuction("Scooter");
		bidder1 = bidderService.createBidder("Alice");
		bidder2 = bidderService.createBidder("Aaron");
		bidder3 = bidderService.createBidder("Amanda");
		bidService.deleteAllBids();
	}
	
	@Test
	public void createBidTest() {
		
		//prepare & act
		BidResponse res = bidService.createBid(bidder1.getId(), auction1.getId(), 50.00d, 80.00d, 3.00d);
		double startingBid = bidService.fetchBidById(res.getId()).getStartingBid();
		double maxBid = bidService.fetchBidById(res.getId()).getMaxBid();
		double incrementAmount = bidService.fetchBidById(res.getId()).getIncrementAmount();
		
		//assert
		Assert.assertEquals(50.00d, startingBid, 0d);
		Assert.assertEquals(80.00d, maxBid, 0d);
		Assert.assertEquals(3.00d, incrementAmount, 0d);
	}
	
	@Test
	public void fetchAllBidsTest() {
		
		//prepare
		BidResponse res1 = (BidResponse) bidService.createBid(bidder1.getId(), auction1.getId(), startingBid, maxBid, incrementAmount);
		BidResponse res2 = (BidResponse) bidService.createBid(bidder2.getId(), auction1.getId(), startingBid, maxBid, incrementAmount);
		BidResponse res3 = (BidResponse) bidService.createBid(bidder3.getId(), auction2.getId(), startingBid, maxBid, incrementAmount);
		List<Integer> bidsCreated = new ArrayList<>();
		bidsCreated.add(res1.getId());
		bidsCreated.add(res2.getId());
		bidsCreated.add(res3.getId());
		
		//act
		List<BidResponse> bids = bidService.fetchAllBids();
		List<Integer> bidsFetched = new ArrayList<>();
		for(BidResponse bid : bids) {
			bidsFetched.add(bid.getId());
		}
		
		//assert
		Assert.assertTrue(bidsFetched.containsAll(bidsCreated));
	}
	
	@Test
	public void fetchBidsByAuctionTest() {
		
		//prepare
		BidResponse res1 = (BidResponse) bidService.createBid(bidder1.getId(), auction1.getId(), startingBid, maxBid, incrementAmount);
		BidResponse res2 = (BidResponse) bidService.createBid(bidder1.getId(), auction2.getId(), startingBid, maxBid, incrementAmount);
		BidResponse res3 = (BidResponse) bidService.createBid(bidder2.getId(), auction2.getId(), startingBid, maxBid, incrementAmount);
		
		//act
		List<BidResponse> bids = bidService.fetchBidsByAuction(auction2.getId());
		
		Collections.sort(bids, new Comparator<BidResponse>() {
	        public int compare(BidResponse a1, BidResponse a2) {
	        return (a1.getId() < a2.getId() ? a1.getId() : a2.getId());
	        }
	    });
		
		//assert
		Assert.assertEquals(res2.getId(), bids.get(0).getId());
		Assert.assertEquals(res3.getId(), bids.get(1).getId());
	}
	
	@Test
	public void updateBidTest() {
		
		//prepare
		BidResponse res1 = (BidResponse) bidService.createBid(bidder1.getId(), auction1.getId(), startingBid, maxBid, incrementAmount);
		
		//act
		bidService.updateBid(res1.getId(), 120.00d, 270.00d, 20.00d);
		BidResponse bid = bidService.fetchBidById(res1.getId());
		
		//assert
		Assert.assertEquals(120.00d, bid.getStartingBid(), 0d);
		Assert.assertEquals(270.00d, bid.getMaxBid(), 0d);
		Assert.assertEquals(20.00d, bid.getIncrementAmount(), 0d);		
	}
	
	@Test
	public void deleteBidTest() {
		
		//prepare
		BidResponse res1 = (BidResponse) bidService.createBid(bidder1.getId(), auction1.getId(), startingBid, maxBid, incrementAmount);
		
		//act
		bidService.deleteBid(res1.getId());
		
		//assert
		Assert.assertFalse(bidService.checkIfBidExists(res1.getId()));
	}
	
}
