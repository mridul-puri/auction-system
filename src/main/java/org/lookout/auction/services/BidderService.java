package org.lookout.auction.services;

import java.util.ArrayList;
import java.util.List;

import org.lookout.auction.dao.BidderDAO;
import org.lookout.auction.models.Bidder;
import org.lookout.auction.models.responses.BidderResponse;
import org.springframework.stereotype.Service;

@Service
public class BidderService {

		public BidderResponse createBidder(String name) {
			
			//create a bidder object
			Bidder bidder = new Bidder(name);
			
			//add object to memory
			BidderDAO.addBidder(bidder);
			
			//generate a response
			return new BidderResponse(bidder.getId(), bidder.getName());
		}
		
		public List<BidderResponse> fetchAllBidders() {
			List<BidderResponse> res = new ArrayList<>();
			
			//fetch all bidder objects from memory
			List<Bidder> bidders = BidderDAO.getAllBidders();
			
			//form response object
			for(Bidder bidder : bidders) {
				res.add(new BidderResponse(bidder.getId(), bidder.getName()));
			}
			
			return res;
		}
		
		public BidderResponse fetchBidderById(int bidderId) {
			Bidder bidder = BidderDAO.getBidderById(bidderId);
			if(bidder == null)
				return null;
			
			return new BidderResponse(bidder.getId(), bidder.getName());
		}
		
		public BidderResponse updateBidder(int bidderId, String name) {
			
			//update bidder to memory
			Bidder bidder = BidderDAO.updateBidder(bidderId, name);
			if(bidder == null)
				return null;
			
			//genearate a response
			return new BidderResponse(bidder.getId(), bidder.getName());
		}
		
		public boolean deleteBidder(int bidderId) {
			
			//delete bidder from memory
			return BidderDAO.deleteBidder(bidderId);
		}
		
		public void deleteAllBidders() {
			BidderDAO.deleteAllBidders();
		}
		
		public boolean checkIfBidderExists(int bidderId) {
			return BidderDAO.checkIfExists(bidderId);
		}
}
