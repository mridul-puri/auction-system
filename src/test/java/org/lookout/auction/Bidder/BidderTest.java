package org.lookout.auction.Bidder;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lookout.auction.AuctionApplication;
import org.lookout.auction.models.responses.BidderResponse;
import org.lookout.auction.services.BidderService;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuctionApplication.class)
public class BidderTest {

	private BidderService service;
	
	@BeforeEach
	public void setUp() {
		service = new BidderService();
		service.deleteAllBidders();
	}
	
	@Test
	public void createBidderTest() {
		
		//prepare & act
		BidderResponse res = service.createBidder("Alice");
		String bidderName = service.fetchBidderById(res.getId()).getName();
		
		//assert
		Assert.assertEquals("Alice", bidderName);
	}
	
	@Test
	public void fetchAllBiddersTest() {
		
		//prepare
		BidderResponse res1 = service.createBidder("Alice");
		BidderResponse res2 = service.createBidder("Aaron");
		BidderResponse res3 = service.createBidder("Amanda");
		List<Integer> biddersCreated = new ArrayList<>();
		biddersCreated.add(res1.getId());
		biddersCreated.add(res2.getId());
		biddersCreated.add(res3.getId());
		
		//act
		List<BidderResponse> bidders = service.fetchAllBidders();
		List<Integer> biddersFetched = new ArrayList<>();
		for(BidderResponse bidder : bidders) {
			biddersFetched.add(bidder.getId());
		}
		
		//assert
		Assert.assertTrue(biddersFetched.containsAll(biddersCreated));
	}
	
	@Test
	public void updateBidderTest() {
		
		//prepare
		BidderResponse res1 = service.createBidder("Alice");
		
		//act
		service.updateBidder(res1.getId(), "Aaron");
		BidderResponse bidder = service.fetchBidderById(res1.getId());
		
		//assert
		Assert.assertEquals("Aaron", bidder.getName());
	}
	
	@Test
	public void deleteBidderTest() {
		
		//prepare
		BidderResponse res1 = service.createBidder("Amanda");
		
		//act
		service.deleteBidder(res1.getId());
		
		//assert
		Assert.assertFalse(service.checkIfBidderExists(res1.getId()));
	}
	
}
