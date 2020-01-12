#!/usr/bin/env bash
git_dir=$1
if [[ ! -d ${git_dir} ]]; then
    echo "$git_dir is not a directory."
    exit
fi

if [[ "${git_dir}" =~ '*vrgroupserver' ]] || [[ "${git_dir}" =~ '*vrgroupserver/' ]]; then
  echo "Pass the directory to git clone ex. /home/<dir>/vrgroupserver"
  exit
fi

echo "Updating package lists..."
sudo apt-get update > /dev/null 2>&1

echo "Downloading openjdk 11..."
sudo apt-get install openjdk-11-jdk

echo "Installing maven..."
sudo apt-get install maven

if command -v python3 &>/dev/null; then
    echo "Installing python3"
    sudo apt-get install python3
fi

echo "Installing MySql"
sudo apt install mysql-server

read -r -p 'Run mysql secure installation [y/N]?' runMySqlInstallation
if [[ "$runMySqlInstallation" =~ ^([yY][eE][sS]|[yY])$ ]]; then
    sudo mysql_secure_installation
fi

read -r -p 'Create the database vrserver(don'\''t accept if database is already created) [y/N]?' createUser
if [[ "$createUser" =~ ^([yY][eE][sS]|[yY])$ ]]; then
    sudo mysql -e "create database vrserver;"
    sudo mysql -e "use vrserver;source ${git_dir}/setup/init.sql;"
    sudo echo "wait_timeout=31536000" | sudo tee -a /etc/mysql/mysql.conf.d/mysqld.cnf
    sudo echo "interactive_timeout=31536000" | sudo tee -a /etc/mysql/mysql.conf.d/mysqld.cnf
fi

read -r -p 'Create new user access to vrserver database [y/N]?' createUser
if [[ "$createUser" =~ ^([yY][eE][sS]|[yY])$ ]]; then
    read -p 'Username: ' username
    read -sp 'Password: ' password
    echo
    sudo mysql -e "create user '$username'@'localhost' identified by '$password'"
    sudo mysql -e "use vrserver;GRANT ALL PRIVILEGES ON vrserver.* to '$username'@'localhost'"
    python3 "${git_dir}"/setup/update.py "${git_dir}"/src/main/resources/META-INF/persistence.xml "${username}" "${password}"
fi

read -r -p 'Restart database [y/N]?' restartDb
if [[ "$restartDb" =~ ^([yY][eE][sS]|[yY])$ ]]; then
    echo
    sudo /etc/init.d/mysql restart
fi

read -r -p 'Run the server now[y/N]?' runServer
if [[ "$runServer" =~ ^([yY][eE][sS]|[yY])$ ]]; then
    if dpkg -s net-tools &>/dev/null; then
        if  [[ $(netstat -tlpn | grep 80 | grep java) ]] &>/dev/null; then
            echo "Shut down server before executing this script"
            exit
        fi
    else
        echo "If the server is running please shut it down before continuing with the execution of this script"
        read -r -p 'Force free the port [y/N]?' response
        if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
            sudo fuser 80/tcp -k
            echo "Port 80 free"
        fi
    fi
    cd "${git_dir}" || exit
    nohup mvn jetty:run -Pproduction &
fi