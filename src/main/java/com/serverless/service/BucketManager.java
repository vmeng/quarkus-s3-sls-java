package com.serverless.service;

import java.util.ArrayList;
import java.util.List;


import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketConfiguration;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsRequest;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;

public class BucketManager {
	
//	private static final Logger LOG = LogManager.getLogger(BucketManager.class);
	
	Region region = Region.EU_WEST_2;
    S3Client s3 = S3Client.builder().region(region).build();
    
    
	public void createBucket(String bucketName) {
//		LOG.info("creating bucket ...", bucketName);
		CreateBucketRequest createBucketRequest = CreateBucketRequest
                .builder()
                .bucket(bucketName)
                .createBucketConfiguration(CreateBucketConfiguration.builder()
                        .locationConstraint(region.id())
                        .build()
                 )
                .build();
        s3.createBucket(createBucketRequest);
//        LOG.info("create bucket success...", bucketName);
	}
	
	public boolean isExist(String bucketName) {
		List<String> list = listBukets();
		boolean exit = list.contains(bucketName);
//		LOG.info("isExist bucketName: ", bucketName, exit);
		return exit;
	}
	
	private List<String> listBukets() {
		List<String> list = new ArrayList<String>();
		ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
		ListBucketsResponse listBucketsResponse = s3.listBuckets(listBucketsRequest);
		listBucketsResponse.buckets().stream().forEach(x -> {
				System.out.println(x.name());
				list.add(x.name());
			}
		);
		return list;
	}
}
