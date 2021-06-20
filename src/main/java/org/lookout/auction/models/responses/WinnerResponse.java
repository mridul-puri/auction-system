package org.lookout.auction.models.responses;

import org.lookout.auction.models.Bidder;

public class WinnerResponse {
	
	//model of winner
	
	private final Bidder winner;
	private final double winning_amount;
	
	public WinnerResponse(Bidder winner, double winning_amount) {
		this.winner = winner;
		this.winning_amount = winning_amount;
	}

	public Bidder getWinner() {
		return winner;
	}

	public double getWinning_amount() {
		return winning_amount;
	}

}
