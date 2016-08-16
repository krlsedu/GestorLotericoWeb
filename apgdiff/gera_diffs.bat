set versao=%1
set pasta=%2
set versao_maior=%3
set versao_menor=%4
set build=%5
set compilation=%6
set build_time=%build%-%compilation%

set apgdif="%pasta%\apgdiff\apgdiff-2.4.jar"
set charset=--in-charset-name UTF-8 --out-charset-name UTF-8

set estrutura_velha="%pasta%\apgdiff\bkps\estrutura_velha.sql"
set estrutura_nova="%pasta%\apgdiff\bkps\estrutura_nova.sql"
set nome_dif_estrutura="%pasta%\db_versions\diffs_liquibase\diffs_sql\%versao%_estrutura.sql"

set bkp_estrutura=--host localhost --port 5432 --username "postgres" --no-password  --format plain --schema-only --disable-dollar-quoting --file

"C:\Program Files\PostgreSQL\9.5\bin\pg_dump.exe" %bkp_estrutura% %estrutura_velha% "glwEspelhoVersionamento"
"C:\Program Files\PostgreSQL\9.5\bin\pg_dump.exe" %bkp_estrutura% %estrutura_nova% "glw"

echo --liquibase formatted sql > %nome_dif_estrutura%
echo --changeset carloseduardo (generated):%build_time%-1 >> %nome_dif_estrutura%
java -jar %apgdif% %charset% --ignore-start-with %estrutura_velha% %estrutura_nova% >> %nome_dif_estrutura%
echo INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('%versao_maior%', '%versao_menor%', '%build%','%compilation%'); >> %nome_dif_estrutura%

del %pasta%\apgdiff\bkps\*.sql

copy "%pasta%\db_versions\cabecalho_diff.xml" %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml" 
echo ^<changeSet author="carloseduardo (generated)" id="%build_time%-1"  objectQuotingStrategy="QUOTE_ALL_OBJECTS" ^> >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo     ^<sqlFile dbms="PostgreSQL" >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo             encoding="utf8" >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo             endDelimiter=";" >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo		     stripComments="true" >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo		     splitStatements="true" >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo             path="diffs_sql/%versao%_estrutura.sql" >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo             relativeToChangelogFile="true" /^>>> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo ^</changeSet^> >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"
echo ^</databaseChangeLog^> >> %pasta%\db_versions\diffs_liquibase\%versao%_estrutura.xml"

rem psql -d IntegradorContabilEspelhoVersionamento -U postgres -f %nome_dif_estrutura%

