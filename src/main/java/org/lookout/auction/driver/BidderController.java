package org.lookout.auction.driver;

import java.util.List;

import org.lookout.auction.models.request.BidderPayload;
import org.lookout.auction.models.responses.BidderResponse;
import org.lookout.auction.services.BidderService;
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

@RestController
@RequestMapping("/lookout/v1/bidder")
public class BidderController {

	@Autowired
	private BidderService bidderService;
	
	public BidderController() {
		
	}
	
	//Add a new bidder to the system
	
	@PostMapping("/createBidder")
	public ResponseEntity<BidderResponse> createBidder(@RequestBody BidderPayload bidderPayload)
	{
		return new ResponseEntity<BidderResponse>(bidderService.createBidder(bidderPayload.getName()),HttpStatus.CREATED);
	}
	
	//Retrieve all existing bidders from the system
	
	@GetMapping("/fetchAllBidders")
	public ResponseEntity fetchAllBidders()
	{
		List<BidderResponse> res = bidderService.fetchAllBidders();
		if(res == null || res.size() == 0)
			return new ResponseEntity("No Bidders Exist",HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(res,HttpStatus.OK);
	}
	
	//Retrieve existing bidder from the system
	
	@GetMapping("/fetchBidder/{bidderId}")
	public ResponseEntity fetchBidder(@PathVariable int bidderId)
	{
		BidderResponse res = bidderService.fetchBidderById(bidderId);
		if(res == null)
			return new ResponseEntity("Bidder does not exist",HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(res,HttpStatus.OK);
	}
	
	//Update an existing auction in the system
	
	@RequestMapping(value = "/updateBidder/{bidderId}", method = RequestMethod.PUT)
	public ResponseEntity<BidderResponse> updateBidder(@PathVariable int bidderId, @RequestBody BidderPayload bidderPayload)
	{
		BidderResponse res = bidderService.updateBidder(bidderId, bidderPayload.getName());
		if(res == null)
			return new ResponseEntity("Bidder does not exist", HttpStatus.NOT_FOUND);
		return new ResponseEntity<BidderResponse>(res, HttpStatus.OK);
	}
	
	//Delete and existing bidder from the system
	
	@RequestMapping(value = "/deleteBidder/{bidderId}", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteBidder(@PathVariable int bidderId)
	{
		Boolean res = bidderService.deleteBidder(bidderId);
		if(!res)
			return new ResponseEntity("Bidder does not exist", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

}
