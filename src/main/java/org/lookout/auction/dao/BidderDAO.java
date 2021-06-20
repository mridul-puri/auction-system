package org.lookout.auction.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lookout.auction.models.Bidder;
import org.springframework.stereotype.Repository;

@Repository
public class BidderDAO {
	public static Map<Integer, Bidder> list_of_bidders = new HashMap<>();
	
	public static void addBidder(Bidder bidder) {
		list_of_bidders.put(bidder.getId(), bidder);
	}
	
	public static List<Bidder> getAllBidders() {
		List<Bidder> bidders = new ArrayList<>();
		for(Bidder bidder : list_of_bidders.values()) {
			bidders.add(bidder);
		}
		return bidders;
	}
	
	public static Bidder getBidderById(int id) {
		return list_of_bidders.get(id);
	}
	
	public static Bidder updateBidder(int id, String name) {
		if(list_of_bidders.containsKey(id)) {
			
			//fetch bidder
			Bidder bidder = list_of_bidders.get(id);
			
			//set new value
			bidder.setName(name);
			
			//put bidder back
			list_of_bidders.put(id, bidder);
			return bidder;
		}
		
		return null;
	}
	
	public static boolean deleteBidder(int id) {
		if(list_of_bidders.containsKey(id)) {
			
			//remove bidder from memory
			list_of_bidders.remove(id);
			return true;
		}
		
		return false;
	}
	
	public static void deleteAllBidders() {
		list_of_bidders.clear();
	}
	
	public static boolean checkIfExists(int id) {
		return list_of_bidders.containsKey(id);
	}
}
