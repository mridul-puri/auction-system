package org.lookout.auction.models;

public class Bidder {
	
	//model of a bidder
	
	private static int inc = 0;
	public int id;
	public String name;
	
	public Bidder(String name) {
		++inc;
		this.id = inc;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
