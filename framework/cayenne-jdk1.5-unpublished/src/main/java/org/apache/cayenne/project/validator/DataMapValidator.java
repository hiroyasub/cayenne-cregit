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
name|project
operator|.
name|validator
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DataDomain
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
name|project
operator|.
name|ProjectPath
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
name|Util
import|;
end_import

begin_comment
comment|/**  * Validator for DataMaps.  *   */
end_comment

begin_class
specifier|public
class|class
name|DataMapValidator
extends|extends
name|TreeNodeValidator
block|{
comment|/**      * Constructor for DataMapValidator.      */
specifier|public
name|DataMapValidator
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validateObject
parameter_list|(
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|path
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|validateName
argument_list|(
name|map
argument_list|,
name|path
argument_list|,
name|validator
argument_list|)
expr_stmt|;
comment|// check if data map is not attached to any nodes
name|validateNodeLinks
argument_list|(
name|map
argument_list|,
name|path
argument_list|,
name|validator
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|validateNodeLinks
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|DataDomain
name|domain
init|=
operator|(
name|DataDomain
operator|)
name|path
operator|.
name|getObjectParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|boolean
name|unlinked
init|=
literal|true
decl_stmt|;
name|int
name|nodeCount
init|=
literal|0
decl_stmt|;
for|for
control|(
specifier|final
name|DataNode
name|node
range|:
name|domain
operator|.
name|getDataNodes
argument_list|()
control|)
block|{
name|nodeCount
operator|++
expr_stmt|;
if|if
condition|(
name|node
operator|.
name|getDataMaps
argument_list|()
operator|.
name|contains
argument_list|(
name|map
argument_list|)
condition|)
block|{
name|unlinked
operator|=
literal|false
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|unlinked
operator|&&
name|nodeCount
operator|>
literal|0
condition|)
block|{
name|validator
operator|.
name|registerWarning
argument_list|(
literal|"DataMap is not linked to any DataNodes."
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|validateName
parameter_list|(
name|DataMap
name|map
parameter_list|,
name|ProjectPath
name|path
parameter_list|,
name|Validator
name|validator
parameter_list|)
block|{
name|String
name|name
init|=
name|map
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|Util
operator|.
name|isEmptyString
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerError
argument_list|(
literal|"Unnamed DataMap."
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return;
block|}
name|DataDomain
name|domain
init|=
operator|(
name|DataDomain
operator|)
name|path
operator|.
name|getObjectParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|domain
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// check for duplicate names in the parent context
for|for
control|(
specifier|final
name|DataMap
name|otherMap
range|:
name|domain
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
if|if
condition|(
name|otherMap
operator|==
name|map
condition|)
block|{
continue|continue;
block|}
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|otherMap
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|validator
operator|.
name|registerError
argument_list|(
literal|"Duplicate DataMap name: "
operator|+
name|name
operator|+
literal|"."
argument_list|,
name|path
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
block|}
block|}
end_class

end_unit

