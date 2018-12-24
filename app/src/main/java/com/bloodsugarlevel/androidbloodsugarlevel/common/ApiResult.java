package com.bloodsugarlevel.androidbloodsugarlevel.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiResult {

    protected Boolean status = false;

    public Boolean getStatus() {
        return status;
    }

    @SuppressWarnings("unchecked")
    public <T> ApiListResult<List<T>> getListResult() {
        if (this instanceof ApiListResult<?>) {
            return (ApiListResult<List<T>>) this;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> ApiSuccessResult<T> getSuccessResult() {
        if (this instanceof ApiSuccessResult<?>) {
            return (ApiSuccessResult<T>) this;
        }
        return null;
    }

    public static void checkStatus(Map<String, Object> map) throws Exception {

        final Boolean status = (Boolean) map.get("status");
        if (status == null || !status) {
            throw new Exception("ERROR");
        }
    }

    public static Map<String, Object> responseToMap(ApiResponse resp) {
        Map<String, Object> map = resp.jsonPath().get();
        return map;
    }

    public static Map<String, Object> readMapOrFail(ApiResponse resp) throws Exception {
        Map<String, Object> map = responseToMap(resp);
        checkStatus(map);
        return map;
    }

    public static <T> List<ApiOperationStatus<T>> readApiOperationList(ApiResponse resp, Class<T> klass) throws Exception {
        Map<String, Object> map = readMapOrFail(resp);
        return readApiOperationList(map, klass);
    }

    public static <T> List<ApiOperationStatus<T>> readApiOperationList(Map<String, Object> map, Class<T> klass) {
        Object data = map.get("data");
        ObjectMapper mapper = new ObjectMapper();

        JavaType type = mapper.getTypeFactory().constructParametrizedType(
                ApiOperationStatus.class, ApiOperationStatus.class, klass);

        List<ApiOperationStatus<T>> result = new ArrayList<>();
        if (data instanceof List<?>) {
            for (Object o : (List<?>) data) {
                ApiOperationStatus<T> item = mapper.convertValue(o, type);
                result.add(item);
            }
        }
        return result;
    }

    private static <T> ApiSuccessResult<?> readSuccess(Map<String, Object> map, Class<T> klass) {
        Object data = map.get("data");
        ObjectMapper mapper = new ObjectMapper();
        if (data instanceof List<?>) {
            List<T> result = new ArrayList<>();
            for (Object o : (List<?>) data) {
                T item = mapper.convertValue(o, klass);
                result.add(item);
            }
            Object meta = map.get("metaInformation");
            QueryMetaInformation info = mapper.convertValue(meta, QueryMetaInformation.class);
            return new ApiListResult<List<T>>(result, info);
        } else if (klass != null && Map.class.isAssignableFrom(klass)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> m = (Map<String, Object>) data;
            return new ApiMapResult(m, null);
        } else if (klass != null) {
            T result = mapper.convertValue(data, klass);
            return new ApiSuccessResult<>(result);
        }
        return new ApiSuccessResult<>();
    }

    public static ApiErrorResult readError(Map<String, Object> map) {
        if (map.get("errorData") != null) {
            return readDataError(map);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(map, ApiErrorResult.class);
    }

    public static <ED> ApiDataErrorResult<?> readDataError(Map<String, Object> map, Class<ED> errorDataKlass) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ApiDataErrorResult<?> result = mapper.convertValue(map, ApiDataErrorResult.class);
        Object errData = result.getErrorData();
        Object errorDto = null;
        JavaType typeRef = null;
        if (errData instanceof List) {
            typeRef = mapper.getTypeFactory().constructCollectionType(List.class, errorDataKlass);
            errorDto = mapper.convertValue(errData, typeRef);
        } else {
            errorDto = mapper.convertValue(errData, errorDataKlass);
        }
        result.setErrorDto(errorDto);

        return result;
    }

    public static ApiDataErrorResult<?> readDataError(Map<String, Object> map) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.convertValue(map, ApiDataErrorResult.class);
    }

    public static <T> ApiResult fromResponse(final ApiResponse r, Class<T> klass)
            throws Exception {
        Map<String,Object> map =
                new ObjectMapper().readValue(r.toString(), Map.class);
        Boolean status = (Boolean) map.get("status");
        if (status != null && status) {
            return readSuccess(map, klass);
        } else {
            throw new Exception("RESPONSE ERROR");
        }
    }


    public static <T> ApiListResult<List<T>> readListOrFail(final ApiResponse r, final Class<T> listType) throws Exception {
        return fromResponse(r, listType).getListResult();
    }
//
//    public static <T> ApiSuccessResult<T> readOrFail(ApiResponse resp, Class<T> klass) throws Exception {
//        ApiResult result = fromResponse(resp, klass);
//        return result.getSuccessResult();
//    }


    public static <T, ED> ApiSuccessResult<T> readOrFail(ApiResponse resp, Class<T> klass, Class<ED> errorDataKlass)
            throws Exception {
        ApiResult result = fromResponse(resp, klass, errorDataKlass);
        return result.getSuccessResult();
    }

    private static <T, ED> ApiResult fromResponse(ApiResponse resp, Class<T> klass, Class<ED> errorDataKlass) throws Exception {
        Map<String, Object> map;
        map = resp.jsonPath().get();
        Boolean status = (Boolean) map.get("status");
        if (status != null && status) {
            return readSuccess(map, klass);
        } else {
            throw new Exception("RESPONSE ERROR");
        }
    }

    public static <T> ApiSuccessResult<T> readOrFail(final ApiResponse resp, final Class<T> klass)
            throws Exception {
        return fromResponse(resp, klass).getSuccessResult();
    }

    public static <T> List<T> readList(Object data, Class<T> klass) {
        @SuppressWarnings("unchecked")
        List<T> objectList = new ObjectMapper().convertValue(data, List.class);
        List<T> resultList = new ArrayList<>();
        for (Object obj : objectList) {
            T o = new ObjectMapper().convertValue(obj, klass);
            resultList.add(o);
        }
        return resultList;
    }

    public static <ED> ED readErrorData(final ApiErrorResult errData, final Class<ED> errDataClass) {
        if (errData == null) {
            throw new IllegalArgumentException("errData is null but expected an instance");
        }

        if (!(errData instanceof ApiDataErrorResult)) {
            throw new IllegalArgumentException("Parameter 'errData' expected as 'ApiDataErrorResult' type");
        }

        final ApiDataErrorResult<?> data = (ApiDataErrorResult<?>) errData;

        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (data.getErrorData() instanceof List) {
            final JavaType jt = mapper.getTypeFactory().constructCollectionType(List.class, errDataClass);
            return mapper.convertValue(data.getErrorData(), jt);
        }

        return mapper.convertValue(data.getErrorData(), errDataClass);
    }

    public static <ED> List<ED> readErrorDataList(final ApiErrorResult errData, final Class<ED> errDataClass) {

        if (errData == null) {
            throw new IllegalArgumentException("errData is null but expected an instance");
        }

        if (!(errData instanceof ApiDataErrorResult)) {
            throw new IllegalArgumentException("Parameter 'errData' expected as 'ApiDataErrorResult' type");
        }

        final ApiDataErrorResult<?> data = (ApiDataErrorResult<?>) errData;
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        final JavaType jt = mapper.getTypeFactory().constructCollectionType(List.class, errDataClass);
        return mapper.convertValue(data.getErrorData(), jt);
    }
}
