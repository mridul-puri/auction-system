package org.lookout.auction.services;

import java.util.ArrayList;
import java.util.List;

import org.lookout.auction.dao.AuctionDAO;
import org.lookout.auction.models.Auction;
import org.lookout.auction.models.responses.AuctionResponse;
import org.springframework.stereotype.Service;

@Service
public class AuctionService {
	
		public AuctionResponse createAuction(String item) {
			
			//create an auction object
			Auction auction = new Auction(item);
			
			//add object to memory
			AuctionDAO.addAuction(auction);
			
			//generate a response
			return new AuctionResponse(auction.getId(), auction.getItem(), auction.getStatus());
		}
		
		public List<AuctionResponse> fetchAllAuctions() {
			List<AuctionResponse> res = new ArrayList<>();
			
			//fetch all auction objects from memory
			List<Auction> auctions = AuctionDAO.getAllAuctions();
			
			//form response object
			for(Auction auction : auctions) {
				res.add(new AuctionResponse(auction.getId(), auction.getItem(), auction.getStatus()));
			}

			return res;
		}
		
		public AuctionResponse fetchAuctionById(int auctionId) {
			Auction auction = AuctionDAO.getAuctionById(auctionId);
			if(auction == null)
				return null;
			
			return new AuctionResponse(auction.getId(), auction.getItem(), auction.getStatus());
		}
		
		public AuctionResponse updateAuction(int auctionId, String item) {
			
			//update auction to memory
			Auction auction = AuctionDAO.updateAuction(auctionId, item);
			if(auction == null)
				return null;
			
			//generate a response
			return new AuctionResponse(auction.getId(), auction.getItem(), auction.getStatus());
		}
		
		public boolean deleteAuction(int auctionId) {
			
			//delete auction from memory
			return AuctionDAO.deleteAuction(auctionId);
		}
		
		public void deleteAllAuctions() {
			AuctionDAO.deleteAllAuctions();
		}
		
		public boolean checkIfAuctionExists(int auctionId) { 
			return AuctionDAO.checkIfExists(auctionId);
		}
		
}
