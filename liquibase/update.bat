java -jar "C:\CarlosEduardo\Ferramentas\liquibase-3.5.1-bin\liquibase.jar" --classpath="C:\CarlosEduardo\Ferramentas\IntegradorContabil\aplicativo\lib\postgresql-9.1-901-1.jdbc4.jar" --driver=org.postgresql.Driver --url=jdbc:postgresql://localhost:5432/teste2 --username=postgres --password=conversorfly --changeLogFile="C:\CarlosEduardo\Ferramentas\IntegradorContabil\liquibase\db-changelog-master.xml" --logLevel=debug update > "C:\CarlosEduardo\Ferramentas\IntegradorContabil\liquibase\log.txt" 