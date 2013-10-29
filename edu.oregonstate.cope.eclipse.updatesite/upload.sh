#!/bin/bash

site_location='brindesc@access.engr.oregonstate.edu:~/public_html/cope/client-recorder/'

rsync -avz . $site_location
