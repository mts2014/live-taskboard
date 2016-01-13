#!/bin/bash

set -eux

commands=$@
project_dir=$(dirname $(readlink -f $0))/../..
orchestrate_dir=$project_dir/vm/orchestrate
ansible_dir=$orchestrate_dir/ansible
webui_dir=$project_dir/app/webui

for command in $commands; do
  case "$command" in

    "build:images" )
      sudo docker build -t livetaskboard/web -f Dockerfile_web .
    ;;

    "build:webui" )
      cd $webui_dir

      set +eu
      . ~/.nvm/nvm.sh
      nvm use 0.10
      set -eu
      gulp clean build && gulp bundle

      dest_dir=$ansible_dir/roles/webserver_deploy/files/build
      cp \
        index.html \
        config.js \
        favicon.ico \
        $dest_dir
      cp --parents -r \
        styles/styles.css \
        jspm_packages/npm/font-awesome@4.4.0/css \
        jspm_packages/npm/font-awesome@4.4.0/fonts \
        jspm_packages/system.js \
        dist/app-build.js dist/aurelia.js \
        $dest_dir
    ;;

    "run:base" )
      cd $orchestrate_dir/ansible
      set +e
      pkill consul
      set -e
      consul agent -server -bootstrap-expect 1 -data-dir /tmp/consul -bind=172.17.0.1 &
      ansible-playbook -i inventories/develop -c local --tags start devmachine.yml
    ;;

    * )
      echo "invalid command $command"
      exit 1
    ;;

  esac
done
