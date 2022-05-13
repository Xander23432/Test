package com.realnet.externalServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceModel {
	private String urlConsumed;
	private String apiKey;
	private String authType;
	private boolean isAuthenticated;
	private String authUrl; //token url
	private String redirectUrl;
	//BasicAuth
	private String username;
	private String pass;
	//JWT
	private String jwtToken;
	//OAuth2
	private String logoutUrl;
	private String userIdentified;
	private String authorizationUrl;
	private String grantType;
	private String scope;
	private String clientSecret;
	private String clientId;
	private String accessToken;
	
}
