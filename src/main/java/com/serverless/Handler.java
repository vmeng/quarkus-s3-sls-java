package com.serverless;

import java.util.Collections;
import java.util.Map;

import org.eclipse.microprofile.config.inject.ConfigProperty;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.service.BucketManager;
import com.serverless.service.PresignS3URLGenerator;

public class Handler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

//	private static final Logger LOG = LogManager.getLogger(Handler.class);
	PresignS3URLGenerator generator = new PresignS3URLGenerator();
	
	@ConfigProperty(name = "bucket.name")
	String bucketName;
	
//	private String bucketName = "s3-presign-test";
	
	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
//		LOG.info("received: {}", input);
		LambdaLogger logger = context.getLogger();
		logger.log("Got input : " + input.toString());
	
		Map<String,Object> queryStringParameters = (Map<String, Object>) input.get("queryStringParameters");
//		LOG.info("queryStringParameters: {}", queryStringParameters);
		
		if(queryStringParameters == null || queryStringParameters.isEmpty()) {
			return errorResponse("Need query param fileName and signMethod" , input);
		}
		String fileName = (String)queryStringParameters.get("fileName");
//		LOG.info("fileName: ", fileName);
		if(fileName == null || fileName.isEmpty() ) {
			return errorResponse("No fileName param in query param" , input);
		}
		
		String signMethod = (String)queryStringParameters.get("signMethod");
		logger.log("signMethod: "+ signMethod);
		
		String url = "";
		
//		String userId = "123";
//		bucketName += userId;
		logger.log("bucketName: "+ bucketName);
//		BucketManager bucketManager = new BucketManager();
//		boolean bucketExist = bucketManager.isExist(bucketName);
//		if(!bucketExist) {
//			bucketManager.createBucket(bucketName);
//		}
		if(signMethod != null && signMethod.equalsIgnoreCase("get")) {
			url = generator.generateDownloadUrlString(generator.getS3Presigner(), bucketName, fileName);
		}else if(signMethod != null && signMethod.equalsIgnoreCase("put")) {
			url = generator.generateUploadPresignUrl(generator.getS3Presigner(), bucketName, fileName);
		}else {
			return errorResponse("No signMethod param in query param, only support value get or put ", input);
		}
		
		logger.log("presigned url: " + url );
		
		S3Response responseBody = new S3Response(url);
		logger.log(responseBody.toString());
		
		return ApiGatewayResponse.builder()
				.setStatusCode(200)
				.setObjectBody(responseBody)
				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
	}
	
	private ApiGatewayResponse errorResponse(String message, Map<String, Object> input) {
		Response responseBody = new Response(message , input);
		return ApiGatewayResponse.builder()
				.setStatusCode(400)
				.setObjectBody(responseBody)
//				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
				.build();
	}
}
