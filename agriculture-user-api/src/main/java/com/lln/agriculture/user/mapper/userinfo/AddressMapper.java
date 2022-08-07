package com.lln.agriculture.user.mapper.userinfo;

import com.lln.agriculture.common.domain.userinfo.Address;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AddressMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(Address record);

    Integer selectUserIdById(int id);

    int updateAddressByPrimaryKeySelective(Address address);

    int deleteAddressByPrimaryKey(int id);

    List<Address> selectAllAddressByUserId(int id);

    Address selectAddressByIdAndUsername(int id, String username);
}