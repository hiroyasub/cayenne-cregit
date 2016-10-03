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
name|dbsync
operator|.
name|reverse
operator|.
name|db
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|PatternFilter
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
name|dbsync
operator|.
name|reverse
operator|.
name|filters
operator|.
name|TableFilter
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
name|DetectedDbEntity
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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|LinkedList
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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DbTableLoader
block|{
specifier|private
specifier|static
specifier|final
name|Log
name|LOGGER
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DbTableLoader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WILDCARD
init|=
literal|"%"
decl_stmt|;
specifier|private
specifier|final
name|String
name|catalog
decl_stmt|;
specifier|private
specifier|final
name|String
name|schema
decl_stmt|;
specifier|private
specifier|final
name|DatabaseMetaData
name|metaData
decl_stmt|;
specifier|private
specifier|final
name|DbLoaderDelegate
name|delegate
decl_stmt|;
specifier|private
specifier|final
name|DbAttributesLoader
name|attributesLoader
decl_stmt|;
specifier|public
name|DbTableLoader
parameter_list|(
name|String
name|catalog
parameter_list|,
name|String
name|schema
parameter_list|,
name|DatabaseMetaData
name|metaData
parameter_list|,
name|DbLoaderDelegate
name|delegate
parameter_list|,
name|DbAttributesLoader
name|attributesLoader
parameter_list|)
block|{
name|this
operator|.
name|catalog
operator|=
name|catalog
expr_stmt|;
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
name|this
operator|.
name|metaData
operator|=
name|metaData
expr_stmt|;
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|attributesLoader
operator|=
name|attributesLoader
expr_stmt|;
block|}
comment|/** 	 * Returns all tables for given combination of the criteria. Tables returned 	 * as DbEntities without any attributes or relationships. 	 * 	 * @param types 	 *            The types of table names to retrieve, null returns all types. 	 * @since 4.0 	 */
specifier|public
name|List
argument_list|<
name|DetectedDbEntity
argument_list|>
name|getDbEntities
parameter_list|(
name|TableFilter
name|filters
parameter_list|,
name|String
index|[]
name|types
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|LOGGER
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Read tables: catalog="
operator|+
name|catalog
operator|+
literal|", schema="
operator|+
name|schema
operator|+
literal|", types="
operator|+
name|Arrays
operator|.
name|toString
argument_list|(
name|types
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|DetectedDbEntity
argument_list|>
name|tables
init|=
operator|new
name|LinkedList
argument_list|<
name|DetectedDbEntity
argument_list|>
argument_list|()
decl_stmt|;
try|try
init|(
name|ResultSet
name|rs
init|=
name|metaData
operator|.
name|getTables
argument_list|(
name|catalog
argument_list|,
name|schema
argument_list|,
name|WILDCARD
argument_list|,
name|types
argument_list|)
init|;
init|)
block|{
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
condition|)
block|{
comment|// Oracle 9i and newer has a nifty recycle bin feature... but we
comment|// don't
comment|// want dropped tables to be included here; in fact they may
comment|// even result
comment|// in errors on reverse engineering as their names have special
comment|// chars like
comment|// "/", etc. So skip them all together
name|String
name|name
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_NAME"
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|DetectedDbEntity
name|table
init|=
operator|new
name|DetectedDbEntity
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|String
name|catalog
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_CAT"
argument_list|)
decl_stmt|;
name|table
operator|.
name|setCatalog
argument_list|(
name|catalog
argument_list|)
expr_stmt|;
name|String
name|schema
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"TABLE_SCHEM"
argument_list|)
decl_stmt|;
name|table
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
operator|(
name|this
operator|.
name|catalog
operator|==
literal|null
operator|||
name|this
operator|.
name|catalog
operator|.
name|equals
argument_list|(
name|catalog
argument_list|)
operator|)
operator|||
operator|!
operator|(
name|this
operator|.
name|schema
operator|==
literal|null
operator|||
name|this
operator|.
name|schema
operator|.
name|equals
argument_list|(
name|schema
argument_list|)
operator|)
condition|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|catalog
operator|+
literal|"."
operator|+
name|schema
operator|+
literal|"."
operator|+
name|name
operator|+
literal|" wrongly loaded for catalog/schema : "
operator|+
name|this
operator|.
name|catalog
operator|+
literal|"."
operator|+
name|this
operator|.
name|schema
argument_list|)
expr_stmt|;
continue|continue;
block|}
name|PatternFilter
name|includeTable
init|=
name|filters
operator|.
name|isIncludeTable
argument_list|(
name|table
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|includeTable
operator|!=
literal|null
condition|)
block|{
name|tables
operator|.
name|add
argument_list|(
name|table
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|tables
return|;
block|}
comment|/** 	 * Loads dbEntities for the specified tables. 	 *  	 * @param config 	 * @param types 	 */
specifier|public
name|List
argument_list|<
name|DbEntity
argument_list|>
name|loadDbEntities
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|DbLoaderConfiguration
name|config
parameter_list|,
name|String
index|[]
name|types
parameter_list|)
throws|throws
name|SQLException
block|{
comment|/** List of db entities to process. */
name|List
argument_list|<
name|DetectedDbEntity
argument_list|>
name|tables
init|=
name|getDbEntities
argument_list|(
name|config
operator|.
name|getFiltersConfig
argument_list|()
operator|.
name|tableFilter
argument_list|(
name|catalog
argument_list|,
name|schema
argument_list|)
argument_list|,
name|types
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|DbEntity
argument_list|>
name|dbEntities
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|DbEntity
name|dbEntity
range|:
name|tables
control|)
block|{
name|DbEntity
name|oldEnt
init|=
name|map
operator|.
name|getDbEntity
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|oldEnt
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|ObjEntity
argument_list|>
name|oldObjEnt
init|=
name|map
operator|.
name|getMappedEntities
argument_list|(
name|oldEnt
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|oldObjEnt
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|ObjEntity
name|objEntity
range|:
name|oldObjEnt
control|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Delete ObjEntity: "
operator|+
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeObjEntity
argument_list|(
name|objEntity
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|objEntityRemoved
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
block|}
block|}
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Overwrite DbEntity: "
operator|+
name|oldEnt
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|removeDbEntity
argument_list|(
name|oldEnt
operator|.
name|getName
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|dbEntityRemoved
argument_list|(
name|oldEnt
argument_list|)
expr_stmt|;
block|}
name|map
operator|.
name|addDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|dbEntityAdded
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
comment|// delegate might have thrown this entity out... so check if it is
comment|// still
comment|// around before continuing processing
if|if
condition|(
name|map
operator|.
name|getDbEntity
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
operator|==
name|dbEntity
condition|)
block|{
name|dbEntities
operator|.
name|add
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|attributesLoader
operator|.
name|loadDbAttributes
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|config
operator|.
name|isSkipPrimaryKeyLoading
argument_list|()
condition|)
block|{
name|loadPrimaryKey
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|dbEntities
return|;
block|}
specifier|private
name|void
name|loadPrimaryKey
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
init|(
name|ResultSet
name|rs
init|=
name|metaData
operator|.
name|getPrimaryKeys
argument_list|(
name|dbEntity
operator|.
name|getCatalog
argument_list|()
argument_list|,
name|dbEntity
operator|.
name|getSchema
argument_list|()
argument_list|,
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
init|;
init|)
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
name|columnName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"COLUMN_NAME"
argument_list|)
decl_stmt|;
name|DbAttribute
name|attribute
init|=
name|dbEntity
operator|.
name|getAttribute
argument_list|(
name|columnName
argument_list|)
decl_stmt|;
if|if
condition|(
name|attribute
operator|!=
literal|null
condition|)
block|{
name|attribute
operator|.
name|setPrimaryKey
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// why an attribute might be null is not quiet clear
comment|// but there is a bug report 731406 indicating that it is
comment|// possible
comment|// so just print the warning, and ignore
name|LOGGER
operator|.
name|warn
argument_list|(
literal|"Can't locate attribute for primary key: "
operator|+
name|columnName
argument_list|)
expr_stmt|;
block|}
name|String
name|pkName
init|=
name|rs
operator|.
name|getString
argument_list|(
literal|"PK_NAME"
argument_list|)
decl_stmt|;
if|if
condition|(
name|pkName
operator|!=
literal|null
operator|&&
name|dbEntity
operator|instanceof
name|DetectedDbEntity
condition|)
block|{
operator|(
operator|(
name|DetectedDbEntity
operator|)
name|dbEntity
operator|)
operator|.
name|setPrimaryKeyName
argument_list|(
name|pkName
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

