#!/usr/bin/env bash

set -e

VERSION=$1
REGION=$2
TENANCY_NAMESPACE=$3

if [ -z "$VERSION" ]; then
  echo "You must supply a version number!"
  exit 1
elif [ -z "$REGION" ]; then
  echo "You must supply a region!"
  exit 1
elif [ -z "$TENANCY_NAMESPACE" ]; then
  echo "You must supply a container registry tenancy namespace!"
  exit 1
fi

# Check to see if version is valid
rx='^([0-9]+\.){0,2}(\*|[0-9]+)$'
if [[ $VERSION =~ $rx ]]; then
  echo "INFO:<-->Version $VERSION"
else
  echo "ERROR:<->Unable to validate package version: '$VERSION'"
  exit 1
fi

# tag
docker tag spring-hr $REGION/$TENANCY_NAMESPACE/oci-dev/spring-hr:$VERSION

# and push
docker push $REGION/$TENANCY_NAMESPACE/oci-dev/spring-hr:$VERSION

# done :)
printf "\n\nDone!!\n\n"