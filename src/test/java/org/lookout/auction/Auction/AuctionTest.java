package org.lookout.auction.Auction;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.lookout.auction.AuctionApplication;
import org.lookout.auction.models.responses.AuctionResponse;
import org.lookout.auction.services.AuctionService;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AuctionApplication.class)
public class AuctionTest {

	private AuctionService service;
	
	@BeforeEach
	public void setUp() {
		service = new AuctionService();
		service.deleteAllAuctions();
	}
	
	@Test
	public void createAuctionTest() {
		
		//prepare & act
		AuctionResponse res = service.createAuction("Bicycle");
		String auctionItem = service.fetchAuctionById(res.getId()).getItem();
		
		//assert
		Assert.assertEquals(auctionItem, "Bicycle");
	}
	
	@Test
	public void fetchAllAuctionsTest() {

		//prepare
		AuctionResponse res1 = service.createAuction("Bicycle");
		AuctionResponse res2 = service.createAuction("Scooter");
		AuctionResponse res3 = service.createAuction("Boat");
		List<Integer> auctionsCreated = new ArrayList<>();
		auctionsCreated.add(res1.getId());
		auctionsCreated.add(res2.getId());
		auctionsCreated.add(res3.getId());
		
		//act
		List<AuctionResponse> auctions = service.fetchAllAuctions();
		List<Integer> auctionsFetched = new ArrayList<>();
		for(AuctionResponse auction : auctions) {
			auctionsFetched.add(auction.getId());
		}
		
		//assert
		Assert.assertTrue(auctionsFetched.containsAll(auctionsCreated));
	}
	
	@Test
	public void updateAuctionTest() {
		
		//prepare
		AuctionResponse res1 = service.createAuction("Motorcycle");
		
		//act
		service.updateAuction(res1.getId(), "Car");
		AuctionResponse auction = service.fetchAuctionById(res1.getId());
		
		//assert
		Assert.assertEquals("Car", auction.getItem());
	}
	
	@Test
	public void deleteAuctionTest() {
		
		//prepare
		AuctionResponse res1 = service.createAuction("Table");
		
		//act
		service.deleteAuction(res1.getId());
		
		//assert
		Assert.assertFalse(service.checkIfAuctionExists(res1.getId()));
	}
	
}
