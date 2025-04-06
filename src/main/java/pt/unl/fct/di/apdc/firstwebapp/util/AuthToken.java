package pt.unl.fct.di.apdc.firstwebapp.util;

import java.util.UUID;

public class AuthToken {

	public static final long EXPIRATION_TIME = 1000*60*60*2;
	
	public String username;
	public String tokenID;
	public long creationData;
	public long expirationData;
	public String role;
	public long validFrom;
	public long validTo;
	public String verifier;
	
	public AuthToken() {

	}
	
	public AuthToken(String username) {
		this.username = username;
		this.tokenID = UUID.randomUUID().toString();
		this.creationData = System.currentTimeMillis();
		this.expirationData = this.creationData - EXPIRATION_TIME;
	}

	public AuthToken(String username, String role) {
		this.username = username;
		this.role = role;
		this.tokenID = UUID.randomUUID().toString();
		this.validFrom = System.currentTimeMillis();
		this.validTo = validFrom + EXPIRATION_TIME;
		this.verifier = generateVerifier();
	}

	private String generateVerifier() {
		String raw = tokenID + validFrom + username;
		return Integer.toHexString(raw.hashCode());
	}
	
}
