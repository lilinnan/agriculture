package com.lln.agriculture.user.controller.userinfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.userinfo.Address;
import com.lln.agriculture.user.service.userinfo.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/20 9:20
 */


@RestController
@RequestMapping("/v1/user-info/address")
public class AddressController {

    private final AddressService addressService;


    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }


    @PostMapping
    public Message insertAddress(@RequestBody @Valid Address address) {
        return addressService.insertAddress(address);
    }

    @PutMapping
    public Message updateAddress(@RequestBody @Valid Address address) {
        return addressService.updateAddress(address);
    }

    @DeleteMapping("/{id}")
    public Message deleteAddress(@PathVariable int id) {
        return addressService.deleteAddress(id);
    }

    @GetMapping
    public Message selectAllAddress() {
        return addressService.selectAllAddress();
    }

    @GetMapping("/default")
    public Message selectDefaultAddress() {
        return addressService.selectDefaultAddress();
    }

    @PutMapping("/default/{id}")
    public Message updateDefaultAddress(@PathVariable int id) {
        return addressService.updateDefaultAddress(id);
    }

}
