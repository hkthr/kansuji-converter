#!/bin/env bash

if [ "$1" == "--build" ]; then
    echo Build DockerImage before run...
    gradle dockerBuild
fi

docker run -it --rm hkthr/kansujiconv 
