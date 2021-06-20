package org.lookout.auction.models.responses;

import org.lookout.auction.models.Auction;

public class AuctionResponse {
	
	//model of auction output
	
	private final int id;
	private final String item;
	private final Auction.AuctionStatus status;
	
	public AuctionResponse(int id, String item, Auction.AuctionStatus status) {
		this.id = id;
		this.item = item;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public String getItem() {
		return item;
	}

	public Auction.AuctionStatus getStatus() {
		return status;
	}
	
}
