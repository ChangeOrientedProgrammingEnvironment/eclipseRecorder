#!/bin/bash

# This is an old obsolete command that will be deprecated in future releases.
site_location='brindesc@access.engr.oregonstate.edu:~/public_html/cope/client-recorder/'

rsync -avz . $site_location
