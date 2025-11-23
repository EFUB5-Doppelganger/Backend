package com.efub.doppelganger.accommodation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccommodationRegisterRequestDto {

    @NotBlank
    private String name;
    private String description;

    @NotBlank
    private int price;

    @NotBlank
    private String location;
    @NotBlank
    private String address;

    private List<MultipartFile> images;

    @NotBlank
    private int maxGuests;

    @NotBlank
    private int bedroom;
    @NotBlank
    private int bed;
    @NotBlank
    private int bathroom;
}
