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
name|map
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
name|HashMap
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
name|query
operator|.
name|Ordering
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

begin_comment
comment|/**  * A builder that constructs Cayenne queries from abstract configuration information  * defined in cayenne-data-map*.dtd. This abstract builder supports values declared in the  * DTD, allowing subclasses to define their own Query creation logic.  *   * @since 3.0  */
end_comment

begin_class
specifier|abstract
class|class
name|QueryLoader
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
specifier|protected
name|String
name|sql
decl_stmt|;
specifier|protected
name|String
name|ejbql
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|adapterSql
decl_stmt|;
specifier|protected
name|Expression
name|qualifier
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Ordering
argument_list|>
name|orderings
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|prefetches
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|String
name|rootType
decl_stmt|;
specifier|protected
name|String
name|rootName
decl_stmt|;
specifier|protected
name|String
name|resultEntity
decl_stmt|;
comment|/**      * Builds a Query object based on internal configuration information.      */
specifier|abstract
name|Query
name|getQuery
parameter_list|()
function_decl|;
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Determines query root based on configuration info, falls back to a DataMap root if      * the data is invalid.      *       * @throws CayenneRuntimeException if a valid root can't be established.      */
specifier|protected
name|Object
name|getRoot
parameter_list|()
block|{
name|Object
name|root
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|rootType
operator|==
literal|null
operator|||
name|MapLoader
operator|.
name|DATA_MAP_ROOT
operator|.
name|equals
argument_list|(
name|rootType
argument_list|)
operator|||
name|rootName
operator|==
literal|null
condition|)
block|{
name|root
operator|=
name|dataMap
expr_stmt|;
block|}
if|else if
condition|(
name|MapLoader
operator|.
name|OBJ_ENTITY_ROOT
operator|.
name|equals
argument_list|(
name|rootType
argument_list|)
condition|)
block|{
name|root
operator|=
name|dataMap
operator|.
name|getObjEntity
argument_list|(
name|rootName
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|MapLoader
operator|.
name|DB_ENTITY_ROOT
operator|.
name|equals
argument_list|(
name|rootType
argument_list|)
condition|)
block|{
name|root
operator|=
name|dataMap
operator|.
name|getDbEntity
argument_list|(
name|rootName
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|MapLoader
operator|.
name|PROCEDURE_ROOT
operator|.
name|equals
argument_list|(
name|rootType
argument_list|)
condition|)
block|{
name|root
operator|=
name|dataMap
operator|.
name|getProcedure
argument_list|(
name|rootName
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|MapLoader
operator|.
name|JAVA_CLASS_ROOT
operator|.
name|equals
argument_list|(
name|rootType
argument_list|)
condition|)
block|{
comment|// setting root to ObjEntity, since creating a Class requires
comment|// the knowledge of the ClassLoader
name|root
operator|=
name|dataMap
operator|.
name|getObjEntityForJavaClass
argument_list|(
name|rootName
argument_list|)
expr_stmt|;
block|}
return|return
operator|(
name|root
operator|!=
literal|null
operator|)
condition|?
name|root
else|:
name|dataMap
return|;
block|}
name|void
name|setResultEntity
parameter_list|(
name|String
name|resultEntity
parameter_list|)
block|{
name|this
operator|.
name|resultEntity
operator|=
name|resultEntity
expr_stmt|;
block|}
comment|/**      * Sets the information pertaining to the root of the query.      */
name|void
name|setRoot
parameter_list|(
name|DataMap
name|dataMap
parameter_list|,
name|String
name|rootType
parameter_list|,
name|String
name|rootName
parameter_list|)
block|{
name|this
operator|.
name|dataMap
operator|=
name|dataMap
expr_stmt|;
name|this
operator|.
name|rootType
operator|=
name|rootType
expr_stmt|;
name|this
operator|.
name|rootName
operator|=
name|rootName
expr_stmt|;
block|}
name|void
name|setEjbql
parameter_list|(
name|String
name|ejbql
parameter_list|)
block|{
name|this
operator|.
name|ejbql
operator|=
name|ejbql
expr_stmt|;
block|}
comment|/**      * Adds raw sql. If adapterClass parameter is not null, sets the SQL string to be      * adapter-specific. Otherwise it is used as a default SQL string.      */
name|void
name|addSql
parameter_list|(
name|String
name|sql
parameter_list|,
name|String
name|adapterClass
parameter_list|)
block|{
if|if
condition|(
name|adapterClass
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|sql
operator|=
name|sql
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|adapterSql
operator|==
literal|null
condition|)
block|{
name|adapterSql
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|adapterSql
operator|.
name|put
argument_list|(
name|adapterClass
argument_list|,
name|sql
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|setQualifier
parameter_list|(
name|String
name|qualifier
parameter_list|)
block|{
if|if
condition|(
name|qualifier
operator|==
literal|null
operator|||
name|qualifier
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|this
operator|.
name|qualifier
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|qualifier
operator|=
name|Expression
operator|.
name|fromString
argument_list|(
name|qualifier
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|void
name|addProperty
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|properties
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|void
name|addOrdering
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|descending
parameter_list|,
name|String
name|ignoreCase
parameter_list|)
block|{
if|if
condition|(
name|orderings
operator|==
literal|null
condition|)
block|{
name|orderings
operator|=
operator|new
name|ArrayList
argument_list|<
name|Ordering
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|path
operator|=
literal|null
expr_stmt|;
block|}
name|boolean
name|isDescending
init|=
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|descending
argument_list|)
decl_stmt|;
name|boolean
name|isIgnoringCase
init|=
literal|"true"
operator|.
name|equalsIgnoreCase
argument_list|(
name|ignoreCase
argument_list|)
decl_stmt|;
name|orderings
operator|.
name|add
argument_list|(
operator|new
name|Ordering
argument_list|(
name|path
argument_list|,
operator|!
name|isDescending
argument_list|,
name|isIgnoringCase
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|void
name|addPrefetch
parameter_list|(
name|String
name|path
parameter_list|)
block|{
if|if
condition|(
name|path
operator|==
literal|null
operator|||
operator|(
name|path
operator|!=
literal|null
operator|&&
name|path
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
operator|)
condition|)
block|{
comment|// throw??
return|return;
block|}
if|if
condition|(
name|prefetches
operator|==
literal|null
condition|)
block|{
name|prefetches
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
name|prefetches
operator|.
name|add
argument_list|(
name|path
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

