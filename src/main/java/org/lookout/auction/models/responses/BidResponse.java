package org.lookout.auction.models.responses;

public class BidResponse {
	
	//model of bid output
	
	private final int id;
	private final double startingBid;
	private final double maxBid;
	private final double incrementAmount;
	
	public BidResponse(int id, double startingBid, double maxBid, double incrementAmount) {
		this.id = id;
		this.startingBid = startingBid;
		this.maxBid = maxBid;
		this.incrementAmount = incrementAmount;
	}

	public int getId() {
		return id;
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
