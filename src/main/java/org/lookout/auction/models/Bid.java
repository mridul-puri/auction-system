package org.lookout.auction.models;

import java.sql.Timestamp;

public class Bid {
	
	//model of a bid
	
	private static int inc = 0;
	public int id;
	public int bidderId;
	public int auctionId;
	public double startingBid;
	public double maxBid;
	public double incrementAmount;
	public Timestamp timeOfEntry;
	
	public Bid(int bidderId, int auctionId, double startingBid, double maxBid, double incrementAmount, Timestamp timeOfEntry) {
		++inc;
		this.id = inc;
		this.bidderId = bidderId;
		this.auctionId = auctionId;
		this.startingBid = startingBid;
		this.maxBid = maxBid;
		this.incrementAmount = incrementAmount;
		this.timeOfEntry = timeOfEntry;
	}

	public int getId() {
		return id;
	}

	public int getBidderId() {
		return bidderId;
	}

	public int getAuctionId() {
		return auctionId;
	}

	public double getStartingBid() {
		return startingBid;
	}

	public void setStartingBid(double startingBid) {
		this.startingBid = startingBid;
	}

	public double getMaxBid() {
		return maxBid;
	}

	public void setMaxBid(double maxBid) {
		this.maxBid = maxBid;
	}

	public double getIncrementAmount() {
		return incrementAmount;
	}

	public void setIncrementAmount(double incrementAmount) {
		this.incrementAmount = incrementAmount;
	}

	public Timestamp getTimeOfEntry() {
		return timeOfEntry;
	}
	
}
