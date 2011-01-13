#!/bin/sh

# Run_from_custom_git.sh
# Run_from_git.sh
#
# Created by Winter Guerra on 1/9/11.
# Copyright Winter Guerra, All rights reserved.

abspath="$(cd "${0%/*}" 2>/dev/null; echo "$PWD"/"${0##*/}")"
path_only=`dirname "$abspath"`

#display the paths to prove it works
echo $abspath
echo $path_only

cd $path_only

ant run
