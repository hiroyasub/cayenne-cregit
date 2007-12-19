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
name|merge
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|DbGenerator
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
name|DbAdapter
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

begin_class
specifier|public
class|class
name|AddRelationshipToDb
extends|extends
name|AbstractToDbToken
block|{
specifier|private
name|DbEntity
name|entity
decl_stmt|;
specifier|private
name|DbRelationship
name|rel
decl_stmt|;
specifier|public
name|AddRelationshipToDb
parameter_list|(
name|DbEntity
name|entity
parameter_list|,
name|DbRelationship
name|rel
parameter_list|)
block|{
name|this
operator|.
name|entity
operator|=
name|entity
expr_stmt|;
name|this
operator|.
name|rel
operator|=
name|rel
expr_stmt|;
block|}
comment|/**      * @see DbGenerator#createConstraintsQueries(org.apache.cayenne.map.DbEntity)      */
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|createSql
parameter_list|(
name|DbAdapter
name|adapter
parameter_list|)
block|{
comment|// TODO: skip FK to a different DB
if|if
condition|(
operator|!
name|rel
operator|.
name|isToMany
argument_list|()
operator|&&
name|rel
operator|.
name|isToPK
argument_list|()
operator|&&
operator|!
name|rel
operator|.
name|isToDependentPK
argument_list|()
condition|)
block|{
name|String
name|fksql
init|=
name|adapter
operator|.
name|createFkConstraint
argument_list|(
name|rel
argument_list|)
decl_stmt|;
if|if
condition|(
name|fksql
operator|!=
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|fksql
argument_list|)
return|;
block|}
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|MergerToken
name|createReverse
parameter_list|(
name|MergerFactory
name|factory
parameter_list|)
block|{
return|return
name|factory
operator|.
name|createDropRelationshipToModel
argument_list|(
name|entity
argument_list|,
name|rel
argument_list|)
return|;
block|}
specifier|public
name|String
name|getTokenName
parameter_list|()
block|{
return|return
literal|"Add Relationship"
return|;
block|}
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
name|StringBuilder
name|s
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|s
operator|.
name|append
argument_list|(
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
literal|"->"
argument_list|)
expr_stmt|;
name|s
operator|.
name|append
argument_list|(
name|rel
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|s
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

