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
name|derby
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
name|translator
operator|.
name|select
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
name|translator
operator|.
name|select
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
name|resource
operator|.
name|ResourceLocator
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
name|List
import|;
end_import

begin_comment
comment|/**  * DbAdapter implementation for the<a href="http://db.apache.org/derby/"> Derby RDBMS  *</a>. Sample connection settings to use with Derby are shown below.<h3>Embedded</h3>  *   *<pre>  *  test-derby.jdbc.url = jdbc:derby:testdb;create=true  *  test-derby.jdbc.driver = org.apache.derby.jdbc.EmbeddedDriver  *</pre>  *   *<h3>Network Server</h3>  *   *<pre>  *  derbynet.jdbc.url = jdbc:derby://localhost/cayenne  *  derbynet.jdbc.driver = org.apache.derby.jdbc.ClientDriver  *  derbynet.jdbc.username = someuser  *  derbynet.jdbc.password = secret;  *</pre>  */
end_comment

begin_class
specifier|public
class|class
name|DerbyAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|static
specifier|final
name|String
name|FOR_BIT_DATA_SUFFIX
init|=
literal|" FOR BIT DATA"
decl_stmt|;
specifier|public
name|DerbyAdapter
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
argument_list|)
expr_stmt|;
name|setSupportsGeneratedKeys
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setSupportsBatchUpdates
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|PkGenerator
name|createPkGenerator
parameter_list|()
block|{
return|return
operator|new
name|DerbyPkGenerator
argument_list|(
name|this
argument_list|)
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
comment|// address Derby driver inability to handle java.lang.Short and java.lang.Byte
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
block|}
comment|/**      * Appends SQL for column creation to CREATE TABLE buffer. Only change for Derby is      * that " NULL" is not supported.      *       * @since 1.2      */
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
index|[]
name|types
init|=
name|externalTypesForJdbcType
argument_list|(
name|column
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|types
operator|==
literal|null
operator|||
name|types
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|String
name|entityName
init|=
name|column
operator|.
name|getEntity
argument_list|()
operator|!=
literal|null
condition|?
operator|(
operator|(
name|DbEntity
operator|)
name|column
operator|.
name|getEntity
argument_list|()
operator|)
operator|.
name|getFullyQualifiedName
argument_list|()
else|:
literal|"<null>"
decl_stmt|;
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Undefined type for attribute '"
operator|+
name|entityName
operator|+
literal|"."
operator|+
name|column
operator|.
name|getName
argument_list|()
operator|+
literal|"': "
operator|+
name|column
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
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
expr_stmt|;
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|String
name|type
init|=
name|types
index|[
literal|0
index|]
decl_stmt|;
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
comment|// "BLOB" and "CLOB" type support length. default length is 1M.
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|Types
operator|.
name|BLOB
case|:
case|case
name|Types
operator|.
name|CLOB
case|:
case|case
name|Types
operator|.
name|NCLOB
case|:
return|return
literal|true
return|;
default|default:
return|return
name|super
operator|.
name|typeSupportsLength
argument_list|(
name|type
argument_list|)
return|;
block|}
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
name|QualifierTranslator
name|translator
init|=
operator|new
name|DerbyQualifierTranslator
argument_list|(
name|queryAssembler
argument_list|,
literal|"RTRIM"
argument_list|)
decl_stmt|;
name|translator
operator|.
name|setCaseInsensitive
argument_list|(
name|caseInsensitiveCollations
argument_list|)
expr_stmt|;
return|return
name|translator
return|;
block|}
comment|/**      * @since 3.1      */
annotation|@
name|Override
specifier|protected
name|EJBQLTranslatorFactory
name|createEJBQLTranslatorFactory
parameter_list|()
block|{
name|JdbcEJBQLTranslatorFactory
name|translatorFactory
init|=
operator|new
name|DerbyEJBQLTranslatorFactory
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
name|MergerFactory
name|mergerFactory
parameter_list|()
block|{
return|return
operator|new
name|DerbyMergerFactory
argument_list|()
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
name|SQLException
throws|,
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
name|binding
operator|.
name|getAttribute
argument_list|()
operator|.
name|getType
argument_list|()
operator|==
literal|0
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
name|setType
argument_list|(
name|convertNTypes
argument_list|(
name|binding
operator|.
name|getType
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
block|}
end_class

end_unit

