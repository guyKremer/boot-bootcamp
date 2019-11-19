#!/bin/sh
docker build -t bootcampimage .
docker run -d -p 8001:8001 bootcampimage
