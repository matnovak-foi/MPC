package org.foi.common;

public interface MPCexcpetions {
    
    public class IsNullException extends RuntimeException {
        public IsNullException() {
            super();
        }

        public IsNullException(String message) {
            super(message);
        }
    }
}