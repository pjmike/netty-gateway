package com.pjmike.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pjmike.route.Route;
import com.pjmike.utils.PrimitiveTypeUtil;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @description: 获取HttpRequest中的一些详细信息，比如请求头，请求参数等
 * @author: pjmike
 * @create: 2019/12/04
 */
public class HttpRequestDecompose {
    private HttpRequest httpRequest;
    public HttpRequestDecompose(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    /**
     * 获取请求的uri
     *
     * @return
     */
    public String getUri() {
        return this.httpRequest.uri();
    }

    /**
     * 获取请求参数
     *
     * @return
     */
    public Map<String, List<String>> getParams() {
        Map<String, List<String>> paramMap = new HashMap<>();
        HttpMethod method = httpRequest.method();
        if (Objects.equals(HttpMethod.GET,method)) {
            QueryStringDecoder stringDecoder = new QueryStringDecoder(getUri(), CharsetUtil.UTF_8);
            paramMap = stringDecoder.parameters();
        } else if (Objects.equals(HttpMethod.POST, method)) {
            FullHttpRequest fullHttpRequest = (FullHttpRequest) httpRequest;
            paramMap = getPostParamMap(fullHttpRequest);

        }
        return paramMap;
    }

    /**
     * 获取POST请求参数
     *
     * @param httpRequest
     * @return
     */
    private Map<String, List<String>> getPostParamMap(FullHttpRequest httpRequest) {
        Map<String, List<String>> paramMap = new HashMap<>();
        String contentType = getContentType(httpRequest.headers());
        if (HttpHeaderValues.APPLICATION_JSON.toString().equals(contentType)) {
            String content = httpRequest.content().toString();
            JSONObject jsonContent = JSON.parseObject(content);
            for (Map.Entry<String, Object> item : jsonContent.entrySet()) {
                convert2Map(paramMap, item.getKey(), item.getValue());
            }
        } else if (HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString().equals(contentType)) {
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.content().toString(CharsetUtil.UTF_8));
            paramMap = queryStringDecoder.parameters();
        }
        return paramMap;
    }

    /**
     * 获取ContentType
     * @param headers http请求头
     * @return
     */
    public String getContentType(HttpHeaders headers) {
        String contentType = headers.get(HttpHeaderNames.CONTENT_TYPE);
        if (StringUtils.containsIgnoreCase(contentType, ";")) {
            String[] strings = contentType.split(";");
            return strings[0];
        } else {
            return contentType;
        }
    }

    /**
     * 获取请求头
     *
     * @return
     */
    public Map<String, List<String>> getHeader() {
        Map<String, List<String>> header = new HashMap<>();
        httpRequest.headers().entries()
                .forEach(entry-> {
                    List<String> values = header.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
                    values.add(entry.getValue());
                });
        return header;
    }

    /**
     * 格式转换
     *
     * @param paramsMap
     * @param key
     * @param value
     * @return
     */
    private void convert2Map(Map<String, List<String>> paramsMap, String key, Object value) {
        List<String> valueList;
        if (paramsMap.containsKey(key)) {
            valueList = paramsMap.get(key);
        } else {
            valueList = new ArrayList<>();
        }
        Class<?> valueClass = value.getClass();
        if (PrimitiveTypeUtil.isPriType(valueClass)) {
            valueList.add(value.toString());
            paramsMap.put(key, valueList);
        } else if (valueClass.isArray()) {
            int length = Array.getLength(value);
            for(int i=0; i<length; i++){
                String arrayItem = String.valueOf(Array.get(value, i));
                valueList.add(arrayItem);
            }
            paramsMap.put(key, valueList);
        } else if (List.class.isAssignableFrom(valueClass)) {
            if(valueClass.equals(JSONArray.class)){
                JSONArray jArray = JSONArray.parseArray(value.toString());
                for(int i=0; i<jArray.size(); i++){
                    valueList.add(jArray.getString(i));
                }
            }else{
                valueList = (ArrayList<String>) value;
            }
            paramsMap.put(key, valueList);

        }else if(Map.class.isAssignableFrom(valueClass)){
            Map<String, String> tempMap = (Map<String, String>) value;
            tempMap.forEach((k,v)->{
                List<String> tempList = new ArrayList<>();
                tempList.add(v);
                paramsMap.put(k, tempList);
            });
        }
    }
}
