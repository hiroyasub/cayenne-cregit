begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dba
operator|.
name|oracle
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|CayenneRuntimeException
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
name|DataNode
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
name|jdbc
operator|.
name|EJBQLTranslatorFactory
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
name|trans
operator|.
name|QualifierTranslator
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
name|trans
operator|.
name|QueryAssembler
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
name|trans
operator|.
name|TrimmingQualifierTranslator
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
name|types
operator|.
name|ByteArrayType
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
name|types
operator|.
name|ByteType
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
name|types
operator|.
name|CharType
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
name|types
operator|.
name|DefaultType
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
name|types
operator|.
name|ExtendedType
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
name|types
operator|.
name|ExtendedTypeMap
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
name|types
operator|.
name|ShortType
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
name|QuotingStrategy
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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbAttribute
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
name|merge
operator|.
name|MergerFactory
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
name|query
operator|.
name|BatchQuery
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
name|query
operator|.
name|InsertBatchQuery
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
name|query
operator|.
name|Query
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
name|query
operator|.
name|SQLAction
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
name|query
operator|.
name|UpdateBatchQuery
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * DbAdapter implementation for<a href="http://www.oracle.com">Oracle RDBMS</a>. Sample  * connection settings to use with Oracle are shown below:  *   *<pre>  *          test-oracle.cayenne.adapter = org.apache.cayenne.dba.oracle.OracleAdapter  *          test-oracle.jdbc.username = test  *          test-oracle.jdbc.password = secret  *          test-oracle.jdbc.url = jdbc:oracle:thin:@//192.168.0.20:1521/ora1   *          test-oracle.jdbc.driver = oracle.jdbc.driver.OracleDriver     *</pre>  *   */
end_comment

