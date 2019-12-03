#!/usr/bin/env bash
!/bin/bash  cd ~
nodejs curl -sL https://deb.nodesource.com/setup_10.x -o nodesource_setup.sh
sudo bash nodesource_setup.sh
sudo apt install nodejs
echo "nodejs version: `nodejs -v`"
echo "npm version: `npm -v`"
openjdk sudo apt-get install openjdk-11-jdk
maven sudo apt-get install maven

versionInstalled="$(dpkg --get-selections | grep mysql)"
if [[ -z "$versionInstalled" ]]; then
	echo "MySql not installed, starting download..."
	sudo apt update
	sudo apt install mysql-server
	sudo mysql_secure_installation
	echo "MySql installed: $(mysql -V)"

#  PASSWDDB="$(openssl rand -base64 12)"
  dbname="vrserver"
	echo "Please enter root user MySQL password!"
  echo "Note: password will be hidden when typing"
  read -sp rootpasswd
  echo "Creating new MySQL database..."
  mysql -uroot -p${rootpasswd} -e "CREATE DATABASE ${dbname} /*\!40100 DEFAULT CHARACTER SET utf8 */;"
  echo "Database successfully created!"
  echo "Showing existing databases..."
	mysql -uroot -p${rootpasswd} -e "show databases;"

	echo ""
	echo "Please enter the NAME of the new MySQL database user! (example: user1)"
	read username
	echo "Please enter the PASSWORD for the new MySQL database user!"
	echo "Note: password will be hidden when typing"
	read -sp userpass
	echo "Creating new user..."
	mysql -uroot -p${rootpasswd} -e "CREATE USER ${username}@localhost IDENTIFIED BY '${userpass}';"
	echo "User successfully created!"
	echo ""
	echo "Granting ALL privileges on ${dbname} to ${username}!"
	mysql -uroot -p${rootpasswd} -e "GRANT ALL PRIVILEGES ON ${dbname}.* TO '${username}'@'localhost';"
	mysql -uroot -p${rootpasswd} -e "FLUSH PRIVILEGES;"
	echo "Done"
	sudo systemctl start mysql
fi
