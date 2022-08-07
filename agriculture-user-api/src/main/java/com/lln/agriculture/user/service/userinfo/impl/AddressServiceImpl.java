package com.lln.agriculture.user.service.userinfo.impl;

import com.lln.agriculture.common.constant.RegexConstant;
import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.userinfo.Address;
import com.lln.agriculture.common.exception.BadRequestException;
import com.lln.agriculture.common.util.CommonUtils;
import com.lln.agriculture.user.mapper.userinfo.AddressMapper;
import com.lln.agriculture.user.mapper.userinfo.UserMapper;
import com.lln.agriculture.user.service.userinfo.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/20 9:26
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressMapper addressMapper;
    private final UserMapper userMapper;

    public AddressServiceImpl(AddressMapper addressMapper, UserMapper userMapper) {
        this.addressMapper = addressMapper;
        this.userMapper = userMapper;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message insertAddress(Address address) {
        if (!address.getPhone().matches(RegexConstant.PHONE_REGEX)) {
            throw new BadRequestException("错误的手机号");
        }
        int userId = userMapper.selectUserIdByUsername(CommonUtils.getCurrentUsername());
        address.setUserId(userId);
        addressMapper.insert(address);
        return Message.builder()
                .message("添加成功")
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateAddress(Address address) {
        if (address.getId() == null) {
            throw new BadRequestException("无地址id");
        }
        check(address.getId());
        if (!address.getPhone().matches(RegexConstant.PHONE_REGEX)) {
            address.setPhone(null);
        }
        addressMapper.updateAddressByPrimaryKeySelective(address);
        return Message.builder()
                .message("更新成功")
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message deleteAddress(int addressId) {
        check(addressId);
        addressMapper.deleteAddressByPrimaryKey(addressId);
        return Message.builder()
                .message("删除成功")
                .build();
    }

    @Override
    public Message selectAllAddress() {
        int userId = userMapper.selectUserIdByUsername(CommonUtils.getCurrentUsername());
        List<Address> addresses = addressMapper.selectAllAddressByUserId(userId);
        addresses.forEach(address -> {
            if (address != null) {
                address.setPhone(CommonUtils.encryptedPhoneNumber(address.getPhone()));
            }
        });
        return Message.builder()
                .message("查询成功")
                .data(addresses)
                .build();
    }

    @Override
    public Message selectDefaultAddress() {
        Address data = userMapper.selectDefaultAddressByUsername(CommonUtils.getCurrentUsername());
        if (data != null) {
            data.setPhone(CommonUtils.encryptedPhoneNumber(data.getPhone()));
        }
        return Message.builder()
                .message("查询成功")
                .data(data)
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message updateDefaultAddress(int addressId) {
        check(addressId);
        userMapper.updateDefaultAddressByUsername(CommonUtils.getCurrentUsername(), addressId);
        return Message.builder()
                .message("更新成功")
                .build();
    }

    public void check(int addressId) {
        Address address = addressMapper.selectAddressByIdAndUsername(addressId, CommonUtils.getCurrentUsername());
        if (address == null) {
            throw new BadRequestException("不存在的地址");
        }

    }
}