begin_class
specifier|public
class|class
name|OracleAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|public
specifier|static
specifier|final
name|String
name|ORACLE_FLOAT
init|=
literal|"FLOAT"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ORACLE_BLOB
init|=
literal|"BLOB"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ORACLE_CLOB
init|=
literal|"CLOB"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TRIM_FUNCTION
init|=
literal|"RTRIM"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NEW_CLOB_FUNCTION
init|=
literal|"EMPTY_CLOB()"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|NEW_BLOB_FUNCTION
init|=
literal|"EMPTY_BLOB()"
decl_stmt|;
specifier|protected
specifier|static
name|boolean
name|initDone
decl_stmt|;
specifier|protected
specifier|static
name|int
name|oracleCursorType
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
specifier|protected
specifier|static
name|Method
name|outputStreamFromBlobMethod
decl_stmt|;
specifier|protected
specifier|static
name|Method
name|writerFromClobMethod
decl_stmt|;
specifier|protected
specifier|static
name|boolean
name|supportsOracleLOB
decl_stmt|;
static|static
block|{
comment|// TODO: as CAY-234 shows, having such initialization done in a static fashion
comment|// makes it untestable and any potential problems hard to reproduce. Make this
comment|// an instance method (with all the affected vars) and write unit tests.
name|initDriverInformation
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|initDriverInformation
parameter_list|()
block|{
name|initDone
operator|=
literal|true
expr_stmt|;
comment|// configure static information
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|oraTypes
init|=
name|Class
operator|.
name|forName
argument_list|(
literal|"oracle.jdbc.driver.OracleTypes"
argument_list|)
decl_stmt|;
name|Field
name|cursorField
init|=
name|oraTypes
operator|.
name|getField
argument_list|(
literal|"CURSOR"
argument_list|)
decl_stmt|;
name|oracleCursorType
operator|=
name|cursorField
operator|.
name|getInt
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|outputStreamFromBlobMethod
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"oracle.sql.BLOB"
argument_list|)
operator|.
name|getMethod
argument_list|(
literal|"getBinaryOutputStream"
argument_list|)
expr_stmt|;
name|writerFromClobMethod
operator|=
name|Class
operator|.
name|forName
argument_list|(
literal|"oracle.sql.CLOB"
argument_list|)
operator|.
name|getMethod
argument_list|(
literal|"getCharacterOutputStream"
argument_list|)
expr_stmt|;
name|supportsOracleLOB
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// ignoring...
block|}
block|}
comment|/**      * @deprecated since 3.0, as a generic BLOB method is used to write BLOBs.      */
specifier|public
specifier|static
name|Method
name|getOutputStreamFromBlobMethod
parameter_list|()
block|{
return|return
name|outputStreamFromBlobMethod
return|;
block|}
comment|// TODO: rename to something that looks like English ...
specifier|public
specifier|static
name|boolean
name|isSupportsOracleLOB
parameter_list|()
block|{
return|return
name|supportsOracleLOB
return|;
block|}
comment|/**      * Utility method that returns<code>true</code> if the query will update at least      * one BLOB or CLOB DbAttribute.      *       * @since 1.2      */
specifier|static
name|boolean
name|updatesLOBColumns
parameter_list|(
name|BatchQuery
name|query
parameter_list|)
block|{
name|boolean
name|isInsert
init|=
name|query
operator|instanceof
name|InsertBatchQuery
decl_stmt|;
name|boolean
name|isUpdate
init|=
name|query
operator|instanceof
name|UpdateBatchQuery
decl_stmt|;
if|if
condition|(
operator|!
name|isInsert
operator|&&
operator|!
name|isUpdate
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|DbAttribute
argument_list|>
name|updatedAttributes
init|=
operator|(
name|isInsert
operator|)
condition|?
name|query
operator|.
name|getDbAttributes
argument_list|()
else|:
operator|(
operator|(
name|UpdateBatchQuery
operator|)
name|query
operator|)
operator|.
name|getUpdatedAttributes
argument_list|()
decl_stmt|;
for|for
control|(
name|DbAttribute
name|attr
range|:
name|updatedAttributes
control|)
block|{
name|int
name|type
init|=
name|attr
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CLOB
operator|||
name|type
operator|==
name|Types
operator|.
name|BLOB
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
comment|/**      * @deprecated since 3.0, as a generic CLOB method is used to write CLOBs.      */
specifier|public
specifier|static
name|Method
name|getWriterFromClobMethod
parameter_list|()
block|{
return|return
name|writerFromClobMethod
return|;
block|}
comment|/**      * Returns an Oracle JDBC extension type defined in      * oracle.jdbc.driver.OracleTypes.CURSOR. This value is determined from Oracle driver      * classes via reflection in runtime, so that Cayenne code has no compile dependency      * on the driver. This means that calling this method when the driver is not available      * will result in an exception.      */
specifier|public
specifier|static
name|int
name|getOracleCursorType
parameter_list|()
block|{
if|if
condition|(
name|oracleCursorType
operator|==
name|Integer
operator|.
name|MAX_VALUE
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No information exists about oracle types. "
operator|+
literal|"Check that Oracle JDBC driver is available to the application."
argument_list|)
throw|;
block|}
return|return
name|oracleCursorType
return|;
block|}
specifier|public
name|OracleAdapter
parameter_list|()
block|{
comment|// enable batch updates by default
name|setSupportsBatchUpdates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 3.0      */
annotation|@
name|Override
specifier|protected
name|EJBQLTranslatorFactory
name|createEJBQLTranslatorFactory
parameter_list|()
block|{
return|return
operator|new
name|OracleEJBQLTranslatorFactory
argument_list|()
return|;
block|}
comment|/**      * Installs appropriate ExtendedTypes as converters for passing values between JDBC      * and Java layers.      */
annotation|@
name|Override
specifier|protected
name|void
name|configureExtendedTypes
parameter_list|(
name|ExtendedTypeMap
name|map
parameter_list|)
block|{
name|super
operator|.
name|configureExtendedTypes
argument_list|(
name|map
argument_list|)
expr_stmt|;
comment|// create specially configured CharType handler
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|CharType
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// create specially configured ByteArrayType handler
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|ByteArrayType
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// override date handler with Oracle handler
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|OracleUtilDateType
argument_list|()
argument_list|)
expr_stmt|;
comment|// At least on MacOS X, driver does not handle Short and Byte properly
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|ShortType
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|ByteType
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
comment|// these two types are needed to replace PreparedStatement binding
comment|// via "setObject()" to a call to setInt or setDouble to make
comment|// Oracle happy, esp. with the AST* expression classes
comment|// that do not evaluate constants to BigDecimals, but rather
comment|// Integer and Double
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|OracleIntegerType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|OracleDoubleType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|OracleBooleanType
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates and returns a primary key generator. Overrides superclass implementation to      * return an instance of OraclePkGenerator.      */
annotation|@
name|Override
specifier|protected
name|PkGenerator
name|createPkGenerator
parameter_list|()
block|{
return|return
operator|new
name|OraclePkGenerator
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns a query string to drop a table corresponding to<code>ent</code>      * DbEntity. Changes superclass behavior to drop all related foreign key constraints.      *       * @since 3.0      */
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|dropTableStatements
parameter_list|(
name|DbEntity
name|table
parameter_list|)
block|{
name|QuotingStrategy
name|context
init|=
name|getQuotingStrategy
argument_list|(
name|table
operator|.
name|getDataMap
argument_list|()
operator|.
name|isQuotingSQLIdentifiers
argument_list|()
argument_list|)
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|(
literal|"DROP TABLE "
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|context
operator|.
name|quoteFullyQualifiedName
argument_list|(
name|table
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" CASCADE CONSTRAINTS"
argument_list|)
expr_stmt|;
return|return
name|Collections
operator|.
name|singleton
argument_list|(
name|buf
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|bindParameter
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
name|object
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|sqlType
parameter_list|,
name|int
name|scale
parameter_list|)
throws|throws
name|SQLException
throws|,
name|Exception
block|{
comment|// Oracle doesn't support BOOLEAN even when binding NULL, so have to intercept
comment|// NULL Boolean here, as super doesn't pass it through ExtendedType...
if|if
condition|(
name|object
operator|==
literal|null
operator|&&
name|sqlType
operator|==
name|Types
operator|.
name|BOOLEAN
condition|)
block|{
name|ExtendedType
name|typeProcessor
init|=
name|getExtendedTypes
argument_list|()
operator|.
name|getRegisteredType
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|typeProcessor
operator|.
name|setJdbcObject
argument_list|(
name|statement
argument_list|,
name|object
argument_list|,
name|pos
argument_list|,
name|sqlType
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|object
argument_list|,
name|pos
argument_list|,
name|sqlType
argument_list|,
name|scale
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Fixes some reverse engineering problems. Namely if a columns is created as DECIMAL      * and has non-positive precision it is converted to INTEGER.      */
annotation|@
name|Override
specifier|public
name|DbAttribute
name|buildAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|typeName
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|size
parameter_list|,
name|int
name|scale
parameter_list|,
name|boolean
name|allowNulls
parameter_list|)
block|{
name|DbAttribute
name|attr
init|=
name|super
operator|.
name|buildAttribute
argument_list|(
name|name
argument_list|,
name|typeName
argument_list|,
name|type
argument_list|,
name|size
argument_list|,
name|scale
argument_list|,
name|allowNulls
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|DECIMAL
operator|&&
name|scale
operator|<=
literal|0
condition|)
block|{
name|attr
operator|.
name|setType
argument_list|(
name|Types
operator|.
name|INTEGER
argument_list|)
expr_stmt|;
name|attr
operator|.
name|setScale
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|OTHER
condition|)
block|{
comment|// in this case we need to guess the attribute type
comment|// based on its string value
if|if
condition|(
name|ORACLE_FLOAT
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|attr
operator|.
name|setType
argument_list|(
name|Types
operator|.
name|FLOAT
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|ORACLE_BLOB
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|attr
operator|.
name|setType
argument_list|(
name|Types
operator|.
name|BLOB
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|ORACLE_CLOB
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|attr
operator|.
name|setType
argument_list|(
name|Types
operator|.
name|CLOB
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|DATE
condition|)
block|{
comment|// Oracle DATE can store JDBC TIMESTAMP
if|if
condition|(
literal|"DATE"
operator|.
name|equals
argument_list|(
name|typeName
argument_list|)
condition|)
block|{
name|attr
operator|.
name|setType
argument_list|(
name|Types
operator|.
name|TIMESTAMP
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|attr
return|;
block|}
comment|/**      * Returns a trimming translator.      */
annotation|@
name|Override
specifier|public
name|QualifierTranslator
name|getQualifierTranslator
parameter_list|(
name|QueryAssembler
name|queryAssembler
parameter_list|)
block|{
return|return
operator|new
name|TrimmingQualifierTranslator
argument_list|(
name|queryAssembler
argument_list|,
name|OracleAdapter
operator|.
name|TRIM_FUNCTION
argument_list|)
return|;
block|}
comment|/**      * Uses OracleActionBuilder to create the right action.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|SQLAction
name|getAction
parameter_list|(
name|Query
name|query
parameter_list|,
name|DataNode
name|node
parameter_list|)
block|{
return|return
name|query
operator|.
name|createSQLAction
argument_list|(
operator|new
name|OracleActionBuilder
argument_list|(
name|this
argument_list|,
name|node
operator|.
name|getEntityResolver
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @since 1.1      */
specifier|final
class|class
name|OracleIntegerType
extends|extends
name|DefaultType
block|{
specifier|public
name|OracleIntegerType
parameter_list|()
block|{
name|super
argument_list|(
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|st
operator|.
name|setInt
argument_list|(
name|pos
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|val
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setJdbcObject
argument_list|(
name|st
argument_list|,
name|val
argument_list|,
name|pos
argument_list|,
name|type
argument_list|,
name|precision
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @since 1.1      */
specifier|final
class|class
name|OracleDoubleType
extends|extends
name|DefaultType
block|{
specifier|public
name|OracleDoubleType
parameter_list|()
block|{
name|super
argument_list|(
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|st
operator|.
name|setDouble
argument_list|(
name|pos
argument_list|,
operator|(
operator|(
name|Number
operator|)
name|val
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|super
operator|.
name|setJdbcObject
argument_list|(
name|st
argument_list|,
name|val
argument_list|,
name|pos
argument_list|,
name|type
argument_list|,
name|precision
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * @since 3.0      */
specifier|final
class|class
name|OracleBooleanType
implements|implements
name|ExtendedType
block|{
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**          * @deprecated since 3.0 as validation should not be done at the DataNode level.          */
specifier|public
name|boolean
name|validateProperty
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|value
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Oracle does not support Types.BOOLEAN, so we have to override user mapping
comment|// unconditionally
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
name|st
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|Types
operator|.
name|INTEGER
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|boolean
name|flag
init|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|val
argument_list|)
decl_stmt|;
name|st
operator|.
name|setInt
argument_list|(
name|pos
argument_list|,
name|flag
condition|?
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Oracle does not support Types.BOOLEAN, so we have to override user mapping
comment|// unconditionally
name|int
name|i
init|=
name|rs
operator|.
name|getInt
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
operator|(
name|rs
operator|.
name|wasNull
argument_list|()
operator|)
condition|?
literal|null
else|:
name|i
operator|==
literal|0
condition|?
name|Boolean
operator|.
name|FALSE
else|:
name|Boolean
operator|.
name|TRUE
return|;
block|}
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
name|st
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
comment|// Oracle does not support Types.BOOLEAN, so we have to override user mapping
comment|// unconditionally
name|int
name|i
init|=
name|st
operator|.
name|getInt
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
operator|(
name|st
operator|.
name|wasNull
argument_list|()
operator|)
condition|?
literal|null
else|:
name|i
operator|==
literal|0
condition|?
name|Boolean
operator|.
name|FALSE
else|:
name|Boolean
operator|.
name|TRUE
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|MergerFactory
name|mergerFactory
parameter_list|()
block|{
return|return
operator|new
name|OracleMergerFactory
argument_list|()
return|;
block|}
block|}
end_class

end_unit

