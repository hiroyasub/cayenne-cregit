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
name|access
operator|.
name|select
package|;
end_package

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
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|exp
operator|.
name|Expression
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
name|map
operator|.
name|ObjAttribute
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
name|ObjRelationship
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
name|PathComponent
import|;
end_import

begin_comment
comment|/**  * @since 3.0  */
end_comment

begin_class
specifier|abstract
class|class
name|MappedColumnBuilder
block|{
specifier|protected
name|List
argument_list|<
name|EntitySelectColumn
argument_list|>
name|columns
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|columnMap
decl_stmt|;
specifier|protected
name|ExtendedTypeMap
name|extendedTypes
decl_stmt|;
name|MappedColumnBuilder
parameter_list|(
name|ExtendedTypeMap
name|extendedTypes
parameter_list|)
block|{
name|this
operator|.
name|columns
operator|=
operator|new
name|ArrayList
argument_list|<
name|EntitySelectColumn
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|columnMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
expr_stmt|;
name|this
operator|.
name|extendedTypes
operator|=
name|extendedTypes
expr_stmt|;
block|}
comment|/**      * Appends an ObjAttribute belonging to a root ObjEntity.      */
specifier|protected
name|void
name|append
parameter_list|(
name|ObjAttribute
name|attribute
parameter_list|)
block|{
if|if
condition|(
operator|!
name|columnMap
operator|.
name|containsKey
argument_list|(
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|path
init|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|attribute
operator|.
name|getDbPathIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|pathComponent
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|pathComponent
operator|instanceof
name|DbRelationship
operator|)
condition|)
block|{
break|break;
block|}
name|path
operator|.
name|add
argument_list|(
operator|(
name|DbRelationship
operator|)
name|pathComponent
argument_list|)
expr_stmt|;
block|}
name|makeColumn
argument_list|(
name|attribute
operator|.
name|getDbAttributePath
argument_list|()
argument_list|,
name|attribute
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Appends a DbAttribute belonging to a root DbEntity.      */
specifier|protected
name|void
name|append
parameter_list|(
name|DbAttribute
name|attribute
parameter_list|)
block|{
comment|// skip if already appended via ObjAttributes
if|if
condition|(
operator|!
name|columnMap
operator|.
name|containsKey
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|makeColumn
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|,
name|attribute
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Appends a column matching a path Expression rooted in DbEntity.      */
specifier|protected
name|void
name|append
parameter_list|(
name|DbEntity
name|root
parameter_list|,
name|Expression
name|dbPath
parameter_list|)
block|{
name|String
name|pathString
init|=
name|dbPath
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|columnMap
operator|.
name|containsKey
argument_list|(
name|pathString
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|DbAttribute
argument_list|,
name|DbRelationship
argument_list|>
name|c
range|:
name|root
operator|.
name|resolvePath
argument_list|(
name|dbPath
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|c
operator|.
name|isLast
argument_list|()
condition|)
block|{
name|makeColumn
argument_list|(
name|pathString
argument_list|,
name|c
operator|.
name|getAttribute
argument_list|()
argument_list|,
name|relationships
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|relationships
operator|==
literal|null
condition|)
block|{
name|relationships
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|relationships
operator|.
name|add
argument_list|(
name|c
operator|.
name|getRelationship
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Appends a column matching a path Expression rooted in ObjEntity.      */
specifier|protected
name|void
name|append
parameter_list|(
name|ObjEntity
name|root
parameter_list|,
name|Expression
name|objPath
parameter_list|)
block|{
name|Expression
name|dbPath
init|=
name|root
operator|.
name|translateToDbPath
argument_list|(
name|objPath
argument_list|)
decl_stmt|;
name|String
name|pathString
init|=
name|dbPath
operator|.
name|getOperand
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|columnMap
operator|.
name|containsKey
argument_list|(
name|pathString
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
init|=
literal|null
decl_stmt|;
for|for
control|(
name|PathComponent
argument_list|<
name|ObjAttribute
argument_list|,
name|ObjRelationship
argument_list|>
name|c
range|:
name|root
operator|.
name|resolvePath
argument_list|(
name|objPath
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
control|)
block|{
if|if
condition|(
name|c
operator|.
name|isLast
argument_list|()
condition|)
block|{
name|makeColumn
argument_list|(
name|pathString
argument_list|,
name|c
operator|.
name|getAttribute
argument_list|()
argument_list|,
name|relationships
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|relationships
operator|==
literal|null
condition|)
block|{
name|relationships
operator|=
operator|new
name|ArrayList
argument_list|<
name|DbRelationship
argument_list|>
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
name|relationships
operator|.
name|addAll
argument_list|(
name|c
operator|.
name|getRelationship
argument_list|()
operator|.
name|getDbRelationships
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|EntitySelectColumn
name|makeColumn
parameter_list|(
name|String
name|dataRowKey
parameter_list|,
name|ObjAttribute
name|attribute
parameter_list|,
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
parameter_list|)
block|{
name|EntitySelectColumn
name|column
init|=
operator|new
name|EntitySelectColumn
argument_list|()
decl_stmt|;
name|DbAttribute
name|dbAttribute
init|=
name|attribute
operator|.
name|getDbAttribute
argument_list|()
decl_stmt|;
comment|// void column
if|if
condition|(
name|dbAttribute
operator|==
literal|null
condition|)
block|{
name|int
name|jdbcType
init|=
name|TypesMapping
operator|.
name|getSqlTypeByJava
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|column
operator|.
name|setColumnName
argument_list|(
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|jdbcType
argument_list|)
condition|?
literal|"1"
else|:
literal|"'1'"
argument_list|)
expr_stmt|;
name|column
operator|.
name|setJdbcType
argument_list|(
name|jdbcType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|column
operator|.
name|setColumnName
argument_list|(
name|dbAttribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|column
operator|.
name|setJdbcType
argument_list|(
name|dbAttribute
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|column
operator|.
name|setDataRowKey
argument_list|(
name|dataRowKey
argument_list|)
expr_stmt|;
name|column
operator|.
name|setConverter
argument_list|(
name|extendedTypes
operator|.
name|getRegisteredType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|column
operator|.
name|setPath
argument_list|(
name|relationships
argument_list|)
expr_stmt|;
name|columnMap
operator|.
name|put
argument_list|(
name|dataRowKey
argument_list|,
name|columns
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|columns
operator|.
name|add
argument_list|(
name|column
argument_list|)
expr_stmt|;
return|return
name|column
return|;
block|}
specifier|private
name|EntitySelectColumn
name|makeColumn
parameter_list|(
name|String
name|dataRowKey
parameter_list|,
name|DbAttribute
name|attribute
parameter_list|,
name|List
argument_list|<
name|DbRelationship
argument_list|>
name|relationships
parameter_list|)
block|{
name|EntitySelectColumn
name|column
init|=
operator|new
name|EntitySelectColumn
argument_list|()
decl_stmt|;
name|column
operator|.
name|setColumnName
argument_list|(
name|attribute
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|column
operator|.
name|setJdbcType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|column
operator|.
name|setDataRowKey
argument_list|(
name|dataRowKey
argument_list|)
expr_stmt|;
name|String
name|javaType
init|=
name|TypesMapping
operator|.
name|getJavaBySqlType
argument_list|(
name|attribute
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
name|column
operator|.
name|setConverter
argument_list|(
name|extendedTypes
operator|.
name|getRegisteredType
argument_list|(
name|javaType
argument_list|)
argument_list|)
expr_stmt|;
name|column
operator|.
name|setPath
argument_list|(
name|relationships
argument_list|)
expr_stmt|;
name|columnMap
operator|.
name|put
argument_list|(
name|dataRowKey
argument_list|,
name|columns
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|columns
operator|.
name|add
argument_list|(
name|column
argument_list|)
expr_stmt|;
return|return
name|column
return|;
block|}
block|}
end_class

end_unit

