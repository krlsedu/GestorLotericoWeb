set versao=%1
set pasta=%2
set versao_maior=%3
set versao_menor=%4
set build=%5
set compilation=%6
set build_time=%7

set apgdif="%pasta%\apgdiff\apgdiff_meu.jar"
set charset=--in-charset-name UTF-8 --out-charset-name UTF-8

set estrutura_velha="%pasta%\apgdiff\bkps\estrutura_velha.sql"
set estrutura_nova="%pasta%\apgdiff\bkps\estrutura_nova.sql"
set nome_dif_estrutura="%pasta%\db_versions\%versao%.postgresql.sql"

set bkp_estrutura=--host localhost --port 5432 --username "postgres" --no-password  --format plain --schema-only --disable-dollar-quoting --file

"C:\Program Files\PostgreSQL\9.5\bin\pg_dump.exe" %bkp_estrutura% %estrutura_velha% "IntegradorContabilEspelhoVersionamento"
"C:\Program Files\PostgreSQL\9.5\bin\pg_dump.exe" %bkp_estrutura% %estrutura_nova% "IntegradorContabil"

java -jar %apgdif% %charset% --ignore-start-with %estrutura_velha% %estrutura_nova% > %nome_dif_estrutura%
echo INSERT INTO public.db_version(versao_major, versao_minor, build, compilation) VALUES ('%versao_maior%', '%versao_menor%', '%build%','%compilation%'); >> %nome_dif_estrutura%

del %pasta%\apgdiff\bkps\*.sql

rem psql -d IntegradorContabilEspelhoVersionamento -U postgres -f %nome_dif_estrutura%

