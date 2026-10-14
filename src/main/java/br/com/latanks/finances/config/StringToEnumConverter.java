package br.com.latanks.finances.config;

import br.com.latanks.finances.utils.csv.Banks;
import org.springframework.core.convert.converter.Converter;


public class StringToEnumConverter implements Converter<String, Banks> {


    @Override
    public Banks convert(String source) {
        try {
            return Banks.fromText(source);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
