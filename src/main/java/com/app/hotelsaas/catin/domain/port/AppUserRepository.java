package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.AppUser;

public interface AppUserRepository {

    AppUser save(AppUser appUser);

}
