package com.MSGFoundation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Client s3Client;

    public String uploadFile(MultipartFile file, String filename) throws IOException {
        try {
            PutObjectRequest putObjectAclRequest = PutObjectRequest.builder()
                    .bucket("bucket-msgfoundation")
                    .key(filename)
                    .build();

            s3Client.putObject(putObjectAclRequest, RequestBody.fromBytes(file.getBytes()));
            return "File load successfully";
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String downloadFile(String fileName) throws IOException {
        String localPath = "/Users/"+System.getProperty("user.name")+"/";
        if (!doesObjectExists(fileName)) {
            return "El archivo introducido no existe";
        }
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket("bucket-msgfoundation")
                .key(fileName)
                .build();
        ResponseInputStream<GetObjectResponse> result = s3Client.getObject(request);

        try (FileOutputStream fos = new FileOutputStream(localPath+fileName)) {
            byte[] read_buf = new byte[1024];
            int read_len = 0;

            while((read_len = result.read(read_buf)) > 0){
                fos.write(read_buf,0,read_len);
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return "Archivo descargado correctamente";
    }

    public boolean doesObjectExists(String objectKey) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket("bucket-msgfoundation")
                    .key(objectKey)
                    .build();
            s3Client.headObject(headObjectRequest);
            return true;
        } catch (S3Exception e) {
            if (e.statusCode() == 404) {
                return false;
            }
        }
        return true;
    }

    public String deleteFile(String filename) throws IOException {
        if(!doesObjectExists(filename)){
            System.out.println("File has not exists");
        }
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket("bucket-msgfoundation")
                    .key(filename)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
            return "File has been deleted";
        } catch (S3Exception e){
            throw new IOException(e.getMessage());
        }
    }



}
