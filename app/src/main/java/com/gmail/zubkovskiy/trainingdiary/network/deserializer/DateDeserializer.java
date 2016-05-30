package com.gmail.zubkovskiy.trainingdiary.network.deserializer;


import com.gmail.zubkovskiy.trainingdiary.network.NetworkConstants;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
//This class adapter to deserialize date type in Retrofit creation
public class DateDeserializer implements JsonDeserializer<Date> {
    //Create array for future, if appeared any date formats
    private final String[] DATE_FORMATS = new String[] {
            NetworkConstants.BAAS_DATE_FORMAT
    };

    @Override
    public Date deserialize(JsonElement jsonElement, Type typeOF,
                            JsonDeserializationContext context) throws JsonParseException {
        try{
            long value = jsonElement.getAsLong();
            return new Date(value);
        }catch (Exception e){}
        for (String format : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
            } catch (ParseException e) {
            }
        }
        throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
    }
}
