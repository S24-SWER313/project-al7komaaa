package com.project1.project;


// public class NFException extends RuntimeException {

//     NFException(Class clazz) {
        
//         super("Could not find  " + clazz.getName() );
      
      

      public class NFException extends RuntimeException {
      
          public NFException(Class<?> clazz) {
              super("Could not find " + clazz.getSimpleName());
          }
      }
      

