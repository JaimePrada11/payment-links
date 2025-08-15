package com.laura.paymentlinks.paymentlinks.util.Exceptions;

import java.util.Map;

class ValidationException extends RuntimeException {
    private final Map<String,Object> errors;
    public ValidationException(String m){super(m); this.errors = Map.of();}
    public ValidationException(String m, Map<String,Object> errors){super(m); this.errors = errors;}
    public Map<String,Object> getErrors(){return errors;}
}