package org.lookout.auction.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.lookout.auction.models.request.AuctionPayload;
import org.lookout.auction.models.responses.AuctionResponse;
import org.lookout.auction.services.AuctionService;

@RestController
@RequestMapping("/lookout/v1/auction")
public class AuctionController {
	
	@Autowired
	private AuctionService auctionService;
	
	public AuctionController() {
		
	}
	
	//Add a new auction to the system
	
	@PostMapping("/createAuction")
	public ResponseEntity<AuctionResponse> createAuction(@RequestBody AuctionPayload auctionPayload)
	{
		return new ResponseEntity<AuctionResponse>(auctionService.createAuction(auctionPayload.getItem()), HttpStatus.CREATED);
	}
	
	//Retrieve all existing auctions from the system
	
	@GetMapping("/fetchAllAuctions")
	public ResponseEntity fetchAllAuctions()
	{
		List<AuctionResponse> res = auctionService.fetchAllAuctions();
		if(res == null || res.size() == 0) 
			return new ResponseEntity("No Auctions Exist", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(res, HttpStatus.OK);
	}
	
	//Retrieve existing auction from the system
	
	@GetMapping("/fetchAuction/{auctionId}")
	public ResponseEntity fetchAuction(@PathVariable int auctionId)
	{
		AuctionResponse res = auctionService.fetchAuctionById(auctionId);
		if(res == null) 
			return new ResponseEntity("Auction does not exist", HttpStatus.NOT_FOUND);
				
		return new ResponseEntity(res, HttpStatus.OK);
	}
	
	//Update an existing auction in the system
	
	@RequestMapping(value = "/updateAuction/{auctionId}", method = RequestMethod.PUT)
	public ResponseEntity<AuctionResponse> updateAuction(@PathVariable int auctionId, @RequestBody AuctionPayload auctionPayload)
	{
		AuctionResponse res = auctionService.updateAuction(auctionId, auctionPayload.getItem());
		if(res == null)
			return new ResponseEntity("Auction does not exist", HttpStatus.NOT_FOUND);
		return new ResponseEntity<AuctionResponse>(res, HttpStatus.OK);
	}
	
	//Delete an existing auction from the system
	
	@RequestMapping(value = "/deleteAuction/{auctionId}", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteAuction(@PathVariable int auctionId)
	{
		Boolean res = auctionService.deleteAuction(auctionId);
		if(!res)
			return new ResponseEntity("Auction does not exist", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	
}
