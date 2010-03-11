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
name|project2
operator|.
name|validation
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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|Embeddable
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_class
class|class
name|EmbeddableValidator
extends|extends
name|ConfigurationNodeValidator
block|{
name|void
name|validate
parameter_list|(
name|Embeddable
name|embeddable
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
name|String
name|name
init|=
name|embeddable
operator|.
name|getClassName
argument_list|()
decl_stmt|;
comment|// Must have name
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
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|embeddable
argument_list|,
literal|"Unnamed Embeddable"
argument_list|)
expr_stmt|;
return|return;
block|}
name|DataMap
name|map
init|=
name|embeddable
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
return|return;
block|}
comment|// check for duplicate names in the parent context
for|for
control|(
name|Embeddable
name|otherEmb
range|:
name|map
operator|.
name|getEmbeddables
argument_list|()
control|)
block|{
if|if
condition|(
name|otherEmb
operator|==
name|embeddable
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
name|otherEmb
operator|.
name|getClassName
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|embeddable
argument_list|,
literal|"Duplicate Embeddable class name: %s"
argument_list|,
name|name
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
comment|// check for duplicates in other DataMaps
name|DataChannelDescriptor
name|domain
init|=
name|map
operator|.
name|getDataChannelDescriptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|domain
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|DataMap
name|nextMap
range|:
name|domain
operator|.
name|getDataMaps
argument_list|()
control|)
block|{
if|if
condition|(
name|nextMap
operator|==
name|map
condition|)
block|{
continue|continue;
block|}
name|Embeddable
name|conflictingEmbeddable
init|=
name|nextMap
operator|.
name|getEmbeddable
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|conflictingEmbeddable
operator|!=
literal|null
condition|)
block|{
name|addFailure
argument_list|(
name|validationResult
argument_list|,
name|embeddable
argument_list|,
literal|"Duplicate Embeddable name in another DataMap: %s"
argument_list|,
name|name
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

