package org.lookout.auction.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.lookout.auction.dao.AuctionDAO;
import org.lookout.auction.dao.BidDAO;
import org.lookout.auction.dao.BidderDAO;
import org.lookout.auction.exceptions.NoWinnerException;
import org.lookout.auction.models.Auction;
import org.lookout.auction.models.Bid;
import org.lookout.auction.models.Bidder;
import org.lookout.auction.models.responses.BidResponse;
import org.lookout.auction.models.responses.WinnerResponse;
import org.springframework.stereotype.Service;

@Service
public class BidService {
	
		public BidResponse createBid(int bidderId, int auctionId, double startingBid, double maxBid, double incrementAmount) {
			
			//create a bid object
			Bid bid = new Bid(bidderId, auctionId, startingBid, maxBid, incrementAmount, new Timestamp(System.currentTimeMillis()));
			
			//add object to memory
			BidDAO.addBid(bid);

			//generate a response
			return new BidResponse(bid.getId(), bid.getStartingBid(), bid.getMaxBid(), bid.getIncrementAmount());
		}
		
		public List<BidResponse> fetchAllBids() {
			List<BidResponse> res = new ArrayList<>();
			
			//fetch all bids
			List<Bid> bids = BidDAO.getAllBids();
			
			//form a response
			for(Bid bid : bids) {
				res.add(new BidResponse(bid.getId(), bid.getStartingBid(), bid.getMaxBid(), bid.getIncrementAmount()));
			}
			
			return res;
		}
		
		public BidResponse fetchBidById(int bidId) {
			Bid bid = BidDAO.getBidById(bidId);
			if(bid == null)
				return null;
			
			return new BidResponse(bid.getId(), bid.getStartingBid(), bid.getMaxBid(), bid.getIncrementAmount());
		}
		
		public List<BidResponse> fetchBidsByAuction(int auctionId) {
			if(!AuctionDAO.checkIfExists(auctionId))
				return null;
			
			List<BidResponse> res = new ArrayList<>();
			
			//fetch bids by auction id
			List<Bid> bids = BidDAO.getBidsByAuctionId(auctionId);
			
			//form a response
			for(Bid bid : bids) {
				res.add(new BidResponse(bid.getId(), bid.getStartingBid(), bid.getMaxBid(), bid.getIncrementAmount()));
			}
			
			return res;
		}
		
		public BidResponse updateBid(int bidId, double startingBid, double maxBid, double incrementAmount) {
			
			//update bid to memory
			Bid bid = BidDAO.updateBid(bidId, startingBid, maxBid, incrementAmount);
			if(bid == null)
				return null;
			
			//generate a response
			return new BidResponse(bid.getId(), bid.getStartingBid(), bid.getMaxBid(), bid.getIncrementAmount());
		}
		
		public boolean deleteBid(int bidId) {
			
			//delete bid from memory
			return BidDAO.deleteBid(bidId);
		}
	
		public void deleteAllBids() {
			BidDAO.deleteAllBids();
		}
		
		public boolean checkIfBidExists(int bidId) {
			return BidDAO.checkIfExists(bidId);
		}
		
		public boolean validateAuctionAndBidder(int auctionId, int bidderId) {
			return BidDAO.validateAuctionAndBidder(auctionId, bidderId);
		}
		
		public boolean validateDuplicateBidAttempt(int auctionId, int bidderId) {
			return BidDAO.validateDuplicateBidAttempt(auctionId, bidderId);
		}
		
		public WinnerResponse executeBidding(int auctionId) throws NoWinnerException {
			
			//validate auction id
			if(!AuctionDAO.checkIfExists(auctionId))
				return null;
			//get auction object from memory
			Auction auction = AuctionDAO.getAuctionById(auctionId);
			
			//return winner if auction already processed
			if(auction.status == Auction.AuctionStatus.COMPLETED) {
				return new WinnerResponse(auction.getWinner(), auction.getWinningAmount());
			}
			
			//get all bids in auction
			List<Bid> bids = BidDAO.getBidsByAuctionId(auctionId);
			
			Pair<Bid, Double> winner = null;
			try {
				winner = findWinner(bids);
			} catch(NoWinnerException ex) {
				throw ex;
			}
			
			Bidder winningBidder = getBidderFromBid(winner.getLeft());
			double winningAmount = winner.getRight();
			
			//update auction status and winner once processed
			AuctionDAO.setAuctionStatus(auctionId, Auction.AuctionStatus.COMPLETED);
			AuctionDAO.setAuctionWinner(auctionId, winningBidder, winningAmount);
			
			return new WinnerResponse(winningBidder, winningAmount);
		}
		
