package com.danikvitek.IdentityService.service;

import com.danikvitek.IdentityService.api.dto.SimpleUserDto;

import java.util.Set;

public interface PluginService {
    Set<SimpleUserDto> fetchByPluginId(long pluginId);
}
