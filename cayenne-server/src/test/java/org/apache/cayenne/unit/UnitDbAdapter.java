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
name|unit
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
name|exp
operator|.
name|parser
operator|.
name|ASTExtract
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
name|Procedure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
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
name|Statement
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Defines API and a common superclass for testing various database features.  * Different databases support different feature sets that need to be tested  * differently. Many things implemented in subclasses may become future  * candidates for inclusion in the corresponding adapter code.  */
end_comment

begin_class
specifier|public
class|class
name|UnitDbAdapter
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UnitDbAdapter
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RuntimeProperties
name|runtimeProperties
decl_stmt|;
specifier|protected
name|DbAdapter
name|adapter
decl_stmt|;
specifier|public
name|UnitDbAdapter
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Null adapter."
argument_list|)
throw|;
block|}
name|this
operator|.
name|adapter
operator|=
name|adapter
expr_stmt|;
block|}
specifier|public
name|boolean
name|supportsPKGeneratorConcurrency
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|String
name|getIdentifiersStartQuote
parameter_list|()
block|{
return|return
literal|"\""
return|;
block|}
specifier|public
name|String
name|getIdentifiersEndQuote
parameter_list|()
block|{
return|return
literal|"\""
return|;
block|}
comment|/**      * Returns whether the target DB treats REAL values as DOUBLEs. Default is      * false, i.e. REALs are treated as FLOATs.      */
specifier|public
name|boolean
name|realAsDouble
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Drops all table constraints.      */
specifier|public
name|void
name|willDropTables
parameter_list|(
name|Connection
name|conn
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|tablesToDrop
parameter_list|)
throws|throws
name|Exception
block|{
name|dropConstraints
argument_list|(
name|conn
argument_list|,
name|map
argument_list|,
name|tablesToDrop
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|dropConstraints
parameter_list|(
name|Connection
name|conn
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|tablesToDrop
parameter_list|)
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|constraintsMap
init|=
name|getConstraints
argument_list|(
name|conn
argument_list|,
name|map
argument_list|,
name|tablesToDrop
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|constraintsMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|constraints
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|constraints
operator|==
literal|null
operator|||
name|constraints
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|Object
name|tableName
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|DbEntity
name|entity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|tableName
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|QuotingStrategy
name|strategy
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|constraint
range|:
name|constraints
control|)
block|{
name|StringBuilder
name|drop
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|drop
operator|.
name|append
argument_list|(
literal|"ALTER TABLE "
argument_list|)
operator|.
name|append
argument_list|(
name|strategy
operator|.
name|quotedFullyQualifiedName
argument_list|(
name|entity
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|" DROP CONSTRAINT "
argument_list|)
operator|.
name|append
argument_list|(
name|constraint
argument_list|)
expr_stmt|;
name|executeDDL
argument_list|(
name|conn
argument_list|,
name|drop
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|droppedTables
parameter_list|(
name|Connection
name|con
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
block|}
comment|/**      * Callback method that allows Delegate to customize test procedure.      */
specifier|public
name|void
name|tweakProcedure
parameter_list|(
name|Procedure
name|proc
parameter_list|)
block|{
block|}
specifier|public
name|void
name|willCreateTables
parameter_list|(
name|Connection
name|con
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
block|}
specifier|public
name|void
name|createdTables
parameter_list|(
name|Connection
name|con
parameter_list|,
name|DataMap
name|map
parameter_list|)
throws|throws
name|Exception
block|{
block|}
specifier|public
name|boolean
name|supportsStoredProcedures
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Returns whether the target database supports expressions in the WHERE      * clause in the form "VALUE = COLUMN".      */
specifier|public
name|boolean
name|supportsReverseComparison
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns whether an aggregate query like "SELECT min(X) FROM" returns a      * NULL row when no data matched the WHERE clause. Most DB's do.      */
specifier|public
name|boolean
name|supportNullRowForAggregateFunctions
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns whether the database supports synatax like "X = NULL".      */
specifier|public
name|boolean
name|supportsEqualNullSyntax
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsAllAnySome
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns whether the DB supports a TRIM function for an arbitrary      * character.      */
specifier|public
name|boolean
name|supportsTrimChar
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
comment|/**      * Returns false if stored procedures are not supported or if it is a victim      * of CAY-148 (column name capitalization).      */
specifier|public
name|boolean
name|canMakeObjectsOutOfProcedures
parameter_list|()
block|{
return|return
name|supportsStoredProcedures
argument_list|()
return|;
block|}
comment|/**      * Returns whether LOBs can be inserted from test XML files.      */
specifier|public
name|boolean
name|supportsLobInsertsAsStrings
parameter_list|()
block|{
return|return
name|supportsLobs
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|supportsFKConstraints
parameter_list|(
name|DbEntity
name|entity
parameter_list|)
block|{
if|if
condition|(
literal|"FK_OF_DIFFERENT_TYPE"
operator|.
name|equals
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsFKConstraints
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsColumnTypeReengineering
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Returns true if the target database has support for large objects (BLOB,      * CLOB).      */
specifier|public
name|boolean
name|supportsLobs
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|supportsLobComparisons
parameter_list|()
block|{
return|return
name|supportsLobs
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|supportsBinaryPK
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsHaving
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsCaseSensitiveLike
parameter_list|()
block|{
return|return
operator|!
name|runtimeProperties
operator|.
name|getBoolean
argument_list|(
name|Constants
operator|.
name|CI_PROPERTY
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|supportsCaseInsensitiveOrder
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsCatalogs
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|supportsBatchPK
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsBitwiseOps
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|protected
name|void
name|executeDDL
parameter_list|(
name|Connection
name|con
parameter_list|,
name|String
name|ddl
parameter_list|)
throws|throws
name|Exception
block|{
name|logger
operator|.
name|info
argument_list|(
name|ddl
argument_list|)
expr_stmt|;
name|Statement
name|st
init|=
name|con
operator|.
name|createStatement
argument_list|()
decl_stmt|;
try|try
block|{
name|st
operator|.
name|execute
argument_list|(
name|ddl
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|st
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|executeDDL
parameter_list|(
name|Connection
name|con
parameter_list|,
name|String
name|database
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|executeDDL
argument_list|(
name|con
argument_list|,
name|ddlString
argument_list|(
name|database
argument_list|,
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns a file under test resources DDL directory for the specified      * database.      */
specifier|private
name|String
name|ddlString
parameter_list|(
name|String
name|database
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|StringBuffer
name|location
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|location
operator|.
name|append
argument_list|(
literal|"ddl/"
argument_list|)
operator|.
name|append
argument_list|(
name|database
argument_list|)
operator|.
name|append
argument_list|(
literal|"/"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|InputStream
name|resource
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|location
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|resource
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't find DDL file: "
operator|+
name|location
argument_list|)
throw|;
block|}
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|resource
argument_list|)
argument_list|)
decl_stmt|;
name|StringBuffer
name|buf
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
try|try
block|{
name|String
name|line
init|=
literal|null
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|in
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|buf
operator|.
name|append
argument_list|(
name|line
argument_list|)
operator|.
name|append
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Error reading DDL file: "
operator|+
name|location
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
block|}
block|}
return|return
name|buf
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|handlesNullVsEmptyLOBs
parameter_list|()
block|{
return|return
name|supportsLobs
argument_list|()
return|;
block|}
comment|/**      * Returns a map of database constraints with DbEntity names used as keys,      * and Collections of constraint names as values.      */
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|getConstraints
parameter_list|(
name|Connection
name|conn
parameter_list|,
name|DataMap
name|map
parameter_list|,
name|Collection
argument_list|<
name|String
argument_list|>
name|includeTables
parameter_list|)
throws|throws
name|SQLException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Collection
argument_list|<
name|String
argument_list|>
argument_list|>
name|constraintMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|DatabaseMetaData
name|metadata
init|=
name|conn
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|name
range|:
name|includeTables
control|)
block|{
name|DbEntity
name|entity
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|entity
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|QuotingStrategy
name|strategy
init|=
name|adapter
operator|.
name|getQuotingStrategy
argument_list|()
decl_stmt|;
comment|// Get all constraints for the table
name|ResultSet
name|rs
init|=
name|metadata
operator|.
name|getExportedKeys
argument_list|(
name|entity
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|entity
operator|.
name|getSchema
argument_list|()
argument_list|,
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
name|String
name|fk
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"FK_NAME"
argument_list|)
decl_stmt|;
name|String
name|fkTable
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"FKTABLE_NAME"
argument_list|)
decl_stmt|;
if|if
condition|(
name|fk
operator|!=
literal|null
operator|&&
name|fkTable
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|constraints
init|=
name|constraintMap
operator|.
name|get
argument_list|(
name|fkTable
argument_list|)
decl_stmt|;
if|if
condition|(
name|constraints
operator|==
literal|null
condition|)
block|{
comment|// use a set to avoid duplicate constraints
name|constraints
operator|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|constraintMap
operator|.
name|put
argument_list|(
name|fkTable
argument_list|,
name|constraints
argument_list|)
expr_stmt|;
block|}
name|constraints
operator|.
name|add
argument_list|(
name|strategy
operator|.
name|quotedIdentifier
argument_list|(
name|entity
argument_list|,
name|fk
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
finally|finally
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|constraintMap
return|;
block|}
specifier|public
name|boolean
name|isLowerCaseNames
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|onlyGenericNumberType
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|supportsTimeSqlType
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|onlyGenericDateType
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|supportsNullBoolean
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsBoolean
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsGeneratedKeys
parameter_list|()
block|{
return|return
name|adapter
operator|.
name|supportsGeneratedKeys
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|supportsGeneratedKeysAdd
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|supportsGeneratedKeysDrop
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|supportsEscapeInLike
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsExpressionInHaving
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Support for select like this:      * SELECT (intColumn< 10) AS bool FROM table      */
specifier|public
name|boolean
name|supportsSelectBooleanExpression
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsExtractPart
parameter_list|(
name|ASTExtract
operator|.
name|DateTimePart
name|part
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|boolean
name|supportsSerializableTransactionIsolation
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|supportsLongIn
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

