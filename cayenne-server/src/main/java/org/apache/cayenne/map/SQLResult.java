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
name|map
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
name|query
operator|.
name|DefaultEmbeddableResultSegment
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
name|reflect
operator|.
name|ClassDescriptor
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
name|Collections
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

begin_comment
comment|/**  * A metadata object that defines how a row in a result set can be converted to  * result objects. SQLResult can be mapped to a single scalar, a single entity  * or a mix of scalars and entities that is represented as an Object[].  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|SQLResult
block|{
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|resultDescriptors
decl_stmt|;
comment|/**      * Creates an unnamed SQLResultSet.      */
specifier|public
name|SQLResult
parameter_list|()
block|{
block|}
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getResolvedComponents
parameter_list|(
name|EntityResolver
name|resolver
parameter_list|)
block|{
if|if
condition|(
name|resultDescriptors
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
name|List
argument_list|<
name|Object
argument_list|>
name|resolvedComponents
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|resultDescriptors
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|offset
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Object
name|component
range|:
name|getComponents
argument_list|()
control|)
block|{
if|if
condition|(
name|component
operator|instanceof
name|String
condition|)
block|{
name|resolvedComponents
operator|.
name|add
argument_list|(
operator|new
name|DefaultScalarResultSegment
argument_list|(
operator|(
name|String
operator|)
name|component
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
name|offset
operator|=
name|offset
operator|+
literal|1
expr_stmt|;
block|}
if|else if
condition|(
name|component
operator|instanceof
name|EntityResult
condition|)
block|{
name|EntityResult
name|entityResult
init|=
operator|(
name|EntityResult
operator|)
name|component
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fields
init|=
name|entityResult
operator|.
name|getDbFields
argument_list|(
name|resolver
argument_list|)
decl_stmt|;
name|String
name|entityName
init|=
name|entityResult
operator|.
name|getEntityName
argument_list|()
decl_stmt|;
if|if
condition|(
name|entityName
operator|==
literal|null
condition|)
block|{
name|entityName
operator|=
name|resolver
operator|.
name|getObjEntity
argument_list|(
name|entityResult
operator|.
name|getEntityClass
argument_list|()
argument_list|)
operator|.
name|getName
argument_list|()
expr_stmt|;
block|}
name|ClassDescriptor
name|classDescriptor
init|=
name|resolver
operator|.
name|getClassDescriptor
argument_list|(
name|entityName
argument_list|)
decl_stmt|;
name|resolvedComponents
operator|.
name|add
argument_list|(
operator|new
name|DefaultEntityResultSegment
argument_list|(
name|classDescriptor
argument_list|,
name|fields
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
name|offset
operator|=
name|offset
operator|+
name|fields
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
if|else if
condition|(
name|component
operator|instanceof
name|EmbeddedResult
condition|)
block|{
name|EmbeddedResult
name|embeddedResult
init|=
operator|(
name|EmbeddedResult
operator|)
name|component
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|fields
init|=
name|embeddedResult
operator|.
name|getFields
argument_list|()
decl_stmt|;
name|resolvedComponents
operator|.
name|add
argument_list|(
operator|new
name|DefaultEmbeddableResultSegment
argument_list|(
name|embeddedResult
operator|.
name|getEmbeddable
argument_list|()
argument_list|,
name|fields
argument_list|,
name|offset
argument_list|)
argument_list|)
expr_stmt|;
name|offset
operator|=
name|offset
operator|+
name|fields
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported result descriptor component: "
operator|+
name|component
argument_list|)
throw|;
block|}
block|}
return|return
name|resolvedComponents
return|;
block|}
comment|/**      * Creates a named SQLResultSet.      */
specifier|public
name|SQLResult
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
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
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
comment|/**      * Returns a list of "uncompiled" result descriptors. Column descriptors are      * returned as Strings, entity descriptors - as {@link EntityResult}. To get      * fully resolved descriptors, use      * {@link #getResolvedComponents(EntityResolver)}.      */
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|getComponents
parameter_list|()
block|{
return|return
name|resultDescriptors
operator|!=
literal|null
condition|?
name|resultDescriptors
else|:
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|void
name|addEntityResult
parameter_list|(
name|EntityResult
name|entityResult
parameter_list|)
block|{
name|checkAndAdd
argument_list|(
name|entityResult
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addEmbeddedResult
parameter_list|(
name|EmbeddedResult
name|embeddedResult
parameter_list|)
block|{
name|checkAndAdd
argument_list|(
name|embeddedResult
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a result set column name to the mapping.      */
specifier|public
name|void
name|addColumnResult
parameter_list|(
name|String
name|column
parameter_list|)
block|{
name|checkAndAdd
argument_list|(
name|column
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkAndAdd
parameter_list|(
name|Object
name|result
parameter_list|)
block|{
if|if
condition|(
name|resultDescriptors
operator|==
literal|null
condition|)
block|{
name|resultDescriptors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
expr_stmt|;
block|}
name|resultDescriptors
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