		//master method to process the bidding
		private Pair<Bid, Double> findWinner(List<Bid> bids) throws NoWinnerException {
			Map<Bid, Double> maxBidsMapping = new HashMap<Bid, Double>();
			
			//form a map of bid object and the maximum possible value of bid
			for (Bid participant : bids) {
		        double maxMinusStarting = participant.getMaxBid() - participant.getStartingBid();
		        if (maxMinusStarting % participant.getIncrementAmount() == 0) {
		        	maxBidsMapping.put(participant, participant.getMaxBid());
		        } else {
		        	maxBidsMapping.put(participant, participant.getMaxBid() - (maxMinusStarting % participant.getIncrementAmount()));
		        }
		    }

			//convert map to list
		    List<Map.Entry<Bid, Double>> maxBidsMappingList = new LinkedList<Map.Entry<Bid, Double>>(maxBidsMapping.entrySet());

		    //sort the list in ascending order of maximum possible value of bid
		    Collections.sort(maxBidsMappingList, new Comparator<Map.Entry<Bid, Double>>() {
		        public int compare(Map.Entry<Bid, Double> o1, Map.Entry<Bid, Double> o2) {
		        return (o1.getValue()).compareTo(o2.getValue());
		        }
		    });
		    
		    //top two contenders will be at the end of list
		    Map.Entry<Bid, Double> winningEntry = maxBidsMappingList.get(maxBidsMappingList.size() - 1);
		    Map.Entry<Bid, Double> secondPlaceEntry = maxBidsMappingList.get(maxBidsMappingList.size() - 2);
		    Bid winningBid = winningEntry.getKey();

		    //check for tie on max possible value of bid
		    boolean tie = isTie(winningEntry, secondPlaceEntry);

		    if (tie) {
		    	
		    	//tie condition reached
		    	
		    	Map.Entry<Bid, Double> tieWinner = null;
		    	try {
		    		tieWinner = determineByTimestamp(winningEntry, secondPlaceEntry);
		    	} catch(NoWinnerException ex) {
		    		throw ex;
		    	}
		    	
		    	return Pair.of(tieWinner.getKey(), tieWinner.getValue());
		    }
		    
		    //calculate winner
		    double secondPlaceMaxBet = secondPlaceEntry.getValue();
		    
		    double quotient = (secondPlaceMaxBet - winningBid.getStartingBid()) / winningBid.getIncrementAmount();
		    
		    Double winnigAmount = winningBid.getStartingBid() + ((int) quotient + 1) * winningBid.getIncrementAmount();
			return Pair.of(winningEntry.getKey(), winnigAmount);
		}
		
		private boolean isTie(Map.Entry<Bid, Double> winningEntry, Map.Entry<Bid, Double> secondPlaceEntry) {
		    if (winningEntry.getValue().equals(secondPlaceEntry.getValue())) {
		        return true;
		    }
		    
		    return false;
		}
		
		//Tie Breaker
		private Map.Entry<Bid, Double> determineByTimestamp(Map.Entry<Bid, Double> winningEntry,
		        Map.Entry<Bid, Double> secondPlaceEntry) throws NoWinnerException {
			Bid firstPlace = winningEntry.getKey();
			Bid secondPlace = secondPlaceEntry.getKey();
			
		    if (firstPlace.getTimeOfEntry().compareTo(secondPlace.getTimeOfEntry()) < 0) {
		        return winningEntry;
		    } else if(firstPlace.getTimeOfEntry().compareTo(secondPlace.getTimeOfEntry()) > 0) {
		        return secondPlaceEntry;
		    } else {
		    	//Handling corner case to make fail-safe
		    	throw new NoWinnerException("There was no winner in this auction : Tie of Timestamp");
		    }
		}
		
		private Bidder getBidderFromBid(Bid bid) {
			return BidderDAO.getBidderById(bid.getBidderId());
		}
}
