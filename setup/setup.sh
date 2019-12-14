#!/usr/bin/env bash
directory=$1
cd ~
curl -sL https://deb.nodesource.com/setup_10.x -o nodesource_setup.sh
sudo bash nodesource_setup.sh
sudo apt install nodejs
sudo apt update
sudo apt-get install openjdk-11-jdk
sudo apt-get install maven

GREEN=`tput setaf 10`
sudo apt install mysql-server
sudo mysql_secure_installation
read -p 'Username (used for login): ' username
read -sp 'Password (used for login): ' password
mysql -u root -e "create database vrserver; GRANT ALL PRIVILEGES ON vrserver.* to '$username'@localhost IDENTIFIED BY '$password';"
mysql -h localhost -u '$username' --password='$password' vrserver < ~/vrgroupserver/setup/init.sql