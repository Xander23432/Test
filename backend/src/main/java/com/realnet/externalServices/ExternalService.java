package com.realnet.externalServices;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ExternalService {
    private RestTemplate restTemplate;
	@Autowired
    public ExternalService(RestTemplateBuilder restTemplateBuilder) {
        RestTemplate restTemplate = restTemplateBuilder
          .errorHandler(new RestTemplateResponseErrorHandler())
          .build();
    }

	@SuppressWarnings("deprecation")
	public ResponseEntity<?> oAuth2Service(ServiceModel serviceModel) {
		HttpPost post = new HttpPost(serviceModel.getAuthUrl());
		String clientId =serviceModel.getClientId();
		String clientSecret = serviceModel.getClientSecret();
		//String scope = "https://www.googleapis.com/auth/gmail.modify";
	//	String code1 = "4/0AX4XfWhn1Le92LE7izW7cWvVdYK0MprSVz6FpFoxP-ssEJW9lKYLXoCwkKsH5UoVhdislw";
		String redirect_uri = serviceModel.getRedirectUrl();
		List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
		parametersBody.add(new BasicNameValuePair("grant_type",
				"client_credentials"));
		
		parametersBody.add(new BasicNameValuePair("client_id",
					clientId));
		
		parametersBody.add(new BasicNameValuePair(
					"client_secret", clientSecret));
			
//		parametersBody.add(new BasicNameValuePair("scope",
//					scope));
//		parametersBody.add(new BasicNameValuePair("code",code1));
		parametersBody.add(new BasicNameValuePair("redirect_uri",redirect_uri));
		parametersBody.add(new BasicNameValuePair("access_type","offline"));

		DefaultHttpClient client = new DefaultHttpClient();
		HttpResponse response = null;
		String accessToken = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(parametersBody, HTTP.UTF_8));

			response = client.execute(post);
			int code = response.getStatusLine().getStatusCode();
			if (code == 401) {
				System.out
						.println("Authorization server expects Basic authentication");
				// Add Basic Authorization header
				post.addHeader(
						"Authorization","Basic "+encodeCredentials(clientId,clientSecret));
				System.out.println("Retry with client credentials");
				post.releaseConnection();
				response = client.execute(post);
				code = response.getStatusLine().getStatusCode();
				if (code == 401 || code == 403) {
					System.out.println("Could not authenticate using client credentials.");
					throw new RuntimeException(
								"Could not retrieve access token for client: "
										+ clientId);
					
				}

			}
			Map<String, String> map = handleResponse(response);
			accessToken = map.get("access_token");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(accessToken);
		
//		RestTemplate restTemplate = new RestTemplate();
		//consuming api
		 RestTemplate restTemplate = new RestTemplateBuilder()
		          .errorHandler(new RestTemplateResponseErrorHandler())
		          .build();
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(accessToken);
		HttpEntity<String> entity = new HttpEntity<>("body", headers);

		ResponseEntity<String> o = restTemplate.exchange(serviceModel.getUrlConsumed(), HttpMethod.GET, entity, String.class);
		client.close();
		return o;
	}
	private Map<String, String> handleResponse(HttpResponse response) {
		// TODO Auto-generated method stub
		String contentType = "application/json";
		if (response.getEntity().getContentType() != null) {
			contentType = response.getEntity().getContentType().getValue();
		}
		if (contentType.contains("application/json")) {
			return handleJsonResponse(response);
		} else if (contentType.contains("application/x-www-form-urlencoded")) {
			return handleURLEncodedResponse(response);
		} else if (contentType.contains("application/xml")) {
			return null;
		} else {
			// Unsupported Content type
			throw new RuntimeException(
					"Cannot handle "
							+ contentType
							+ " content type. Supported content types include JSON, XML and URLEncoded");
		}
	}

	private Map<String, String> handleURLEncodedResponse(HttpResponse response) {
		Map<String, Charset> map = Charset.availableCharsets();
		Map<String, String> oauthResponse = new HashMap<String, String>();
		Set<Map.Entry<String, Charset>> set = map.entrySet();
		Charset charset = null;
		HttpEntity entity = (HttpEntity) response.getEntity();

		System.out.println();
		System.out.println("********** Response Received **********");

		for (Map.Entry<String, Charset> entry : set) {
			System.out.println(String.format("  %s = %s", entry.getKey(),
					entry.getValue()));
			if (entry.getKey().equalsIgnoreCase(HTTP.UTF_8)) {
				charset = entry.getValue();
			}
		}

		try {
			List<NameValuePair> list = URLEncodedUtils.parse(
					EntityUtils.toString((org.apache.http.HttpEntity) entity), Charset.forName(HTTP.UTF_8));
			for (NameValuePair pair : list) {
				System.out.println(String.format("  %s = %s", pair.getName(),
						pair.getValue()));
				oauthResponse.put(pair.getName(), pair.getValue());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Could not parse URLEncoded Response");
		}

		return oauthResponse;
	}
	private Map<String, String> handleJsonResponse(HttpResponse response) {
		Map<String, String> oauthLoginResponse = null;
		String contentType = response.getEntity().getContentType().getValue();
		try {
			oauthLoginResponse = (Map<String, String>) new JSONParser()
					.parse(EntityUtils.toString(response.getEntity()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException();
		}
		System.out.println();
		System.out.println("********** Response Received **********");
		for (Map.Entry<String, String> entry : oauthLoginResponse.entrySet()) {
			System.out.println(String.format("  %s = %s", entry.getKey(),
					entry.getValue()));
		}
		return oauthLoginResponse;
	}
	private String encodeCredentials(String username, String password) {
		// TODO Auto-generated method stub
		String cred = username + ":" + password;
		String encodedValue = null;
		byte[] encodedBytes = Base64.encodeBase64(cred.getBytes());
		encodedValue = new String(encodedBytes);
		System.out.println("encodedBytes " + new String(encodedBytes));

		byte[] decodedBytes = Base64.decodeBase64(encodedBytes);
		System.out.println("decodedBytes " + new String(decodedBytes));

		return encodedValue;
	}
	public ResponseEntity<String> jwtService(ServiceModel serviceModel) {
		Object o = new Object() {
			private String username=serviceModel.getUsername();
			private String password=serviceModel.getPass();
			public String getUserName() {
				return username;
			}
			public void setUserName(String userName) {
				this.username = userName;
			}
			public String getPassword() {
				return password;
			}
			public void setPassword(String password) {
				this.password = password;
			}
			
		};
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response
		  = restTemplate.postForEntity(serviceModel.getAuthUrl(), o,String.class);
		return response;
	}
	public Object basicAuthService(ServiceModel serviceModel) {
		RestTemplate restTemplate =  new RestTemplate(); 
		try {
			restTemplate.getInterceptors().add(
					  new BasicAuthorizationInterceptor(serviceModel.getUsername(),serviceModel.getPass()));
			ResponseEntity<String> exchange = restTemplate.exchange(
					  serviceModel.getUrlConsumed(), 
					  HttpMethod.GET, null, String.class);
			return exchange;
		}catch(Exception e) {
			return null;
		}
	
	}
//	public void oauth2Testing() {
//		 String encodedClientData = 
//			      Base64Utils.encodeToString("bael-client-id:bael-secret".getBytes());
//			    Mono<String> resource = client.post()
//			      .uri("localhost:8085/oauth/token")
//			      .header("Authorization", "Basic " + encodedClientData)
//			      .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
//			      .retrieve()
//			      .bodyToMono(JsonNode.class)
//			      .flatMap(tokenResponse -> {
//			          String accessTokenValue = tokenResponse.get("access_token")
//			            .textValue();
//			          return client.get()
//			            .uri("localhost:8084/retrieve-resource")
//			            .headers(h -> h.setBearerAuth(accessTokenValue))
//			            .retrieve()
//			            .bodyToMono(String.class);
//			        });
//			    System.out.println( resource.map(res ->
//			      "Retrieved the resource using a manual approach: " + res));
//	}
}
