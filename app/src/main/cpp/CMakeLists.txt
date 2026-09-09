cmake_minimum_required(VERSION 3.22.1)
project("eni")

add_library(eni SHARED native_lib.cpp)
target_link_libraries(eni log android)
