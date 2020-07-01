package com.serverless.service;


import java.time.Duration;
import java.util.Properties;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

public class PresignS3URLGenerator {
	
	public S3Presigner getS3Presigner() {
		
//		AwsCredentialsProvider credentialsProvider =  SystemPropertyCredentialsProvider.create();
//		AwsCredentialsProvider credentialsProvider =  ProfileCredentialsProvider.create("dev");
//		AwsCredentialsProvider credentialsProvider =  EnvironmentVariableCredentialsProvider.create();
	
//		S3Presigner presigner = S3Presigner.builder().credentialsProvider( credentialsProvider ).build();
		S3Presigner presigner = S3Presigner.create();
		return presigner;
		
	}
	public String generateUploadPresignUrl( S3Presigner presigner,  String bucketName,  String keyName) {
		String presignedUrl = "";
        try {
        	// Create a PutObjectRequest to be pre-signed
//            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//								                    .bucket(bucketName)
//								                    .key(keyName)
//								                    .build();
//            
//	         // Create a PutObjectPresignRequest to specify the signature duration
//             PutObjectPresignRequest putObjectPresignRequest =
//                 PutObjectPresignRequest.builder()
//                                        .signatureDuration(Duration.ofMinutes(10))
//                                        .putObjectRequest(putObjectRequest)
//                                        .build();
//             PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(putObjectPresignRequest);
             
             PresignedPutObjectRequest presignedRequest =
                     presigner.presignPutObject(z -> z.signatureDuration(Duration.ofMinutes(10))
                             .putObjectRequest(por -> por.bucket(bucketName).key(keyName)));

        	
        
            System.out.println("Pre-upload signed URL to upload a file to: " +
                    presignedRequest.url());
            System.out.println("Which HTTP method needs to be used when uploading a file: " +
                    presignedRequest.httpRequest().method());

            // Upload content to the bucket by using this URL
            java.net.URL url = presignedRequest.url();
            presignedUrl = url.toString();
            System.out.println("upload presigned url: " + presignedUrl);
//
//            // Create the connection and use it to upload the new object by using the pre-signed URL
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestProperty("Content-Type","text/plain");
//            connection.setRequestMethod("PUT");
//            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
//            out.write("This text uploaded as an object via presigned URL.");
//            out.close();
//
//            connection.getResponseCode();
//            System.out.println("HTTP response code: " + connection.getResponseCode());

            /*
            *  It's recommended that you close the S3Presigner when it is done being used, because some credential
            * providers (e.g. if your AWS profile is configured to assume an STS role) require system resources
            * that need to be freed. If you are using one S3Presigner per application (as recommended), this
            * usually isn't needed
            */
//            presigner.close();

        } catch (S3Exception e) {
            e.getStackTrace();
        }
		return presignedUrl;
	}
	
	public String generateDownloadUrlString(S3Presigner presigner, String bucketName, String keyName) {
		String presignedUrl = "";
		try {

			// Create a GetObjectRequest to be pre-signed
			GetObjectRequest getObjectRequest = GetObjectRequest.builder()
					.bucket(bucketName).key(keyName).build();

			// Create a GetObjectPresignRequest to specify the signature duration
			GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
					.signatureDuration(Duration.ofMinutes(10))
					.getObjectRequest(getObjectRequest)
					.build();

			// Generate the presigned request
			PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
			
			java.net.URL url = presignedGetObjectRequest.url();
			presignedUrl = url.toString();
			// Log the presigned URL
			System.out.println("Presigned download URL: " + presignedUrl);

//			// Create a JDK HttpURLConnection for communicating with S3
//			HttpURLConnection connection = (HttpURLConnection) presignedGetObjectRequest.url().openConnection();
//
//			// Specify any headers that the service needs (not needed when
//			// isBrowserExecutable is true)
//			presignedGetObjectRequest.httpRequest().headers().forEach((header, values) -> {
//				values.forEach(value -> {
//					connection.addRequestProperty(header, value);
//				});
//			});
//
//			// Send any request payload that the service needs (not needed when
//			// isBrowserExecutable is true)
//			if (presignedGetObjectRequest.signedPayload().isPresent()) {
//				connection.setDoOutput(true);
//				try (InputStream signedPayload = presignedGetObjectRequest.signedPayload().get().asInputStream();
//						OutputStream httpOutputStream = connection.getOutputStream()) {
//					IoUtils.copy(signedPayload, httpOutputStream);
//				}
//			}
//
//			// Download the result of executing the request
//			try (InputStream content = connection.getInputStream()) {
//				System.out.println("Service returned response: ");
//				IoUtils.copy(content, System.out);
//			}


		} catch (S3Exception e) {
			e.getStackTrace();
		} finally {
			/*
			 * It's recommended that you close the S3Presigner when it is done being used,
			 * because some credential providers (e.g. if your AWS profile is configured to
			 * assume an STS role) require system resources that need to be freed. If you
			 * are using one S3Presigner per application (as recommended), this usually
			 * isn't needed
			 */
//			presigner.close();
		}
		return presignedUrl;
	}
}
