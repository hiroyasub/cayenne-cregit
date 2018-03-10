begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|configuration
operator|.
name|server
package|;
end_package

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
name|DbVersion
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
name|dba
operator|.
name|PkGenerator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|DatabaseMetaData
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_interface
specifier|public
interface|interface
name|PkGeneratorFactory
block|{
comment|/**      * Discovering the primary key based on the database metadata      *      * @param dbType  database type      * @param adapter adapter for generator instantiation      * @param md      connection metadata      * @return an instantiated instance of a specific generator for the current version of the database      * @throws SQLException      */
name|PkGenerator
name|detectPkGenerator
parameter_list|(
name|DbVersion
operator|.
name|DbType
name|dbType
parameter_list|,
name|JdbcAdapter
name|adapter
parameter_list|,
name|DatabaseMetaData
name|md
parameter_list|)
throws|throws
name|SQLException
function_decl|;
block|}
end_interface

end_unit

