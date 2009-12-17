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
name|query
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
name|ConfigurationNodeVisitor
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
name|EntityResolver
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
name|Procedure
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
name|util
operator|.
name|ToStringBuilder
import|;
end_import

begin_comment
comment|/**  * A common superclass of Cayenne queries.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractQuery
implements|implements
name|Query
block|{
comment|/**      * The root object this query. May be an entity name, Java class, ObjEntity or      * DbEntity, depending on the specific query and how it was constructed.      */
specifier|protected
name|Object
name|root
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
comment|/**      * @since 3.1      */
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|acceptVisitor
parameter_list|(
name|ConfigurationNodeVisitor
argument_list|<
name|T
argument_list|>
name|visitor
parameter_list|)
block|{
return|return
name|visitor
operator|.
name|visitQuery
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns a symbolic name of the query.      *       * @since 1.1      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Sets a symbolic name of the query.      *       * @since 1.1      */
specifier|public
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
comment|/**      * Returns default select parameters.      *       * @since 1.2      */
specifier|public
name|QueryMetadata
name|getMetaData
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
name|BaseQueryMetadata
name|md
init|=
operator|new
name|BaseQueryMetadata
argument_list|()
decl_stmt|;
name|md
operator|.
name|resolve
argument_list|(
name|getRoot
argument_list|()
argument_list|,
name|resolver
argument_list|,
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|md
return|;
block|}
comment|/**      * Returns the root of this query.      */
specifier|public
name|Object
name|getRoot
parameter_list|()
block|{
return|return
name|root
return|;
block|}
comment|/**      * Sets the root of the query      *       * @param value The new root      * @throws IllegalArgumentException if value is not a String, ObjEntity, DbEntity,      *             Procedure, DataMap, Class or null.      */
specifier|public
name|void
name|setRoot
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|root
operator|=
literal|null
expr_stmt|;
block|}
comment|// sanity check
if|if
condition|(
operator|!
operator|(
operator|(
name|value
operator|instanceof
name|String
operator|)
operator|||
operator|(
name|value
operator|instanceof
name|ObjEntity
operator|)
operator|||
operator|(
name|value
operator|instanceof
name|DbEntity
operator|)
operator|||
operator|(
name|value
operator|instanceof
name|Class
operator|)
operator|||
operator|(
name|value
operator|instanceof
name|Procedure
operator|)
operator|||
operator|(
name|value
operator|instanceof
name|DataMap
operator|)
operator|)
condition|)
block|{
name|String
name|rootClass
init|=
operator|(
name|value
operator|!=
literal|null
operator|)
condition|?
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|"null"
decl_stmt|;
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": \"setRoot(..)\" takes a DataMap, String, ObjEntity, DbEntity, Procedure, "
operator|+
literal|"or Class. It was passed a "
operator|+
name|rootClass
argument_list|)
throw|;
block|}
name|this
operator|.
name|root
operator|=
name|value
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|"root"
argument_list|,
name|root
argument_list|)
operator|.
name|append
argument_list|(
literal|"name"
argument_list|,
name|getName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * @since 1.2      */
specifier|public
specifier|abstract
name|SQLAction
name|createSQLAction
parameter_list|(
name|SQLActionVisitor
name|visitor
parameter_list|)
function_decl|;
comment|/**      * Implements default routing mechanism relying on the EntityResolver to find DataMap      * based on the query root. This mechanism should be sufficient for most queries that      * "know" their root.      *       * @since 1.2      */
specifier|public
name|void
name|route
parameter_list|(
name|QueryRouter
name|router
parameter_list|,
name|EntityResolver
name|resolver
parameter_list|,
name|Query
name|substitutedQuery
parameter_list|)
block|{
name|DataMap
name|map
init|=
name|getMetaData
argument_list|(
name|resolver
argument_list|)
operator|.
name|getDataMap
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"No DataMap found, can't route query "
operator|+
name|this
argument_list|)
throw|;
block|}
name|router
operator|.
name|route
argument_list|(
name|router
operator|.
name|engineForDataMap
argument_list|(
name|map
argument_list|)
argument_list|,
name|this
argument_list|,
name|substitutedQuery
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

