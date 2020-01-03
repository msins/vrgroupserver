#!/usr/bin/env bash
cd ~
sudo apt-get update
sudo apt-get install openjdk-11-jdk
sudo apt-get install maven

sudo apt install mysql-server
sudo mysql_secure_installation
read -p 'Username (used for login): ' username
read -sp 'Password (used for login): ' password
sudo mysql -e "create user '$username'@'localhost' identified by '$password'"
sudo mysql -e "create database vrserver; GRANT ALL PRIVILEGES ON vrserver.* to '$username'@'localhost'"
sudo mysql -e "use vrserver;source $HOME/vrgroupserver/setup/init.sql;"

sudo echo "wait_timeout=31536000" >> /etc/mysql/mysql.conf.d/mysqld.cnf
sudo echo "interactive_timeout=31536000" >> /etc/mysql/mysql.conf.d/mysqld.cnf
sudo /etc/init.d/mysql restart
