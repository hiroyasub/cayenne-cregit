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
name|modeler
operator|.
name|util
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
name|map
operator|.
name|Attribute
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
name|Relationship
import|;
end_import

begin_class
specifier|public
class|class
name|EntityTreeRelationshipFilter
implements|implements
name|EntityTreeFilter
block|{
specifier|public
name|boolean
name|attributeMatch
parameter_list|(
name|Object
name|node
parameter_list|,
name|Attribute
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|attr
parameter_list|)
block|{
comment|// attrs not allowed here
return|return
literal|false
return|;
block|}
specifier|public
name|boolean
name|relationshipMatch
parameter_list|(
name|Object
name|node
parameter_list|,
name|Relationship
argument_list|<
name|?
argument_list|,
name|?
argument_list|,
name|?
argument_list|>
name|rel
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|node
operator|instanceof
name|Relationship
operator|)
condition|)
block|{
return|return
literal|true
return|;
block|}
comment|/*          * We do not allow A->B->A chains, where relationships are          * to-one          */
name|DbRelationship
name|prev
init|=
operator|(
name|DbRelationship
operator|)
name|node
decl_stmt|;
return|return
operator|!
operator|(
operator|!
name|rel
operator|.
name|isToMany
argument_list|()
operator|&&
name|prev
operator|.
name|getReverseRelationship
argument_list|()
operator|==
name|rel
operator|)
return|;
block|}
block|}
end_class

end_unit

