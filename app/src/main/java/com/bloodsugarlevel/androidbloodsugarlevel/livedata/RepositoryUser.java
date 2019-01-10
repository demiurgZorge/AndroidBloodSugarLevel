package com.bloodsugarlevel.androidbloodsugarlevel.livedata;

import com.bloodsugarlevel.androidbloodsugarlevel.dto.UserDto;
import com.bloodsugarlevel.androidbloodsugarlevel.room.entities.User;

public class RepositoryUser {
    public Long   serverId;
    public Long   dbId;
    public String login;
    public String password;
    public String name;
    public String authToken;
    public boolean active;

    public RepositoryUser(User user){
        this.active = true;
        this.authToken = null;
        this.serverId = null;
        this.dbId = user.id;
        this.login = user.login;
        this.name = user.name;
        this.password = user.password;
    }

    public RepositoryUser(UserDto user){
        this.active = user.active;
        this.authToken = user.authToken;
        this.serverId = user.id;
        this.dbId = null;
        this.login = user.name;
        this.name = user.name;
        this.password = null;
    }

    public UserDto getServerUserDto(){
        UserDto user = new UserDto();
        user.active = this.active;
        user.authToken = this.authToken;
        user.id = this.serverId;
        user.name = this.name;
        return user;
    }

    public User getBdUser(){
        User user = new User();
        user.active = true;
        user.authToken = null;
        user.id = this.dbId;
        user.login = this.login;
        user.name = this.name;
        user.password = this.password;
        return user;
    }
}
