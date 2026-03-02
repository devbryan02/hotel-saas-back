package com.app.hotelsaas.catin.domain.port;

import com.app.hotelsaas.catin.domain.model.AppUser;

import java.util.Optional;

public interface AppUserRepository {

    AppUser save(AppUser appUser);
    Optional<AppUser> findByEmail(String email);

}
