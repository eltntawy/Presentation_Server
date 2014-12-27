Presentation Server
================

this software in beta version and tested on Ubuntu 14.10 LTS

Requirements
--
This project use BlueCove library: https://code.google.com/p/bluecove/wiki/stacks

    Package bluez-libs 3.7 or later installed on your system
    We linked JNI library with libbluetooth.so (Not libbluetooth.so.3 or libbluetooth.so.2) to be able to use same build     with Bluez Version 3 and Version 4
    You need package/rpm that creates a link libbluetooth.so to already installed libbluetooth.so.3 or           
        libbluetooth.so.2
        libbluetooth-dev on Ubuntu
        bluez-libs-devel on Fedora
        bluez-devel on openSUSE 
    To change Discoverable mode of the device you should be root
    On 64-bit Linux platform 64-bit java should be used
    
    
for installation on ubuntu 
==
```sh
 $ sudo apt-get install libbluetooth-dev
```
