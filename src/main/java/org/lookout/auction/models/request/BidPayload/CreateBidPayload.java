package org.lookout.auction.models.request.BidPayload;

public class CreateBidPayload {
	
	//model of create bid input
	
	private int bidderId;
	private int auctionId;
	private double startingBid;
	private double maxBid;
	private double incrementAmount;

	public int getBidderId() {
		return bidderId;
	}

	public int getAuctionId() {
		return auctionId;
	}

	public double getStartingBid() {
		return startingBid;
	}

	public double getMaxBid() {
		return maxBid;
	}

	public double getIncrementAmount() {
		return incrementAmount;
	}
	
}
