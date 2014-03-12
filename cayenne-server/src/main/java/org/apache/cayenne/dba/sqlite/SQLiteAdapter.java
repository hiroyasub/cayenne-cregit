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
name|sqlite
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
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
name|GregorianCalendar
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
comment|/**  * A SQLite database adapter that works with Zentus JDBC driver. See  * http://www.zentus.com/sqlitejdbc/ for the driver information. Also look at  * http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC for another adaptor option.  *   *<pre>  *      sqlite.jdbc.url = jdbc:sqlite:sqlitetest.db  *      sqlite.jdbc.driver = org.sqlite.JDBC  *</pre>  *   * @since 3.0  */
end_comment

begin_comment
comment|// check http://cwiki.apache.org/CAY/sqliteadapter.html for current limitations.
end_comment

begin_class
specifier|public
class|class
name|SQLiteAdapter
extends|extends
name|JdbcAdapter
block|{
specifier|public
name|SQLiteAdapter
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
name|this
operator|.
name|setSupportsUniqueConstraints
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
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
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|SQLiteDateType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|SQLiteBigDecimalType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|SQLiteFloatType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|SQLiteByteArrayType
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|SQLiteCalendarType
argument_list|(
name|GregorianCalendar
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|registerType
argument_list|(
operator|new
name|SQLiteCalendarType
argument_list|(
name|Calendar
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|createUniqueConstraint
parameter_list|(
name|DbEntity
name|source
parameter_list|,
name|Collection
argument_list|<
name|DbAttribute
argument_list|>
name|columns
parameter_list|)
block|{
comment|// TODO: andrus 10/9/2007 - only ALTER TABLE ADD CONSTRAINT is not supported,
comment|// presumably there's some other syntax (a part of CREATE TABLE?) that would
comment|// create a unique constraint.
return|return
literal|null
return|;
block|}
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
name|SQLiteActionBuilder
argument_list|(
name|this
argument_list|,
name|node
operator|.
name|getEntityResolver
argument_list|()
argument_list|,
name|node
operator|.
name|getRowReaderFactory
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Appends AUTOINCREMENT clause to the column definition for generated columns.      */
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
name|super
operator|.
name|createTableAppendColumn
argument_list|(
name|sqlBuffer
argument_list|,
name|column
argument_list|)
expr_stmt|;
name|DbEntity
name|entity
init|=
operator|(
name|DbEntity
operator|)
name|column
operator|.
name|getEntity
argument_list|()
decl_stmt|;
if|if
condition|(
name|column
operator|.
name|isGenerated
argument_list|()
operator|&&
name|column
operator|.
name|isPrimaryKey
argument_list|()
operator|&&
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|sqlBuffer
operator|.
name|append
argument_list|(
literal|" PRIMARY KEY AUTOINCREMENT"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|createTableAppendPKClause
parameter_list|(
name|StringBuffer
name|sqlBuffer
parameter_list|,
name|DbEntity
name|entity
parameter_list|)
block|{
comment|// do not append " PRIMARY KEY () " for single column generated primary key
if|if
condition|(
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|DbAttribute
name|column
init|=
name|entity
operator|.
name|getPrimaryKeys
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|column
operator|.
name|isGenerated
argument_list|()
condition|)
block|{
return|return;
block|}
block|}
name|super
operator|.
name|createTableAppendPKClause
argument_list|(
name|sqlBuffer
argument_list|,
name|entity
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

