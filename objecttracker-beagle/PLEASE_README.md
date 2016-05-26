#ObjectTracker for Beagle
The robot's object tracking functionality, implemented in C++.

##Usage
To track an object, you must DEFINE the color (in HSV) at the top of eye.cpp before compiling.

##Compiling
To compile, you must have CMake (minimum 3.1) and OpenCV (minimum 3.0) installed.
Follow these steps to compile:
```bash
mkdir build
cd build
cmake ..
make
```
