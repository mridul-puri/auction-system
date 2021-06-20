package org.lookout.auction.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lookout.auction.models.Auction;
import org.lookout.auction.models.Bidder;
import org.springframework.stereotype.Repository;

@Repository
public class AuctionDAO {
	public static Map<Integer, Auction> list_of_auctions = new HashMap<>();
	
	public static void addAuction(Auction auction) {
		list_of_auctions.put(auction.getId(), auction);
	}
	
	public static List<Auction> getAllAuctions() {
		List<Auction> auctions = new ArrayList<>();
		for(Auction auction : list_of_auctions.values()) {
			auctions.add(auction);
		}
		return auctions;
	}
	
	public static Auction getAuctionById(int id) {
		return list_of_auctions.get(id);
	}
	
	public static Auction updateAuction(int id, String item) {
		if(list_of_auctions.containsKey(id)) {
			
			//fetch auction
			Auction auction = list_of_auctions.get(id);
			
			//set new value
			auction.setItem(item);
			
			//put auction back
			list_of_auctions.put(id, auction);
			
			return auction;
		}
		
		return null;
	}
	
	public static boolean deleteAuction(int id) {
		if(list_of_auctions.containsKey(id)) {
			
			//remove auction from memory
			list_of_auctions.remove(id);
			return true;
		}
		
		return false;
	}
	
	public static void deleteAllAuctions() {
		list_of_auctions.clear();
	}
	
	public static boolean checkIfExists(int id) {
		return list_of_auctions.containsKey(id);
	}
	
	//Set auction's status based on auction id
	
	public static void setAuctionStatus(int id, Auction.AuctionStatus status) {
		if(list_of_auctions.containsKey(id)) {
			Auction auction = list_of_auctions.get(id);
			auction.setStatus(status);
			list_of_auctions.put(id, auction);
		}
	}
	
	//Set auction's winning bidder and amount of winning bid based on auction id
	
	public static void setAuctionWinner(int id, Bidder winningBidder, double winningAmount) {
		if(list_of_auctions.containsKey(id)) {
			Auction auction = list_of_auctions.get(id);
			auction.setWinner(winningBidder);
			auction.setWinningAmount(winningAmount);
			list_of_auctions.put(id, auction);
		}
	}
}
