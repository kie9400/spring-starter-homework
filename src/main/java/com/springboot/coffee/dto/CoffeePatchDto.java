package com.springboot.coffee.dto;

import com.springboot.coffee.entitiy.Coffee;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class CoffeePatchDto {
    @Setter
    private long coffeeId;

    //중간에 공백을 넣기 위해 validator 패키지에 NotSpace 애너테이션 생성
    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]+( [A-Za-z0-9]+)*$", message = "공백은 한 번만 허용됩니다.")
    private String korName;

    @Pattern(regexp = "^([A-Za-z])(\\s?[A-Za-z])*$", message = "커피명(영문)은 영문이어야 합니다.")
    private String engName;

    @Range(min = 1000, max = 30000)
    private int price;

    private Coffee.CoffeeStatus coffeeStatus;

    public Integer getPrice() {
        return price;
    }
}
