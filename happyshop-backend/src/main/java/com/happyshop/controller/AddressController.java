package com.happyshop.controller;

import com.happyshop.common.Result;
import com.happyshop.entity.Address;
import com.happyshop.service.AddressService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public Result<List<Address>> list() {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Result.success(addressService.getAddresses(userId));
    }

    @PostMapping
    public Result<?> add(@RequestBody Address address) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addressService.addAddress(userId, address);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Address address) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addressService.updateAddress(userId, id, address);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addressService.deleteAddress(userId, id);
        return Result.success();
    }

    @PutMapping("/{id}/default")
    public Result<?> setDefault(@PathVariable Long id) {
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        addressService.setDefault(userId, id);
        return Result.success();
    }
}
