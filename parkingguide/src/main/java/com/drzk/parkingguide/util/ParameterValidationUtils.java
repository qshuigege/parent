package com.drzk.parkingguide.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ParameterValidationUtils {

    public static <T> ValidationResult validate(String[] fields, T obj) throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = obj.getClass();
        ValidationResult validationResult = new ValidationResult();
        List<String> failureFields = new ArrayList<>();
        for (String fName : fields) {
            Field field = clazz.getDeclaredField(fName);
            field.setAccessible(true);
            Object o = field.get(obj);
            if (null == o){
                validationResult.setSuccess(false);
                failureFields.add(fName);
            }else {
                if (o instanceof String){
                    String tmp = (String) o;
                    if ("".equals(tmp.trim())){
                        validationResult.setSuccess(false);
                        failureFields.add(fName);
                    }
                }
            }
        }
        validationResult.setFailureFields(failureFields);
        return validationResult;
    }

    public static String concatFailureFields(ValidationResult validationResult) {
        StringBuilder sb = new StringBuilder();
        for (String fName : validationResult.getFailureFields()) {
            sb.append(fName).append(", ");
        }
        sb.setLength(sb.length()-2);
        return sb.toString();
    }

    public static class ValidationResult{
        private boolean success = true;
        private List<String> failureFields;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public List<String> getFailureFields() {
            return failureFields;
        }

        public void setFailureFields(List<String> failureFields) {
            this.failureFields = failureFields;
        }
    }

}
