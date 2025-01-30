package com.springboot.coffee.dto;

import lombok.Getter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class CoffeePostDto {
    @NotBlank
    private String korName;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z])(\\s?[A-Za-z])*$",
            message = "커피명(영문)은 영문이어야 합니다")
    private String engName;

    @Range(min = 1000, max = 30000)
    private int price;

    @NotBlank
    @Pattern(regexp = "^([A-Za-z]){3}$")
    private String coffeeCode;
}
