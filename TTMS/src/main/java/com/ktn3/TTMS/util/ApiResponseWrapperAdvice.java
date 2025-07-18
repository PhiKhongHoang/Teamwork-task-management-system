package com.ktn3.TTMS.util;

import com.ktn3.TTMS.dto.response.ResCommonApi;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ApiResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Áp dụng cho tất cả các response có kiểu ResCommonApi
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        if (body instanceof ResCommonApi) {
            ResCommonApi<?> api = (ResCommonApi<?>) body;
            // Nếu statusCode chưa gán, tự động lấy theo HTTP Status code của response (mặc định 200 nếu null)
            if (api.getStatusCode() == null) {
                int status = 200; // default
                if (response instanceof org.springframework.http.server.ServletServerHttpResponse servletResp) {
                    status = servletResp.getServletResponse().getStatus();
                }
                api.setStatusCode(status);
            }
            return api;
        }
        // Nếu không phải ResCommonApi thì không bọc lại, trả nguyên
        return body;
    }
}
