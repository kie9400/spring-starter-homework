package com.springboot.coffee.controller;

import com.springboot.coffee.dto.CoffeePatchDto;
import com.springboot.coffee.dto.CoffeePostDto;
import com.springboot.coffee.dto.CoffeeResponseDto;
import com.springboot.coffee.entitiy.Coffee;
import com.springboot.coffee.mapper.CoffeeMapper;
import com.springboot.coffee.service.CoffeeService;
import com.springboot.response.MultiResponseDto;
import com.springboot.response.SingleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/v2/coffees")
@Validated
public class CoffeeController {
    private final CoffeeService coffeeService;
    private final CoffeeMapper mapper;

    public CoffeeController(CoffeeService coffeeService, CoffeeMapper mapper) {
        this.coffeeService = coffeeService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postCoffee(@Valid @RequestBody CoffeePostDto coffeePostDto){
        //PostDto -> 엔티티
        Coffee coffee = coffeeService.createCoffee(mapper.coffeePostDtoToCoffee(coffeePostDto));
        return new ResponseEntity<>(mapper.coffeeToCoffeeResponseDto(coffee), HttpStatus.CREATED);
    }

    @PatchMapping("/{coffee-id}")
    public ResponseEntity patchCoffee(@PathVariable("coffee-id") @Positive long coffeeId,
                                      @Valid @RequestBody CoffeePatchDto coffeePatchDto){
        //id를 dto에 저장
        coffeePatchDto.setCoffeeId(coffeeId);
        Coffee coffee = coffeeService.updateCoffee(mapper.coffeePatchDtoToCoffee(coffeePatchDto));
        return new ResponseEntity<>(mapper.coffeeToCoffeeResponseDto(coffee),HttpStatus.OK);
    }

    @GetMapping("/{coffee-id}")
    public ResponseEntity getCoffee(@Positive @PathVariable("coffee-id") long coffeeId) {
        Coffee coffee = coffeeService.findCoffee(coffeeId);
        return new ResponseEntity<>(new SingleResponseDto<>(
                mapper.coffeeToCoffeeResponseDto(coffee)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getCoffees(@Positive @RequestParam int page,
                                     @Positive @RequestParam int size) {
        //요청으로 받아오는 page는 1이지만, 페이지네이션은 페이지 번호가 0부터 시작[인덱스]
        Page<Coffee> pageCoffees = coffeeService.findCoffees(page - 1, size);
        //페이지에서 content(실제 데이터)만 받아서 List형태로 변환
        List<Coffee> coffees = pageCoffees.getContent();
        return new ResponseEntity<>(new MultiResponseDto<>
                (mapper.coffeesToCoffeeResponseDtos(coffees), pageCoffees), HttpStatus.OK);
    }

    @DeleteMapping("/{coffee-id}")
    public ResponseEntity deleteCoffee(@Positive @PathVariable long coffeeId) {
        coffeeService.deleteCoffee(coffeeId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
