package com.app.hotelsaas.catin.infrastructure.security;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

public class CustomUserDetails implements UserDetails {

    private final UserDetails delegate;
    private final UUID tenantId;

    public CustomUserDetails(UserDetails delegate, UUID tenantId) {
        this.delegate = delegate;
        this.tenantId = tenantId;
    }

    @NullMarked
    public UUID getTenantId() {
        return tenantId;
    }

    @Override
    @NullMarked
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public @Nullable String getPassword() {
        return delegate.getPassword();
    }

    @Override
    @NullMarked
    public String getUsername() {
        return delegate.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return delegate.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return delegate.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return delegate.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }
}
