package org.lookout.auction.driver;

import java.util.List;

import org.lookout.auction.exceptions.NoWinnerException;
import org.lookout.auction.models.request.BidPayload.CreateBidPayload;
import org.lookout.auction.models.request.BidPayload.UpdateBidPayload;
import org.lookout.auction.models.responses.BidResponse;
import org.lookout.auction.models.responses.WinnerResponse;
import org.lookout.auction.services.BidService;
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
@RequestMapping("/lookout/v1/bid")
public class BidController {
	
	@Autowired
	private BidService bidService;
	
	public BidController() {
		
	}
	
	//Add a new bid to the system
	
	@PostMapping("/createBid")
	public ResponseEntity<BidResponse> createBid(@RequestBody CreateBidPayload bidPayload)
	{
		//validate auction id and bidder id
		if(!bidService.validateAuctionAndBidder(bidPayload.getAuctionId(), bidPayload.getBidderId()))
			return new ResponseEntity("Auction id OR Bidder id do not exist", HttpStatus.NOT_FOUND);
		
		//check if this bidder has already added a bid for this auction
		if(!bidService.validateDuplicateBidAttempt(bidPayload.getAuctionId(), bidPayload.getBidderId()))
			return new ResponseEntity("Bid already exists for provided auction and bidder", HttpStatus.FORBIDDEN);
		
		BidResponse res = bidService.createBid(bidPayload.getBidderId(), bidPayload.getAuctionId(), 
				bidPayload.getStartingBid(), bidPayload.getMaxBid(), bidPayload.getIncrementAmount());
			
		return new ResponseEntity<BidResponse>((BidResponse) res,HttpStatus.CREATED);
	}
	
	//Retrieve all existing bids from the system
	
	@GetMapping("/fetchAllBids")
	public ResponseEntity fetchAllBids()
	{
		List<BidResponse> res = bidService.fetchAllBids();
		if(res == null || res.size() == 0) 
			return new ResponseEntity("No Bids Exist", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(res, HttpStatus.OK);
	}
	
	//Retrieve existing bid from the system
	
	@GetMapping("/fetchBid/{bidId}")
	public ResponseEntity fetchBid(@PathVariable int bidId)
	{
		BidResponse res = bidService.fetchBidById(bidId);
		if(res == null) 
			return new ResponseEntity("Bid does not exist", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(res, HttpStatus.OK);
	}
	
	//Retrieve existing bids for a particular auction from the system
	
	@GetMapping("/fetchBidsByAuction/{auctionId}")
	public ResponseEntity fetchBidsByAuction(@PathVariable int auctionId)
	{
		List<BidResponse> res = bidService.fetchBidsByAuction(auctionId);
		if(res == null)
			return new ResponseEntity("Auction id does not exist", HttpStatus.NOT_FOUND);
		
		if(res.size() == 0)
			return new ResponseEntity("No Bids Exist", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(res, HttpStatus.OK);
	}
	
	//Update an existing bid in the system
	
	@RequestMapping(value = "/updateBid/{bidId}", method = RequestMethod.PUT)
	public ResponseEntity<BidResponse> updateBid(@PathVariable int bidId, @RequestBody UpdateBidPayload bidPayload)
	{
		BidResponse res = bidService.updateBid(bidId, bidPayload.getStartingBid(), bidPayload.getMaxBid(), bidPayload.getIncrementAmount());
		if(res == null)
			return new ResponseEntity("Bid does not exist", HttpStatus.NOT_FOUND);
		return new ResponseEntity<BidResponse>(res, HttpStatus.OK);
	}
	
	//Delete an existing bid from the system
	
	@RequestMapping(value = "/deleteBid/{bidId}", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteBid(@PathVariable int bidId)
	{
		Boolean res = bidService.deleteBid(bidId);
		if(!res)
			return new ResponseEntity("Bid does not exist", HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
	

	@GetMapping("/executeBidding/{auctionId}")
	public ResponseEntity executeBidding(@PathVariable int auctionId)
	{
		WinnerResponse res;
		try {
			res = bidService.executeBidding(auctionId);
		}
		catch(NoWinnerException ex) {
			return new ResponseEntity(ex, HttpStatus.CONFLICT);
		}
		if(res == null)
			return new ResponseEntity("Auction with ID : "+auctionId+" not found.",HttpStatus.NOT_FOUND);
		
		return new ResponseEntity(res,HttpStatus.OK);
	}

}
