package org.lookout.auction.models;

public class Auction {
	
	//model of an auction
	
	private static int inc = 0;
	public int id;
	public String item;
	public AuctionStatus status;
	public Bidder winner;
	public double winningAmount;
	
	public enum AuctionStatus {
		IN_PROGRESS, COMPLETED
	}
	
	public Auction(String item) {
		++inc;
		this.id = inc;
		this.item = item;
		this.status = AuctionStatus.IN_PROGRESS;
		this.winner = null;
		this.winningAmount = -1.00d;
	}

	public int getId() {
		return id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public AuctionStatus getStatus() {
		return status;
	}

	public void setStatus(AuctionStatus status) {
		this.status = status;
	}

	public Bidder getWinner() {
		return winner;
	}

	public void setWinner(Bidder winner) {
		this.winner = winner;
	}

	public double getWinningAmount() {
		return winningAmount;
	}

	public void setWinningAmount(double winningAmount) {
		this.winningAmount = winningAmount;
	}
	
}
