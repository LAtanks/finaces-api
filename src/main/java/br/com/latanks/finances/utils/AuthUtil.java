package br.com.latanks.finances.utils;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static String getAuthenticatedEmail(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
