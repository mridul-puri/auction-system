package org.lookout.auction.models.request.BidPayload;

public class UpdateBidPayload {
	
	//model of update bid input

	private double startingBid;
	private double maxBid;
	private double incrementAmount;

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
