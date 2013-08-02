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
name|wocompat
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
name|java
operator|.
name|util
operator|.
name|StringTokenizer
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
name|exp
operator|.
name|ExpressionException
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
name|ExpressionFactory
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
name|Entity
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
name|Relationship
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
name|commons
operator|.
name|collections
operator|.
name|Transformer
import|;
end_import

begin_comment
comment|/**  * An extension of ObjEntity used to accomodate extra EOModel entity properties.  */
end_comment

begin_class
specifier|public
class|class
name|EOObjEntity
extends|extends
name|ObjEntity
block|{
specifier|protected
name|boolean
name|subclass
decl_stmt|;
specifier|protected
name|boolean
name|abstractEntity
decl_stmt|;
specifier|private
name|Collection
name|filteredQueries
decl_stmt|;
specifier|private
name|Map
name|eoMap
decl_stmt|;
specifier|public
name|EOObjEntity
parameter_list|()
block|{
block|}
specifier|public
name|EOObjEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns stored EOQuery.      *       * @since 1.1      */
specifier|public
name|EOQuery
name|getEOQuery
parameter_list|(
name|String
name|queryName
parameter_list|)
block|{
name|Query
name|query
init|=
name|getDataMap
argument_list|()
operator|.
name|getQuery
argument_list|(
name|qualifiedQueryName
argument_list|(
name|queryName
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|query
operator|instanceof
name|EOQuery
condition|)
block|{
return|return
operator|(
name|EOQuery
operator|)
name|query
return|;
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Overrides super to support translation of EO attributes that have no ObjAttributes.      *       * @since 1.2      */
annotation|@
name|Override
specifier|public
name|Expression
name|translateToDbPath
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
if|if
condition|(
name|expression
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|getDbEntity
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Can't translate expression to DB_PATH, no DbEntity for '"
operator|+
name|getName
argument_list|()
operator|+
literal|"'."
argument_list|)
throw|;
block|}
comment|// converts all OBJ_PATH expressions to DB_PATH expressions
comment|// and pass control to the DB entity
return|return
name|expression
operator|.
name|transform
argument_list|(
operator|new
name|DBPathConverter
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @since 1.2      */
comment|// TODO: andrus, 5/27/2006 - make public after 1.2. Also maybe move entity
comment|// initialization code from EOModelProcessor to this class, kind of like EOQuery does.
name|Map
name|getEoMap
parameter_list|()
block|{
return|return
name|eoMap
return|;
block|}
comment|/**      * @since 1.2      */
comment|// TODO: andrus, 5/27/2006 - make public after 1.2. Also maybe move entity
comment|// initialization code from EOModelProcessor to this class, kind of like EOQuery does.
name|void
name|setEoMap
parameter_list|(
name|Map
name|eoMap
parameter_list|)
block|{
name|this
operator|.
name|eoMap
operator|=
name|eoMap
expr_stmt|;
block|}
comment|/**      * Returns a collection of queries for this entity.      *       * @since 1.1      */
specifier|public
name|Collection
name|getEOQueries
parameter_list|()
block|{
if|if
condition|(
name|filteredQueries
operator|==
literal|null
condition|)
block|{
name|Collection
name|queries
init|=
name|getDataMap
argument_list|()
operator|.
name|getQueries
argument_list|()
decl_stmt|;
if|if
condition|(
name|queries
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|filteredQueries
operator|=
name|Collections
operator|.
name|EMPTY_LIST
expr_stmt|;
block|}
else|else
block|{
name|Map
name|params
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"root"
argument_list|,
name|EOObjEntity
operator|.
name|this
argument_list|)
decl_stmt|;
name|Expression
name|filter
init|=
name|Expression
operator|.
name|fromString
argument_list|(
literal|"root = $root"
argument_list|)
operator|.
name|expWithParameters
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|filteredQueries
operator|=
name|filter
operator|.
name|filter
argument_list|(
name|queries
argument_list|,
operator|new
name|ArrayList
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|filteredQueries
return|;
block|}
specifier|public
name|boolean
name|isAbstractEntity
parameter_list|()
block|{
return|return
name|abstractEntity
return|;
block|}
specifier|public
name|void
name|setAbstractEntity
parameter_list|(
name|boolean
name|abstractEntity
parameter_list|)
block|{
name|this
operator|.
name|abstractEntity
operator|=
name|abstractEntity
expr_stmt|;
block|}
specifier|public
name|boolean
name|isSubclass
parameter_list|()
block|{
return|return
name|subclass
return|;
block|}
specifier|public
name|void
name|setSubclass
parameter_list|(
name|boolean
name|subclass
parameter_list|)
block|{
name|this
operator|.
name|subclass
operator|=
name|subclass
expr_stmt|;
block|}
comment|/**      * Translates query name local to the ObjEntity to the global name. This translation      * is needed since EOModels store queries by entity, while Cayenne DataMaps store them      * globally.      *       * @since 1.1      */
specifier|public
name|String
name|qualifiedQueryName
parameter_list|(
name|String
name|queryName
parameter_list|)
block|{
return|return
name|getName
argument_list|()
operator|+
literal|"_"
operator|+
name|queryName
return|;
block|}
comment|/**      * @since 1.1      */
specifier|public
name|String
name|localQueryName
parameter_list|(
name|String
name|qualifiedQueryName
parameter_list|)
block|{
return|return
operator|(
name|qualifiedQueryName
operator|!=
literal|null
operator|&&
name|qualifiedQueryName
operator|.
name|startsWith
argument_list|(
name|getName
argument_list|()
operator|+
literal|"_"
argument_list|)
operator|)
condition|?
name|qualifiedQueryName
operator|.
name|substring
argument_list|(
name|getName
argument_list|()
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
else|:
name|qualifiedQueryName
return|;
block|}
specifier|final
class|class
name|DBPathConverter
implements|implements
name|Transformer
block|{
specifier|public
name|Object
name|transform
parameter_list|(
name|Object
name|input
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|input
operator|instanceof
name|Expression
operator|)
condition|)
block|{
return|return
name|input
return|;
block|}
name|Expression
name|expression
init|=
operator|(
name|Expression
operator|)
name|input
decl_stmt|;
if|if
condition|(
name|expression
operator|.
name|getType
argument_list|()
operator|!=
name|Expression
operator|.
name|OBJ_PATH
condition|)
block|{
return|return
name|input
return|;
block|}
comment|// convert obj_path to db_path
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|EOObjEntity
name|entity
init|=
name|EOObjEntity
operator|.
name|this
decl_stmt|;
name|StringTokenizer
name|toks
init|=
operator|new
name|StringTokenizer
argument_list|(
name|expression
operator|.
name|toString
argument_list|()
argument_list|,
literal|"."
argument_list|)
decl_stmt|;
while|while
condition|(
name|toks
operator|.
name|hasMoreTokens
argument_list|()
operator|&&
name|entity
operator|!=
literal|null
condition|)
block|{
name|String
name|chunk
init|=
name|toks
operator|.
name|nextToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|toks
operator|.
name|hasMoreTokens
argument_list|()
condition|)
block|{
comment|// this is a relationship
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|chunk
argument_list|)
expr_stmt|;
name|Relationship
name|r
init|=
name|entity
operator|.
name|getRelationship
argument_list|(
name|chunk
argument_list|)
decl_stmt|;
if|if
condition|(
name|r
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ExpressionException
argument_list|(
literal|"Invalid path component: "
operator|+
name|chunk
argument_list|)
throw|;
block|}
name|entity
operator|=
operator|(
name|EOObjEntity
operator|)
name|r
operator|.
name|getTargetEntity
argument_list|()
expr_stmt|;
block|}
comment|// this is an attribute...
else|else
block|{
name|List
name|attributes
init|=
operator|(
name|List
operator|)
name|entity
operator|.
name|getEoMap
argument_list|()
operator|.
name|get
argument_list|(
literal|"attributes"
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|attributes
operator|.
name|iterator
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
name|Map
name|attribute
init|=
operator|(
name|Map
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|chunk
operator|.
name|equals
argument_list|(
name|attribute
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
name|buffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|Entity
operator|.
name|PATH_SEPARATOR
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|attribute
operator|.
name|get
argument_list|(
literal|"columnName"
argument_list|)
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|expressionOfType
argument_list|(
name|Expression
operator|.
name|DB_PATH
argument_list|)
decl_stmt|;
name|exp
operator|.
name|setOperand
argument_list|(
literal|0
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exp
return|;
block|}
block|}
block|}
end_class

end_unit

