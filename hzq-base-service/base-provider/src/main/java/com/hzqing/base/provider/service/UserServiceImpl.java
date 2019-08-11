package com.hzqing.base.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzqing.base.api.dto.user.*;
import com.hzqing.base.api.service.IUserService;
import com.hzqing.base.provider.converter.UserConverter;
import com.hzqing.base.provider.dal.entity.User;
import com.hzqing.base.provider.dal.mapper.UserMapper;
import com.hzqing.common.core.constants.GlobalConstants;
import com.hzqing.common.core.service.exception.ExceptionProcessUtils;
import com.hzqing.common.core.service.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author hzqing
 * @date 2019-08-09 14:57
 */
@Slf4j
@Service(version = GlobalConstants.VERSION_V1)
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserConverter userConverter;

    @Override
    public CommonResponse createUser(AddUserRequest request) {
        log.info("UserServiceImpl.createUser request: " + request);
        CommonResponse response = new CommonResponse();
        try {
            // 参数校验
            request.checkParams();
            User user = userConverter.req2User(request);
            int insert = userMapper.insert(user);
            log.info("UserServiceImpl.createUser effect row:  " + insert);
        }catch (Exception e) {
            // 抛出异常
            log.error("UserServiceImpl.createUser occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CommonResponse<UserDto> userDetail(UserDetailRequest request) {
        log.info("UserServiceImpl.userDetail request: " + request);
        CommonResponse<UserDto> response = new CommonResponse<UserDto>();
        try {
            request.checkParams();
            User user = userMapper.selectById(request.getId());
            UserDto userDto = userConverter.user2Dto(user);
            response.setData(userDto);
        }catch (Exception e){
            log.error("UserServiceImpl.userDetail occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CommonResponse<Page<UserDto>> userPage(UserPageRequest request) {
        log.info("UserServiceImpl.userLists request: " + request);
        CommonResponse<Page<UserDto>>  response = new CommonResponse<Page<UserDto>>();
        try {
            request.checkParams();
            User user = userConverter.req2User(request);
            Page<User> userPage = (Page<User>) userMapper.selectPage(
                    new Page<>(request.getPageNum(),request.getPageSize()),
                    new QueryWrapper<>(user));
            Page<UserDto> userDtoPage = userConverter.pageUser2PageDto(userPage);
            response.setData(userDtoPage);
        }catch (Exception e){
            log.error("UserServiceImpl.userLists occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CommonResponse deleteUser(DeleteUserRequest request) {
        log.info("UserServiceImpl.deleteUser request: " + request);
        CommonResponse response = new CommonResponse();
        try {
            request.checkParams();
            int row = userMapper.deleteById(request.getId());
            log.info("UserServiceImpl.deleteUser effect: " + row);
        } catch (Exception e){
            log.error("UserServiceImpl.deleteUser occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }
}
