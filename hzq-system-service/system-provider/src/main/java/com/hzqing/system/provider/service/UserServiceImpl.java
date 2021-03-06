package com.hzqing.system.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hzqing.system.api.dto.user.*;
import com.hzqing.system.api.service.IUserService;
import com.hzqing.system.provider.converter.UserConverter;
import com.hzqing.system.provider.dal.entity.User;
import com.hzqing.system.provider.dal.entity.UserRole;
import com.hzqing.system.provider.dal.mapper.UserMapper;
import com.hzqing.system.provider.dal.mapper.UserRoleMapper;
import com.hzqing.common.core.constants.GlobalConstants;
import com.hzqing.common.core.service.exception.ExceptionProcessUtils;
import com.hzqing.common.core.service.request.IDRequest;
import com.hzqing.common.core.service.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public CommonResponse save(AddUserRequest request) {
        log.info("UserServiceImpl.createUser request: " + request);
        CommonResponse response = new CommonResponse();
        try {
            // 参数校验
            request.checkParams();
            User user = userConverter.req2User(request);
            user.setCreateTime(LocalDateTime.now());
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
    public CommonResponse<UserDto> getById(IDRequest request) {
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
    public CommonResponse<Page<UserDto>> page(UserPageRequest request) {
        log.info("UserServiceImpl.userLists request: " + request);
        CommonResponse<Page<UserDto>>  response = new CommonResponse<>();
        try {
            request.checkParams();
            User user = userConverter.req2User(request);
            IPage<User> userIPage = userMapper.selectPage(
                    new Page<User>(request.getPageNum(), request.getPageSize()),
                    new QueryWrapper<>(user));
            Page<UserDto> userDtoPage = userConverter.pageUser2PageDto(userIPage);
            response.setData(userDtoPage);
        }catch (Exception e){
            log.error("UserServiceImpl.userLists occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CommonResponse removeById(IDRequest request) {
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

    @Override
    public CommonResponse updateById(UpdateUserRequest request) {
        log.info("UserServiceImpl.updateUser request: " + request);
        CommonResponse response = new CommonResponse();
        try {
            request.checkParams();
            User user = userConverter.req2User(request);
            user.setUpdateTime(LocalDateTime.now());
            int row = userMapper.updateById(user);
            log.info("UserServiceImpl.updateUser effec row: " + row);
        }catch (Exception e) {
            log.error("UserServiceImpl.updateUser occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CommonResponse<Page<UserDto>> pageByRoleId(UserRelationPageRequest request) {
        log.info("UserServiceImpl.pageByRoleId request: " + request);
        CommonResponse<Page<UserDto>>  response = new CommonResponse<>();
        try {
            request.checkParams();
            IPage<User> userIPage = userMapper.selectPageByRoleId(
                    new Page<User>(request.getPageNum(), request.getPageSize()),
                    request.getRoleId()
            );
            Page<UserDto> userDtoPage = userConverter.pageUser2PageDto(userIPage);
            response.setData(userDtoPage);
        }catch (Exception e) {
            log.error("UserServiceImpl.pageByRoleId occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CommonResponse<List<UserDto>> listByRoleId(UserListByRoleIdRequest request) {
        log.info("UserServiceImpl.listByRoleId request: " + request);
        CommonResponse<List<UserDto>> response = new CommonResponse<>();
        try {
            request.checkParams();
            List<User> users = userMapper.selectListByRoleId(request.getRoleId());
            response.setData(userConverter.users2List(users));
        }catch (Exception e) {
            log.error("UserServiceImpl.listByRoleId occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }

    @Override
    public CommonResponse<Page<UserDto>> pageNotByRoleId(UserRelationPageRequest request) {
        log.info("UserServiceImpl.pageNotByRoleId request: " +  request);
        CommonResponse<Page<UserDto>>  response = new CommonResponse<>();
        try {
            request.checkParams();
            IPage<User> userIPage = userMapper.selectPageNotByRoleId(
                    new Page<User>(request.getPageNum(), request.getPageSize()),
                    request.getRoleId()
            );
            Page<UserDto> userDtoPage = userConverter.pageUser2PageDto(userIPage);
            response.setData(userDtoPage);
        }catch (Exception e) {
            log.error("UserServiceImpl.pageNotByRoleId occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;

    }

    @Override
    public CommonResponse saveBatchUserRole(UserRoleRequest request) {
        log.info("UserServiceImpl.saveBatchUserRole request: " + request);
        CommonResponse response = new CommonResponse();
        try {
            String[] userIds = request.getUserId().split(",");
            log.info("UserServiceImpl.saveBatchUserRole 绑定的用户数量：" + userIds.length);
            List<UserRole> users = new ArrayList<>(userIds.length);
            for (String userId : userIds) {
                users.add(new UserRole(
                        UUID.randomUUID().toString().replaceAll("-",""),
                        userId,
                        request.getRoleId()));
            }
            int row = userRoleMapper.insertBatch(users);
            log.info("UserServiceImpl.saveBatchUserRole 绑定结果，影响行数： " + row);
        }catch (Exception e) {
            log.error("UserServiceImpl.saveBatchUserRole occur Exception: ", e);
            ExceptionProcessUtils.wrapperHandlerException(response,e);
        }
        return response;
    }
}
