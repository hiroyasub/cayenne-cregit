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
operator|.
name|Entity
block|{
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
name|super
argument_list|(
literal|"Add foreign key"
argument_list|,
name|entity
argument_list|)
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
name|this
operator|.
name|shouldGenerateFkConstraint
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
name|boolean
name|shouldGenerateFkConstraint
parameter_list|()
block|{
return|return
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
comment|// TODO it is not necessary primary key it can be unique index
operator|&&
operator|!
name|rel
operator|.
name|isToDependentPK
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
name|getEntity
argument_list|()
argument_list|,
name|rel
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getTokenValue
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|shouldGenerateFkConstraint
argument_list|()
condition|)
block|{
return|return
name|rel
operator|.
name|getSourceEntity
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"->"
operator|+
name|rel
operator|.
name|getTargetEntityName
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|"Skip. No sql representation."
return|;
block|}
block|}
specifier|public
name|DbRelationship
name|getRelationship
parameter_list|()
block|{
return|return
name|rel
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|compareTo
parameter_list|(
name|MergerToken
name|o
parameter_list|)
block|{
comment|// add all AddRelationshipToDb to the end.
if|if
condition|(
name|o
operator|instanceof
name|AddRelationshipToDb
condition|)
block|{
return|return
name|super
operator|.
name|compareTo
argument_list|(
name|o
argument_list|)
return|;
block|}
return|return
literal|1
return|;
block|}
block|}
end_class

end_unit

