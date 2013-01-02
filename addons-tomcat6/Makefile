M=/opt/apache-maven-3.0.3/bin/mvn deploy:deploy-file -DgroupId=de.eonas.portal -Dpackaging=jar -DrepositoryId=central -Durl=https://www.office.eonas.de/artifactory/ext-release-local

all:

install:
	#-rm -fr a
	#mkdir a
	#unzip third/AlkaconOCEEClusterPackage_V3_0_2_23.08.11_jbq4sd.zip -d a
	for file in third/*.jar; do FILE=` basename $$file .jar`; ARTI=` echo $$FILE | cut -f 1 -d _ ` ; VERSION=` echo $$FILE | cut -f 2 -d _ `  ; $(M) -DartifactId=$$ARTI -Dversion=$$VERSION -Dfile=$$file ; done

