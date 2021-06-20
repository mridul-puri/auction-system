package org.lookout.auction.models.responses;

public class BidderResponse {
	
	//model of bidder output
	
	private final int id;
	private final String name;
	
	public BidderResponse(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

}
