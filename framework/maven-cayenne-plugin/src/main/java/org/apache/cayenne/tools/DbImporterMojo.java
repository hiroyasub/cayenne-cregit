begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|tools
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|ObjEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|MapLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DbLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DbLoaderDelegate
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|DbAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|JdbcAdapter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|conn
operator|.
name|DriverDataSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Driver
import|;
end_import

begin_comment
comment|/**  * Maven mojo to reverse engineer datamap from DB.  *  * @since 3.0  *  * @phase generate-sources  * @goal cdbimport  */
end_comment

begin_class
specifier|public
class|class
name|DbImporterMojo
extends|extends
name|AbstractMojo
block|{
comment|/** 	 * DataMap XML file to use as a base for DB importing. 	 * 	 * @parameter expression="${cdbimport.map}" 	 * @required 	 */
specifier|private
name|File
name|map
decl_stmt|;
comment|/**      * DB schema to use for DB importing.      */
specifier|private
name|String
name|schemaName
decl_stmt|;
comment|/**      * Pattern for tables to import from DB.      */
specifier|private
name|String
name|tablePattern
decl_stmt|;
comment|/**      * Java class implementing org.apache.cayenne.dba.DbAdapter.      * While this attribute is optional (a generic JdbcAdapter is used if not set),      * it is highly recommended to specify correct target adapter.      *      * @parameter expression="${cdbimport.adapter}"      */
specifier|private
name|String
name|adapter
decl_stmt|;
comment|/**      * A class of JDBC driver to use for the target database.      *      * @parameter expression="${cdbimport.driver}"      * @required      */
specifier|private
name|String
name|driver
decl_stmt|;
comment|/**      * JDBC connection URL of a target database.      *      * @parameter expression="${cdbimport.url}"      * @required      */
specifier|private
name|String
name|url
decl_stmt|;
comment|/**      * Database user name.      *      * @parameter expression="${cdbimport.username}"      */
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      * Database user password.      *      * @parameter expression="${cdbimport.password}"      */
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|Log
name|logger
decl_stmt|;
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|logger
operator|=
operator|new
name|MavenLogger
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"connection settings - [driver: %s, url: %s, username: %s]"
argument_list|,
name|driver
argument_list|,
name|url
argument_list|,
name|username
argument_list|)
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"importer options - [map: %s, schemaName: %s, tablePattern: %s, driver: %s, url: %s, username: %s, password: %s]"
argument_list|,
name|map
argument_list|,
name|schemaName
argument_list|,
name|tablePattern
argument_list|,
name|driver
argument_list|,
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
specifier|final
name|DbAdapter
name|adapterInst
init|=
operator|(
name|adapter
operator|==
literal|null
operator|)
condition|?
operator|new
name|JdbcAdapter
argument_list|()
else|:
operator|(
name|DbAdapter
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|adapter
argument_list|)
operator|.
name|newInstance
argument_list|()
decl_stmt|;
comment|// load driver taking custom CLASSPATH into account...
name|DriverDataSource
name|dataSource
init|=
operator|new
name|DriverDataSource
argument_list|(
operator|(
name|Driver
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|driver
argument_list|)
operator|.
name|newInstance
argument_list|()
argument_list|,
name|url
argument_list|,
name|username
argument_list|,
name|password
argument_list|)
decl_stmt|;
comment|// Load the data map and run the db importer.
name|DataMap
name|dataMap
init|=
name|loadDataMap
argument_list|()
decl_stmt|;
specifier|final
name|DbLoader
name|loader
init|=
operator|new
name|DbLoader
argument_list|(
name|dataSource
operator|.
name|getConnection
argument_list|()
argument_list|,
name|adapterInst
argument_list|,
operator|new
name|LoaderDelegate
argument_list|()
argument_list|)
decl_stmt|;
name|loader
operator|.
name|loadDataMapFromDB
argument_list|(
name|schemaName
argument_list|,
name|tablePattern
argument_list|,
name|dataMap
argument_list|)
expr_stmt|;
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|map
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
name|PrintWriter
name|pw
init|=
operator|new
name|PrintWriter
argument_list|(
name|f
argument_list|)
decl_stmt|;
name|dataMap
operator|.
name|encodeAsXML
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|pw
operator|.
name|flush
argument_list|()
expr_stmt|;
name|pw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|Throwable
name|th
init|=
name|Util
operator|.
name|unwindException
argument_list|(
name|ex
argument_list|)
decl_stmt|;
name|String
name|message
init|=
literal|"Error importing database schema"
decl_stmt|;
if|if
condition|(
name|th
operator|.
name|getLocalizedMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|+=
literal|": "
operator|+
name|th
operator|.
name|getLocalizedMessage
argument_list|()
expr_stmt|;
block|}
name|logger
operator|.
name|error
argument_list|(
name|message
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|MojoExecutionException
argument_list|(
name|message
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
specifier|final
class|class
name|LoaderDelegate
implements|implements
name|DbLoaderDelegate
block|{
specifier|public
name|boolean
name|overwriteDbEntity
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
throws|throws
name|CayenneException
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|dbEntityAdded
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Added DB entity: "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|addDbEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|dbEntityRemoved
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Removed DB entity: "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|removeDbEntity
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objEntityAdded
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Added obj entity: "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|addObjEntity
argument_list|(
name|ent
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|objEntityRemoved
parameter_list|(
name|ObjEntity
name|ent
parameter_list|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Removed obj entity: "
operator|+
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ent
operator|.
name|getDataMap
argument_list|()
operator|.
name|removeObjEntity
argument_list|(
name|ent
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** Loads and returns DataMap based on<code>map</code> attribute. */
specifier|protected
name|DataMap
name|loadDataMap
parameter_list|()
throws|throws
name|Exception
block|{
name|InputSource
name|in
init|=
operator|new
name|InputSource
argument_list|(
name|map
operator|.
name|getCanonicalPath
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|MapLoader
argument_list|()
operator|.
name|loadDataMap
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
end_class

end_unit

