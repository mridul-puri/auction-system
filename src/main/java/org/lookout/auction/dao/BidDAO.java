package org.lookout.auction.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lookout.auction.models.Bid;
import org.springframework.stereotype.Repository;

@Repository
public class BidDAO {
	public static Map<Integer, Bid> list_of_bids = new HashMap<>();
	
	public static void addBid(Bid bid) {
		list_of_bids.put(bid.getId(), bid);
	}
	
	public static List<Bid> getAllBids() {
		List<Bid> bids = new ArrayList<>();
		for(Bid bid : list_of_bids.values()) {
			bids.add(bid);
		}
		return bids;
	}
	
	public static Bid getBidById(int id) {
		return list_of_bids.get(id);
	}
	
	public static List<Bid> getBidsByAuctionId(int auctionId) {
		List<Bid> bids = new ArrayList<>();
		
		//travers all bids and compare auction id
		for(Map.Entry<Integer, Bid> bidEntry : list_of_bids.entrySet()) {
			Bid bid = bidEntry.getValue();
			if(bid.getAuctionId() == auctionId) 
				bids.add(bid);
		}
		
		return bids;
	}
	
	public static Bid updateBid(int id, double startingBid, double maxBid, double incrementAmount) {
		if(list_of_bids.containsKey(id)) {
			
			//fetch bid
			Bid bid = list_of_bids.get(id);
			
			//update new values
			bid.setStartingBid(startingBid);
			bid.setMaxBid(maxBid);
			bid.setIncrementAmount(incrementAmount);
			
			//put bid back
			list_of_bids.put(id, bid);
			return bid;
		}
		
		return null;
	}
	
	public static boolean deleteBid(int id) {
		if(list_of_bids.containsKey(id)) {
			list_of_bids.remove(id);
			return true;
		}
		
		return false;
	}
	
	public static void deleteAllBids() {
		list_of_bids.clear();
	}
	
	public static boolean checkIfExists(int id) {
		return list_of_bids.containsKey(id);
	}
	
	public static boolean validateAuctionAndBidder(int auctionId, int bidderId) {
		if(!AuctionDAO.checkIfExists(auctionId) || !BidderDAO.checkIfExists(bidderId))
			return false;
		
		return true;
	}
	
	public static boolean validateDuplicateBidAttempt(int auctionId, int bidderId) {
		for(Bid b : list_of_bids.values()) {
			if(b.getAuctionId() == auctionId && b.getBidderId() == bidderId) 
				return false;
		}
		
		return true;
	}
}
