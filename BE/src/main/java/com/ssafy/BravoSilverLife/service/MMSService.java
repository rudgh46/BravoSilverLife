package com.ssafy.BravoSilverLife.service;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import com.ssafy.BravoSilverLife.dto.BookmarkDto;
import com.ssafy.BravoSilverLife.entity.PhoneAuth;
import com.ssafy.BravoSilverLife.repository.PhoneAuthRepository;
import com.ssafy.BravoSilverLife.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MMSService {

    private final PhoneAuthRepository phoneAuthRepository;

    private final UserRepository userRepository;

    private static final String SMS_OAUTH_TOKEN_URL = "https://sms.gabia.com/oauth/token"; // ACCESSTOKEN 발급 API URL 입니다.
    private static final String smsId = "chlasnmzx2"; // SMS ID 를 입력해 주세요.
    private static final String apiKey = "3be1a53cadc7d54d78210396ff2a14ec"; // SMS 관리툴에서 발급받은 API KEY 를 입력해 주세요.

    public static final String SMS_SEND_URL = "https://sms.gabia.com/api/send/sms"; // SMS 발송 API URL
    public static final String LMS_SEND_URL = "https://sms.gabia.com/api/send/lms"; // LMS 발송 API URL

    public String userAuth() {
        String access_token = "";
        String authValue =
                Base64.getEncoder().encodeToString(String.format("%s:%s", smsId,
                        apiKey).getBytes(StandardCharsets.UTF_8)); // Authorization Header 에 입력할 값입니다.

        // 사용자 인증 API 를 호출합니다.
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "client_credentials")
                .build();

        Request request = new Request.Builder()
                .url(SMS_OAUTH_TOKEN_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + authValue)
                .addHeader("cache-control", "no-cache")
                .build();

        // Response 를 key, value 로 확인하실 수 있습니다.
        Response response = null;
        try {
            response = client.newCall(request).execute();
            HashMap<String, String> result = new Gson().fromJson(Objects.requireNonNull(response.body()).string(), HashMap.class);
            for (String key : result.keySet()) {
                System.out.printf("%s: %s%n", key, result.get(key));
                if (key.equals("access_token")) {
                    access_token = result.get(key);

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return access_token;
    }


    public boolean checkAuthByMMS(String accessToken, String phoneNumber) {

        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) return false;

        Random random = new Random();
        String randomNumber = "";
        for (int i = 0; i < 4; i++) {
            randomNumber += random.nextInt(10);
        }

        PhoneAuth phoneAuth;

        phoneAuth = PhoneAuth.builder()
                .phoneNumber(phoneNumber)
                .phoneAuth(randomNumber)
                .build();

        PhoneAuth pAuth = phoneAuthRepository.save(phoneAuth);

        String authValue =
                Base64.getEncoder().encodeToString(String.format("%s:%s", smsId,
                        accessToken).getBytes(StandardCharsets.UTF_8)); // Authorization Header 에 입력할 값입니다.

        // SMS 발송 API 를 호출합니다.
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone", phoneNumber) // 수신번호를 입력해 주세요. (수신번호가 두 개 이상인 경우 ',' 를 이용하여 입력합니다.ex)01011112222, 01033334444)
                .addFormDataPart("callback", "01045588466") // 발신번호를 입력해 주세요.
                .addFormDataPart("message", "인증번호는 " + randomNumber + " 입니다.") // SMS 내용을 입력해 주세요.
                .addFormDataPart("refkey", "YOUR_REF_KEY") // 발송 결과 조회를 위한 임의의 랜덤 키 값을 입력해 주세요.
                .build();

        Request request = new Request.Builder()
                .url(SMS_SEND_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + authValue)
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Response 를 key, value 로 확인하실 수 있습니다.
        HashMap<String, String> result = null;
        try {
            result = new Gson().fromJson(Objects.requireNonNull(response.body()).string(), HashMap.class);
            for (String key : result.keySet()) {
                System.out.printf("%s: %s%n", key, result.get(key));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }


    public void sendBookMarkMMS(String phoneNumber, String accessToken, String description) {


        String authValue =
                Base64.getEncoder().encodeToString(String.format("%s:%s", smsId,
                        accessToken).getBytes(StandardCharsets.UTF_8)); // Authorization Header 에 입력할 값입니다.

        // SMS 발송 API 를 호출합니다.
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone", phoneNumber) // 수신번호를 입력해 주세요. (수신번호가 두 개 이상인 경우 ',' 를 이용하여 입력합니다.ex)01011112222, 01033334444)
                .addFormDataPart("callback", "01045588466") // 발신번호를 입력해 주세요.
                .addFormDataPart("message", "북마크 매물 정보는 " + description) // SMS 내용을 입력해 주세요.
                .addFormDataPart("refkey", "YOUR_REF_KEY") // 발송 결과 조회를 위한 임의의 랜덤 키 값을 입력해 주세요.
                .build();

        Request request = new Request.Builder()
                .url(SMS_SEND_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + authValue)
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Response 를 key, value 로 확인하실 수 있습니다.
        HashMap<String, String> result = null;
        try {
            result = new Gson().fromJson(Objects.requireNonNull(response.body()).string(), HashMap.class);
            for (String key : result.keySet()) {
                System.out.printf("%s: %s%n", key, result.get(key));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendLMS(String accessToken, String phoneNumber) {


        String authValue =
                Base64.getEncoder().encodeToString(String.format("%s:%s", smsId,
                        accessToken).getBytes(StandardCharsets.UTF_8)); // Authorization Header 에 입력할 값입니다.

        // LMS 발송 API 를 호출합니다.
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("phone", "RECEIPT_PHONE_NUMBER")
                .addFormDataPart("callback","CALLING_PHONE_NUMBER") // 발신번호를 입력해 주세요.
                .addFormDataPart("message","LMS_CONTENT") // LMS 내용을 입력해 주세요.
                .addFormDataPart("refkey","YOUR_REF_KEY") // 발송 결과 조회를 위한 임의의 랜덤 키 값을 입력해 주세요.
                .addFormDataPart("subject","LMS_TITLE") // LMS 제목을 입력해 주세요.
                .build();

        Request request = new Request.Builder()
                .url(LMS_SEND_URL)
                .post(requestBody)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + authValue)
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Response 를 key, value 로 확인하실 수 있습니다.
        HashMap<String, String> result = null;
        try {
            result = new Gson().fromJson(Objects.requireNonNull(response.body()).string(), HashMap.class);
            for (String key : result.keySet()) {
                System.out.printf("%s: %s%n", key, result.get(key));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

