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
name|access
operator|.
name|translator
operator|.
name|select
package|;
end_package

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|GroupByStage
implements|implements
name|TranslationStage
block|{
annotation|@
name|Override
specifier|public
name|void
name|perform
parameter_list|(
name|TranslatorContext
name|context
parameter_list|)
block|{
if|if
condition|(
operator|!
name|hasAggregate
argument_list|(
name|context
argument_list|)
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ResultNodeDescriptor
name|resultNode
range|:
name|context
operator|.
name|getResultNodeList
argument_list|()
control|)
block|{
if|if
condition|(
name|resultNode
operator|.
name|isAggregate
argument_list|()
condition|)
block|{
continue|continue;
block|}
name|context
operator|.
name|getSelectBuilder
argument_list|()
operator|.
name|groupBy
argument_list|(
name|resultNode
operator|.
name|getNode
argument_list|()
operator|.
name|deepCopy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|hasAggregate
parameter_list|(
name|TranslatorContext
name|context
parameter_list|)
block|{
if|if
condition|(
name|context
operator|.
name|getQuery
argument_list|()
operator|.
name|getHavingQualifier
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
for|for
control|(
name|ResultNodeDescriptor
name|resultNode
range|:
name|context
operator|.
name|getResultNodeList
argument_list|()
control|)
block|{
if|if
condition|(
name|resultNode
operator|.
name|isAggregate
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

