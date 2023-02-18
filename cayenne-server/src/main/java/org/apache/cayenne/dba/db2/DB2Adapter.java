begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|db2
package|;
end_package

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
name|Types
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
name|sqlbuilder
operator|.
name|sqltree
operator|.
name|SQLTreeProcessor
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
name|translator
operator|.
name|ParameterBinding
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
name|translator
operator|.
name|ejbql
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
name|translator
operator|.
name|ejbql
operator|.
name|JdbcEJBQLTranslatorFactory
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
name|BooleanType
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
name|ExtendedTypeFactory
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
name|JsonType
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
name|ValueObjectTypeRegistry
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
name|configuration
operator|.
name|Constants
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
name|configuration
operator|.
name|RuntimeProperties
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
name|di
operator|.
name|Inject
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
name|resource
operator|.
name|ResourceLocator
import|;
end_import

begin_comment
comment|/**  * DbAdapter implementation for the<a href="http://www.ibm.com/db2/"> DB2 RDBMS</a>.  * Sample connection settings to use with DB2 are shown below:  *   *<pre>  *       test-db2.jdbc.username = test  *       test-db2.jdbc.password = secret  *       test-db2.jdbc.url = jdbc:db2://servername:50000/databasename  *       test-db2.jdbc.driver = com.ibm.db2.jcc.DB2Driver  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|DB2Adapter
extends|extends
name|JdbcAdapter
block|{
specifier|private
specifier|static
specifier|final
name|String
name|FOR_BIT_DATA_SUFFIX
init|=
literal|" FOR BIT DATA"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TRIM_FUNCTION
init|=
literal|"RTRIM"
decl_stmt|;
specifier|public
name|DB2Adapter
parameter_list|(
annotation|@
name|Inject
name|RuntimeProperties
name|runtimeProperties
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_DEFAULT_TYPES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|defaultExtendedTypes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_USER_TYPES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedType
argument_list|>
name|userExtendedTypes
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_TYPE_FACTORIES_LIST
argument_list|)
name|List
argument_list|<
name|ExtendedTypeFactory
argument_list|>
name|extendedTypeFactories
parameter_list|,
annotation|@
name|Inject
argument_list|(
name|Constants
operator|.
name|SERVER_RESOURCE_LOCATOR
argument_list|)
name|ResourceLocator
name|resourceLocator
parameter_list|,
annotation|@
name|Inject
name|ValueObjectTypeRegistry
name|valueObjectTypeRegistry
parameter_list|)
block|{
name|super
argument_list|(
name|runtimeProperties
argument_list|,
name|defaultExtendedTypes
argument_list|,
name|userExtendedTypes
argument_list|,
name|extendedTypeFactories
argument_list|,
name|resourceLocator
argument_list|,
name|valueObjectTypeRegistry
argument_list|)
expr_stmt|;
name|setSupportsGeneratedKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
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
name|CharType
name|charType
init|=
operator|new
name|CharType
argument_list|(
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|map
operator|.
name|registerType
argument_list|(
name|charType
argument_list|)
expr_stmt|;
comment|// configure boolean type to work with numeric columns
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|DB2BooleanType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|ByteArrayType
argument_list|(
literal|false
argument_list|,
literal|false
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|JsonType
argument_list|(
name|charType
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @since 4.0      */
annotation|@
name|Override
specifier|public
name|void
name|createTableAppendColumn
parameter_list|(
name|StringBuffer
name|sqlBuffer
parameter_list|,
name|DbAttribute
name|column
parameter_list|)
block|{
name|String
name|type
init|=
name|getType
argument_list|(
name|this
argument_list|,
name|column
argument_list|)
decl_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedName
argument_list|(
name|column
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
comment|// DB2 GRAPHIC type that is used for NCHAR type length is in characters not in bytes
comment|// so divide max size by 2 and later restore the value
name|int
name|maxLength
init|=
name|column
operator|.
name|getMaxLength
argument_list|()
decl_stmt|;
if|if
condition|(
name|column
operator|.
name|getType
argument_list|()
operator|==
name|Types
operator|.
name|NCHAR
condition|)
block|{
name|column
operator|.
name|setMaxLength
argument_list|(
name|maxLength
operator|/
literal|2
argument_list|)
expr_stmt|;
block|}
name|String
name|length
init|=
name|sizeAndPrecision
argument_list|(
name|this
argument_list|,
name|column
argument_list|)
decl_stmt|;
name|column
operator|.
name|setMaxLength
argument_list|(
name|maxLength
argument_list|)
expr_stmt|;
comment|// assemble...
comment|// note that max length for types like XYZ FOR BIT DATA must be entered in the
comment|// middle of type name, e.g. VARCHAR (100) FOR BIT DATA.
name|int
name|suffixIndex
init|=
name|type
operator|.
name|indexOf
argument_list|(
name|FOR_BIT_DATA_SUFFIX
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|length
operator|.
name|isEmpty
argument_list|()
operator|&&
name|suffixIndex
operator|>
literal|0
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
name|type
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|suffixIndex
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|length
argument_list|)
operator|.
name|append
argument_list|(
name|FOR_BIT_DATA_SUFFIX
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|length
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|column
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" NOT NULL"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|column
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" GENERATED BY DEFAULT AS IDENTITY"
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @since 4.0      */
annotation|@
name|Override
specifier|public
name|boolean
name|typeSupportsLength
parameter_list|(
name|int
name|type
parameter_list|)
block|{
return|return
name|type
operator|==
name|Types
operator|.
name|LONGVARCHAR
operator|||
name|type
operator|==
name|Types
operator|.
name|LONGVARBINARY
operator|||
name|super
operator|.
name|typeSupportsLength
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * @since 4.2      */
annotation|@
name|Override
specifier|public
name|SQLTreeProcessor
name|getSqlTreeProcessor
parameter_list|()
block|{
return|return
operator|new
name|DB2SQLTreeProcessor
argument_list|()
return|;
block|}
comment|/**      * @since 4.0      */
annotation|@
name|Override
specifier|public
name|EJBQLTranslatorFactory
name|getEjbqlTranslatorFactory
parameter_list|()
block|{
name|JdbcEJBQLTranslatorFactory
name|translatorFactory
init|=
operator|new
name|DB2EJBQLTranslatorFactory
argument_list|()
decl_stmt|;
name|translatorFactory
operator|.
name|setCaseInsensitive
argument_list|(
name|caseInsensitiveCollations
argument_list|)
expr_stmt|;
return|return
name|translatorFactory
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
name|ParameterBinding
name|binding
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|binding
operator|.
name|getValue
argument_list|()
operator|==
literal|null
operator|&&
operator|(
name|binding
operator|.
name|getJdbcType
argument_list|()
operator|==
literal|0
operator|||
name|binding
operator|.
name|getJdbcType
argument_list|()
operator|==
name|Types
operator|.
name|BOOLEAN
operator|)
condition|)
block|{
name|statement
operator|.
name|setNull
argument_list|(
name|binding
operator|.
name|getStatementPosition
argument_list|()
argument_list|,
name|Types
operator|.
name|VARCHAR
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|binding
operator|.
name|setJdbcType
argument_list|(
name|convertNTypes
argument_list|(
name|binding
operator|.
name|getJdbcType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|bindParameter
argument_list|(
name|statement
argument_list|,
name|binding
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Uses special action builder to create the right action.      *       * @since 3.1      */
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
name|DB2ActionBuilder
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @since 4.0      */
specifier|private
name|int
name|convertNTypes
parameter_list|(
name|int
name|sqlType
parameter_list|)
block|{
switch|switch
condition|(
name|sqlType
condition|)
block|{
case|case
name|Types
operator|.
name|NCHAR
case|:
return|return
name|Types
operator|.
name|CHAR
return|;
case|case
name|Types
operator|.
name|NVARCHAR
case|:
return|return
name|Types
operator|.
name|VARCHAR
return|;
case|case
name|Types
operator|.
name|LONGNVARCHAR
case|:
return|return
name|Types
operator|.
name|LONGVARCHAR
return|;
case|case
name|Types
operator|.
name|NCLOB
case|:
return|return
name|Types
operator|.
name|CLOB
return|;
default|default:
return|return
name|sqlType
return|;
block|}
block|}
specifier|final
class|class
name|DB2BooleanType
extends|extends
name|BooleanType
block|{
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Boolean
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
name|val
condition|?
literal|1
else|:
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

