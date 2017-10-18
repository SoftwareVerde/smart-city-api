#!/bin/bash

SCRIPTS_DIR="$(dirname $0)"
cd $SCRIPTS_DIR

rm -rf ../out
./make-jar.sh

# Copy HTTP Assets
mkdir -p ../out/www
cp -R ../www/* ../out/www/.

# Copy SSL Certs
mkdir -p ../out/ssl
cp -R ../ssl/out/* ../out/ssl/.

# Copy Config
mkdir -p ../out/conf
cp -R ../conf/* ../out/conf/.

# Create Run-Script
echo -e "#!/bin/bash\n\ncd \"\$(dirname \$0)\"\n\nexec java -jar bin/main.jar \"conf/server.conf\"\n" > ../out/run.sh
chmod 770 ../out/run.sh

