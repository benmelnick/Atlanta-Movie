package com.gatech.cs4400.AtlantaMovieService.security;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal (errorOnInvalidType = true)
public @interface CurrentUser {

}
