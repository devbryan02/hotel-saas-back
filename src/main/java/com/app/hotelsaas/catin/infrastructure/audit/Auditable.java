package com.app.hotelsaas.catin.infrastructure.audit;

import com.app.hotelsaas.catin.domain.enums.ActionEnum;
import com.app.hotelsaas.catin.domain.enums.EntityEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    ActionEnum action();
    EntityEnum entity();

}
