// IAccessAidlInterface.aidl
package com.heli.providerapp;
import  com.heli.providerapp.IAccessSetInterface;
// Declare any non-default types here with import statements

interface IAccessAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void registerIAccessCallBack(IAccessSetInterface iAccessSetInterface);

}
