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
name|openbase
package|;
end_package

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
name|Types
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|dba
operator|.
name|TypesMapping
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
name|map
operator|.
name|DbJoin
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
name|DbRelationship
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
comment|/**  * DbAdapter implementation for<a href="http://www.openbase.com">OpenBase</a>. Sample  * connection settings to use with OpenBase are shown below:  *   *<pre>  * openbase.jdbc.username = test  * openbase.jdbc.password = secret  * openbase.jdbc.url = jdbc:openbase://serverhostname/cayenne  * openbase.jdbc.driver = com.openbase.jdbc.ObDriver  *</pre>  *   * @since 1.1  */
end_comment

begin_class
specifier|public
class|class
name|OpenBaseAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|public
name|OpenBaseAdapter
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
comment|// init defaults
name|this
operator|.
name|setSupportsUniqueConstraints
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Uses special action builder to create the right action.      *       * @since 1.2      */
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
name|OpenBaseActionBuilder
argument_list|(
name|node
argument_list|)
argument_list|)
return|;
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
comment|// Byte handling doesn't work on read...
comment|// need special converter
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|OpenBaseByteType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|OpenBaseCharType
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
comment|// OpenBase makes no distinction between CHAR and VARCHAR
comment|// so lets use VARCHAR, since it seems more generic
if|if
condition|(
name|type
operator|==
name|Types
operator|.
name|CHAR
condition|)
block|{
name|type
operator|=
name|Types
operator|.
name|VARCHAR
expr_stmt|;
block|}
return|return
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
return|;
block|}
comment|/**      * Returns word "go".      */
annotation|@
name|Override
specifier|public
name|String
name|getBatchTerminator
parameter_list|()
block|{
return|return
literal|"go"
return|;
block|}
comment|/**      * Returns null, since views are not yet supported in openbase.      */
annotation|@
name|Override
specifier|public
name|String
name|tableTypeForView
parameter_list|()
block|{
comment|// TODO: according to OpenBase docs views *ARE* supported.
return|return
literal|null
return|;
block|}
comment|/**      * Returns OpenBase-specific translator for queries.      */
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
name|OpenBaseQualifierTranslator
argument_list|(
name|queryAssembler
argument_list|)
return|;
block|}
comment|/**      * Creates and returns a primary key generator. Overrides superclass implementation to      * return an instance of OpenBasePkGenerator that uses built-in multi-server primary      * key generation.      */
annotation|@
name|Override
specifier|protected
name|PkGenerator
name|createPkGenerator
parameter_list|()
block|{
return|return
operator|new
name|OpenBasePkGenerator
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns a SQL string that can be used to create database table corresponding to      *<code>ent</code> parameter.      */
annotation|@
name|Override
specifier|public
name|String
name|createTable
parameter_list|(
name|DbEntity
name|ent
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"CREATE TABLE "
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|ent
argument_list|)
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|" ("
argument_list|)
expr_stmt|;
comment|// columns
name|Iterator
argument_list|<
name|DbAttribute
argument_list|>
name|it
init|=
name|ent
operator|.
name|getAttributes
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|boolean
name|first
init|=
literal|true
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
if|if
condition|(
name|first
condition|)
block|{
name|first
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
name|DbAttribute
name|at
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// attribute may not be fully valid, do a simple check
if|if
condition|(
name|at
operator|.
name|getType
argument_list|()
operator|==
name|TypesMapping
operator|.
name|NOT_DEFINED
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Undefined type for attribute '"
operator|+
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"."
operator|+
name|at
operator|.
name|getName
argument_list|()
operator|+
literal|"'."
argument_list|)
throw|;
block|}
name|String
index|[]
name|types
init|=
name|externalTypesForJdbcType
argument_list|(
name|at
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
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Undefined type for attribute '"
operator|+
name|ent
operator|.
name|getFullyQualifiedName
argument_list|()
operator|+
literal|"."
operator|+
name|at
operator|.
name|getName
argument_list|()
operator|+
literal|"': "
operator|+
name|at
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|type
init|=
name|types
index|[
literal|0
index|]
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
name|quotingStrategy
operator|.
name|quotedName
argument_list|(
name|at
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
comment|// append size and precision (if applicable)
if|if
condition|(
name|typeSupportsLength
argument_list|(
name|at
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
name|int
name|len
init|=
name|at
operator|.
name|getMaxLength
argument_list|()
decl_stmt|;
name|int
name|scale
init|=
name|TypesMapping
operator|.
name|isDecimal
argument_list|(
name|at
operator|.
name|getType
argument_list|()
argument_list|)
condition|?
name|at
operator|.
name|getScale
argument_list|()
else|:
operator|-
literal|1
decl_stmt|;
comment|// sanity check
if|if
condition|(
name|scale
operator|>
name|len
condition|)
block|{
name|scale
operator|=
operator|-
literal|1
expr_stmt|;
block|}
if|if
condition|(
name|len
operator|>
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|'('
argument_list|)
operator|.
name|append
argument_list|(
name|len
argument_list|)
expr_stmt|;
if|if
condition|(
name|scale
operator|>=
literal|0
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|scale
argument_list|)
expr_stmt|;
block|}
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|at
operator|.
name|isMandatory
argument_list|()
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" NOT NULL"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|buf
operator|.
name|append
argument_list|(
literal|" NULL"
argument_list|)
expr_stmt|;
block|}
block|}
name|buf
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns a SQL string that can be used to create a foreign key constraint for the      * relationship.      */
annotation|@
name|Override
specifier|public
name|String
name|createFkConstraint
parameter_list|(
name|DbRelationship
name|rel
parameter_list|)
block|{
name|StringBuilder
name|buf
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// OpendBase Specifics is that we need to create a constraint going
comment|// from destination to source for this to work...
name|DbEntity
name|sourceEntity
init|=
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getSourceEntity
argument_list|()
decl_stmt|;
name|DbEntity
name|targetEntity
init|=
operator|(
name|DbEntity
operator|)
name|rel
operator|.
name|getTargetEntity
argument_list|()
decl_stmt|;
name|String
name|toMany
init|=
operator|(
operator|!
name|rel
operator|.
name|isToMany
argument_list|()
operator|)
condition|?
literal|"'1'"
else|:
literal|"'0'"
decl_stmt|;
comment|// TODO: doesn't seem like OpenBase supports compound joins...
comment|// need to doublecheck that
name|int
name|joinsLen
init|=
name|rel
operator|.
name|getJoins
argument_list|()
operator|.
name|size
argument_list|()
decl_stmt|;
if|if
condition|(
name|joinsLen
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Relationship has no joins: "
operator|+
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
if|else if
condition|(
name|joinsLen
operator|>
literal|1
condition|)
block|{
comment|// ignore extra joins
block|}
name|DbJoin
name|join
init|=
name|rel
operator|.
name|getJoins
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"INSERT INTO _SYS_RELATIONSHIP ("
argument_list|)
operator|.
name|append
argument_list|(
literal|"dest_table, dest_column, source_table, source_column, "
argument_list|)
operator|.
name|append
argument_list|(
literal|"block_delete, cascade_delete, one_to_many, operator, relationshipName"
argument_list|)
operator|.
name|append
argument_list|(
literal|") VALUES ('"
argument_list|)
operator|.
name|append
argument_list|(
name|sourceEntity
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"', '"
argument_list|)
operator|.
name|append
argument_list|(
name|join
operator|.
name|getSourceName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"', '"
argument_list|)
operator|.
name|append
argument_list|(
name|targetEntity
operator|.
name|getFullyQualifiedName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"', '"
argument_list|)
operator|.
name|append
argument_list|(
name|join
operator|.
name|getTargetName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"', 0, 0, "
argument_list|)
operator|.
name|append
argument_list|(
name|toMany
argument_list|)
operator|.
name|append
argument_list|(
literal|", '=', '"
argument_list|)
operator|.
name|append
argument_list|(
name|rel
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"')"
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
comment|// OpenBase JDBC driver has trouble reading "integer" as byte
comment|// this converter addresses such problem
specifier|static
class|class
name|OpenBaseByteType
extends|extends
name|ByteType
block|{
name|OpenBaseByteType
parameter_list|()
block|{
name|super
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
comment|// read value as int, and then narrow it down
name|int
name|val
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
name|Byte
operator|.
name|valueOf
argument_list|(
operator|(
name|byte
operator|)
name|val
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|materializeObject
parameter_list|(
name|CallableStatement
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
comment|// read value as int, and then narrow it down
name|int
name|val
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
name|Byte
operator|.
name|valueOf
argument_list|(
operator|(
name|byte
operator|)
name|val
argument_list|)
return|;
block|}
block|}
specifier|static
class|class
name|OpenBaseCharType
extends|extends
name|CharType
block|{
name|OpenBaseCharType
parameter_list|()
block|{
name|super
argument_list|(
literal|false
argument_list|,
literal|true
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
comment|// These to types map to "text"; and when setting "text" as object
comment|// OB assumes that the object is the actual CLOB... weird
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
name|LONGVARCHAR
condition|)
block|{
name|st
operator|.
name|setString
argument_list|(
name|pos
argument_list|,
operator|(
name|String
operator|)
name|val
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
annotation|@
name|Override
specifier|public
name|MergerFactory
name|mergerFactory
parameter_list|()
block|{
return|return
operator|new
name|OpenBaseMergerFactory
argument_list|()
return|;
block|}
block|}
end_class

end_unit

