package com.project1.project;

public class Exception extends RuntimeException {

    Exception(Class clazz) {
        
        super("Could not find  " + clazz.getName() );
      }
    
}
