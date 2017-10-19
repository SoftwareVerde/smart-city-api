#!/bin/bash

# This script is intended to run the application on a production environment.
# This script will forward port 8080 and 4443 to 80 and 443, respectively.
# It is important that the production server have the user created before this
#   is executed, and that ./scripts/make.sh has been run and the resulting
#   "out" directory copied to the user's home directory and renamed "api".

# Forward HTTP Ports
sudo iptables -A PREROUTING -t nat -p tcp --dport 80 -j REDIRECT --to-port 8080
sudo iptables -t nat -A OUTPUT -o lo -p tcp --dport 80 -j REDIRECT --to-port 8080

sudo iptables -A PREROUTING -t nat -p tcp --dport 443 -j REDIRECT --to-port 4443
sudo iptables -t nat -A OUTPUT -o lo -p tcp --dport 443 -j REDIRECT --to-port 4443

# Run Application as Non-Priviledged User
user='smart-city-api'
su ${user} -c "cd /home/${user}/api; nohup ./run.sh > ~/server.out &"

