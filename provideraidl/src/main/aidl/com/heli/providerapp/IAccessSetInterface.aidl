// IAccessSetInterface.aidl
package com.heli.providerapp;

// Declare any non-default types here with import statements

interface IAccessSetInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
   void commonAccessChanged(String accessName ,boolean accessON);
    void appAccessChanged(String packageName,String accessName,boolean accessON);
}
