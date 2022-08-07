package com.lln.agriculture.user.mapper.userinfo;

import com.lln.agriculture.common.domain.userinfo.Address;
import com.lln.agriculture.common.domain.userinfo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(User record);

    User selectByUsername(String username);

    boolean isExistByUsername(String username);

    boolean isExistByEmail(String email);

    boolean isMailValidated(String email);

    int enableUser(String username);

    String selectUsernameByEmail(String email);

    int changePassword(String username, String password);

    String selectUserPassword(String username);

    int selectUserIdByUsername(String username);

    String selectUsernameById(int id);

    Address selectDefaultAddressByUsername(String username);

    int updateDefaultAddressByUsername(String username, Integer addressId);
}