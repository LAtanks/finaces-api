package br.com.latanks.finances.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

public class PropertiesUtil {

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        return emptyNames.toArray(new String[0]);
    }

    public static void copyProperties(Object source, Object target){
        BeanUtils.copyProperties(source, target, PropertiesUtil.getNullPropertyNames(source));
    }

    public static boolean hasAnyNumber(String s){
        for (Character c : s.toCharArray()){
            if(Character.isDigit(c)){
                return true;
            }
        }

        return false;
    }
}
