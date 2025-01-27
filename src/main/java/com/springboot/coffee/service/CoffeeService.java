package com.springboot.coffee.service;

import com.springboot.coffee.entitiy.Coffee;
import com.springboot.coffee.repository.CoffeeRepository;
import com.springboot.exception.BusinessLogicException;
import com.springboot.exception.ExceptionCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class CoffeeService {
    private final CoffeeRepository coffeeRepository;

    public CoffeeService(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    public Coffee createCoffee(Coffee coffee) {
        //커피 코드는 무조건 대문자여야 한다.
        String coffeeCode = coffee.getCoffeeCode().toUpperCase();
        //커피 코드가 중복인지 확인
        verifyExistCoffee(coffeeCode);
        //DB에 저장하기전에 대문자로 변경된 커피코드를 저장한다.
        coffee.setCoffeeCode(coffeeCode);

        return coffeeRepository.save(coffee);
    }

    public Coffee updateCoffee(Coffee coffee) {
        //변경할 커피가 존재하는지 확인
        Coffee findCoffee = findVerifiedCoffee(coffee.getCoffeeId());

        Optional.ofNullable(coffee.getKorName()).ifPresent(korName -> findCoffee.setKorName(korName));
        Optional.ofNullable(coffee.getEngName()).ifPresent(korName -> findCoffee.setKorName(korName));
        Optional.ofNullable(coffee.getPrice()).ifPresent(price -> findCoffee.setPrice(price));
        Optional.ofNullable(coffee.getCoffeeStatus())
                .ifPresent(coffeeStatus -> findCoffee.setCoffeeStatus(coffeeStatus));

        return findCoffee;
    }

    public Coffee findCoffee(long coffeeId){
        return findVerifiedCoffee(coffeeId);
    }

    public Page<Coffee> findCoffees(int page, int size){
        return coffeeRepository.findAll(PageRequest.of(page, size, Sort.by
                ("coffeeId").descending()));
    }

    public void deleteCoffee(long coffeeId){
        Coffee coffee = findVerifiedCoffee(coffeeId);

        coffeeRepository.delete(coffee);
    }

    private void verifyExistCoffee(String coffeeCode){
        Optional<Coffee> coffee = coffeeRepository.findByCoffeeCode(coffeeCode);
        //만약 커피코드가 중복이라면 예외처리
        if(coffee.isPresent()){
            throw new BusinessLogicException(ExceptionCode.COFFEE_EXISTS);
        }
    }

    private Coffee findVerifiedCoffee(long coffeeId){
        Optional<Coffee> coffee = coffeeRepository.findById(coffeeId);

        //만약 DB에 존재하지 않는 커피라면 예외처리
        Coffee findCoffee = coffee.orElseThrow(()
                -> new BusinessLogicException(ExceptionCode.COFFEE_NOT_FOUND));

        return findCoffee;
    }
}
