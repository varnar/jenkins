import groovy.sql.Sql
import jenkins.model.*

nodes = Jenkins.instance.globalNodeProperties
nodes.getAll(hudson.slaves.EnvironmentVariablesNodeProperty.class)
Server = nodes[0].envVars.get("GLOBAL_DE_SQL_SERVER","unknown")
sql = Sql.newInstance("jdbc:sqlserver://$Server;integratedSecurity=true", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
datacenters = sql.rows("exec [TechOps].[env].[GetDataCenters] @IncludeOnly = '', @StatusId = ''").collect({ query -> query.Code})
//datacenters .add(1,'')
return datacenters 
