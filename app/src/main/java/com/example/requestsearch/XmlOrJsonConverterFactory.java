package com.example.requestsearch;

import android.util.Xml;
import android.util.*;

import androidx.annotation.NonNull;

import com.example.requestsearch.network.NaverAPI;
import com.google.gson.GsonBuilder;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class XmlOrJsonConverterFactory extends Converter.Factory {
    final Converter.Factory xml =SimpleXmlConverterFactory.create();
    final Converter.Factory json = GsonConverterFactory.create();
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type   type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == NaverAPI.Xml.class) {
//                return xml.responseBodyConverter(type,annotations,retrofit);
                return SimpleXmlConverterFactory.createNonStrict(new Persister(
                        new AnnotationStrategy())).responseBodyConverter(type, annotations, retrofit);
            }
            if (annotation.annotationType() == NaverAPI.Json.class) {
                return json.responseBodyConverter(type,annotations,retrofit);
            }
        }
        return null;
    }
}