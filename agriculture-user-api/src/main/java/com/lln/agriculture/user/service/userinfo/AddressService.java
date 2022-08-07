package com.lln.agriculture.user.service.userinfo;

import com.lln.agriculture.common.domain.Message;
import com.lln.agriculture.common.domain.userinfo.Address;

/**
 * @author LiLinnan
 * @version 1.0
 * @date 2020/10/20 9:22
 */

public interface AddressService {

    Message insertAddress(Address address);

    Message updateAddress(Address address);

    Message deleteAddress(int addressId);

    Message selectAllAddress();

    /**
     * 默认地址
     *
     * @return 返回默认地址，没有就是null
     */
    Message selectDefaultAddress();

    Message updateDefaultAddress(int addressId);

}
