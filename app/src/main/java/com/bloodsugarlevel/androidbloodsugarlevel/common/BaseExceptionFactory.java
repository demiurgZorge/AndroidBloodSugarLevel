package com.bloodsugarlevel.androidbloodsugarlevel.common;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BaseExceptionFactory {

    public static BaseException photositeExceptionBuilder(final ApiErrorResult res)
            throws IllegalStateException {
        String[] list = res.getCode().toLowerCase().split("_");
        return new BaseException(res.getCode());

    }
}
