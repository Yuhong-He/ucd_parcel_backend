package org.example.receiver.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.receiver.dto.Email;
import org.example.receiver.entity.Parcel;
import org.example.receiver.entity.ParcelTrack;
import org.example.receiver.message.MQ;
import org.example.receiver.utils.EmailEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/")
public class ReceiverController {

    @Value("${database.address}")
    private String database = "";

    @Value("${email.address}")
    private String mailUrl = "";

    RestTemplate restTemplate = new RestTemplate();

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Access via web browser", description = "Allows anyone get the service introduction via root path.")
    @GetMapping("/")
    public String get() {
        return "<h2>This is the Receiver System in UCD Parcel Delivery System.</h2>" +
                "<h2>Swagger API Document: <a href='/swagger-ui/index.html'>/swagger-ui/index.html</a>.</h2>" +
                "<h2>For more information, please refer: <a href='https://github.com/Yuhong-He/ucd_parcel_backend/tree/main/Receiver'>GitHub page</a>.</h2>";
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Get parcel tracks", description = "Allowed student gets one parcel tracks")
    @GetMapping("/getParcelTracks")
    public List<ParcelTrack> getParcelTracks(@RequestParam int receiverId, @RequestParam String parcelId) {
        Parcel parcel = restTemplate.getForObject(database + "/parcel/getParcelWithId/{id}", Parcel.class, parcelId);
        if (parcel == null || parcel.getStudent() != receiverId)
            return null;
        return parcel.getTracks();
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Get a parcelList", description = "Allowed student gets their parcels")
    @GetMapping("/getParcelList")
    public String getParcelList(@RequestParam int receiverID, int pageNo) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> responseEntity = template.getForEntity(database + "/parcel/getReceiverParcel?receiverId=" + receiverID +
                "&pageNo=" + pageNo, String.class);
        return responseEntity.getBody();
    }

    @ApiResponse(responseCode = "200", description = "Success")
    @Operation(summary = "Confirm address", description = "Allowed student confirm the delivery address")
    @PostMapping("/confirmed")
    public boolean confirmed(@RequestParam int receiverId, String parcelId) {
        Parcel parcel = restTemplate.getForObject(database + "/parcel/getParcelWithId/{id}", Parcel.class, parcelId);

        if (parcel != null && receiverId == parcel.getStudent()){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = LocalDateTime.now().format(formatter);
            int postmanId = parcel.getTracks().get(parcel.getTracks().size() - 1).getPostman();
            String postmanEmail = restTemplate.getForObject(database + "/user/getUserEmail?id=" + postmanId, String.class);
            parcel.setTracks(List.of(new ParcelTrack("Receiver Confirmed the address", receiverId, formattedDateTime)));

            try {
                MQ.sendToDatabase(parcel);
                Email email = new Email(postmanEmail, "Student confirmed address",
                        getConfirmAddressEmailBody(parcel.getAddress1() + ", " + parcel.getAddress2()));
                restTemplate.postForEntity(mailUrl + "/send", EmailEncryptor.encrypt(email), String.class);
            } catch (Exception e) {
                log.info("Exception: " + e);
            }
            return true;
        }
        return false;
    }

    private static String getConfirmAddressEmailBody(String address) {
        return "<p>Dear postman,</p>" +
                "<p>The student has confirmed the deliver address for: <span style='color: green'>" + address + "</span>.</p>";
    }
}
